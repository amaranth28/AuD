package expression;

/**
 * Bildet einen konstanten Audruck ab
 * 
 * @author Cedric Heinrich, Clemens Heinrich
 * 
 */
public class ConstantExpression extends LeafExpression {

    private static final String TRUE_LABEL = "T";
    private static final String FALSE_LABEL = "F";

    private final boolean value;

    /**
     * Erzeugt einen Konstanten Audruck mit dem Wert value.
     * 
     * @param value der Wert der Konstante
     */
    public ConstantExpression(boolean value) {
        this.value = value;
    }

    @Override
    public boolean evaluateShort(Context c) throws IncompleteContextException {
        return value;
    }

    @Override
    protected String getGraphvizLabel() {
        return value ? TRUE_LABEL : FALSE_LABEL;
    }

}
