package simplex;

import static simplex.TestUtils.f;
import static simplex.TestUtils.fs;
import static simplex.TestUtils.fss;
import static simplex.TestUtils.ints;
import static simplex.TestUtils.r;
import static simplex.TestUtils.rs;

import org.junit.Assert;
import org.junit.Test;

import simplex.LinearProgram.Restriction.Type;
import simplex.LinearProgram.SolveType;
import simplex.SimplexSolver.SimplexState;

/**
 * Beispieltests für die Klassen Fraction und SimplexSolver
 * 
 * @author kar, mhe
 *
 */

public class ExampleTests {

    @Test(timeout = 2 * 1000)
    public void fraction_multiplyBy() {
        Assert.assertEquals("2/3 * 3/4", f(1, 2), f(2, 3).multiplyBy(f(3, 4)));
        Assert.assertEquals("12/3 * 13/4", f(13), f(12, 3).multiplyBy(f(13, 4)));
        Assert.assertEquals("1/2 * 1/2", f(1, 4), f(1, 2).multiplyBy(f(1, 2)));

        // Multilplikation mit neg Zahlen
        Assert.assertEquals("-2/3 * 3/4", f(-1, 2), f(-2, 3).multiplyBy(f(3, 4)));
        Assert.assertEquals("-2/3 * 0", f(0), f(-2, 3).multiplyBy(f(0)));
        Assert.assertEquals("-2/3 * -245/23", f(490, 69), f(-2, 3).multiplyBy(f(-245, 23)));

    }

    @Test(timeout = 2 * 1000)
    public void fraction_compareTo() {

        // Gleicher Nenner
        Assert.assertTrue("12/3 > 2/3", f(12, 3).compareTo(f(2, 3)) > 0);
        Assert.assertTrue("1/3 < 2/3", f(1, 3).compareTo(f(2, 3)) < 0);
        Assert.assertTrue("2/3 = 2/3", f(2, 3).compareTo(f(2, 3)) == 0);

        // Gleicher Zähler
        Assert.assertTrue("1/5 > 1/7", f(1, 5).compareTo(f(1, 7)) > 0);
        Assert.assertTrue("1/7 < 1/5", f(1, 7).compareTo(f(1, 5)) < 0);
        Assert.assertTrue("0/7 < 1/7", f(0, 7).compareTo(f(1, 7)) < 0);
        Assert.assertTrue("12/12 < 12/12", f(12, 12).compareTo(f(12, 12)) == 0);

        // Gemischt
        Assert.assertTrue("2/3 > 3/7", f(2, 3).compareTo(f(3, 7)) > 0);
        Assert.assertTrue("3/4 < 7/8", f(3, 4).compareTo(f(7, 8)) < 0);
        Assert.assertTrue("233/4 > 7/342", f(233, 4).compareTo(f(7, 342)) > 0);
        Assert.assertTrue("490/1 > 32/12", f(490, 1).compareTo(f(32, 12)) > 0);
        Assert.assertTrue("324/129 < 32/12", f(324, 129).compareTo(f(12, 3)) < 0);
        Assert.assertTrue("8594/1213 > 3401/10934", f(8594, 1213).compareTo(f(3401, 10934)) > 0);
        Assert.assertTrue("1342/340 < 48390/3949", f(1342, 340).compareTo(f(48390, 3949)) < 0);
    }

    @Test(timeout = 2 * 1000)
    public void simplex() {
        SimplexSolver s = new SimplexSolver(new LinearProgram(rs(r(fs(f(4), f(3)), Type.LE, f(320)),
                r(fs(f(2), f(4)), Type.GE, f(100)), r(fs(f(3), f(3)), Type.EQ, f(270))),
                SolveType.MIN, f(2), f(8)));

        Assert.assertEquals("Beispiel 1 (Ausgangstableau): getState", SimplexState.INVALID_SOLUTION,
                s.getState());
        Assert.assertArrayEquals("Beispiel 1 (Ausgangstableau): getTable",
                fss(fs(f(4), f(3), f(1), f(0), f(0), f(0), f(0), f(0), f(320)),
                        fs(f(2), f(4), f(0), f(-1), f(0), f(0), f(1), f(0), f(100)),
                        fs(f(3), f(3), f(0), f(0), f(0), f(0), f(0), f(1), f(270)),
                        fs(f(-2), f(-8), f(0), f(0), f(0), f(0), f(0), f(0), f(0))),
                s.getTable());
        Assert.assertArrayEquals("Beispiel 1 (Ausgangstableau): getBaseVars", ints(2, 6, 7),
                s.getBaseVars());

        Assert.assertArrayEquals("Beispiel 1: getSolution", fs(f(50), f(40), f(420)),
                s.getSolution());
        Assert.assertEquals("Beispiel 1 (Lösung): getState", SimplexState.OPTIMAL, s.getState());
        Assert.assertArrayEquals("Beispiel 1 (Lösung): getBaseVars", ints(3, 0, 1),
                s.getBaseVars());
        Assert.assertArrayEquals("Beispiel 1 (Lösung): getTable",
                fss(fs(f(0), f(0), f(-2), f(1), f(0), f(0), f(-1), f(10, 3), f(160)),
                        fs(f(1), f(0), f(1), f(0), f(0), f(0), f(0), f(-1), f(50)),
                        fs(f(0), f(1), f(-1), f(0), f(0), f(0), f(0), f(4, 3), f(40)),
                        fs(f(0), f(0), f(-6), f(0), f(0), f(0), f(0), f(26, 3), f(420))),
                s.getTable());
    }

