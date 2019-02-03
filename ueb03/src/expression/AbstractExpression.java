package expression;

/**
 * Abstrakte Klasse für einfache Audrücke
 * 
 * @author Cedric Heinrich, Clemens Heinrich
 * 
 */
public abstract class AbstractExpression implements Expression {

    protected static final String EMPTY_LABEL = "[label=\"\"]";

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    @Override
    public StringBuilder toGraphviz(StringBuilder sb, String prefix) {

        assert sb != null;
        assert prefix != null;

        sb.append(prefix);
        sb.append(" [label=\"" + getGraphvizLabel() + "\"]\n");

        return sb;
    }

    @Override
    public String toGraphviz() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");
        toGraphviz(sb, "_");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * 
     * @return label für Graphviz
     */
    protected String getGraphvizLabel() {
        return getOperator() + " [" + getChildrenCount() + "]";
    }

    /**
     * 
     * @return der Operator für Stringrepräsentation
     */
    protected abstract String getOperator();

}
