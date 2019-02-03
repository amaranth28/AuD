package expression.unary;

import expression.AbstractExpression;
import expression.Expression;

/**
 *
 * Bildet einen unären Ausdruck ab
 * 
 * @author Cedric Heinrich, Clemens Heinrich
 *
 */
public abstract class AbstractUnaryExpression extends AbstractExpression implements Expression {

    protected Expression parameter;

    /**
     * 
     * @param parameter Konstruktor
     */
    public AbstractUnaryExpression(Expression parameter) {
        this.parameter = parameter;
    }

    @Override
    public int getChildrenCount() {
        return 1 + parameter.getChildrenCount();
    }

    @Override
    public StringBuilder toGraphviz(StringBuilder sb, String prefix) {
        assert sb != null;
        assert prefix != null;

        sb.append(prefix);
        sb.append(" [label=\"" + getGraphvizLabel() + "\"]\n");
        sb.append(prefix);
        sb.append(" -> ");
        sb.append(prefix + "_ ");
        sb.append(EMPTY_LABEL);
        sb.append("\n");
        parameter.toGraphviz(sb, prefix + "_");

        return sb;
    }

    @Override
    public StringBuilder toString(StringBuilder builder) {
        assert builder != null;

        builder.append("(");
        builder.append(getOperatorForToString());
        parameter.toString(builder);
        builder.append(")");

        return builder;
    }

    /**
     * 
     * @return Operator-Label für {@link #toString(StringBuilder)} Methode
     */
    protected abstract String getOperatorForToString();

}