    /* -----Eigene Tests---- */

    // @Test(timeout = 2 * 1000)
    @Test
    public void simplex_with_Steps() {
        // lineares Problem wird in tableau ueberfuehrt
        SimplexSolver s = new SimplexSolver(new LinearProgram(rs(r(fs(f(4), f(3)), Type.LE, f(320)),
                r(fs(f(2), f(4)), Type.GE, f(100)), r(fs(f(3), f(3)), Type.EQ, f(270))),
                SolveType.MIN, f(2), f(8)));

        // prueft ob status, tableau und baseVars richtig gesetzt werden
        Assert.assertEquals("Beispiel 1 (Ausgangstableau): getState", SimplexState.INVALID_SOLUTION,
                s.getState());
        Assert.assertArrayEquals("Beispiel 1 (Ausgangstableau): getTable",
                fss(fs(f(4), f(3), f(1), f(0), f(0), f(0), f(0), f(0), f(320)),
                        fs(f(2), f(4), f(0), f(-1), f(0), f(0), f(1), f(0), f(100)),
                        fs(f(3), f(3), f(0), f(0), f(0), f(0), f(0), f(1), f(270)),
                        fs(f(-2), f(-8), f(0), f(0), f(0), f(0), f(0), f(0), f(0))),
                s.getTable());
        Assert.assertArrayEquals("Beispiel 1 (Ausgangstableau): getBaseVars", ints(2, 6, 7),
                s.getBaseVars());

        // 2.Phase
        s.step();
        Assert.assertArrayEquals("Beispiel 1 (2.Phase): getTable",
                fss(fs(f(5, 2), f(0), f(1), f(3, 4), f(0), f(0), f(-3, 4), f(0), f(245)),
                        fs(f(1, 2), f(1), f(0), f(-1, 4), f(0), f(0), f(1, 4), f(0), f(25)),
                        fs(f(3, 2), f(0), f(0), f(3, 4), f(0), f(0), f(-3, 4), f(1), f(195)),
                        fs(f(2), f(0), f(0), f(-2), f(0), f(0), f(2), f(0), f(200))),
                s.getTable());
        Assert.assertEquals("Beispiel 1 (2.Phase): getState", SimplexState.INVALID_SOLUTION,
                s.getState());
        Assert.assertArrayEquals("Beispiel 1 (2.Phase): getBaseVars", ints(2, 1, 7),
                s.getBaseVars());

        // 3.Phase
        s.step();
        Assert.assertArrayEquals("Beispiel 1 (3.Phase): getTable",
                fss(fs(f(0), f(-5), f(1), f(2), f(0), f(0), f(-2), f(0), f(120)),
                        fs(f(1), f(2), f(0), f(-1, 2), f(0), f(0), f(1, 2), f(0), f(50)),
                        fs(f(0), f(-3), f(0), f(3, 2), f(0), f(0), f(-3, 2), f(1), f(120)),
                        fs(f(0), f(-4), f(0), f(-1), f(0), f(0), f(1), f(0), f(100))),
                s.getTable());

        Assert.assertEquals("Beispiel 1 (3.Phase): getState", SimplexState.INVALID_SOLUTION,
                s.getState());
        Assert.assertArrayEquals("Beispiel 1 (3.Phase): getBaseVars", ints(2, 0, 7),
                s.getBaseVars());

        // 4.Phase
        s.step();

        Assert.assertArrayEquals("Beispiel 1 (4.Phase): getTable",
                fss(fs(f(0), f(-5, 2), f(1, 2), f(1), f(0), f(0), f(-1), f(0), f(60)),
                        fs(f(1), f(3, 4), f(1, 4), f(0), f(0), f(0), f(0), f(0), f(80)),
                        fs(f(0), f(3, 4), f(-3, 4), f(0), f(0), f(0), f(0), f(1), f(30)),
                        fs(f(0), f(-13, 2), f(1, 2), f(0), f(0), f(0), f(0), f(0), f(160))),
                s.getTable());
        Assert.assertEquals("Beispiel 1 (4.Phase): getState", SimplexState.INVALID_SOLUTION,
                s.getState());
        Assert.assertArrayEquals("Beispiel 1 (4.Phase): getBaseVars", ints(3, 0, 7),
                s.getBaseVars());

        // 5.Phase
        s.step();

        Assert.assertArrayEquals("Beispiel 1 (5.Phase): getTable",
                fss(fs(f(0), f(0), f(-2), f(1), f(0), f(0), f(-1), f(10, 3), f(160)),
                        fs(f(1), f(0), f(1), f(0), f(0), f(0), f(0), f(-1), f(50)),
                        fs(f(0), f(1), f(-1), f(0), f(0), f(0), f(0), f(4, 3), f(40)),
                        fs(f(0), f(0), f(-6), f(0), f(0), f(0), f(0), f(26, 3), f(420))),
                s.getTable());
        Assert.assertEquals("Beispiel 1 (5.Phase): getState", SimplexState.OPTIMAL, s.getState());
        Assert.assertArrayEquals("Beispiel 1 (5.Phase): getBaseVars", ints(3, 0, 1),
                s.getBaseVars());

    }

