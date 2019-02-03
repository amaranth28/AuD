import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

import db.DBTable;
import db.SortDirection;

public class testdbtablewhoop {



@Test

    public void isValidIdentifierTrueOneLetterSmall() {

        String identifier = "a";

        assertTrue(DBTable.isValidIdentifier(identifier));

    }

 

    @Test

    public void isValidIdentifierTrueOneLetterBig() {

        String identifier = "Z";

        assertTrue(DBTable.isValidIdentifier(identifier));

    }

 

    @Test

    public void isValidIdentifierTrueOneLetter_() {

        String identifier = "a_";

        assertTrue(DBTable.isValidIdentifier(identifier));

    }

 

    @Test

    public void isValidIdentifierTrueMoreLetters() {

        String identifier = "abd_ahrehgego_aefjaoiejf";

        assertTrue(DBTable.isValidIdentifier(identifier));

    }

 

    @Test

    public void isValidIdentifierFalse_() {

        String identifier = "_";

        assertFalse(DBTable.isValidIdentifier(identifier));

    }

 

    @Test

    public void isValidIdentifierFalse_a() {

        String identifier = "_a";

        assertFalse(DBTable.isValidIdentifier(identifier));

    }

 

    @Test

    public void isValidIdentifierFalseAB$a() {

        String identifier = "AB$a";

        assertFalse(DBTable.isValidIdentifier(identifier));

    }

 

    @Test

    public void areValidIdentifiersTrue() {

        String[] identifier = { "a", "Z", "a_A", "abcD" };

        assertTrue(DBTable.areValidIdentifiers(identifier));

    }

 

    @Test

    public void areValidIdentifiersFalseFirstOfFour() {

        String[] identifier = { "!a", "Z", "a_A", "abcD" };

        assertFalse(DBTable.areValidIdentifiers(identifier));

    }

 

    @Test

    public void areValidIdentifiersFalseSecondOfFour() {

        String[] identifier = { "a", "ZbdeÂ§s", "a_A", "abcD" };

        assertFalse(DBTable.areValidIdentifiers(identifier));

    }

 

    @Test

    public void areValidIdentifiersFalseThirdOfFour() {

        String[] identifier = { "a", "Z", "_aA", "abcD" };

        assertFalse(DBTable.areValidIdentifiers(identifier));

    }

 

    @Test

    public void areValidIdentifiersFalseFourthOfFour() {

        String[] identifier = { "!a", "Z", "a_A", "abcD=" };

        assertFalse(DBTable.areValidIdentifiers(identifier));

    }

 

    // @Test

    // public void areUniqueCollNamesTrueOne() {

    // String[] identifier = { "a" };

    // String[] tempi = { "Test" };

    // DBTable temp = new DBTable("Test", tempi);

    // assertTrue(temp.areUniqueCollNames(identifier));

    // }

    //

    // @Test

    // public void areUniqueCollNamesTrueTwo() {

    // String[] identifier = { "a", "AE" };

    // String[] tempi = { "Test", "Testi" };

    // DBTable temp = new DBTable("Test", tempi);

    // assertTrue(temp.areUniqueCollNames(identifier));

    // }

    //

    // @Test

    // public void areUniqueCollNamesTrueThree() {

    // String[] identifier = { "a", "ade", "ewe" };

    // String[] tempi = { "Test", "Testi", "Tester" };

    // DBTable temp = new DBTable("Test", tempi);

    // assertTrue(temp.areUniqueCollNames(identifier));

    // }

    //

    // @Test

    // public void areUniqueCollNamesFalseTwo() {

    // String[] identifier = { "a", "a" };

    // String[] tempi = { "Test", "Testi" };

    // DBTable temp = new DBTable("Test", tempi);

    // assertFalse(temp.areUniqueCollNames(identifier));

    // }

    //

    // @Test

    // public void areUniqueCollNamesFalseThree() {

    // String[] identifier = { "a", "ade", "a" };

    // String[] tempi = { "Test", "Testi", "Tester" };

    // DBTable temp = new DBTable("Test", tempi);

    // assertFalse(temp.areUniqueCollNames(identifier));

    // }

 

    @Test

    public void constructorCorrect() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        List<String> result = new ArrayList();

