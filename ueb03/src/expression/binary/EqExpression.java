package expression.binary;

import expression.Expression;
import expression.IncompleteContextException;

/**
 * Ausdruck bildet die logische Äquivalenz ab
 * 
 * @author Cedric Heinrich, Clemens Heinrich
 *
 */
public class EqExpression extends AbstractBinaryExpression {

    /**
     * Erzeugt einen neuen Ausdruck, der die logische Äquivalenz abbildet
     * 
     * @param left erster Operand der Operation
     * @param right zweiter Operant der Operation
     */
    public EqExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean test(boolean left, SupplierWithCE<Boolean, IncompleteContextException> right)
            throws IncompleteContextException {
        return left == right.get();
    }

    @Override
    public String getOperator() {
        return "<->";
    }

}
