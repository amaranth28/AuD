package db;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
//import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Eine Datenbanktabelle hat einen Namen bzw. Bezeichner und eine feste Spaltenanzahl, die ebenso
 * wie die Bezeichner (Datentyp String) der einzelnen Spalten und deren Reihenfolge bei der
 * Erzeugung festgelegt werden. Die Tabelle besteht darüber hinaus noch aus einer flexiblen Anzahl
 * von Zeilen, in denen jeweils genau so viele Werte (Datentyp String) stehen, wie es Spalten gibt.
 * Eine neue Zeile wird immer nach der letzten Zeile an die Tabelle angehängt.
 *
 * Der Bezeichner der Datenbanktabelle und die Bezeichner der Spalten müssen einem vorgegebenen
 * Muster folgen um gültig zu sein. Ein valider Bezeichner besteht stets aus einem Zeichen aus der
 * Menge [a-zA-Z] gefolgt von einer beliebigen Anzahl von Zeichen aus der Menge [a-zA-Z0-9_].
 * 
 * @author kar, mhe, Cedric Heinrich, Clemens Heinrich
 */
public final class DBTable {

    /** Pattern zum Testen eines validen Bezeichners */
    private static final Pattern VALID_IDENTIFIER_PATTERN =
            Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9_]*");

    /** Prädikat zum Testen eines validen Bezeichners */
    private static final Predicate<String> VALID_INDENTIFIER_PREDICATE =
            s -> VALID_IDENTIFIER_PATTERN.matcher(s).matches();

    private static final char COMMA = ',';
    private static final char LINEBREAK = '\n';

    /** Der Name der Tabelle */
    private String id;

    /** Kopf der Tabelle */
    private List<String> head;

    /** Zeilen der Tabelle */
    private List<String[]> rows;

    /**
     * Erzeugt eine leere Datenbanktabelle mit dem Bezeichner anId und den Spaltenbezeichnern
     * someColIds.
     *
     * @param anId Bezeichner der Datenbanktabelle, die erzeugt werden soll.
     * @param someColIds Feld mit den Spaltenbezeichnern.
     *
     * @pre der Bezeichner anId muss gültig sein.
     * @pre Das Feld mit den Spaltenbezeichnern muss mindestens einen Wert enthalten.
     * @pre Alle Werte im Feld mit den Spaltenbezeichnern müssen gültige Bezeichner sein.
     * @pre Alle Spaltenbezeichner müssen eindeutig sein.
     */
    public DBTable(final String anId, final String[] someColIds) {
        assert isValidIdentifier(anId);
        assert someColIds != null && someColIds.length > 0;
        assert areValidIdentifiers(someColIds);
        assert areOnlyUniqueValues(someColIds);

        id = anId;
        head = Arrays.asList(someColIds);
        rows = new LinkedList<>();
    }

    /**
     * @param other Tabelle die kopiert wird
     */
    public DBTable(DBTable other) {
        head = other.head;
        rows = other.rows;
        id = other.id;

    }

    /**
     * Liefert den Bezeichner der Datenbanktabelle.
     *
     * @return Bezeichner der Datenbanktabelle.
     */
    public String getId() {
        return id;
    }

    /**
     * Prüft, ob die Tabelle eine Spalte mit dem Bezeichner aColId hat.
     *
     * @param aColId Bezeichner, der geprüft wird.
     *
     * @pre der Bezeichner aColId muss gültig sein.
     *
     * @return boolscher Wert, der angibt, ob die Tabelle eine Spalte mit dem Bezeichner aColId hat.
     */
    public boolean hasCol(final String aColId) {
        assert isValidIdentifier(aColId);

        return head.contains(aColId);
    }

    /**
     * Prüft, ob die Strings im Feld someColIds Spaltenbezeichner der Tabelle sind.
     *
     * @param someColIds Bezeichner, die geprüft werden.
     *
     * @return boolscher Wert, der angibt, ob die Strings im Feld someColIds Spaltenbezeichner der
     *         Tabelle sind.
     *
     * @pre Das Feld mit den Spaltenbezeichnern muss mindestens einen Wert enthalten.
     * @pre Alle Werte im Feld mit den Spaltenbezeichnern müssen gültige Bezeichner sein.
     */
    public boolean hasCols(final String[] someColIds) {
        assert someColIds != null && someColIds.length > 0;
        assert areValidIdentifiers(someColIds);

        for (int i = 0; i < someColIds.length; i++) {
            String aColId = someColIds[i];
            if (!hasCol(aColId)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Liefert die Spaltenanzahl der Datenbanktabelle.
     *
     * @return Spaltenanzahl der Datenbanktabelle.
     */
    public int getColCnt() {
        return head.size();
    }

    /**
     * Liefert die Zeilenanzahl der Datenbanktabelle.
     *
     * @return Zeilenanzahl der Datenbanktabelle.
     */
    public int getRowCnt() {
        return rows.size();
    }

    /**
     * Fügt die Werte des Arrays row als letzte Zeile in die Tabelle ein.
     *
     * @param row Array, dessen Werte als letzte Zeile eingefügt werden sollen.
     *
     * @pre Die Anzahl der Werte in row muss der Spaltenanzahl der Tabelle entsprechen.
     */
    public void appendRow(final String[] row) {
        assert row != null && row.length == getColCnt();
        String[] copy = Arrays.copyOf(row, row.length);
        rows.add(copy);
    }

    /**
     * Löscht alle Zeilen der Tabelle.
     *
     * @post die Tabelle enthält keine Zeilen.
     */
    public void removeAllRows() {
        rows.clear(); // löscht clear wirklich die zeilen oder nur die werte in den Zeilen?
        assert rows.size() == 0;
    }

    /**
     * Liefert eine Liste der Spaltenbezeichner. Die Reihenfolge entspricht dabei der Reihenfolge
     * der Spalten in der Tabelle.
     *
     * @return Liste der Spaltenbezeichner.
     */
    public List<String> getColIds() {
        return Collections.unmodifiableList(head);

    }

    /**
     * Sortiert die Zeilen anhand der Werte in der Spalte mit dem Bezeichner aColId in der
     * Sortierreihenfolge sortDir.
     *
     *
     * @param aColId Bezeichner der Spalte, nach der sortiert werden soll.
     * @param sortDir Reihenfolge, nach der sortiert werden soll.
     *
     * @pre der Bezeichner aColId muss gültig sein.
     * @pre die Tabelle muss eine Spalte mit dem Bezeichner aColId haben.
     */
    public void sort(final String aColId, final SortDirection sortDir) {
        assert isValidIdentifier(aColId);
        assert hasCol(aColId);

        Comparator<String[]> comparator =
                Comparator.comparing(row -> row[getColIndexForColId(aColId)]);

        if (sortDir == SortDirection.DESC) {
            comparator = comparator.reversed();
        }

        // mergesort
        Collections.sort(rows, comparator);
    }

    /**
     * Entfernt alle Zeilen aus der Tabelle, bei denen ein Test über dem Wert in der Spalte, die mit
     * aColId bezeichnet ist, erfolgreich ist.
     *
     * @param aColId Bezeichner der Spalte, deren Werte für den Test herangezogen werden sollen.
     * @param p Ein Predicate-Objekt zum Testen des jeweiligen Spaltenwertes
     *
     * @pre der Bezeichner aColId muss gültig sein.
     * @pre die Tabelle muss eine Spalte mit dem Bezeichner aColId haben.
     */
    public void removeRows(final String aColId, final Predicate<String> p) {
        assert isValidIdentifier(aColId);
        assert hasCol(aColId);

        int aColIdx = getColIndexForColId(aColId);

        Iterator<String[]> iterator = rows.iterator();

        while (iterator.hasNext()) {
            String[] row = iterator.next();
            if (testPredicateOnRow(p, aColIdx, row)) {
                iterator.remove();
            }
        }

    }

    /**
     * Erzeugt eine Tabelle mit dem Bezeichner newTableID, die alle Zeilen enthält, bei denen ein
     * Test über dem Wert in der Spalte, die mit aColId bezeichnet ist, erfolgreich ist.
     *
     * @param aColId Bezeichner der Spalte, deren Werte für den Vergleich herangezogen werden
     *            sollen.
     * @param p Ein Predicate-Objekt zum Testen des jeweiligen Spaltenwertes
     * @param newTableId Bezeichner der erzeugten Tabelle.
     *
     * @pre der Bezeichner aColId muss gültig sein.
     * @pre die Tabelle muss eine Spalte mit dem Bezeichner aColId haben.
     * @pre der Bezeichner newTableId muss gültig sein.
     *
     * @return erzeugte Tabelle.
     */
    public DBTable select(final String aColId, final Predicate<String> p, final String newTableId) {
        assert isValidIdentifier(aColId);
        assert hasCol(aColId);
        assert isValidIdentifier(newTableId);

        DBTable res = new DBTable(newTableId, head.toArray(new String[getColCnt()]));
        int aColIdx = getColIndexForColId(aColId);

        for (int i = 0; i < getRowCnt(); i++) {
            String[] row = rows.get(i);
            if (testPredicateOnRow(p, aColIdx, row)) {
                res.appendRow(row);
            }
        }

        return res;
    }

    /**
     * Erzeugt eine Tabelle mit dem Bezeichner newTableID, die alle Spalten enthält, deren
     * Bezeichner in dem Feld someColIds aufgeführt sind.
     *
     * @param someColIds Feld mit den Bezeichnern der Spalten, die in die erzeugte Tabelle
     *            übernommen werden. Die Reihenfolge der Spalten in dem Feld entspricht der in der
     *            erzeugten Tabelle.
     * @param newTableId Bezeichner der Tabelle, die erzeugt wird.
     *
     * @pre Das Feld mit den Spaltenbezeichnern muss mindestens einen Wert enthalten.
     * @pre zu allen Einträgen in someColIds gibt es eine entsprechende Spalte in der Tabelle.
     * @pre der Bezeichner newTableId muss gültig sein.
     *
     * @return die erzeugte Tabelle.
     */
    public DBTable project(final String[] someColIds, final String newTableId) {
        assert someColIds != null && someColIds.length > 0;
        assert hasCols(someColIds);
        assert isValidIdentifier(newTableId);

        final DBTable res = new DBTable(newTableId, someColIds);

        for (String[] row : rows) {
            res.appendRow(projectRow(row, someColIds));
        }

        return res;
    }

    /**
     * Führt eine join-Operation mit der aktuellen und der übergebenen Tabelle other durch. Hierbei
     * wird eine neue Tabelle mit dem Bezeichner newTableID erzeugt, die alle Spalten beider
     * Tabellen enthält: zunächst die Spalten der aktuellen Tabelle und danach die Spalten der
     * übergebenen Tabelle. In der neuen Tabelle befinden sich alle Zeilen in denen die Werte an den
     * Positionen der übergebenen Spaltenbezeichner (thisColId für die aktuelle Tabelle und
     * otherColId für die übergebene Tabelle) übereinstimmen. Es bleibt die Reihenfolge der Zeilen
     * aus der aktuellen Tabelle bzw. der Tabelle other erhalten.
     *
     * Die Spaltenbezeichner der neuen Tabelle werden aus den Spaltenbezeichnern der beiden
     * vorhandenen Tabellen erzeugt und zwar nach dem Schema, dass vor jeden vorhandenen Bezeichner
     * der Name der entsprechenden Ursprungstabelle gefolgt von einem Unterstrich geschrieben wird.
     *
     * @param other die Tabelle, mit der this gejoint werden soll
     * @param newTableId Bezeichner der Tabelle, die erzeugt wird.
     * @param thisColId Spaltenbezeichner der Spalte deren Werte in this verglichen werden.
     * @param otherColId Spaltenbezeichner der Spalte deren Werte in other verglichen werden.
     *
     * @pre der Bezeichner newTableId muss gültig sein.
     * @pre die Tabellennamen der beiden Tabellen this und other müssen verschieden sein.
     * @pre der Bezeichner thisColId muss in der Tabelle this vorhanden sein
     * @pre der Bezeichner otherColId muss in der Tabelle other vorhanden sein
     * @post die Bezeichner der neuen Tabellenspalten müssen gültig sein
     * @post die Bezeichner der neuen Tabellenspalten müssen eindeutig sein
     *
     * @return die erzeugte Tabelle
     */
    public DBTable equijoin(DBTable other, final String thisColId, final String otherColId,
            final String newTableId) {
        assert isValidIdentifier(newTableId);
        assert hasCol(thisColId);
        assert other.hasCol(otherColId);
        assert !this.getId().equals(other.getId());

        int offset = 0;
        String comparevalue;

        final DBTable copy = new DBTable(other);

        // Array welches alle ColIds der gejointen Tabelle enthält
        String[] resColIds = new String[this.getColCnt() + other.getColCnt()];

        for (int i = 0; i < this.head.size(); i++) {
            resColIds[i] = this.getId() + "_" + this.head.get(i);
            offset++;
        }

        for (int i = 0; i < copy.head.size(); i++) {
            resColIds[offset] = copy.getId() + "_" + copy.head.get(i);
            offset++;
        }

        offset = 0;

        DBTable res = new DBTable(newTableId, resColIds);

        int colIndexThis = getColIndexForColId(thisColId, this);
        int colIndexOther = getColIndexForColId(otherColId, copy);
        for (int i = 0; i < this.rows.size(); i++) {
            comparevalue = this.rows.get(i)[colIndexThis];
            for (int j = 0; j < copy.rows.size(); j++) {
                offset = 0;
                if (copy.rows.get(j)[colIndexOther].equals(comparevalue)) {
                    String[] newRow = new String[resColIds.length];

                    for (int j2 = 0; j2 < this.head.size(); j2++) {
                        newRow[j2] = this.rows.get(i)[j2];
                        offset++;
                    }
                    for (int j3 = 0; j3 < copy.head.size(); j3++) {
                        newRow[offset] = copy.rows.get(j)[j3];
                        offset++;
                    }

                    res.appendRow(newRow);

                }

            }
        }
        assert areValidIdentifiers(resColIds);
        assert areOnlyUniqueValues(resColIds);

        return res;
    }

    /**
     *
     * Liefert die Stringrepräsentation der Datenbanktabelle. Die Stringrepräsentation ist wie folgt
     * aufgebaut:
     * <ul>
     * <li>In der ersten Zeile stehen durch Kommata getrennt die Bezeichner der Spalten der
     * Datenbanktabelle.</li>
     * <li>Es folgen die Zeilen der Datenbanktabelle in der Reihenfolge, in der sie in der
     * Datenbanktabelle gespeichert sind. Die einzelnen Werte einer Zeile der Datenbanktabelle
     * werden durch Kommata getrennt hintereinander gereiht.</li>
     * </ul>
     *
     * Zeilenumbrüche (Unix, DOS, MacOs) innerhalb der Felder der Tabelle werden zugunsten der
     * besseren Lesbarkeit hierbei entfernt.
     *
     * Es werden stets Unix-Zeilenumbrüche (\n) verwendet.
     *
     * @return Stringrepräsentation der Datenbanktabelle.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String colId : head) {
            sb.append(colId);
            sb.append(COMMA);
        }

        sb.deleteCharAt(sb.length() - 1);

        sb.append(LINEBREAK);

        if (rows.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        Iterator<String[]> rowIterator = rows.listIterator();
        while (rowIterator.hasNext()) {
            String[] row = rowIterator.next();
            for (String colId : head) {
                int index = getColIndexForColId(colId);
                String value = row[index];
                sb.append(value.replaceAll("(\\r|\\n|\\r\\n)", " "));
                sb.append(COMMA);
            }
            sb.deleteCharAt(sb.length() - 1);
            if (rowIterator.hasNext()) {
                sb.append(LINEBREAK);
            }
        }

        return sb.toString();
    }

    /**
     * Liefert die Stringrepräsentation der Datenbanktabelle so wie sie in eine Datei geschrieben
     * wird.
     *
     * Diese Stringrepräsentation ist wie folgt aufgebaut:
     * <ul>
     * <li>In der ersten Zeile steht nur der Bezeichner der Datenbanktabelle.</li>
     * <li>In der zweiten Zeile stehen durch Kommata getrennt die Bezeichner der Spalten der
     * Datenbanktabelle.</li>
     * <li>Es folgen die Zeilen der Datenbanktabelle in der Reihenfolge, in der sie in der
     * Datenbanktabelle gespeichert sind, in einer einzigen Zeile. Die Felder der Zeilen werden
     * durch Kommata voneinander getrennt und hintereinander gereiht.</li>
     * </ul>
     *
     * Da in den Tabellenfeldern alle Zeichen erlaubt sind, werden sowohl Kommata (",") als auch
     * Backslashes ("\") bei dieser Ausgabe jeweils mit einem Backslash ("\") gequotet.
     *
     * Es werden stets Unix-Zeilenumbrüche (\n) verwendet.
     *
     * @return Dateirepräsentation der Datenbanktabelle.
     */
    public String toFile() {
        StringBuilder sb = new StringBuilder();

        sb.append(id);
        sb.append(LINEBREAK);

        for (String colId : head) {
            sb.append(colId);
            sb.append(COMMA);
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append(LINEBREAK);

        for (String[] row : rows) {
            for (String colId : head) {
                int index = getColIndexForColId(colId);
                String value = row[index];
                sb.append(value.replaceAll("(,|\\\\)", "\\\\$1"));
                sb.append(COMMA);
            }
        }

        if (!rows.isEmpty()) {
            // letztes Komma löschen
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();

    }

    /**
     * Prüft, ob die Zeichenkette str ein gültiger Bezeichner für eine Datenbanktabelle bzw. die
     * Spalte einer Datenbanktabelle ist. Ein valider Bezeichner besteht aus einem Zeichen aus der
     * Menge [a-zA-Z] gefolgt von einer beliebigen Anzahl von Zeichen aus der Menge [a-zA-Z0-9_].
     *
     * @param str Zeichenkette, die geprüft wird.
     *
     * @return boolscher Wert, der angibt, ob die Zeichenkette str ein gültiger Bezeichner ist.
     */
    public static boolean isValidIdentifier(final String str) {
        return str != null && VALID_INDENTIFIER_PREDICATE.test(str);
    }

    /**
     * Prüft, ob in dem Array aus Zeichenketten strs nur gültige Bezeichner für eine
     * Datenbanktabelle bzw. die Spalte einer Datenbanktabelle ist. Ein valider Bezeichner besteht
     * aus einem Zeichen aus der Menge [a-zA-Z] gefolgt von einer beliebigen Anzahl von Zeichen aus
     * der Menge [a-zA-Z0-9_].
     *
     * @param strs StringArray, dessen Elemente geprüft werden.
     *
     * @return boolscher Wert, der angibt, ob das Array strs nur gültige Bezeichner enthält.
     */
    public static boolean areValidIdentifiers(final String[] strs) {
        for (String str : Optional.ofNullable(strs).orElse(new String[0])) {
            if (!isValidIdentifier(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft ob alle übergebenen Strings eindeutig sind. Sollte mindestens eine Zeichenkette doppelt
     * vorkommen, so gibt die Methode false zurück.
     *
     * @param strs Die Liste mit Bezeichnern
     * @return true, wenn alle Zeichenketten einmalig sind
     */
    public static boolean areOnlyUniqueValues(final String[] strs) {
        Set<String> uniques = new HashSet<>();

        for (String str : Optional.ofNullable(strs).orElse(new String[0])) {
            if (!uniques.add(str)) {
                return false;
            }
        }
        return true;
    }

    // ~~ private Methods

    /**
     * @param colIdx Index der Column
     * @param row Zeile die zurrückgegeben wird
     * @return Zeile
     */
    private String getAttributeFromRow(int colIdx, String[] row) {
        return row[colIdx];
    }

    /**
     * @param colId sdfsdf
     * @return sdfsf
     */
    private int getColIndexForColId(String colId) {
        return getColIndexForColId(colId, this);
    }

    /**
     * @param colId fgbfgbfg
     * @param table sdfsd
     * @return ghjghj
     */
    private int getColIndexForColId(String colId, DBTable table) {
        return table.head.indexOf(colId);
    }

    /**
     * @param p hjkhj
     * @param colIdx hjkhjk
     * @param row hjkhjk
     * @return ghjghj
     */
    private boolean testPredicateOnRow(Predicate<String> p, int colIdx, String[] row) {
        return p.test(getAttributeFromRow(colIdx, row));
    }

    /**
     * @param row jkljkl
     * @param someColIds jkljkl
     * @return ghjghj
     */
    private String[] projectRow(String[] row, String... someColIds) {
        String[] res = new String[someColIds.length];
        for (int i = 0; i < someColIds.length; i++) {
            String someColId = someColIds[i];
            res[i] = row[getColIndexForColId(someColId)];
        }
        return res;
    }

}
