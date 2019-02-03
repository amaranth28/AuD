package expression;

/**
 * Schnittstelle für einen Ausdruck.
 * 
 * Dieses Interface ist bereits komplett vorgegeben und darf nicht verändert werden.
 * 
 * @author kar, mhe
 * 
 */
public interface Expression {

    /**
     * Wertet den Ausdruck vollständig aus, d.h. keiner der Operatoren wird verkürzt ausgewertet.
     * 
     * @param c Der Kontext indem der Ausdruck ausgewertet werden soll. Falls der Kontext nicht
     *            benötigt wird, darf c leer oder null sein.
     * @return Der Wahrheitswert des Ausdrucks.
     * @throws IncompleteContextException Sofern eine der Variablen innerhalb des Ausdrucks nicht im
     *             Kontext definiert ist.
     */
    boolean evaluateComplete(Context c) throws IncompleteContextException;

    /**
     * Wertet den Ausdruck verkürzt aus. Hierbei werden die Elemente des Ausdrucks von links nach
     * rechts ausgewertet. Steht das Ergebnis einer binären Operation fest, wird die weitere
     * Auswertung übersprungen.
     * 
     * @param c Der Kontext indem der Ausdruck ausgewertet werden soll. Falls der Kontext nicht
     *            benötigt wird, darf c leer oder null sein.
     * @return Der Wahrheitswert des Ausdrucks.
     * @throws IncompleteContextException Sofern eine der Variablen innerhalb des Ausdrucks nicht im
     *             Kontext definiert ist.
     */
    boolean evaluateShort(Context c) throws IncompleteContextException;

    /**
     * Nimmt eine parallelisierte und vollständige Auswertung des Ausdrucks vor. Hierbei werden
     * Verarbeitungsschritte genau dann in eigene Threads ausgelagert, wenn die Anzahl der
     * Kindknoten einer binären Operation größer gleich der übergebenen Grenze ist.
     * 
     * @param c Der Kontext indem der Ausdruck ausgewertet werden soll. Falls der Kontext nicht
     *            benötigt wird, darf c leer oder null sein.
     * @param bound Die Grenze ab der parallelisiert werden soll.
     * @return Der Wahrheitswert des Ausdrucks.
     * @throws IncompleteContextException Sofern eine der Variablen innerhalb des Ausdrucks nicht im
     *             Kontext definiert ist.
     */
    boolean evaluateParallel(Context c, int bound) throws IncompleteContextException;

    /**
     * Gibt die Anzahl aller Unterknoten dieses Ausdrucks zurück.
     * 
     * @return die Anzahl aller Unterknoten dieses Ausdrucks
     */
    int getChildrenCount();

    /**
     * Erzeugt eine Stringdarstellung des Ausdrucks.
     * 
     * @param builder StringBuilder Puffer an den der String des Ausdrucks angehängt werden soll
     * @pre builder ist nicht null
     * @return Der übergebene Puffer
     */
    StringBuilder toString(StringBuilder builder);

    /**
     * Erzeugt eine Stringdarstellung des Ausdrucks unter Verwendung der Methode mit dem
     * StringBuilder.
     * 
     * @return Die Stringdarstellung.
     */
    @Override
    String toString();

    /**
     * Schreibt die Graphviz-Repräsentation des Baums in den übergebenen StringBuilder.
     * 
     * @param sb Der StringBuilder, in den die Repräsentation des Baum geschrieben werden soll
     * @param prefix das Prefix für das entsprechende Element des Baums
     * @pre sb ist nicht null
     * @pre prefix ist nicht null
     * @return Der gefüllte StringBuilder
     */
    StringBuilder toGraphviz(StringBuilder sb, String prefix);

    /**
     * Gibt die Graphviz-Repäsentation des Baums als String zurück (unter Verwendung der Methode mit
     * dem StringBuilder).
     * 
     * @return Die Graphviz-Repäsentation des Baums
     */
    String toGraphviz();
}
