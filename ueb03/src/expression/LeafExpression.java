/**
 * 
 */
package expression;

/**
 * Klasse für Blätter
 * 
 */
public abstract class LeafExpression extends AbstractExpression {

    @Override
    public int getChildrenCount() {
        // einfache Audrücke haben keine Kinder
        return 0;
    }

    @Override
    public boolean evaluateComplete(Context c) throws IncompleteContextException {
        // nur bei BinaryExpressions sinnvoll, daher Verwendung von evaluteShort
        return evaluateShort(c);
    }

    @Override
    public boolean evaluateParallel(Context c, int bound) throws IncompleteContextException {
        // nur bei BinaryExpressions sinnvoll, daher Verwendung von evaluteShort
        return evaluateShort(c);
    }

    @Override
    public StringBuilder toString(StringBuilder builder) {
        assert builder != null;

        return builder.append(getGraphvizLabel());
    }

    @Override
    protected String getOperator() {
        return "";
    }

}
