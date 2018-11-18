package simplex;

import java.util.Arrays; // darf nur in "toMatrix" verwendet werden

/**
 * Ein vollständig gekürzter mathematischer Bruch, der aus einem Zähler und einem Nenner besteht.
 * Fraction-Instanzen sind unveränderlich, d.h. für Änderungen werden stets neue Instanzen angelegt.
 * 
 * @author kar, mhe, Cedric Heinrich, Clemens Heinrich
 */
public class Fraction implements Comparable<Fraction> {
    /** Bruch mit Zähler -1 und Nenner 1 */
    public static final Fraction MINUS_ONE = new Fraction(-1);

    /** Bruch mit Zähler 0 und Nenner 1 */
    public static final Fraction ZERO = new Fraction(0);

    /** Bruch mit Zähler 1 und Nenner 1 */
    public static final Fraction ONE = new Fraction(1);

    /** Der Zähler des Bruchs */
    private final long numerator;

    /** Der Nenner des Bruchs */
    private final long denominator;

    /**
     * Erstellt einen vollständig gekürzten Bruch aus dem übergebenen Zähler und Nenner. Die interne
     * Repräsentation des Nenners ist nicht-negativ. Die Zahl 0 wird durch den Zähler 0 und den
     * Nenner 1 dargestellt.
     * 
     * @param numerator Zu verwendener Zähler (beliebige ganze Zahl)
     * @param denominator Zu verwendener Nenner (beliebige ganze Zahl, außer 0)
     * @pre denominator != 0
     */
    public Fraction(long numerator, long denominator) {
        assert denominator != 0;

        long gcd = gcd(numerator, denominator);
        if ((denominator < 0 && gcd > 0) || (denominator > 0 && gcd < 0)) {
            gcd = -gcd;
        }

        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;

    }

    /**
     * Erstellt einen Bruch aus dem übergebenen Zähler und dem Nenner 1.
     * 
     * @param numerator Zu verwendener Zähler (beliebige ganze Zahl)
     */
    public Fraction(long numerator) {
        this(numerator, 1);
    }

    /**
     * Gibt einen neuen Bruch zurück, der aus der Addition des übergebenen Bruchs entsteht.
     * 
     * @param other zu addierender Bruch (Summand)
     * @return neuer Bruch (Summe)
     * @pre other != null
     */
    public Fraction add(Fraction other) {
        assert other != null;

        // a/b + c/d = a*d/b*d + c*b/d*b
        return new Fraction(this.numerator * other.denominator + other.numerator * this.denominator,
                this.denominator * other.denominator);
    }

    /**
     * Gibt einen neuen Bruch zurück, der aus der Subtraktion des übergebenen Bruchs entsteht.
     * 
     * @param other zu subtrahierender Bruch (Subtrahend)
     * @return neuer Bruch (Differenz)
     * @pre other != null
     */
    public Fraction subtract(Fraction other) {
        assert other != null;

        // a - b = -b + a
        return other.multiplyBy(MINUS_ONE).add(this);
    }

    /**
     * Gibt einen neuen Bruch zurück, der aus der Multiplikation mit dem übergebenen Bruch entsteht.
     * 
     * @param other zu multiplizierenden Bruch (Faktor)
     * @return neuer Bruch (Produkt)
     * @pre other != null
     */
    public Fraction multiplyBy(Fraction other) {
        assert other != null;

        // a/b * c/d = a*c/b*d
        return new Fraction(this.numerator * other.numerator, this.denominator * other.denominator);
    }

    /**
     * Gibt einen neuen Bruch zurück, der aus der Division des übergebenen Bruchs entsteht.
     * 
     * @param other Bruch, durch den geteilt wird (Divisor)
     * @return neuer Bruch (Quotient)
     * @pre other != null
     */
    public Fraction divideBy(Fraction other) {
        assert other != null;

        // TODO: check auf other = 0 & throw ArithmeticException ?

        // a/b : c/d = a/b * d/c
        return this.multiplyBy(new Fraction(other.denominator, other.numerator));
    }

    /**
     * Liefert den Zähler des Bruchs.
     * 
     * @return der Zähler des Bruchs
     */
    public long getNumerator() {
        return this.numerator;
    }

    /**
     * Liefert den Nenner des Bruchs.
     * 
     * @return der Nenner des Bruchs
     */
    public long getDenominator() {
        return this.denominator;
    }

    /**
     * Liefert den Wert des Bruches als Gleitkommazahl (floating-point number).
     * 
     * @return der Wert des Bruchs als Gleitkommazahl (floating-point number)
     */
    public double getAsFPN() {
        return (double) this.numerator / this.denominator;
    }

    /**
     * Gibt die Stringrepräsentation des Bruchs zurück.
     * 
     * @return Stringrepräsentation des Bruchs
     */
    @Override
    public String toString() {
        return this.numerator + (this.denominator == 1 ? "" : "/" + this.denominator);
    }

    /**
     * Vergleicht diesen Bruch mit dem übergebenen Bruch. Gibt eine Zahl kleiner bzw. größer als 0
     * zurück, wenn die von diesem Bruch repräsentierte Zahl kleiner bzw. größer als die des
     * übergebenen Bruchs ist. Wenn die repräsentierten Zahlen gleich sind, wird 0 zurückgegeben.
     * 
     * @param other Bruch, mit dem dieser Bruch verglichen wird
     * @return Vergleichsergebnis (kleiner, gleich, oder größer 0)
     * @pre other != null
     */
    @Override
    public int compareTo(Fraction other) {
        assert other != null;

        Fraction diff = this.subtract(other);
        long numerator = diff.numerator;

        return numerator > 0 ? 1 : (numerator == 0 ? 0 : -1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Fraction)) {
            return false;
        }
        Fraction other = (Fraction) obj;
        return this.numerator == other.numerator && this.denominator == other.denominator;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (numerator ^ (numerator >>> 32));
        result = prime * result + (int) (denominator ^ (denominator >>> 32));
        return result;
    }

    /**
     * Gibt die Stringrepräsentation einer übergebenen Fraction-Matrix zurück.
     * 
     * @param fractions Matrix
     * @return Stringrepräsentation von fractions
     * @pre fractions != null
     */
    public static String toMatrix(Fraction[][] fractions) {
        assert fractions != null;

        StringBuilder sb = new StringBuilder();
        for (Fraction[] row : fractions) {
            sb.append(Arrays.toString(row));
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Liefert den größten gemeinsamen Teiler von num1 und num2.
     * 
     * @param num1 die erste Zahl
     * @param num2 die zweite Zahl
     * @return des größten gemeinsamen Teiler der Zahlen
     */
    public static long gcd(long num1, long num2) {
        // GCD nach Euklid mit Rekursion
        if ((num1 % num2) == 0) {
            return num2;
        } else {
            return gcd(num2, num1 % num2);
        }
    }

}
