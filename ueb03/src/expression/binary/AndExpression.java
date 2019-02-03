package expression.binary;

import expression.Expression;
import expression.IncompleteContextException;

/**
 * Bildet einen binären Ausdruck ab, der die AND verknüpfung abbildet
 * 
 * @author Cedric Heinrich, Clemens Heinrich
 *
 */
public class AndExpression extends AbstractBinaryExpression {

    /**
     * Erzeugt einen And-Verknüpften Ausdruck von
     * 
     * @param left erster Operand des Audrucks
     * @param right zweiter Operand des Ausdrucks
     */
    public AndExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean test(boolean left, SupplierWithCE<Boolean, IncompleteContextException> right)
            throws IncompleteContextException {
        return left && right.get();
    }

    @Override
    protected String getOperator() {
        return "&&";
    }

}
