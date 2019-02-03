import db.DB;
import db.DBTable;
import db.SortDirection;
import fileio.FileUtilImpl;
import fileio.WrongSyntaxException;

import java.io.IOException;
import java.util.function.Predicate;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomTestDBTable {

    private DBTable table;
    
    /** Intialisierung für alle Tests. Leere Datenbank wird erzeugt. */
    @Before
    public void setUp()
    {
        this.table = new DBTable("Tabelle", new String[]{"s1", "s2", "s3", "testwert", "Peter", "Panda", "FAHRRad"});
        this.table.appendRow(new String[]{"ersterWert", "etwas", "Keks", "Wochenende", "Hallo", "toll", "123"});
        this.table.appendRow(new String[]{"Klaus/n", "ging", "gestern///", "zum", "Bäcker", "u1/m", "Lotto"});
        this.table.appendRow(new String[]{"zu", "spielen", "/, er", "erhofft", "sich", "Gewinne", "in"});
        this.table.appendRow(new String[]{"höhe", "von", "30", "Millionen", "/!", " ", "Nichts"});
    }

    /**
     * Aufräummethode für alle Tests. Die Instanz der Datenbankklasse wird
     * gelöscht.
     */
    @After
    public void tearDown()
    {
        this.table = null;
    }

    @Test
    public void testTableCreation() {
        Assert.assertEquals("Tabelle", table.getId());
        Assert.assertEquals(7, table.getColCnt());
        Assert.assertEquals(4, table.getRowCnt());
        Assert.assertTrue(table.hasCol("s1"));
        Assert.assertTrue(table.hasCol("s2"));
        Assert.assertTrue(table.hasCol("s3"));
        Assert.assertFalse(table.hasCol("s4"));
        Assert.assertTrue(table.hasCols(new String[]{"s1","s2","s3"}));
        Assert.assertTrue(table.hasCols(new String[]{"s1","s3"}));
        Assert.assertTrue(table.hasCols(new String[]{"s2","s3"}));
        Assert.assertTrue(table.hasCols(new String[]{"s3","s1","s3"}));
        Assert.assertFalse(table.hasCols(new String[]{"s1","s2","s3","s4"}));
        Assert.assertFalse(table.hasCols(new String[]{"s0","s2"}));
        Assert.assertArrayEquals(new String[]{"s1", "s2", "s3", "testwert", "Peter", "Panda", "FAHRRad"},
                table.getColIds().toArray(new String[table.getColIds().size()]));
    }
    
    @Test
    public void testSort() {
        this.table.sort("s1", SortDirection.DESC);
        Assert.assertEquals("s1,s2,s3,testwert,Peter,Panda,FAHRRad\n" +
                            "zu,spielen,/, er,erhofft,sich,Gewinne,in\n" +
                            "Klaus/n,ging,gestern///,zum,Bäcker,u1/m,Lotto\n" +
                            "höhe,von,30,Millionen,/!, ,Nichts\n" +
                            "ersterWert,etwas,Keks,Wochenende,Hallo,toll,123", 
                            this.table.toString());
        this.table.sort("s1", SortDirection.ASC);
        Assert.assertEquals("s1,s2,s3,testwert,Peter,Panda,FAHRRad\n" +
                            "ersterWert,etwas,Keks,Wochenende,Hallo,toll,123\n" +
                            "höhe,von,30,Millionen,/!, ,Nichts\n" +
                            "Klaus/n,ging,gestern///,zum,Bäcker,u1/m,Lotto\n" +
                            "zu,spielen,/, er,erhofft,sich,Gewinne,in",
                            this.table.toString());
    }
    
    @Test
    public void testRemoveRows() {
        this.table.removeRows("s1", new EqualsPredicate("ersterWert"));
        this.table.sort("s1", SortDirection.ASC);
        Assert.assertEquals("s1,s2,s3,testwert,Peter,Panda,FAHRRad\n" +
             "höhe,von,30,Millionen,/!, ,Nichts\n" +  
                "Klaus/n,ging,gestern///,zum,Bäcker,u1/m,Lotto\n" +
                
                "zu,spielen,/, er,erhofft,sich,Gewinne,in",
                this.table.toString());
    }
    
    @Test
    public void testSelect() {
        DBTable neu = this.table.select("FAHRRad", new EqualsPredicate("in"), "Neu");
        Assert.assertEquals("Neu", neu.getId());
        Assert.assertEquals("s1,s2,s3,testwert,Peter,Panda,FAHRRad\n" +
                            "zu,spielen,/, er,erhofft,sich,Gewinne,in",
                            neu.toString());
    }
    
    @Test
    public void testProject() {
        DBTable neu = this.table.project(new String[]{"s3","Panda","Peter"},
                "NichtDieTabelleNachDerWirSuchen");
        Assert.assertEquals("NichtDieTabelleNachDerWirSuchen", neu.getId());
        neu.sort("s3", SortDirection.ASC);
        Assert.assertEquals("s3,Panda,Peter\n" +              
                "/, er,Gewinne,sich\n" +
                "30, ,/!\n" +
                "gestern///,u1/m,Bäcker\n" +
                "Keks,toll,Hallo",
                neu.toString());
    }
    
    @Test
    public void testEquijoin(){
        DBTable db1 = new DBTable("Etwas", new String[] {"Hat","Werte"});
        db1.appendRow(new String[]{"doch", "nicht"});
        DBTable db2 = new DBTable("Anderes", new String[] {"Mit","GanzAnderenWerten"}); 
        db2.appendRow(new String[]{"doch", "Gleich"});
        db2.appendRow(new String[]{"wie", "zuvor"});
        DBTable result = db1.equijoin(db2, "Hat", "Mit", "neueTabelle");
        DBTable expected = new DBTable("neueTabelle",  new String[]{"Etwas_Hat", "Etwas_Werte", "Anderes_Mit", "Anderes_GanzAnderenWerten"});
        expected.appendRow(new String[]{"doch", "nicht", "doch", "Gleich"});
        Assert.assertEquals(expected.getColIds(), result.getColIds());
        
        Assert.assertEquals(expected.toString(), result.toString());
        
        DBTable db3 = new DBTable("NochEins", new String[] {"s1","s2", "s3"});
        db3.appendRow(new String[]{"doch", "wert2", ""});
        db3.appendRow(new String[]{"", "", ""});
        
        DBTable expected2 = new DBTable("neueTabelle2",  new String[]{"Etwas_Hat", "Etwas_Werte", "NochEins_s1","NochEins_s2", 
                "NochEins_s3"});
        expected2.appendRow(new String[]{"doch", "nicht", "doch", "wert2", ""});
        Assert.assertEquals(expected2.toString(), db1.equijoin(db3, "Hat", "s1", "neueTabelle2").toString());
        
        DBTable t1 = new DBTable("T1", new String[]{"S1", "S2"});
        DBTable t2 = new DBTable("T2", new String[]{"S1", "S3", "S4"});
        t1.appendRow(new String[]{"W1a", "W2a"});
        t1.appendRow(new String[]{"W1b", "W2b"});
        t2.appendRow(new String[]{"W1a","W3a","W4a"});
        t2.appendRow(new String[]{"W1b","W3b","W4b"});
        t2.appendRow(new String[]{"W1b","W3c","W4c"});
        DBTable neu = t1.equijoin(t2, "S1", "S1", "neu");
        Assert.assertEquals("T1_S1,T1_S2,T2_S1,T2_S3,T2_S4\nW1a,W2a,W1a,W3a,W4a\nW1b,W2b,W1b,W3b,W4b\nW1b,W2b,W1b,W3c,W4c", neu.toString());
    }
    
    @Test
    public void testToFile(){
        String s = this.table.toFile();
        try {
            TestToolkit.writeAndAssert(this.table, "lotto");
            DBTable in = TestToolkit.read("lotto");
            Assert.assertEquals(this.table.toString(), in.toString());
        }
        catch(IOException | WrongSyntaxException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void testFirma() {
        try {
            DBTable person = TestToolkit.read("firma_person");
            DBTable gehalt = TestToolkit.read("firma_gehalt");
            DBTable praemie = TestToolkit.read("firma_prämie");
            DBTable abteilung = TestToolkit.read("firma_abteilung");
            
            DB db = new DB("Firma");
            db.addTable(person);
            db.addTable(gehalt);
            db.addTable(praemie);
            db.addTable(abteilung);
            
            DBTable pMitG = person.equijoin(gehalt, "geh_stufe", "geh_stufe", "PersonenMitGehalt");
            Assert.assertEquals("Person_pnr,Person_name,Person_vorname,Person_geh_stufe,Person_abt_nr,Person_krankenkasse,Gehalt_geh_stufe,Gehalt_betrag\n" +
"123,Lehmann,Karl,it3,d13,aok,it3,3027\n" +
"124,Meier,Richard,it5,d13,aok,it5,3782\n" +
"125,Wutschke,Oskar,it3,d13,aok,it3,3027\n" +
"126,Schroeder,Karl-Heinz,it4,d13,aok,it4,3341\n" +
"127,Ehlert,Siegfried,it1,d15,kkh,it1,2523\n" +
"133,Schulz,Harry,it1,d13,aok,it1,2523\n" +
"134,Meier,Gerd,it5,d11,tkk,it5,3782\n" +
"135,Tietze,Lutz,it2,d13,tkk,it2,2873\n" +
"137,Haase,Gert,it1,d14,kkh,it1,2523\n" +
"156,Hartmann,Juergen,it1,d14,aok,it1,2523\n" +
"157,Schultze,Hans,it1,d14,aok,it1,2523\n" +
"159,Osswald,Petra,it2,d15,bak,it2,2873\n" +
"167,Krause,Gustav,it3,d12,dak,it3,3027\n" +
"168,Hahn,Egon,it4,d11,bek,it4,3341\n" +
"227,Wagner,Walter,it2,d13,dak,it2,2873\n" +
"234,Krohn,August,it4,d13,aok,it4,3341", pMitG.toString());
            
            TestToolkit.writeAndAssert(pMitG, "personenmitgehalt");
            Assert.assertEquals(pMitG.toString(), TestToolkit.read("personenmitgehalt").toString());
            
            TestToolkit.writeAndAssert(abteilung, "firma_abteilung");
            
        }
        catch(IOException | WrongSyntaxException ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
