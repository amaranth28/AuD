package expression.unary;

import expression.Context;
import expression.Expression;
import expression.IncompleteContextException;

/**
 * Ausdruck bildet die Identität ab
 * 
 * @author Cedric Heinrich, Clemens Heinrich *
 * 
 */
public class IdExpression extends AbstractUnaryExpression {

    /**
     * 
     * @param x übergebene Expression
     */
    public IdExpression(Expression x) {
        super(x);
    }

    @Override
    public boolean evaluateShort(Context c) throws IncompleteContextException {
        return parameter.evaluateShort(c);
    }

    @Override
    public boolean evaluateComplete(Context c) throws IncompleteContextException {
        return parameter.evaluateComplete(c);
    }

    @Override
    public boolean evaluateParallel(Context c, int bound) throws IncompleteContextException {
        return parameter.evaluateParallel(c, bound);
    }

    @Override
    protected String getOperator() {
        return "()";
    }

    @Override
    protected String getOperatorForToString() {
        return "";
    }

}
