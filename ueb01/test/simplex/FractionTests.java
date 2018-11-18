package simplex;

import static simplex.TestUtils.f;

import org.junit.Assert;
import org.junit.Test;

public class FractionTests {

    @Test
    public void constructor() {
        Assert.assertEquals("1/1 = 1", Fraction.ONE, f(1));
        Assert.assertEquals("3/3 = 1", Fraction.ONE, f(3, 3));
        Assert.assertEquals("2/2 = 1", Fraction.ONE, f(2, 2));
        Assert.assertEquals("4/2 = 2", f(2), f(4, 2));
        Assert.assertEquals("2/4 = 1/2", f(1, 2), f(2, 4));
    }

    @Test
    public void construct_positive_num_positive_denom() {
        Fraction f = f(1, 2);
        Assert.assertEquals(1, f.getNumerator());
        Assert.assertEquals(2, f.getDenominator());
    }

    @Test
    public void construct_negative_num_negative_denom() {
        Fraction f = f(-1, -2);
        Assert.assertEquals(1, f.getNumerator());
        Assert.assertEquals(2, f.getDenominator());
    }

    @Test
    public void construct_positive_num_negative_denom() {
        // negative Werte werden mit positiven Zähler und negativen Nenner abgebildet

        Fraction f = f(1, -2);
        Assert.assertEquals(-1, f.getNumerator());
        Assert.assertEquals(2, f.getDenominator());
    }

    @Test
    public void construct_negative_num_positive_denom() {
        Fraction f = f(-1, 2);
        Assert.assertEquals(-1, f.getNumerator());
        Assert.assertEquals(2, f.getDenominator());
    }

    @Test
    public void add() {
        Assert.assertEquals("1/2 + 1/2", f(1, 1), f(1, 2).add(f(1, 2)));
        Assert.assertEquals("1/2 + 0/1", f(1, 2), f(1, 2).add(Fraction.ZERO));
        Assert.assertEquals("4/2 + 1/1", f(3, 1), f(4, 2).add(Fraction.ONE));
        Assert.assertEquals("1/2 + 0/1", f(1, 2), f(1, 2).add(Fraction.ZERO));
        Assert.assertEquals("0/1 + 0/1", Fraction.ZERO, Fraction.ZERO.add(Fraction.ZERO));
    }

    @Test
    public void subtract() {
        Assert.assertEquals("1/2 - 1/2", Fraction.ZERO, f(1, 2).subtract(f(1, 2)));
        Assert.assertEquals("1/2 - 1/4", f(1, 4), f(1, 2).subtract(f(1, 4)));
        Assert.assertEquals("1/2 - 0/1", f(1, 2), f(1, 2).subtract(Fraction.ZERO));
        Assert.assertEquals("0/1 - 0/1", Fraction.ZERO, Fraction.ZERO.subtract(Fraction.ZERO));
    }

    @Test
    public void multiplyBy() {
        Assert.assertEquals("2/3 * 3/4", f(1, 2), f(2, 3).multiplyBy(f(3, 4)));
        Assert.assertEquals("1/2 * 1/2", f(1, 4), f(1, 2).multiplyBy(f(1, 2)));
        Assert.assertEquals("1/2 * 1/1", f(1, 2), f(1, 2).multiplyBy(Fraction.ONE));
        Assert.assertEquals("1/2 * 0/1", Fraction.ZERO, f(1, 2).multiplyBy(Fraction.ZERO));
        Assert.assertEquals("2/2 * 1/1", Fraction.ONE, f(2, 2).multiplyBy(Fraction.ONE));
    }

    @Test
    public void divide() {
        Assert.assertEquals("1/2 : 1/2", Fraction.ONE, f(1, 2).divideBy(f(1, 2)));
        Assert.assertEquals("1/2 : 1/1", f(1, 2), f(1, 2).divideBy(Fraction.ONE));
        Assert.assertEquals("1/2 : 1/4", f(2, 1), f(1, 2).divideBy(f(1, 4)));
        Assert.assertEquals("1/2 : 2/1", f(1, 4), f(1, 2).divideBy(f(2, 1)));
        Assert.assertEquals("0/1 : 1/2", Fraction.ZERO, Fraction.ZERO.divideBy(f(1, 2)));
    }

    @Test
    public void compareTo() {
        Assert.assertTrue("2/3 > 3/7", f(2, 3).compareTo(f(3, 7)) > 0);
        Assert.assertTrue("3/7 < 2/3", f(3, 7).compareTo(f(2, 3)) < 0);
        Assert.assertTrue("2/3 = 2/3", f(2, 3).compareTo(f(2, 3)) == 0);
    }

}
