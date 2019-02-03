package expression.binary;

import expression.AbstractExpression;
import expression.Context;
import expression.Counter;
import expression.Expression;
import expression.ExpressionRunner;
import expression.IncompleteContextException;

/**
 * Bildet binäre Ausdrücke ab
 * 
 * @author Cedric Heinrich, Clemens Heinrich
 *
 */
public abstract class AbstractBinaryExpression extends AbstractExpression {

    /** Erster Operand */
    protected Expression left;
    /** Zweiter Operand */
    protected Expression right;

    /**
     * Erzeugt einen binären Ausdruck
     * 
     * @param left erster Operand des Audrucks
     * @param right zweiter Operand des Ausdrucks
     */
    public AbstractBinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int getChildrenCount() {
        return 2 + left.getChildrenCount() + right.getChildrenCount();
    }

    @Override
    public StringBuilder toString(StringBuilder builder) {
        assert builder != null;

        builder.append("(");
        left.toString(builder);
        builder.append(" ");
        builder.append(getOperator());
        builder.append(" ");
        right.toString(builder);
        builder.append(")");

        return builder;
    }

    @Override
    public StringBuilder toGraphviz(StringBuilder sb, String prefix) {

        assert sb != null;
        assert prefix != null;

        // _ [label="*OPERATOR* [2]"]
        // _ -> _l [label=""]
        // _ -> _r [label=""]

        sb.append(prefix);
        sb.append(" [label=\"" + getGraphvizLabel() + "\"]\n");
        sb.append(prefix);
        sb.append(" -> ");
        sb.append(prefix + "l");
        sb.append(" ");
        sb.append(EMPTY_LABEL);
        sb.append("\n");
        sb.append(prefix);
        sb.append(" ");
        sb.append("->");
        sb.append(" ");
        sb.append(prefix + "r");
        sb.append(" ");
        sb.append(EMPTY_LABEL);
        sb.append("\n");
        left.toGraphviz(sb, prefix + "l");
        right.toGraphviz(sb, prefix + "r");

        return sb;

    }

    @Override
    public boolean evaluateShort(Context c) throws IncompleteContextException {
        return test(left.evaluateShort(c), () -> right.evaluateShort(c));
    }

    @Override
    public boolean evaluateComplete(Context c) throws IncompleteContextException {
        return test(left.evaluateComplete(c), right.evaluateComplete(c));
    }

    @Override
    public boolean evaluateParallel(Context c, int bound) throws IncompleteContextException {
        // prüfen ob parallesiert werden soll, ansonsten evaluateComplete
        if (getChildrenCount() >= bound) {
            ExpressionRunner leftRunner = new ExpressionRunner(left, c, bound);
            ExpressionRunner rightRunner = new ExpressionRunner(right, c, bound);

            // Threads erzeugen
            Thread leftThread = new Thread(leftRunner);
            Counter.increment();
            Thread rightThread = new Thread(rightRunner);
            Counter.increment();

            // Threads starten
            leftThread.start();
            rightThread.start();

            try {
                // auf Threads warten bis fertig oder abgebrochen
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }

            // Prüfen ob eine Exception bei der Auswertung aufgetreten ist
            if (leftRunner.getResult() == null) {
                throw leftRunner.getException();
            } else if (rightRunner.getResult() == null) {
                throw rightRunner.getException();
            } else {
                // wenn nicht Ergebnisse der Operanden verknüpfen
                return test(leftRunner.getResult(), rightRunner.getResult());
            }

        } else {
            return evaluateComplete(c);
        }
    }

    /**
     * Verknüpft die einzelnen Resultate der Operanden (eager)
     * 
     * @param left Resultat des ersten Operanden
     * @param right Resultat des zweiten Operanden
     * @return das Resultat der Operation
     * @throws IncompleteContextException kann bei der Eager-Auswertung nicht vorkommen, da Resultat
     *             des zweiten Operanden schon berechnet wurde
     */
    private boolean test(boolean left, boolean right) throws IncompleteContextException {
        return test(left, () -> right);
    }

    /**
     * Verknüpft die einzelnen Resultate der Operanden (lazy)
     * 
     * @param left Resultat des ersten Operanden
     * @param right Resultat des zweiten Operanden
     * @return das Resultat der Operation
     * @throws IncompleteContextException falls bei der Lazy-Auswertung des zweiten Operanden eine
     *             IncompleteContextException auftritt
     */
    protected abstract boolean test(boolean left,
            SupplierWithCE<Boolean, IncompleteContextException> right)
            throws IncompleteContextException;

}
