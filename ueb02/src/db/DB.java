package db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * In einer Datenbank werden Datenbanktabellen verwaltet, die jeweils durch einen eindeutigen
 * Bezeichner (Datentyp String) identifiziert werden. Auch die Datenbank selbst hat einen Bezeichner
 * (Datentyp String).
 * 
 * Ein valider Bezeichner besteht stets aus einem Zeichen aus der Menge [a-zA-Z] gefolgt von einer
 * beliebigen Anzahl von Zeichen aus der Menge [a-zA-Z0-9_].
 *
 * @author kar, mhe, Cedric Heinrich, Clemens Heinrich
 */
public final class DB {

    /** Der Name der Datenbank */
    private String id;

    /** Die Datenbanktabellen */
    private Map<String, DBTable> tables;

    /**
     * Erzeugt eine leere Datenbank mit dem Bezeichner anId.
     *
     * @param anId Bezeichner der Datenbank, die erzeugt werden soll.
     *
     * @pre der Bezeichner anId muss gültig sein.
     */
    public DB(final String anId) {
        assert DBTable.isValidIdentifier(anId);

        id = anId;
        tables = new TreeMap<>();
    }

    /**
     * Fügt die Tabelle tab in die Datenbank ein.
     *
     * @param tab Tabelle, die in die Datenbank eingefügt werden soll.
     *
     * @pre es darf keine Tabelle mit demselben Bezeichner wie dem von tab in der Datenbank
     *      existieren.
     *
     */
    public void addTable(final DBTable tab) {
        assert !tableExists(tab.getId());

        tables.put(tab.getId(), tab);
    }

    /**
     * Entfernt die Tabelle mit dem Bezeichner anId aus der Datenbank.
     *
     * @param anId Bezeichner der Tabelle, die aus der Datenbank entfernt werden soll.
     *
     * @pre der Bezeichner anId muss gültig sein.
     *
     * @post in der Datenbank befindet sich keine Tabelle mit dem Bezeichner anId.
     */
    public void removeTable(final String anId) {
        assert DBTable.isValidIdentifier(anId);

        tables.remove(anId);

        assert !tableExists(anId);
    }

    /**
     * Entfernt alle Tabellen aus der Datenbank.
     *
     * @post die Datenbank enthält keine Tabellen.
     */
    public void removeAllTables() {
        tables.clear();

        assert getTableCnt() == 0;
    }

    /**
     * Liefert eine aufsteigend sortierte Liste der Tabellenbezeichner.
     *
     * @return aufsteigend sortierte Liste der Tabellenbezeichner.
     */
    public List<String> getTableIds() {
        List<String> tableIds = new ArrayList<>(tables.keySet());
        Collections.sort(tableIds);
        return tableIds;
    }

    /**
     * Gibt an, ob eine Tabelle mit dem Bezeichner anId in der Datenbank existiert.
     *
     * @param anId Bezeichner der Tabelle, deren Existenz geprüft werden soll.
     *
     * @pre der Bezeichner anId muss gültig sein.
     *
     * @return boolscher Wert, der angibt, ob eine Tabelle mit dem Bezeichner anId in der Datenbank
     *         existiert.
     */
    public boolean tableExists(final String anId) {
        assert DBTable.isValidIdentifier(anId);

        return tables.containsKey(anId);
    }

    /**
     * Liefert die Tabelle mit dem Bezeichner anId.
     *
     * @param anId Bezeichner der Tabelle, die geliefert werden soll.
     *
     * @pre der Bezeichner anId muss gültig sein.
     *
     * @return Tabelle mit dem Bezeichner anId (falls vorhanden, sonst NULL-Referenz).
     */
    public DBTable getTable(final String anId) {
        assert DBTable.isValidIdentifier(anId);

        return tables.get(anId);
    }

    /**
     * Liefert die Stringrepräsentation der Datenbank. Die Stringrepräsentation ist wie folgt
     * aufgebaut:
     * <ul>
     * <li>In der ersten Zeile steht <code>Datenbankname: </code> gefolgt von dem Bezeichner der
     * Datenbank.</li>
     * <li>Die zweite Zeile ist eine Leerzeile.</li>
     * <li>Es folgen Datenbanktabellen in aufsteigender Reihenfolge ihrer Bezeichner in folgender
     * Form:
     * <ul>
     * <li>Vor jeder Tabelle steht <code>Tabellenname: </code> gefolgt von dem Bezeichner der
     * Datenbanktabelle.</li>
     * <li>Danach folgt eine Leerzeile.</li>
     * <li>Es folgt die Stringrepräsentation der Datenbanktabelle.</li>
     * </ul>
     * </li>
     * <li>Nach jeder Tabelle folgt eine Leerzeile.</li>
     * </ul>
     *
     * @return die Stringrepräsentation der Datenbank.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        final String lineSeperator = "\n"; // System.lineSeperator()
        sb.append("Datenbankname: " + id);
        sb.append(lineSeperator);
        sb.append(lineSeperator);

        Iterator<String> iterator = tables.keySet().iterator();
        while (iterator.hasNext()) {
            String tabId = iterator.next();
            DBTable tab = tables.get(tabId);
            sb.append("Tabellenname: " + tabId + lineSeperator);
            sb.append(lineSeperator);
            sb.append(tab.toString());
            sb.append(lineSeperator);

            if (iterator.hasNext()) {
                sb.append(lineSeperator);
            } else {
                sb.deleteCharAt(sb.length() - 1);
            }
        }

        return sb.toString();
    }

    /**
     * Liefert die Anzahl der Tabellen in der Datenbank.
     *
     * @return Anzahl der Tabellen in der Datenbank.
     */
    public int getTableCnt() {
        return tables.size();
    }

    /**
     * Liefert den Bezeichner der Datenbank.
     *
     * @return Bezeichner der Datenbank.
     */
    public String getId() {
        return id;
    }

}