        result.add("Das");

        result.add("ist");

        result.add("das");

        result.add("Haus");

        result.add("vom");

        result.add("Nikolaus");

        DBTable temp = new DBTable("Test", clmIDs);

 

        assertEquals(0, temp.getRowCnt());

        assertEquals(6, temp.getColCnt());

        assertEquals("Test", temp.getId());

        assertEquals(result, temp.getColIds());

    }

 

    @Test

    public void constructorAssertionUncorrectID() {

        boolean test = false;

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        try {

            DBTable temp = new DBTable("1Test", clmIDs);

        } catch (AssertionError e) {

            test = true;

        }

        assertTrue(test);

    }

 

    @Test

    public void constructorAssertionClmIDsAtLeastOne() {

        boolean test = false;

        String[] clmIDs = {};

        try {

            DBTable temp = new DBTable("1Test", clmIDs);

        } catch (AssertionError e) {

            test = true;

        }

        assertTrue(test);

    }

 

    @Test

    public void constructorAssertionClmIDsCorrectFirstWrong() {

        boolean test = false;

        String[] clmIDs = { "1Das", "ist", "das" };

        try {

            DBTable temp = new DBTable("Test", clmIDs);

        } catch (AssertionError e) {

            test = true;

        }

        assertTrue(test);

    }

 

    @Test

    public void constructorAssertionClmIDsCorrectSecondWrong() {

        boolean test = false;

        String[] clmIDs = { "Das", "i-st", "das" };

        try {

            DBTable temp = new DBTable("Test", clmIDs);

        } catch (AssertionError e) {

            test = true;

        }

        assertTrue(test);

    }

 

    @Test

    public void constructorAssertionClmIDsCorrectThirdWrong() {

        boolean test = false;

        String[] clmIDs = { "Das", "ist", "1Das" };

        try {

            DBTable temp = new DBTable("Test", clmIDs);

        } catch (AssertionError e) {

            test = true;

        }

        assertTrue(test);

    }

 

    @Test

    public void getID() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        DBTable temp = new DBTable("Test", clmIDs);

 

        assertEquals("Test", temp.getId());

    }

 

    @Test

    public void getIDSideEffectTest() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        DBTable temp = new DBTable("Test", clmIDs);

        String test = temp.getId();

        test = "AnotherName";

        assertEquals("Test", temp.getId());

    }

 

    @Test

    public void hasClmTrueFirstOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        assertTrue(temp.hasCol("Das"));

    }

 

    @Test

    public void hasClmTrueSecondOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        assertTrue(temp.hasCol("ist"));

    }

 

    @Test

    public void hasClmTrueThirdOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        assertTrue(temp.hasCol("das"));

    }

 

    @Test

    public void hasClmFalse() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        DBTable temp = new DBTable("Test", clmIDs);

        assertFalse(temp.hasCol("Schloss"));

    }

 

    @Test

    public void hasClmsAssertionNoEntry() {

        boolean test = false;

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        String[] someCols = new String[1];

        try {

            temp.hasCols(someCols);

        } catch (AssertionError e) {

            test = true;

        }

        assertTrue(test);

    }

 

    @Test

    public void hasClmsTrueFirstOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        String[] someCols = { "Das" };

        assertTrue(temp.hasCols(someCols));

    }

 

    @Test

    public void hasClmsTrueSecondOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        String[] someCols = { "Das" };

        assertTrue(temp.hasCols(someCols));

    }

 

    @Test

    public void hasClmsTrueThirdOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        String[] someCols = { "Das" };

        assertTrue(temp.hasCols(someCols));

    }

 

    @Test

    public void hasClmsTrueFirstAndSecondOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        String[] someCols = { "Das", "ist" };

        assertTrue(temp.hasCols(someCols));

    }

 

    @Test

    public void hasClmsTrueFirstAndThirdOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

 

        String[] someCols = { "Das", "das" };

        assertTrue(temp.hasCols(someCols));

    }

 

    @Test

    public void hasClmsTrueSecondAndThirdOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

 

        String[] someCols = { "ist", "das" };

        assertTrue(temp.hasCols(someCols));

    }

 

    @Test

    public void hasClmsTrueThreeOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

 

        String[] someCols = { "Das", "ist", "das" };

        assertTrue(temp.hasCols(someCols));

    }

 

    @Test

    public void hasColsFalse() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

 

        String[] someCols = { "Das", "ist", "das", "Haus" };

        assertFalse(temp.hasCols(someCols));

    }

 

    @Test

    public void getColCnt() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        DBTable temp = new DBTable("Test", clmIDs);

        assertEquals(6, temp.getColCnt());

    }

 

    @Test

    public void getColCntSideEffects() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        DBTable temp = new DBTable("Test", clmIDs);

        int test = temp.getColCnt();

        test = 3;

        assertEquals(6, temp.getColCnt());

    }

 

    @Test

    public void getRowCntZero() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        DBTable temp = new DBTable("Test", clmIDs);

        assertEquals(0, temp.getRowCnt());

    }

 

    @Test

    public void getRowCntOne() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(clmIDs);

        assertEquals(1, temp.getRowCnt());

    }

 

    @Test

    public void getRowCntTwo() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Muh", "Moh" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(clmIDs);

        assertEquals(2, temp.getRowCnt());

    }

 

    @Test

    public void appendRowAssertionWrongClmsToMany() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Muh", "Moh", "Mih" };

        boolean testi = false;

        DBTable temp = new DBTable("Test", clmIDs);

        try {

            temp.appendRow(test);

        } catch (AssertionError e) {

            testi = true;

        }

        assertTrue(testi);

    }

 

    @Test

    public void appendRowAssertionWrongClmsToLess() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Muh" };

        boolean testi = false;

        DBTable temp = new DBTable("Test", clmIDs);

        try {

            temp.appendRow(test);

        } catch (AssertionError e) {

            testi = true;

        }

        assertTrue(testi);

    }

 

    @Test

    public void removeAllRows() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(clmIDs);

        temp.removeAllRows();

        assertEquals(0, temp.getRowCnt());

        assertEquals("Das,ist,das,Haus,vom,Nikolaus", temp.toString());

        ;

    }

 

    @Test

    public void toStringClmIDs() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        DBTable temp = new DBTable("Test", clmIDs);

        assertEquals("Das,ist,das,Haus,vom,Nikolaus", temp.toString());

        ;

    }

 

    @Test

    public void toStringOneRow() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        assertEquals("Das,ist,das\nMah,Moh,Muh", temp.toString());

        ;

    }

 

    @Test

    public void toStringTwoRow() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(clmIDs);

        assertEquals("Das,ist,das\nMah,Moh,Muh\nDas,ist,das", temp.toString());

        ;

    }

 

    @Test

    public void getColIDs() {

        String[] clmIDs = { "Das", "ist", "das", "Haus", "vom", "Nikolaus" };

        DBTable temp = new DBTable("Test", clmIDs);

        List<String> result = new ArrayList();

        result.add("Das");

        result.add("ist");

        result.add("das");

        result.add("Haus");

        result.add("vom");

        result.add("Nikolaus");

        assertEquals(result, temp.getColIds());

        ;

    }

 

    @Test

    public void sortZeroRowASC() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.sort("ist", SortDirection.ASC);

        assertEquals("Das,ist,das", temp.toString());

    }

 

    @Test

    public void sortZeroRowDESC() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.sort("ist", SortDirection.DESC);

        assertEquals("Das,ist,das", temp.toString());

    }

 

    @Test

    public void sortOneRowASC() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.sort("ist", SortDirection.ASC);

        assertEquals("Das,ist,das\nMah,Moh,Muh", temp.toString());

    }

 

    @Test

    public void sortOneRowDESC() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.sort("ist", SortDirection.DESC);

        assertEquals("Das,ist,das\nMah,Moh,Muh", temp.toString());

    }

 

    @Test

    public void sortTwoRowASC() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        temp.sort("ist", SortDirection.ASC);

        assertEquals("Das,ist,das\nA,B,C\nMah,Moh,Muh", temp.toString());

    }

 

    @Test

    public void sortTwoRowDESC() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        temp.sort("ist", SortDirection.DESC);

        assertEquals("Das,ist,das\nMah,Moh,Muh\nA,B,C", temp.toString());

    }

 

    @Test

    public void sortThreeRowASC() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        String[] testit = { "D", "E", "F" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        temp.appendRow(testit);

        temp.sort("ist", SortDirection.ASC);

        assertEquals("Das,ist,das\nA,B,C\nD,E,F\nMah,Moh,Muh", temp.toString());

    }

 

    @Test

    public void sortThreeRowDESC() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        String[] testit = { "D", "E", "F" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        temp.appendRow(testit);

        temp.sort("ist", SortDirection.DESC);

        assertEquals("Das,ist,das\nMah,Moh,Muh\nD,E,F\nA,B,C", temp.toString());

    }

 

    @Test

    public void removeRowsOneOfTwo() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        final Predicate<String> p = s -> s.equals("B");

        temp.removeRows("ist", p);

        assertEquals("Das,ist,das\nMah,Moh,Muh", temp.toString());

    }

 

    @Test

    public void removeRowsOneOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        String[] testit = { "D", "E", "F" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        temp.appendRow(testit);

        final Predicate<String> p = s -> s.equals("B");

        temp.removeRows("ist", p);

        assertEquals("Das,ist,das\nMah,Moh,Muh\nD,E,F", temp.toString());

    }

 

    @Test

    public void removeRowsTwoOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "D", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        String[] testit = { "D", "E", "F" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        temp.appendRow(testit);

        final Predicate<String> p = s -> s.equals("D");

        temp.removeRows("Das", p);

        assertEquals("Das,ist,das\nA,B,C", temp.toString());

    }

 

    @Test

    public void removeRowsZeroOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        String[] testit = { "D", "E", "F" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        temp.appendRow(testit);

        final Predicate<String> p = s -> s.equals("Q");

        temp.removeRows("ist", p);

        assertEquals("Das,ist,das\nMah,Moh,Muh\nA,B,C\nD,E,F", temp.toString());

    }

 

    @Test

    public void selectEmptyTable() {

        String[] clmIDs = { "Das", "ist", "das" };

        DBTable temp = new DBTable("Test", clmIDs);

        final Predicate<String> p = s -> s.equals("Bier");

        DBTable toTest = temp.select("Das", p, "Test");

        assertEquals("Das,ist,das", toTest.toString());

    }

 

    @Test

    public void selectOneOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        final Predicate<String> p = s -> s.equals("Mah");

        DBTable toTest = temp.select("Das", p, "Test");

        assertEquals("Das,ist,das\nMah,Moh,Muh", toTest.toString());

    }

   

    @Test

    public void selectOneOftrheeLast() {

      String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        final Predicate<String> p = s -> s.equals("A");

        DBTable toTest = temp.select("Das", p, "Test");

        assertEquals("Das,ist,das\nA,B,C", toTest.toString());

    }

 

    @Test

    public void selectNotFound() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "A", "B", "C" };

        String[] test2 = { "K", "L", "M" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        temp.appendRow(test2);

        final Predicate<String> p = s -> s.equals("Test");

        boolean bool = false;

        try {

            DBTable toTest = temp.select("Test", p, "Test");

        } catch (AssertionError e) {

            bool = true;

        }

        assertTrue(bool);

    }

 

    @Test

    public void selectTwoOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "Mah", "Moh", "C" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        final Predicate<String> p = s -> s.equals("Moh");

        DBTable toTest = temp.select("ist", p, "Test");

        assertEquals("Das,ist,das\nMah,Moh,Muh\nMah,Moh,C", toTest.toString());

    }

 

    @Test

    public void selectNoRows() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "Mah", "Moh", "C" };

        DBTable temp = new DBTable("Test", clmIDs);

 

        final Predicate<String> p = s -> s.equals("Moh");

        DBTable toTest = temp.select("ist", p, "Test");

        assertEquals("Das,ist,das", toTest.toString());

    }

   

    

 

   

    

    

 

    @Test

    public void projectOneOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "Mah", "B", "C" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        String[] testtest = { "Das" };

        DBTable toTest = temp.project(testtest, "muhaha");

        assertEquals("Das\nMah\nMah", toTest.toString());

    }

 

    @Test

    public void projectTwoOfThree() {

        String[] clmIDs = { "Das", "ist", "das" };

        String[] test = { "Mah", "Moh", "Muh" };

        String[] testi = { "Mah", "B", "C" };

        DBTable temp = new DBTable("Test", clmIDs);

        temp.appendRow(test);

        temp.appendRow(testi);

        String[] testtest = { "Das", "ist" };

        DBTable toTest = temp.project(testtest, "muhaha");

        assertEquals("Das,ist\nMah,Moh\nMah,B", toTest.toString());

    }

 

    // @Test

    // public void projectZeroOfThree() {

    // String[] clmIDs = { "Das", "ist", "das" };

    // String[] test = { "Mah", "Moh", "Muh" };

    // String[] testi = { "Mah", "B", "C" };

    // DBTable temp = new DBTable("Test", clmIDs);

    // temp.appendRow(test);

    // temp.appendRow(testi);

    // String[] testtest = { "" };

    // DBTable toTest = temp.project(testtest, "muhaha");

    // assertEquals("", toTest.toString());

    // }

 

    @Test

    public void equijionFirstBigger() {

        String[] clmIDsOne = { "S1", "S2" };

        String[] testOne = { "W1a", "W2a" };

        String[] testiOne = { "W1b", "W2b" };

        DBTable tempOne = new DBTable("T1", clmIDsOne);

        tempOne.appendRow(testOne);

        tempOne.appendRow(testiOne);

 

        String[] clmIDsTwo = { "S1", "S3", "S4" };

        String[] testTwo = { "W1a", "W3a", "W4a" };

        String[] testiTwo = { "W1b", "W3b", "W4b" };

        String[] testitTwo = { "W1b", "W3c", "W4c" };

        DBTable tempTwo = new DBTable("T2", clmIDsTwo);

        tempTwo.appendRow(testTwo);

        tempTwo.appendRow(testiTwo);

        tempTwo.appendRow(testitTwo);

 

        String[] clmIDsExp = { "T2_S1", "T2_S3", "T2_S4", "T1_S1", "T1_S2" };

        String[] testExp = { "W1a", "W3a", "W4a", "W1a", "W2a" };

        String[] testiExp = { "W1b", "W3b", "W4b", "W1b", "W2b" };

        String[] testitExp = { "W1b", "W3c", "W4c", "W1b", "W2b" };

        DBTable tempExp = new DBTable("testTest", clmIDsExp);

        tempExp.appendRow(testExp);

        tempExp.appendRow(testiExp);

        tempExp.appendRow(testitExp);

 

        DBTable test = tempTwo.equijoin(tempOne, "S1", "S1", "testTest");

 

        assertEquals(tempExp.toString(), test.toString());

    }

 

    @Test

    public void equijionSecondBigger() {

        String[] clmIDsOne = { "S1", "S2" };

        String[] testOne = { "W1a", "W2a" };

        String[] testiOne = { "W1b", "W2b" };

        DBTable tempOne = new DBTable("T1", clmIDsOne);

        tempOne.appendRow(testOne);

        tempOne.appendRow(testiOne);

 

        String[] clmIDsTwo = { "S1", "S3", "S4" };

        String[] testTwo = { "W1a", "W3a", "W4a" };

        String[] testiTwo = { "W1b", "W3b", "W4b" };

        String[] testitTwo = { "W1b", "W3c", "W4c" };

        DBTable tempTwo = new DBTable("T2", clmIDsTwo);

        tempTwo.appendRow(testTwo);

        tempTwo.appendRow(testiTwo);

        tempTwo.appendRow(testitTwo);

 

        String[] clmIDsExp = { "T1_S1", "T1_S2", "T2_S1", "T2_S3", "T2_S4" };

        String[] testExp = { "W1a", "W2a", "W1a", "W3a", "W4a" };

        String[] testiExp = { "W1b", "W2b", "W1b", "W3b", "W4b" };

        String[] testitExp = { "W1b", "W2b", "W1b", "W3c", "W4c" };

        DBTable tempExp = new DBTable("testTest", clmIDsExp);

        tempExp.appendRow(testExp);

        tempExp.appendRow(testiExp);

        tempExp.appendRow(testitExp);

 

        DBTable test = tempOne.equijoin(tempTwo, "S1", "S1", "testTest");

 

        assertEquals(tempExp.toString(), test.toString());

    }
}
