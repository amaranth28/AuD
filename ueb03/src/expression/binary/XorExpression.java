package expression.binary;

import expression.Expression;
import expression.IncompleteContextException;

/**
 * 
 * Audruck bildet die XOR-Operation ab
 * 
 * @author Cedric Heinrich, Clemens Heinrich
 * 
 */
public class XorExpression extends AbstractBinaryExpression {

    /** Operations-Zeichen */
    private static final String OPERATOR = "^";

    /**
     * Erzeugt einen neuen Audruck, der die XOR-Verknüpfung abbildet
     * 
     * @param left erster Operand der Operation
     * @param right zweiter Operand der Operation
     */
    public XorExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean test(boolean left, SupplierWithCE<Boolean, IncompleteContextException> right)
            throws IncompleteContextException {
        return left ^ right.get();
    }

    @Override
    public String getOperator() {
        return OPERATOR;
    }

}
