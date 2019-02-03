import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.DBTable;

/**
 * Grundlegende Tests für Operationen auf Tabellen mit einer Zeile.
 *
 * @author kar, mhe
 */
public class TestOneRowDBTable {

    /** Testtabelle mit einer Spalte. */
    private DBTable tab1 = null;

    /** Testtabelle mit zwei Spalten. */
    private DBTable tab2 = null;

    /** Testtabelle mit drei Spalten. */
    private DBTable tab3 = null;

    /**
     * Intialisierung für alle Tests. Drei Tabellen mit je einer Zeile werden erzeugt.
     */
    @Before
    public void setUp() {
        this.tab1 = this.createTable1();
        this.tab2 = this.createTable2();
        this.tab3 = this.createTable3();
    }

    /**
     * Aufräummethode für alle Tests. Die Instanzen der Tabellenklasse werden gelöscht.
     */
    @After
    public void tearDown() {
        this.tab1 = null;
        this.tab2 = null;
        this.tab3 = null;
    }

    /** Test für die Methode {@link DBTable#appendRow(String[])}. */
    @Test
    public final void testAppendRow() {
        // setUp macht alles
    }

    /** Test für die Methode {@link DBTable#getId()}. */
    @Test
    public final void testGetId() {
        assertEquals(TestValues.TABLE_1_ID, this.tab1.getId());
        assertEquals(TestValues.TABLE_2_ID, this.tab2.getId());
        assertEquals(TestValues.TABLE_3_ID, this.tab3.getId());
    }

    /** Test für die Methode {@link DBTable#getColCnt()}. */
    @Test
    public final void testGetColCnt() {
        assertEquals(TestValues.TABLE_1_COL_IDS.length, this.tab1.getColCnt());
        assertEquals(TestValues.TABLE_2_COL_IDS.length, this.tab2.getColCnt());
        assertEquals(TestValues.TABLE_3_COL_IDS.length, this.tab3.getColCnt());
    }

    /** Test für die Methode {@link DBTable#getRowCnt()}. */
    @Test
    public final void testGetRowCnt() {
        assertEquals(1, this.tab1.getRowCnt());
        assertEquals(1, this.tab2.getRowCnt());
        assertEquals(1, this.tab3.getRowCnt());
    }

    /** Test für die Methode {@link DBTable#hasCol(String)}. */
    @Test
    public final void testHasCol() {
        assertTrue(this.tab1.hasCol(TestValues.TABLE_1_COL_IDS[0]));

        assertTrue(this.tab2.hasCol(TestValues.TABLE_2_COL_IDS[0]));
        assertTrue(this.tab2.hasCol(TestValues.TABLE_2_COL_IDS[1]));

        assertTrue(this.tab3.hasCol(TestValues.TABLE_3_COL_IDS[0]));
        assertTrue(this.tab3.hasCol(TestValues.TABLE_3_COL_IDS[1]));
        assertTrue(this.tab3.hasCol(TestValues.TABLE_3_COL_IDS[2]));
    }

    /** Test für die Methode {@link DBTable#getColIds()}. */
    @Test
    public final void testGetColIds() {
        assertArrayEquals(TestValues.TABLE_1_COL_IDS, this.tab1.getColIds().toArray());
        assertArrayEquals(TestValues.TABLE_2_COL_IDS, this.tab2.getColIds().toArray());
        assertArrayEquals(TestValues.TABLE_3_COL_IDS, this.tab3.getColIds().toArray());
    }

