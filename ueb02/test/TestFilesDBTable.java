import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import db.DBTable;
import fileio.WrongSyntaxException;
import java.io.IOException;

/**
 * Beispielhafte Tests zum Dateivergleich.
 *
 * @author kar, mhe
 */
public class TestFilesDBTable {
    /** Testtabelle mit einer Spalte. */
    private DBTable tab1 = null;

    /**
     * Intialisierung für alle Tests.
     */
    @Before
    public void setUp() {
        this.tab1 = this.createTable1();
    }

    /**
     * Aufräummethode für alle Tests. Die Instanzen der Tabellenklasse werden gelöscht.
     */
    @After
    public void tearDown() {
        this.tab1 = null;
    }

    @Test
    public final void testWriteFile() throws IOException {
        TestToolkit.writeAndAssert(tab1, "simple");
    }

    @Test
    public final void testReadFile() throws IOException, WrongSyntaxException {
        DBTable tab = TestToolkit.read("simple");

        Assert.assertEquals(1, tab.getColCnt());
        Assert.assertEquals(3, tab.getRowCnt());
        assertEquals(TestValues.TABLE_1_ID, tab.getId());

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
        tab.appendRow(TestValues.TABLE_1_ROWS[1]);
        tab.appendRow(TestValues.TABLE_1_ROWS[2]);

        return tab;
    }

}