    // Testet den Konstruktor, Gekürzter Bruch wird erwartet
    @Test(timeout = 2 * 1000)
    public void fraction_Fraction() {
        Assert.assertEquals(f(2, 3), f(24, 36));
        Assert.assertEquals(f(2, 3), f(2, 3));
        Assert.assertEquals(f(1243, 324), f(1243, 324));
        Assert.assertEquals(f(2, 1), f(22, 11));
        Assert.assertEquals(f(1, 2), f(11, 22));
        Assert.assertEquals(f(3, 7), f(3, 7));
        Assert.assertEquals(f(12, 7), f(12, 7));

        Assert.assertEquals(f(1, 2), f(2 * 3, 3 * 4));

        // negativer Bruch
        Assert.assertEquals(f(-2, 3), f(-24, 36));
    }

    // Testet das addieren von Brüchen
    @Test(timeout = 2 * 1000)
    public void fraction_add() {

        /* Gleichnamige Nenner */
        Assert.assertEquals("12/3 + 3/3", f(5), f(12, 3).add(f(3, 3)));
        Assert.assertEquals("156/19 + 164/19", f(320, 19), f(156, 19).add(f(164, 19)));
        Assert.assertEquals("1/7 + 3/7", f(4, 7), f(1, 7).add(f(3, 7)));
        Assert.assertEquals("-789/5 + 447/5", f(-342, 5), f(-789, 5).add(f(447, 5)));

        /* Ungleichnamige Nenner */
        Assert.assertEquals("12/6 + 24/12", f(4), f(12, 6).add(f(24, 12)));
        Assert.assertEquals("5/19 + 4/4", f(24, 19), f(5, 19).add(f(4, 4)));
        Assert.assertEquals("123/102 + 243/54", f(97, 17), f(123, 102).add(f(243, 54)));
        Assert.assertEquals("164/78 + 741/31", f(31441, 1209), f(164, 78).add(f(741, 31)));
        Assert.assertEquals("985/3 + 437/45", f(15212, 45), f(985, 3).add(f(437, 45)));
        Assert.assertEquals("795/678 + 237/102", f(6716, 1921), f(795, 678).add(f(237, 102)));
        Assert.assertEquals("0 + 64/7", f(64, 7), f(0).add(f(64, 7)));
    }

    // Testet das subrahieren von Brüchen
    @Test(timeout = 2 * 1000)
    public void fraction_subtract() {

        /* Gleichnamige Nenner */
        Assert.assertEquals("12/3 - 3/3", f(3), f(12, 3).subtract(f(3, 3)));
        Assert.assertEquals("156/19 - 164/19", f(-8, 19), f(156, 19).subtract(f(164, 19)));
        Assert.assertEquals("6/7 - 3/7", f(3, 7), f(6, 7).subtract(f(3, 7)));

        /* Ungleichnamige Nenner */
        Assert.assertEquals("12/6 - 24/12", f(0), f(12, 6).subtract(f(24, 12)));
        Assert.assertEquals("5/19 - 4/4", f(14, 19), f(4, 4).subtract(f(5, 19)));
        Assert.assertEquals("123/102 - 243/54", f(-154, 459), f(47, 54).subtract(f(123, 102)));
        Assert.assertEquals("795/678 - 237/102", f(-2211, 1921), f(795, 678).subtract(f(237, 102)));
        Assert.assertEquals("178/65 - 783/14", f(-48403, 910), f(178, 65).subtract(f(783, 14)));
        Assert.assertEquals("65/123 - 77/145", f(-46, 17835), f(65, 123).subtract(f(77, 145)));
        Assert.assertEquals("6/1245 - 8/47", f(-3226, 19505), f(6, 1245).subtract(f(8, 47)));
        Assert.assertEquals("979/13 - 64/7", f(6021, 91), f(979, 13).subtract(f(64, 7)));
        Assert.assertEquals("0 - 45/98", f(-45, 98), f(0).subtract(f(45, 98)));

    }

