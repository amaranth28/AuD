import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.DB;

/**
 * Grundlegende Tests für Operationen auf einer leeren Datenbank.
 *
 * @author kar, mhe
 */
public final class TestEmptyDB {

    /** Die Datenbank, die getestet wird. */
    private DB db = null;

    /** Intialisierung für alle Tests. Leere Datenbank wird erzeugt. */
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

    /** Test für den Konstruktor {@link DB#DB(java.lang.String)}. */
    @Test
    public void testDB() {
        // Konstruktoraufruf in setUp
    }

    /** Test für die Methode {@link DB#getId()}. */
    @Test
    public void testGetId() {
        assertEquals(TestValues.DB_ID, this.db.getId());
    }

    /** Test für die Methode {@link DB#getTableCnt()}. */
    @Test
    public void testTableCnt() {
        assertEquals(0, db.getTableCnt());
    }

    /** Test für die Methode {@link DB#toString()}. */
    @Test
    public void testToString() {
        String[] strs = null;

        strs = this.db.toString().split("\n");

        assertEquals(1, strs.length);
        assertEquals("Datenbankname: " + TestValues.DB_ID, strs[0]);
    }

    /**
     * Erzeugt eine leere Datenbank mit dem Bezeichner {@link TestValues#DB_ID}.
     *
     * @return die erzeugte Datenbank.
     */
    private DB createDB() {
        final DB res = new DB(TestValues.DB_ID);

        return res;
    }

}
