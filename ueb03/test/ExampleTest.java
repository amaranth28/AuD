import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import api.ExpressionAPI;
import expression.Context;
import expression.Counter;
import expression.Expression;
import expression.IncompleteContextException;
import expression.binary.SupplierWithCE;

public class ExampleTest {

    private static final ExpressionAPI EXPRESSION_API = new ExpressionAPI();

    private Context context;
    private static final Expression TRUE = EXPRESSION_API.makeConstantExpression(true);
    private static final Expression FALSE = EXPRESSION_API.makeConstantExpression(false);
    private static final Expression VAR = EXPRESSION_API.makeVariableExpression("a");

    @Before
    public void setup() {
        context = new Context();
    }

    @Test(expected = AssertionError.class)
    public final void testAPIMakeVariableExpressionInvalidName() {
        EXPRESSION_API.makeVariableExpression("123");
    }

    @Test
    public final void testToStringEquivalenceOr() {
        Assert.assertEquals("((T || F) <-> T)", EXPRESSION_API
                .makeEquivalenceExpression(EXPRESSION_API.makeOrExpression(TRUE, FALSE), TRUE)
                .toString());
    }

    @Test
    public final void testToGraphvizEquivalenceOr() throws IOException {
        TestToolkit.assertDotEquals("((T || F) <-> T) als Graphviz", EXPRESSION_API
                .makeEquivalenceExpression(EXPRESSION_API.makeOrExpression(TRUE, FALSE), TRUE),
                "a1");
    }

    @Test
    public final void testToStringEquivalenceAnd() {
        Assert.assertEquals("((T && F) <-> F)", EXPRESSION_API
                .makeEquivalenceExpression(EXPRESSION_API.makeAndExpression(TRUE, FALSE), FALSE)
                .toString());
    }

    @Test
    public final void testToGraphvizEquivalenceAnd() throws IOException {
        TestToolkit.assertDotEquals("((T && F) <-> F) als Graphviz", EXPRESSION_API
                .makeEquivalenceExpression(EXPRESSION_API.makeAndExpression(TRUE, FALSE), FALSE),
                "a2");
    }

    @Test
    public final void testToStringAlmostAll() {
        Assert.assertEquals("((a -> b) <-> ((!a) || (b)))",
                EXPRESSION_API
                        .makeEquivalenceExpression(
                                EXPRESSION_API.makeConsequenceExpression(
                                        EXPRESSION_API.makeVariableExpression("a"),
                                        EXPRESSION_API.makeVariableExpression("b")),
                                EXPRESSION_API.makeOrExpression(
                                        EXPRESSION_API.makeNotExpression(
                                                EXPRESSION_API.makeVariableExpression("a")),
                                        EXPRESSION_API.makeIdExpression(
                                                EXPRESSION_API.makeVariableExpression("b"))))
                        .toString());
    }

    @Test
    public final void testToGraphvizAlmostAll() throws IOException {
        TestToolkit.assertDotEquals("((a -> b) <-> ((!a) || (b))) als Graphviz",
                EXPRESSION_API.makeEquivalenceExpression(
                        EXPRESSION_API.makeConsequenceExpression(
                                EXPRESSION_API.makeVariableExpression("a"),
                                EXPRESSION_API.makeVariableExpression("b")),
                        EXPRESSION_API.makeOrExpression(
                                EXPRESSION_API.makeNotExpression(
                                        EXPRESSION_API.makeVariableExpression("a")),
                                EXPRESSION_API.makeIdExpression(
                                        EXPRESSION_API.makeVariableExpression("b")))),
                "a3");
    }

    @Test
    public final void testEvaluateNot() throws IncompleteContextException {
        context.set("a", false);

        Assert.assertTrue(EXPRESSION_API.makeNotExpression(VAR).evaluateShort(context));
        Assert.assertTrue(EXPRESSION_API.makeNotExpression(VAR).evaluateComplete(context));
    }

    @Test
    public final void testEvaluateOr() throws IncompleteContextException {

        Assert.assertTrue(EXPRESSION_API.makeOrExpression(TRUE, FALSE).evaluateShort(context));
        Assert.assertTrue(EXPRESSION_API.makeOrExpression(TRUE, FALSE).evaluateComplete(context));
    }

    @Test
    public final void testEvaluateAnd() throws IncompleteContextException {

        Assert.assertFalse(EXPRESSION_API.makeAndExpression(TRUE, FALSE).evaluateShort(context));
        Assert.assertFalse(EXPRESSION_API.makeAndExpression(TRUE, FALSE).evaluateComplete(context));
    }

    @Test
    public final void testEvaluateAndParallel() throws IncompleteContextException {

        Counter.initialize();
        Assert.assertFalse(
                EXPRESSION_API.makeAndExpression(TRUE, FALSE).evaluateParallel(context, 2));
        Assert.assertEquals(2, Counter.getCounter());
    }

    @Test
    public final void testParallelAlmostAll() throws IncompleteContextException {

        final Expression bigger = EXPRESSION_API.makeEquivalenceExpression(
                EXPRESSION_API.makeConsequenceExpression(FALSE, TRUE),
                EXPRESSION_API.makeOrExpression(EXPRESSION_API.makeNotExpression(FALSE),
                        EXPRESSION_API.makeIdExpression(TRUE)));

        Counter.initialize();
        Assert.assertTrue(bigger.evaluateParallel(context, 6));
        Assert.assertEquals(2, Counter.getCounter());

        Counter.initialize();
        Assert.assertTrue(bigger.evaluateParallel(context, 2));
        Assert.assertEquals(6, Counter.getCounter());
    }