    // Testet das dividierne von Brüchen
    @Test(timeout = 2 * 1000)
    public void fraction_divideBy() {

        Assert.assertEquals("7/8 / 1/16", f(14), f(7, 8).divideBy(f(1, 16)));
        Assert.assertEquals("7/2 / 19/2", f(7, 19), f(7, 2).divideBy(f(19, 2)));
        Assert.assertEquals("3/7 / 5", f(3, 35), f(3, 7).divideBy(f(5)));
        Assert.assertEquals("137/14 / 798/15", f(685, 3724), f(137, 14).divideBy(f(798, 15)));
        Assert.assertEquals("176 / 78/47", f(4136, 39), f(176).divideBy(f(78, 47)));
        Assert.assertEquals("26 / 784/4", f(13, 98), f(26).divideBy(f(784, 4)));
        Assert.assertEquals("-87/4653 / 14/47", f(-29, 462), f(-87, 4653).divideBy(f(14, 47)));
        Assert.assertEquals("0 / 14/5", f(0), f(0).divideBy(f(14, 5)));
        Assert.assertEquals("-8 / 0", f(-5, 3), f(-5).divideBy(f(3)));
        Assert.assertEquals("50 * (-1/2)", f(-100), f(50).divideBy(f(-1, 2)));
    }

    // Testet ggt-Funktion
    @Test(timeout = 2 * 1000)
    public void fraction_ggt() {
        // Assert.assertEquals(6, f(-12, 6).ggt(-12, 6));
        // Assert.assertEquals(2, f(234, 128).ggt(234, 128));
        // Assert.assertEquals(1, f(122, 3).ggt(122, 3));
        // Assert.assertEquals(1, f(127, 332).ggt(127, 323));
    }

    // Testet kgv-Funktions
    @Test(timeout = 2 * 1000)
    public void fraction_kgv() {
        // Assert.assertEquals(12, f(12, 6).kgv(12, 6));
        // Assert.assertEquals(14976, f(234, 128).kgv(234, 128));
    }

