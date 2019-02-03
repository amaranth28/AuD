package expression;

/**
 * Implementiert einen threadsicheren Zähler.
 * 
 * Diese Klasse ist bereits komplett vorgegeben und darf nicht verändert werden.
 * 
 * @author kar, mhe
 */
public class Counter {

    private static int counter = 0;

    /**
     * 
     * @return Liefert den aktuellen Wert des Counters.
     */
    public static synchronized int getCounter() {
        return counter;
    }

    /**
     * Initialisiert den Counter mit 0.
     */
    public static synchronized void initialize() {
        counter = 0;
    }

    /**
     * Erhöht den Counter um eins.
     */
    public static synchronized void increment() {
        counter++;
    }

}