    @Test(expected = IncompleteContextException.class)
    public final void testEvaluateVariableVarNotInContext() throws IncompleteContextException {
        VAR.evaluateShort(context);
    }

    @Test(expected = IncompleteContextException.class)
    public final void testEvaluateVariableNoContext() throws IncompleteContextException {
        VAR.evaluateShort(null);
    }

    @Test
    public final void testEvaluateVariable() throws IncompleteContextException {
        context.set("a", true);

        assertTrue(VAR.evaluateShort(context));
        assertTrue(VAR.evaluateComplete(context));
        assertTrue(VAR.evaluateParallel(context, 0));
    }

    @Test
    public final void testEvaluateAndExpression() throws IncompleteContextException {
        assertFalse(EXPRESSION_API.makeAndExpression(FALSE, TRUE).evaluateShort(null));
        assertFalse(EXPRESSION_API.makeAndExpression(TRUE, FALSE).evaluateShort(null));
        assertFalse(EXPRESSION_API.makeAndExpression(FALSE, FALSE).evaluateParallel(context, 0));
        assertTrue(EXPRESSION_API.makeAndExpression(TRUE, TRUE).evaluateComplete(null));

        assertFalse(EXPRESSION_API.makeAndExpression(FALSE, VAR).evaluateShort(context));
        assertThrowsIncompleteException(
                () -> EXPRESSION_API.makeAndExpression(TRUE, VAR).evaluateShort(context));
        assertThrowsIncompleteException(
                () -> EXPRESSION_API.makeAndExpression(FALSE, VAR).evaluateComplete(context));
        assertThrowsIncompleteException(
                () -> EXPRESSION_API.makeAndExpression(TRUE, VAR).evaluateParallel(context, 0));

        context.set("a", true);
        assertTrue(EXPRESSION_API.makeAndExpression(TRUE, VAR).evaluateShort(context));
    }

    @Test
    public void testEvaluateConsequenceExpression() throws IncompleteContextException {
        assertTrue(EXPRESSION_API.makeConsequenceExpression(FALSE, FALSE).evaluateShort(null));
        assertTrue(EXPRESSION_API.makeConsequenceExpression(TRUE, TRUE).evaluateComplete(null));
        assertTrue(EXPRESSION_API.makeConsequenceExpression(FALSE, TRUE).evaluateParallel(null, 0));
        assertFalse(EXPRESSION_API.makeConsequenceExpression(TRUE, FALSE).evaluateComplete(null));

        assertTrue(EXPRESSION_API.makeConsequenceExpression(FALSE, VAR).evaluateShort(context));
        assertThrowsIncompleteException(() -> EXPRESSION_API.makeConsequenceExpression(FALSE, VAR)
                .evaluateComplete(context));
        assertThrowsIncompleteException(() -> EXPRESSION_API.makeConsequenceExpression(FALSE, VAR)
                .evaluateParallel(context, 0));

        context.set("a", true);
        assertFalse(
                EXPRESSION_API.makeConsequenceExpression(TRUE, FALSE).evaluateComplete(context));

    }

    @Test
    public final void testToString() {

        assertEquals("T", TRUE.toString());
        assertEquals("a", VAR.toString());

        assertEquals("(a && F)", EXPRESSION_API.makeAndExpression(VAR, FALSE).toString());
        assertEquals("(T -> a)", EXPRESSION_API.makeConsequenceExpression(TRUE, VAR).toString());
        assertEquals("((T && T) <-> a)", EXPRESSION_API
                .makeEquivalenceExpression(EXPRESSION_API.makeAndExpression(TRUE, TRUE), VAR)
                .toString());
        assertEquals("(T || F)", EXPRESSION_API.makeOrExpression(TRUE, FALSE).toString());
        assertEquals("(F ^ a)", EXPRESSION_API.makeXorExpression(FALSE, VAR).toString());

        assertEquals("(F)", EXPRESSION_API.makeIdExpression(FALSE).toString());
        assertEquals("(!a)", EXPRESSION_API.makeNotExpression(VAR).toString());
        assertEquals("(((a -> b) <-> ((!a) || b)))",
                EXPRESSION_API.makeIdExpression(EXPRESSION_API.makeEquivalenceExpression(
                        EXPRESSION_API.makeConsequenceExpression(
                                EXPRESSION_API.makeVariableExpression("a"),
                                EXPRESSION_API.makeVariableExpression("b")),
                        EXPRESSION_API.makeOrExpression(
                                EXPRESSION_API.makeNotExpression(
                                        EXPRESSION_API.makeVariableExpression("a")),
                                EXPRESSION_API.makeVariableExpression("b"))))
                        .toString());
    }

    @Test
    public final void testToGraphivzComplex() throws IOException {
        TestToolkit.assertDotEquals(
                EXPRESSION_API.makeIdExpression(EXPRESSION_API.makeEquivalenceExpression(
                        EXPRESSION_API.makeConsequenceExpression(
                                EXPRESSION_API.makeVariableExpression("a"),
                                EXPRESSION_API.makeVariableExpression("b")),
                        EXPRESSION_API.makeOrExpression(
                                EXPRESSION_API.makeNotExpression(
                                        EXPRESSION_API.makeVariableExpression("a")),
                                EXPRESSION_API.makeVariableExpression("b")))),
                "a4");
    }

    private static void assertThrowsIncompleteException(
            SupplierWithCE<Boolean, IncompleteContextException> supplier) {
        try {
            supplier.get();
        } catch (IncompleteContextException e) {
            return;
        }
        Assert.fail();
    }

}
