package expression;

/**
 * Bildet einen variablen Audruck ab
 * 
 * @author Cedric Heinrich, Clemens Heinrich
 *
 */
public class VariableExpression extends LeafExpression {

    private final String name;

    /**
     * Erzeugt einen variablen Ausdruck mit dem übergebenen Namen
     * 
     * @param name der Name der Variable
     * @pre name darf nicht null sein
     */
    public VariableExpression(String name) {
        assert name != null;

        this.name = name;
    }

    @Override
    public boolean evaluateShort(Context c) throws IncompleteContextException {

        if (c != null && c.has(name)) {
            return c.get(name);
        } else {
            throw new IncompleteContextException(name);
        }
    }

    @Override
    protected String getGraphvizLabel() {
        return name;
    }

}
