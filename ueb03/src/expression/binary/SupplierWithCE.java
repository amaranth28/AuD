package expression.binary;

/**
 * Supplier Interface mit checked exceptions
 *
 * @param <E> Typ des Ergebnis des Supplier-Aufrufs
 * @param <X> Typ der Exception die beim Supplier-Aufrufs auftreten kann
 */
@FunctionalInterface
public interface SupplierWithCE<E, X extends Exception> {

    /**
     * Liefert das Resultat
     * 
     * @return das Resultat
     * @throws X wenn beim Liefern des Resultats eine Exception auftritt
     */
    E get() throws X;
}