    @Test(timeout = 2 * 1000)
    public void simplex_example1() {

        /* ---- Beispiel 1; 2 Var; 2 Zeilen ----- */
        // 2 - 1 < 1
        // 3 + 1 < 5
        // 4 + 1 -> MAX
        SimplexSolver b1 = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(2), f(-1)), Type.LE, f(1)), r(fs(f(3), f(1)), Type.LE, f(5))),
                SolveType.MAX, f(4), f(1)));

        Assert.assertArrayEquals("Beispiel1 (Ausgangstableau): getTable",
                fss(fs(f(2), f(-1), f(1), f(0), f(0), f(0), f(1)),
                        fs(f(3), f(1), f(0), f(1), f(0), f(0), f(5)),
                        fs(f(4), f(1), f(0), f(0), f(0), f(0), f(0))),
                b1.getTable());

        // Assert.assertEquals(0, b1.getPivotColumn());
        // Assert.assertEquals(0, b1.getPivotRow());

        Assert.assertEquals("Beispiel1 (Ausgangstableau): getState", SimplexState.VALID_SOLUTION,
                b1.getState());
        Assert.assertArrayEquals("Beispiel1 (Ausgangstableau): getBaseVars", ints(2, 3),
                b1.getBaseVars());

        Assert.assertArrayEquals("Beispiel 1: getSolution", fs(f(6, 5), f(7, 5), f(31, 5)),
                b1.getSolution());

    }

    @Test(timeout = 2 * 1000)
    public void simplex_example2() {

        /*
         * ---- Beispiel 2 ; 3 Var, 3 Zeilen----- 1 + 2 - 1 < 50 2 - 1 + 1 > 70 1 + 8 + 0 < 400
         * 
         * 7 - 11 + 3 -> MIN
         */
        SimplexSolver b2 = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(1), f(2), f(-1)), Type.LE, f(50)),
                        r(fs(f(2), f(-1), f(1)), Type.GE, f(70)),
                        r(fs(f(1), f(8), f(0)), Type.LE, f(400))),
                SolveType.MIN, f(7), f(-11), f(3)));

        // Ausgangstableau
        Assert.assertArrayEquals("Beispiel2 (Ausgangstableau): getTable",
                fss(fs(f(1), f(2), f(-1), f(1), f(0), f(0), f(0), f(0), f(0), f(50)),
                        fs(f(2), f(-1), f(1), f(0), f(-1), f(0), f(0), f(1), f(0), f(70)),
                        fs(f(1), f(8), f(0), f(0), f(0), f(1), f(0), f(0), f(0), f(400)),
                        fs(f(-7), f(11), f(-3), f(0), f(0), f(0), f(0), f(0), f(0), f(0))),
                b2.getTable());

        Assert.assertEquals("Beispiel2 (Ausgangstableau): getState", SimplexState.INVALID_SOLUTION,
                b2.getState());
        Assert.assertArrayEquals("Beispiel2 (Ausgangstableau): getBaseVars", ints(3, 7, 5),
                b2.getBaseVars());

        /*
         * b2.step(); Assert.assertEquals(1, b2.getPivotColumn()); Assert.assertEquals(0,
         * b2.getPivotRow()); Assert.assertEquals("Beispiel2 (Ausgangstableau): getState",
         * SimplexState.VALID_SOLUTION, b2.getState());
         * Assert.assertArrayEquals("Beispiel2 (Ausgangstableau): getBaseVars", ints(3, 0, 5),
         * b2.getBaseVars());
         * 
         * b2.step(); Assert.assertEquals(2, b2.getPivotColumn()); Assert.assertEquals(2,
         * b2.getPivotRow()); Assert.assertEquals("Beispiel2 (Ausgangstableau): getState",
         * SimplexState.VALID_SOLUTION, b2.getState());
         * Assert.assertArrayEquals("Beispiel2 (Ausgangstableau): getBaseVars", ints(1, 0, 5),
         * b2.getBaseVars());
         * 
         * 
         * b2.solve(); /* b2.step();
         * 
         * // TODO: groesste wert in der kompletten zielfunktionszeile?
         * 
         * Assert.assertEquals(7, b2.getPivotColumn()); Assert.assertEquals(1, b2.getPivotRow()); //
         * Assert.assertEquals(4, b2.getPivotColumn()); // Assert.assertEquals(1, b2.getPivotRow());
         * Assert.assertEquals("Beispiel2 (Ausgangstableau): getState", SimplexState.VALID_SOLUTION,
         * b2.getState()); Assert.assertArrayEquals("Beispiel2 (Ausgangstableau): getBaseVars",
         * ints(1, 0, 5), b2.getBaseVars());
         * 
         * 
         * b2.step();
         */

        // Das Ergebis des LOP wird ermittelt

        // Problem: hört nach dem 3. durchlauf ab

        Assert.assertArrayEquals("Beispiel2: getSolution", fs(f(0), f(50), f(120), f(190)),
                b2.getSolution());

    }

    @Test(timeout = 2 * 1000)
    public void simplex_oneVar_ONE() {

        /* ---- Beispiel 4 ; 1 Var, 1 Zeile----- */
        // Tableau ist nicht loesbar, Berechnung bricht ab
        // 1 > 1
        // 1 -> MIN

        SimplexSolver b4 = new SimplexSolver(
                new LinearProgram(rs(r(fs(f(1)), Type.GE, f(1))), SolveType.MIN, f(1)));

        Assert.assertArrayEquals("Beispiel4 (Ausgangstableau): getTable",
                fss(fs(f(1), f(-1), f(1), f(1)), fs(f(-1), f(0), f(0), f(0))), b4.getTable());

        Assert.assertEquals("Beispiel3 (Ausgangstableau): getState", SimplexState.INVALID_SOLUTION,
                b4.getState());
        Assert.assertArrayEquals("Beispiel3 (Ausgangstableau): getBaseVars", ints(2),
                b4.getBaseVars());
        Assert.assertArrayEquals("Beispiel 1: getSolution", fs(f(1), f(1)), b4.getSolution());

    }

    @Test(timeout = 2 * 1000)
    // @Test
    public void simplex_oneVar_MIN_and_MAX() {

        // 3 > 2
        // 5 -> MAX

        SimplexSolver b5 = new SimplexSolver(
                new LinearProgram(rs(r(fs(f(3)), Type.GE, f(2))), SolveType.MAX, f(5)));

        Assert.assertArrayEquals("Beispiel4 (Ausgangstableau): getTable",
                fss(fs(f(3), f(-1), f(1), f(2)), fs(f(5), f(0), f(0), f(0))), b5.getTable());

        Assert.assertEquals("Beispiel3 (Ausgangstableau): getState", SimplexState.INVALID_SOLUTION,
                b5.getState());
        Assert.assertArrayEquals("Beispiel3 (Ausgangstableau): getBaseVars", ints(2),
                b5.getBaseVars());

        b5.solve();
        Assert.assertEquals("Beispiel3 (Ausgangstableau): getState", SimplexState.UNSOLVABLE,
                b5.getState());

        // 3 > 2
        // 5 -> MIN

        SimplexSolver b6 = new SimplexSolver(
                new LinearProgram(rs(r(fs(f(3)), Type.GE, f(2))), SolveType.MIN, f(5)));

        Assert.assertArrayEquals("Beispiel4 (Ausgangstableau): getTable",
                fss(fs(f(3), f(-1), f(1), f(2)), fs(f(-5), f(0), f(0), f(0))), b6.getTable());

        Assert.assertEquals("Beispiel3 (Ausgangstableau): getState", SimplexState.INVALID_SOLUTION,
                b6.getState());
        Assert.assertArrayEquals("Beispiel3 (Ausgangstableau): getBaseVars", ints(2),
                b6.getBaseVars());

        Assert.assertArrayEquals("Beispiel 1: getSolution", fs(f(2, 3), f(10, 3)),
                b6.getSolution());
        Assert.assertEquals("Beispiel 1 (Lösung): getState", SimplexState.OPTIMAL, b6.getState());

        // 3 < 2
        // 5 -> MAX

        SimplexSolver b7 = new SimplexSolver(
                new LinearProgram(rs(r(fs(f(3)), Type.LE, f(2))), SolveType.MAX, f(5)));

        Assert.assertArrayEquals("Beispiel4 (Ausgangstableau): getTable",
                fss(fs(f(3), f(1), f(0), f(2)), fs(f(5), f(0), f(0), f(0))), b7.getTable());

        Assert.assertEquals("Beispiel3 (Ausgangstableau): getState", SimplexState.VALID_SOLUTION,
                b7.getState());
        Assert.assertArrayEquals("Beispiel3 (Ausgangstableau): getBaseVars", ints(1),
                b7.getBaseVars());

        Assert.assertArrayEquals("Beispiel 1: getSolution", fs(f(2, 3), f(10, 3)),
                b7.getSolution());
        Assert.assertEquals("Beispiel 1 (Lösung): getState", SimplexState.OPTIMAL, b7.getState());

        // 3 < 2
        // 5 -> MIN

        SimplexSolver b8 = new SimplexSolver(
                new LinearProgram(rs(r(fs(f(3)), Type.LE, f(2))), SolveType.MIN, f(5)));

        Assert.assertArrayEquals("Beispiel4 (Ausgangstableau): getTable",
                fss(fs(f(3), f(1), f(0), f(2)), fs(f(-5), f(0), f(0), f(0))), b8.getTable());

        Assert.assertEquals("Beispiel3 (Ausgangstableau): getState", SimplexState.OPTIMAL,
                b8.getState());
        Assert.assertArrayEquals("Beispiel3 (Ausgangstableau): getBaseVars", ints(1),
                b8.getBaseVars());

        Assert.assertArrayEquals("Beispiel 1: getSolution", fs(f(0), f(0)), b8.getSolution());
        Assert.assertEquals("Beispiel 1 (Lösung): getState", SimplexState.OPTIMAL, b8.getState());

        // 3 = 2
        // 5 -> MIN

        SimplexSolver b9 = new SimplexSolver(
                new LinearProgram(rs(r(fs(f(3)), Type.EQ, f(2))), SolveType.MIN, f(5)));

        Assert.assertArrayEquals("Beispiel4 (Ausgangstableau): getTable",
                fss(fs(f(3), f(0), f(1), f(2)), fs(f(-5), f(0), f(0), f(0))), b9.getTable());

        Assert.assertArrayEquals("Beispiel 1: getSolution", fs(f(2, 3), f(10, 3)),
                b9.getSolution());
        Assert.assertEquals("Beispiel 1 (Lösung): getState", SimplexState.OPTIMAL, b9.getState());

        // 3 = 2
        // 5 -> MAX

        SimplexSolver b10 = new SimplexSolver(
                new LinearProgram(rs(r(fs(f(3)), Type.EQ, f(2))), SolveType.MAX, f(5)));

        Assert.assertArrayEquals("Beispiel4 (Ausgangstableau): getTable",
                fss(fs(f(3), f(0), f(1), f(2)), fs(f(5), f(0), f(0), f(0))), b10.getTable());

        Assert.assertArrayEquals("Beispiel 1: getSolution", fs(f(2, 3), f(10, 3)),
                b10.getSolution());
        Assert.assertEquals("Beispiel 1 (Lösung): getState", SimplexState.OPTIMAL, b10.getState());

    }

    @Test(timeout = 2 * 1000)
    public void simplex_optimal_solution_2() {

        // 1 + 0 + 0 < 1
        // 0 + 1 + 0 > 1
        // 0 + 0 + 1 < 1
        // 1 + 1 + 1 -> MIN

        SimplexSolver os = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(1), f(0), f(0)), Type.LE, f(1)), r(fs(f(0), f(1), f(0)), Type.GE, f(1)),
                        r(fs(f(0), f(0), f(1)), Type.LE, f(1))),
                SolveType.MIN, f(1), f(1), f(1)));

        Assert.assertArrayEquals("Beispiel3 (Ausgangstableau): getTable",
                fss(fs(f(1), f(0), f(0), f(1), f(0), f(0), f(0), f(0), f(0), f(1)),
                        fs(f(0), f(1), f(0), f(0), f(-1), f(0), f(0), f(1), f(0), f(1)),
                        fs(f(0), f(0), f(1), f(0), f(0), f(1), f(0), f(0), f(0), f(1)),
                        fs(f(-1), f(-1), f(-1), f(0), f(0), f(0), f(0), f(0), f(0), f(0))),
                os.getTable());

        // col: 0
        // row: 0

        // memo: problem: col 1 statt 0, row -1 -> deshalb unsolvable
        // eine kVar => invalide lösung => kann nicht mehr optimal sein????

        Assert.assertEquals("Beispiel3 (Ausgangstableau): getState", SimplexState.INVALID_SOLUTION,
                os.getState());
        Assert.assertArrayEquals("Beispiel3 (Ausgangstableau): getBaseVars", ints(3, 7, 5),
                os.getBaseVars());
        Assert.assertArrayEquals("Beispiel 1: getSolution", fs(f(0), f(1), f(0), f(1)),
                os.getSolution());

    }

    @Test(timeout = 2 * 1000)
    public void simplex_optimal_solution_1() {

        // Tableau hat bereits optimale Lösung
        // 1 + 0 + 0 < 1
        // 0 + 1 + 0 < 1
        // 0 + 0 + 1 < 1
        // 0 + 0 + 1 -> MIN

        SimplexSolver os = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(1), f(0), f(0)), Type.LE, f(1)), r(fs(f(0), f(1), f(0)), Type.LE, f(1)),
                        r(fs(f(0), f(0), f(1)), Type.LE, f(1))),
                SolveType.MIN, f(1), f(1), f(1)));

        Assert.assertArrayEquals("Beispiel3 (Ausgangstableau): getTable",
                fss(fs(f(1), f(0), f(0), f(1), f(0), f(0), f(0), f(0), f(0), f(1)),
                        fs(f(0), f(1), f(0), f(0), f(1), f(0), f(0), f(0), f(0), f(1)),
                        fs(f(0), f(0), f(1), f(0), f(0), f(1), f(0), f(0), f(0), f(1)),
                        fs(f(-1), f(-1), f(-1), f(0), f(0), f(0), f(0), f(0), f(0), f(0))),
                os.getTable());

        Assert.assertEquals("simplex_optimal_solution_1 (Ausgangstableau): getState",
                SimplexState.OPTIMAL, os.getState());
        Assert.assertArrayEquals("simplex_optimal_solution_1 (Ausgangstableau): getBaseVars",
                ints(3, 4, 5), os.getBaseVars());
        Assert.assertArrayEquals("simplex_optimal_solution_1: getSolution",
                fs(f(0), f(0), f(0), f(0)), os.getSolution());

    }

    // @Test(timeout = 2 * 1000)
    @Test
    public void simplex_optimal_ZERO_matrix() {

        // 0 + 0 + 0 < 0
        // 0 + 0 + 0 < 0
        // 0 + 0 + 0 < 0
        // 0 + 0 + -> MIN

        SimplexSolver os = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(0), f(0), f(0)), Type.LE, f(0)), r(fs(f(0), f(0), f(0)), Type.LE, f(0)),
                        r(fs(f(0), f(0), f(0)), Type.LE, f(0))),
                SolveType.MIN, f(0), f(0), f(0)));

        Assert.assertArrayEquals("Beispiel3 (Ausgangstableau): getTable",
                fss(fs(f(0), f(0), f(0), f(1), f(0), f(0), f(0), f(0), f(0), f(0)),
                        fs(f(0), f(0), f(0), f(0), f(1), f(0), f(0), f(0), f(0), f(0)),
                        fs(f(0), f(0), f(0), f(0), f(0), f(1), f(0), f(0), f(0), f(0)),
                        fs(f(0), f(0), f(0), f(0), f(0), f(0), f(0), f(0), f(0), f(0))),
                os.getTable());

        Assert.assertEquals("simplex_optimal_solution_1 (Ausgangstableau): getState",
                SimplexState.OPTIMAL, os.getState());
        Assert.assertArrayEquals("simplex_optimal_solution_1 (Ausgangstableau): getBaseVars",
                ints(3, 4, 5), os.getBaseVars());
        Assert.assertArrayEquals("simplex_optimal_solution_1: getSolution",
                fs(f(0), f(0), f(0), f(0)), os.getSolution());

        System.out.println(os);

        // 0 + 0 + 0 > 0
        // 0 + 0 + 0 > 0
        // 0 + 0 + 0 > 0
        // 0 + 0 + -> MAX

        SimplexSolver os2 = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(0), f(0), f(0)), Type.GE, f(0)), r(fs(f(0), f(0), f(0)), Type.GE, f(0)),
                        r(fs(f(0), f(0), f(0)), Type.GE, f(0))),
                SolveType.MAX, f(0), f(0), f(0)));

        Assert.assertArrayEquals("simplex_optimal_solution_1: getSolution",
                fs(f(0), f(0), f(0), f(0)), os2.getSolution());

        System.out.println(os2);

        // 0 + 0 + 0 = 0
        // 0 + 0 + 0 = 0
        // 0 + 0 + 0 = 0
        // 0 + 0 + -> MIN

        SimplexSolver os3 = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(0), f(0), f(0)), Type.GE, f(0)), r(fs(f(0), f(0), f(0)), Type.GE, f(0)),
                        r(fs(f(0), f(0), f(0)), Type.GE, f(0))),
                SolveType.MAX, f(0), f(0), f(0)));

        Assert.assertArrayEquals("simplex_optimal_solution_1: getSolution",
                fs(f(0), f(0), f(0), f(0)), os3.getSolution());

    }

    @Test(timeout = 2 * 1000)
    public void simplex_optimal_4x2_matrix() {

        // Tableau hat bereits optimale Lösung
        // 1 + 1 + 1 < 2
        // 2 + 1 + 1 < 1

        // -1 + 2 + 1 -> MAX

        SimplexSolver os = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(1), f(1), f(1)), Type.LE, f(2)), r(fs(f(2), f(1), f(1)), Type.LE, f(1))),
                SolveType.MAX, f(-1), f(2), f(1)));

        Assert.assertArrayEquals("(Ausgangstableau): getTable",
                fss(fs(f(1), f(1), f(1), f(1), f(0), f(0), f(0), f(2)),
                        fs(f(2), f(1), f(1), f(0), f(1), f(0), f(0), f(1)),
                        fs(f(-1), f(2), f(1), f(0), f(0), f(0), f(0), f(0))),
                os.getTable());

        // Assert.assertEquals(1, os.getPivotColumn());
        // Assert.assertEquals(1, os.getPivotRow());
        Assert.assertArrayEquals("getBaseVars", ints(3, 4), os.getBaseVars());

        // Assert.assertEquals("simplex_optimal_solution_1 (Ausgangstableau): getState",
        // SimplexState.OPTIMAL,os.getState());
        Assert.assertArrayEquals("getSolution", fs(f(0), f(1), f(0), f(2)), os.getSolution());

        // Tableau hat bereits optimale Lösung
        // 1 + 1 + 1 < 2
        // 2 + 1 + 1 < 1

        // -1 + 2 + 1 -> MIN

        SimplexSolver os2 = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(1), f(1), f(1)), Type.LE, f(2)), r(fs(f(2), f(1), f(1)), Type.LE, f(1))),
                SolveType.MIN, f(-1), f(2), f(1)));

        Assert.assertArrayEquals("Beispiel3 (Ausgangstableau): getTable",
                fss(fs(f(1), f(1), f(1), f(1), f(0), f(0), f(0), f(2)),
                        fs(f(2), f(1), f(1), f(0), f(1), f(0), f(0), f(1)),
                        fs(f(1), f(-2), f(-1), f(0), f(0), f(0), f(0), f(0))),
                os2.getTable());

        Assert.assertArrayEquals("simplex_optimal_solution_1: getSolution",
                fs(f(1, 2), f(0), f(0), f(1, 2)), os2.getSolution());

        // Tableau hat bereits optimale Lösung
        // 1 + 1 + 1 < 2
        // 0 + 1 + 1 > 1

        // 5 - 1 + 3 -> MAX

        SimplexSolver os3 = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(1), f(1), f(1)), Type.LE, f(2)), r(fs(f(0), f(1), f(1)), Type.GE, f(1))),
                SolveType.MAX, f(5), f(-1), f(3)));

        Assert.assertArrayEquals("simplex_optimal_solution_1: getSolution",
                fs(f(1), f(0), f(1), f(8)), os3.getSolution());

    }

    @Test(timeout = 2 * 1000)
    public void simplex_Max() {
        SimplexSolver s = new SimplexSolver(new LinearProgram(rs(r(fs(f(4), f(3)), Type.LE, f(320)),
                r(fs(f(2), f(4)), Type.GE, f(100)), r(fs(f(3), f(3)), Type.EQ, f(270))),
                SolveType.MAX, f(2), f(8)));

        Assert.assertEquals("Beispiel 1 (Ausgangstableau): getState", SimplexState.INVALID_SOLUTION,
                s.getState());
        Assert.assertArrayEquals("Beispiel 1 (Ausgangstableau): getTable",
                fss(fs(f(4), f(3), f(1), f(0), f(0), f(0), f(0), f(0), f(320)),
                        fs(f(2), f(4), f(0), f(-1), f(0), f(0), f(1), f(0), f(100)),
                        fs(f(3), f(3), f(0), f(0), f(0), f(0), f(0), f(1), f(270)),
                        fs(f(2), f(8), f(0), f(0), f(0), f(0), f(0), f(0), f(0))),
                s.getTable());
        Assert.assertArrayEquals("Beispiel 1 (Ausgangstableau): getBaseVars", ints(2, 6, 7),
                s.getBaseVars());

        Assert.assertArrayEquals("Beispiel 1: getSolution", fs(f(0), f(90), f(720)),
                s.getSolution());

    }

    @Test(timeout = 2 * 1000)
    public void simplex_bigMatrix() {
        SimplexSolver s =
                new SimplexSolver(
                        new LinearProgram(
                                rs(r(fs(f(1), f(2), f(3), f(4), f(5), f(6)), Type.GE, f(123)),
                                        r(fs(f(234), f(23), f(23), f(5), f(23), f(2)), Type.GE,
                                                f(2))),

                                SolveType.MAX, f(1), f(2), f(3), f(4), f(5), f(6)));

        Assert.assertEquals("Beispiel 1 (Ausgangstableau): getState", SimplexState.INVALID_SOLUTION,
                s.getState());
        Assert.assertArrayEquals("Beispiel 1: getSolution", null, s.getSolution());

    }

    @Test(timeout = 2 * 1000)
    public void simplex_bigNumbers() {
        SimplexSolver s = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(134), f(32), f(134)), Type.GE, f(687)),
                        r(fs(f(32), f(12), f(5)), Type.LE, f(545)),
                        r(fs(f(20), f(40), f(80)), Type.EQ, f(240))),

                SolveType.MIN, f(43), f(2), f(3)));

        Assert.assertArrayEquals("Beispiel 1: getSolution",
                fs(f(190, 67), f(0), f(307, 134), f(17261, 134)), s.getSolution());

    }

    // TODO: 2x3 Matrix????

    // 6 variablen ?
    // 6 zeilen?

}
