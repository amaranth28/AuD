import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.DBTable;

public class TestDBTable {

    private DBTable tab1;
    private DBTable tab2;
    private DBTable tab3;

    @Before
    public void setUp() {
        this.tab1 = this.createTable1();
        this.tab2 = this.createTable2();
        this.tab3 = this.createTable3();
    }

    @After
    public void tearDown() {
        this.tab1 = null;
        this.tab2 = null;
        this.tab3 = null;
    }

    @Test(expected = AssertionError.class)
    public void testRemoveInvalidId() {
        tab1.removeRows("notAValidId", s -> true);
    }

    @Test(expected = AssertionError.class)
    public void testRemoveNotPresentId() {
        tab1.removeRows("NotAColId", s -> true);
    }

    @Test
    public void testRemoveOneRow() {
        assertEquals(3, tab1.getRowCnt());
        tab1.removeRows(TestValues.TABLE_1_COL_IDS[0], s -> s.equals("Christian"));
        assertEquals(2, tab1.getRowCnt());
    }

    @Test
    public void testRemoveAllRows() {
        assertEquals(3, tab1.getRowCnt());
        tab1.removeRows(TestValues.TABLE_1_COL_IDS[0], s -> true);
        assertEquals(0, tab1.getRowCnt());
    }

    @Test
    public void testRemoveNoRows() {
        assertEquals(3, tab1.getRowCnt());
        tab1.removeRows(TestValues.TABLE_1_COL_IDS[0], s -> false);
        assertEquals(3, tab1.getRowCnt());
    }

    @Test
    public void testSelectOneRow() {
        DBTable tab =
                tab1.select(TestValues.TABLE_1_COL_IDS[0], s -> s.startsWith("Ma"), "NewTableId");

        assertEquals("NewTableId", tab.getId());
        assertEquals(tab1.getColIds(), tab.getColIds());
        assertEquals(1, tab.getRowCnt());
    }

    @Test
    public void testSelectAllRows() {
        DBTable tab = tab1.select(TestValues.TABLE_1_COL_IDS[0], s -> true, "NewTableId");

        assertEquals(tab1.getRowCnt(), tab.getRowCnt());
    }

    @Test
    public void testSelectNoRows() {
        DBTable tab = tab1.select(TestValues.TABLE_1_COL_IDS[0], s -> false, "NewTableId");

        assertEquals(0, tab.getRowCnt());
    }

    @Test
    public void testEquiJoinTab2IDTab3PositionID() {
        DBTable tab = tab2.equijoin(tab3, TestValues.TABLE_2_COL_IDS[0],
                TestValues.TABLE_3_COL_IDS[2], "EquiJoinedTab2Tab3");

        assertEquals("EquiJoinedTab2Tab3", tab.getId());
        assertEquals(Arrays.asList("Tabelle_2_ID", "Tabelle_2_Position", "Tabelle_3_Vorname",
                "Tabelle_3_Nachname", "Tabelle_3_Position_ID"), tab.getColIds());
        assertEquals(
                "Tabelle_2_ID,Tabelle_2_Position,Tabelle_3_Vorname,Tabelle_3_Nachname,Tabelle_3_Position_ID\n"
                        + "1,Assistenten,Malte,Heins,1\n" + "1,Assistenten,Helga,Karafiat,1\n"
                        + "2,Dozenten,Christian,Uhlig,2\n" + "3,Verwaltung,Bettina,Otto,3",
                tab.toString());

    }

    @Test
    public void testEquiJoinTab2IDTab3Vorname() {
        DBTable tab = tab2.equijoin(tab3, TestValues.TABLE_2_COL_IDS[0],
                TestValues.TABLE_3_COL_IDS[0], "EquiJoinedTab2Tab3");

        assertEquals(Arrays.asList("Tabelle_2_ID", "Tabelle_2_Position", "Tabelle_3_Vorname",
                "Tabelle_3_Nachname", "Tabelle_3_Position_ID"), tab.getColIds());
        assertEquals(0, tab.getRowCnt());
        assertEquals(
                "Tabelle_2_ID,Tabelle_2_Position,Tabelle_3_Vorname,Tabelle_3_Nachname,Tabelle_3_Position_ID",
                tab.toString());

    }

    private DBTable createTable1() {
        final DBTable tab = new DBTable(TestValues.TABLE_1_ID, TestValues.TABLE_1_COL_IDS);

        tab.appendRow(TestValues.TABLE_1_ROWS[0]);
        tab.appendRow(TestValues.TABLE_1_ROWS[1]);
        tab.appendRow(TestValues.TABLE_1_ROWS[2]);

        return tab;
    }

    private DBTable createTable2() {
        final DBTable tab = new DBTable(TestValues.TABLE_2_ID, TestValues.TABLE_2_COL_IDS);

        tab.appendRow(TestValues.TABLE_2_ROWS[0]);
        tab.appendRow(TestValues.TABLE_2_ROWS[1]);
        tab.appendRow(TestValues.TABLE_2_ROWS[2]);

        return tab;
    }

    private DBTable createTable3() {
        final DBTable tab = new DBTable(TestValues.TABLE_3_ID, TestValues.TABLE_3_COL_IDS);

        tab.appendRow(TestValues.TABLE_3_ROWS[0]);
        tab.appendRow(TestValues.TABLE_3_ROWS[1]);
        tab.appendRow(TestValues.TABLE_3_ROWS[2]);
        tab.appendRow(TestValues.TABLE_3_ROWS[3]);

        return tab;
    }

}