    /** Test für die Methode {@link DBTable#toString()}. */
    @Test
    public final void testToString() {
        String[] strs = null;

        strs = this.tab1.toString().split("\n");

        assertEquals(2, strs.length);
        assertEquals(TestValues.TABLE_1_COL_IDS[0], strs[0]);
        assertEquals(TestValues.TABLE_1_ROWS[0][0], strs[1]);

        strs = this.tab2.toString().split("\n");

        assertEquals(2, strs.length);
        assertEquals(TestValues.TABLE_2_COL_IDS[0] + "," + TestValues.TABLE_2_COL_IDS[1], strs[0]);
        assertEquals(TestValues.TABLE_2_ROWS[0][0] + "," + TestValues.TABLE_2_ROWS[0][1], strs[1]);

        strs = this.tab3.toString().split("\n");

        assertEquals(2, strs.length);
        assertEquals(TestValues.TABLE_3_COL_IDS[0] + "," + TestValues.TABLE_3_COL_IDS[1] + ","
                + TestValues.TABLE_3_COL_IDS[2], strs[0]);
        assertEquals(TestValues.TABLE_3_ROWS[0][0] + "," + TestValues.TABLE_3_ROWS[0][1] + ","
                + TestValues.TABLE_3_ROWS[0][2], strs[1]);
    }

    /** Test für die Methode {@link DBTable#toFile()}. */
    @Test
    public final void testToFile() {
        String[] strs = null;

        strs = this.tab1.toFile().split("\n");

        assertEquals(3, strs.length);
        assertEquals(TestValues.TABLE_1_ID, strs[0]);
        assertEquals(TestValues.TABLE_1_COL_IDS[0], strs[1]);
        assertEquals(TestValues.TABLE_1_ROWS[0][0], strs[2]);

        strs = this.tab2.toFile().split("\n");

        assertEquals(3, strs.length);
        assertEquals(TestValues.TABLE_2_ID, strs[0]);
        assertEquals(TestValues.TABLE_2_COL_IDS[0] + "," + TestValues.TABLE_2_COL_IDS[1], strs[1]);
        assertEquals(TestValues.TABLE_2_ROWS[0][0] + "," + TestValues.TABLE_2_ROWS[0][1], strs[2]);

        strs = this.tab3.toFile().split("\n");

        assertEquals(3, strs.length);
        assertEquals(TestValues.TABLE_3_ID, strs[0]);
        assertEquals(TestValues.TABLE_3_COL_IDS[0] + "," + TestValues.TABLE_3_COL_IDS[1] + ","
                + TestValues.TABLE_3_COL_IDS[2], strs[1]);
        assertEquals(TestValues.TABLE_3_ROWS[0][0] + "," + TestValues.TABLE_3_ROWS[0][1] + ","
                + TestValues.TABLE_3_ROWS[0][2], strs[2]);

    }

    /**
     * Erzeugt eine Tabelle mit dem Bezeichner {@link TestValues#TABLE_1_ID}, den Spalten
     * {@link TestValues#TABLE_1_COL_IDS} und einer Zeile, die dem ersten Wert von
     * {@link TestValues#TABLE_1_ROWS} entspricht.
     *
     * @return die erzeugte Tabelle.
     */
    private DBTable createTable1() {
        final DBTable tab = new DBTable(TestValues.TABLE_1_ID, TestValues.TABLE_1_COL_IDS);

        tab.appendRow(TestValues.TABLE_1_ROWS[0]);

        return tab;
    }

    /**
     * Erzeugt eine Tabelle mit dem Bezeichner {@link TestValues#TABLE_2_ID}, den Spalten
     * {@link TestValues#TABLE_2_COL_IDS} und einer Zeile, die dem ersten Wert von
     * {@link TestValues#TABLE_2_ROWS} entspricht.
     *
     * @return die erzeugte Tabelle.
     */
    private DBTable createTable2() {
        final DBTable tab = new DBTable(TestValues.TABLE_2_ID, TestValues.TABLE_2_COL_IDS);

        tab.appendRow(TestValues.TABLE_2_ROWS[0]);

        return tab;
    }

    /**
     * Erzeugt eine Tabelle mit dem Bezeichner {@link TestValues#TABLE_3_ID}, den Spalten
     * {@link TestValues#TABLE_3_COL_IDS} und einer Zeile, die dem ersten Wert von
     * {@link TestValues#TABLE_3_ROWS} entspricht.
     *
     * @return die erzeugte Tabelle.
     */
    private DBTable createTable3() {
        final DBTable tab = new DBTable(TestValues.TABLE_3_ID, TestValues.TABLE_3_COL_IDS);

        tab.appendRow(TestValues.TABLE_3_ROWS[0]);

        return tab;
    }

}
