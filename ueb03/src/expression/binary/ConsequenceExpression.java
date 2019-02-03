package expression.binary;

import expression.Expression;
import expression.IncompleteContextException;

/**
 * Bildet einen Audruck ab, der die Implikation ausdrückt
 * 
 * @author Cedric Heinrich, Clemens Heinrich
 */
public class ConsequenceExpression extends AbstractBinaryExpression {

    private static final String OPERATOR = "->";

    /**
     * Erzeugt einen Ausdruck der die Implikation darstellt
     * 
     * @param left erster Operand des Audrucks
     * @param right zweiter Operand des Ausdrucks
     */
    public ConsequenceExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean test(boolean left, SupplierWithCE<Boolean, IncompleteContextException> right)
            throws IncompleteContextException {
        // entweder left ist false oder right muss true sein
        return (!left) || right.get();
    }

    @Override
    public String getOperator() {
        return OPERATOR;
    }

}
