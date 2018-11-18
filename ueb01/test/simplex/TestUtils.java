package simplex;

import simplex.LinearProgram.Restriction;
import simplex.LinearProgram.Restriction.Type;

class TestUtils {

    private TestUtils() {
        // hide constructor
    }

    /** Hilfsmethode zum Erstellen einer Fraction-Instanz */
    static Fraction f(long numerator) {
        return new Fraction(numerator);
    }

    /** Hilfsmethode zum Erstellen einer Fraction-Instanz */
    static Fraction f(long numerator, long denominator) {
        return new Fraction(numerator, denominator);
    }

    /** Hilfsmethode zum Erstellen eines Fraction-Arrays */
    static Fraction[] fs(Fraction... fs) {
        return fs;
    }

    /** Hilfsmethode zum Erstellen einer Fraction-Matrix */
    static Fraction[][] fss(Fraction[]... fss) {
        return fss;
    }

    /** Hilfsmethode zum Erstellen einer Restriction-Instanz */
    static Restriction r(Fraction[] term, Type type, Fraction rightSide) {
        return new Restriction(term, type, rightSide);
    }

    /** Hilfsmethode zum Erstellen eines Restriction-Arrays */
    static Restriction[] rs(Restriction... rs) {
        return rs;
    }

    /** Hilfsmethode zum Erstellen eines int-Arrays */
    static int[] ints(int... ints) {
        return ints;
    }

}
