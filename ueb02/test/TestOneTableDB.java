import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.DB;
import db.DBTable;

/**
 * Grundlegende Tests für Operationen einer Datenbank mit einer Tabelle.
 *
 * @author kar, mhe
 */
public final class TestOneTableDB {
    /** Die Datenbank, die getestet wird. */
    private DB db = null;

    /**
     * Intialisierung für alle Tests. Datenbank mit einer Tabelle wird erzeugt.
     */
    @Before
    public void setUp() {
        this.db = this.createDB();
    }

    /**
     * Aufräummethode für alle Tests. Die Instanz der Datenbankklasse wird gelöscht.
     */
    @After
    public void tearDown() {
        this.db = null;
    }

    /** Test für die Methode {@link DB#addTable(DBTable)}. */
    @Test
    public void testAddTable() {
        // setUp macht alles
    }

    /** Test für die Methode {@link DB#getId()}. */
    @Test
    public void testGetId() {
        assertEquals(TestValues.DB_ID, this.db.getId());
    }

    /** Test für die Methode {@link DB#getTableCnt()}. */
    @Test
    public void testTableCnt() {
        assertEquals(1, db.getTableCnt());
    }

    /** Test für die Methode {@link DB#tableExists(java.lang.String)}. */
    @Test
    public void testTableExists() {
        assertTrue(db.tableExists(TestValues.TABLE_1_ID));

        assertFalse(db.tableExists(TestValues.UNKNOWN_ID));
    }

    /** Test für die Methode {@link DB#getTableIds()}. */
    @Test
    public void testGetTableIds() {
        final List<String> ids = this.db.getTableIds();

        assertEquals(1, ids.size());
        assertEquals(TestValues.TABLE_1_ID, ids.get(0));
    }

    /** Test für die Methode {@link DB#getTable(java.lang.String)}. */
    @Test
    public void testGetTable() {
        DBTable tab = null;

        tab = this.db.getTable(TestValues.TABLE_1_ID);
        assertTrue(tab != null);
        assertEquals(TestValues.TABLE_1_ID, tab.getId());
        assertEquals(TestValues.TABLE_1_COL_IDS.length, tab.getColCnt());

        tab = this.db.getTable(TestValues.UNKNOWN_ID);
        assertEquals(null, tab);
    }

    /** Test für die Methode {@link DB#toString()}. */
    @Test
    public void testToString() {
        String[] strs = null;

        strs = this.db.toString().split("\n");

        assertEquals(8, strs.length);
        assertEquals("Datenbankname: " + TestValues.DB_ID, strs[0]);
        assertEquals("", strs[1]);
        assertEquals("Tabellenname: " + TestValues.TABLE_1_ID, strs[2]);
        assertEquals("", strs[3]);
        assertEquals(TestValues.TABLE_1_COL_IDS[0], strs[4]);
        assertEquals(TestValues.TABLE_1_ROWS[0][0], strs[5]);
        assertEquals(TestValues.TABLE_1_ROWS[1][0], strs[6]);
        assertEquals(TestValues.TABLE_1_ROWS[2][0], strs[7]);
    }

    /**
     * Erzeugt eine Datenbank mit dem Bezeichner {@link TestValues#DB_ID} und fügt die in
     * {@link #createTable} erzeugte Tabelle ein.
     *
     * @return die erzeugte Datenbank.
     */
    private DB createDB() {
        final DB res = new DB(TestValues.DB_ID);

        res.addTable(this.createTable());

        return res;
    }

    /**
     * Erzeugt eine Tabelle mit dem Bezeichner {@link TestValues#TABLE_1_ID}, den Spalten
     * {@link TestValues#TABLE_1_COL_IDS} und den Zeilen {@link TestValues#TABLE_1_ROWS}.
     *
     * @return die erzeugte Tabelle.
     */
    private DBTable createTable() {
        final DBTable tab = new DBTable(TestValues.TABLE_1_ID, TestValues.TABLE_1_COL_IDS);

        for (int i = 0; i < TestValues.TABLE_1_ROWS.length; ++i) {
            tab.appendRow(TestValues.TABLE_1_ROWS[i]);
        }

        return tab;
    }

}
