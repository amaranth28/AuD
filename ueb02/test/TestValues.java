/**
 * Werte, die von den Testklassen genutzt werden.
 *
 * @author kar, mhe
 */
public interface TestValues {
    /** Bezeichner der Testdatenbank. */
    String DB_ID = "Meine_Datenbank";

    /** Bezeichner der ersten Testtabelle. */
    String TABLE_1_ID = "Tabelle_1";

    /** Spaltenbezeichner der ersten Testtabelle. */
    String[] TABLE_1_COL_IDS = { "Vorname" };

    /** Zeilen der ersten Testtabelle. */
    String[][] TABLE_1_ROWS = { { "Helga" }, { "Malte" }, { "Christian" } };

    /** Bezeichner der zweiten Testtabelle. */
    String TABLE_2_ID = "Tabelle_2";

    /** Spaltenbezeichner der zweiten Testtabelle. */
    String[] TABLE_2_COL_IDS = { "ID", "Position" };

    /** Zeilen der zweiten Testtabelle. */
    String[][] TABLE_2_ROWS =
            { { "1", "Assistenten" }, { "2", "Dozenten" }, { "3", "Verwaltung" } };

    /** Bezeichner der dritten Testtabelle. */
    String TABLE_3_ID = "Tabelle_3";

    /** Spaltenbezeichner der dritten Testtabelle. */
    String[] TABLE_3_COL_IDS = { "Vorname", "Nachname", "Position_ID" };

    /** Zeilen der zweiten Testtabelle. */
    String[][] TABLE_3_ROWS = { { "Malte", "Heins", "1" }, { "Helga", "Karafiat", "1" },
            { "Christian", "Uhlig", "2" }, { "Bettina", "Otto", "3" } };

    /**
     * Bezeichner von Tabellen und Spalten, die nicht in der Datenbank vorkommen.
     */
    String UNKNOWN_ID = "Not_available";

}
