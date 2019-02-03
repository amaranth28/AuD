package expression;

/**
 * Implementiert die Auswertung des Ausdrucksbaums mit Hilfe von evaluateParallel. Ist nur für eine
 * nebenläufige Implementierung mit Threads vorgesehen.
 * 
 * Diese Klasse ist bereits komplett vorgegeben und darf nicht verändert werden.
 *
 * @author kar, mhe
 *
 */
public class ExpressionRunner implements Runnable {

    private Boolean result = null;
    private final Expression expression;
    private final Context context;
    private final int bound;
    private IncompleteContextException exception;

    /**
     * Konstruktor.
     * 
     * @param expression Auszuwertender Ausdruck
     * @param context Context
     * @param bound Anzahl der Unterknoten (gemäß getChildrenCount), ab der eine parallele
     *            Auswertung stattfindet.
     */
    public ExpressionRunner(Expression expression, Context context, int bound) {
        this.expression = expression;
        this.context = context;
        this.bound = bound;
    }

    /**
     * Liefert das Ergebnis der Auswertung.
     * 
     * Darf erst aufgerufen werden, wenn der umschließende Thread beendet ist.
     * 
     * @return Ergebnis der Auswertung oder null, falls ein Fehler aufgetreten ist.
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * Liefert die zu dem aufgetretenen Fehler passende Exception.
     * 
     * Darf erst aufgerufen werden, wenn der umschließende Thread beendet ist.
     * 
     * @pre es muss ein Fehler aufgetreten sein
     * 
     * @return die passende Exception, wenn ein Fehler aufgetreten ist
     */
    public IncompleteContextException getException() {
        assert result == null;

        return exception;
    }

    @Override
    public void run() {

        try {
            result = expression.evaluateParallel(context, bound);
        } catch (IncompleteContextException e) {
            result = null;
            exception = e;
        }
    }
}
