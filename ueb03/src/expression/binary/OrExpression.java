package expression.binary;

import expression.Expression;
import expression.IncompleteContextException;

/**
 * 
 * Bildet einen Ausdruck ab , der die OR-Verknüpfung darstellt
 * 
 * @author Cedric Heinrich, Clemens Heinrich
 */
public class OrExpression extends AbstractBinaryExpression {

    /**
     * 
     * @param left linker Operand
     * @param right rechter Operand
     */
    public OrExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean test(boolean left, SupplierWithCE<Boolean, IncompleteContextException> right)
            throws IncompleteContextException {
        return left || right.get();
    }

    @Override
    public String getOperator() {
        return "||";
    }

}
