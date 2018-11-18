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
 * @author kar, mhe, ch
 *
 */
public class SimplexTests {

    @Test
    public void simplex_1() {
        // (1) 4x + 3y <= 320
        // (2) 2x + 4y >= 100
        // (3) 3x + 3y == 270
        // (4) 2x + 8y == MIN
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

    @Test
    public void simplex_2() {
        // (1) 1x + 1y ≤ 4
        // (2) 2x + 1y ≤ 5
        // (3) 3x + 4y = MAX
        SimplexSolver s = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(1), f(1)), Type.LE, f(4)), r(fs(f(2), f(1)), Type.LE, f(5))),
                SolveType.MAX, f(3), f(4)));
        // Lösung: x = 0; y = 4; MAX = 16
        Assert.assertArrayEquals("getSolution", fs(f(0), f(4), f(16)), s.getSolution());
    }

    @Test
    public void simplex_3() {
        // (1) 1x + 2y ≤ 6
        // (2) 3x + 2y ≤ 12
        // (3) -2x + 1y = MIN
        SimplexSolver s = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(1), f(2)), Type.LE, f(6)), r(fs(f(3), f(2)), Type.LE, f(12))),
                SolveType.MIN, f(-2), f(1)));
        // Lösung: x = 4; y = 0; MIN = -8
        Assert.assertArrayEquals("getSolution", fs(f(4), f(0), f(-8)), s.getSolution());
    }

    @Test
    public void simplex_4() {
        // (1) 1x + 2y ≥ 4
        // (2) 3x + 2y ≥ 3
        // (3) 2x + 5y = MIN
        SimplexSolver s = new SimplexSolver(new LinearProgram(
                rs(r(fs(f(1), f(2)), Type.GE, f(4)), r(fs(f(3), f(2)), Type.GE, f(3))),
                SolveType.MIN, f(2), f(5)));
        // Lösung: x = 4; y = 0; MIN = 8
        Assert.assertArrayEquals("getSolution", fs(f(4), f(0), f(8)), s.getSolution());
    }

    @Test
    public void simplex_5() {
        // (1) 1x + 1y ≤ 3
        // (2) 1x + 1y ≤ 2
        // (3) 1x - 1y ≥ 1
        // (4) 1x + 1y = MAX
        SimplexSolver s = new SimplexSolver(new LinearProgram(rs(r(fs(f(1), f(1)), Type.LE, f(3)),
                r(fs(f(1), f(1)), Type.LE, f(2)), r(fs(f(1), f(-1)), Type.GE, f(1))), SolveType.MAX,
                f(1), f(1)));
        // Lösung: x = 3/2; y = 1/2; MAX = 2
        Assert.assertArrayEquals("getSolution", fs(f(3, 2), f(1, 2), f(2)), s.getSolution());
    }

    @Test
    public void simplex_6() {
        // (1) 1x + 1y ≤ 6
        // (2) 3x + 1y ≤ 15
        // (3) 1x + 3y ≥ 15
        // (4) 0.5x + 1.5y = MAX
        SimplexSolver s =
                new SimplexSolver(new LinearProgram(
                        rs(r(fs(f(1), f(1)), Type.LE, f(6)), r(fs(f(3), f(1)), Type.LE, f(15)),
                                r(fs(f(1), f(3)), Type.LE, f(15))),
                        SolveType.MAX, f(1, 2), f(3, 2)));
        // Lösung: x = 0; y = 5; MAX = 15/2
        Assert.assertArrayEquals("getSolution", fs(f(0), f(5), f(15, 2)), s.getSolution());
    }

}
