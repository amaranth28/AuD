package api;

import expression.ConstantExpression;
import expression.Expression;
import expression.VariableExpression;
import expression.binary.AndExpression;
import expression.binary.ConsequenceExpression;
import expression.binary.EqExpression;
import expression.binary.OrExpression;
import expression.binary.XorExpression;
import expression.unary.IdExpression;
import expression.unary.NotExpression;

/**
 * API zur Erstellung von Expression-Instanzen.
 * 
 * @author kar, mhe, Cedric Heinrich, Clemens Heinrich
 * 
 */
public class ExpressionAPI {

    /**
     * Erzeugt einen neuen konstanten Ausdruck, dessen Wert entweder "true" oder "false" ist.
     * 
     * @param value Der Wert der Konstanten
     * @return Ein neuer konstanter Ausdruck.
     */
    public Expression makeConstantExpression(boolean value) {
        return new ConstantExpression(value);
    }

    /**
     * Erzeugt einen neuen variablen Ausdruck.
     * 
     * @param name Der Bezeichner der Variablen.
     * @pre Der Bezeichner darf nicht null sein.
     * @pre Der Bezeichner darf nur Buchstaben enthalten.
     * @pre Der Bezeichner darf nicht leer sein.
     * 
     * @return Ein neuer variabler Ausdruck.
     */
    public Expression makeVariableExpression(String name) {
        assert (name != null);
        assert (name.matches("^[a-zA-Z]+$"));

        return new VariableExpression(name);
    }

    /**
     * Erzeugt einen neuen Ausdruck, der die Identitätsoperation auf einen bestehenden Ausdruck
     * anwendent.
     * 
     * @param operand Der Ausdruck auf den die Operation angewendet wird.
     * @pre operand ist nicht null.
     * @return Ein neuer Ausdruck.
     */
    public Expression makeIdExpression(Expression operand) {
        assert (operand != null);

        return new IdExpression(operand);
    }

    /**
     * Erzeugt einen neuen Ausdruck, der einen bestehenden Ausdruck negiert.
     * 
     * @param operand Der Ausdruck der invertiert werden soll.
     * @pre operand ist nicht null.
     * @return Ein neuer invertierender Ausdruck.
     */
    public Expression makeNotExpression(Expression operand) {
        assert (operand != null);

        return new NotExpression(operand);
    }

    /**
     * Erzeugt einen neuen Ausdruck mit einer binären Operation, die ein Und-Verknüpfung darstellt.
     * 
     * @param left Der linke Operand.
     * @param right Der rechte Operand.
     * @pre left und right sind nicht null.
     * @return Ein neuer Konjunktionsausdruck.
     */
    public Expression makeAndExpression(Expression left, Expression right) {
        assert (left != null) && (right != null);

        return new AndExpression(left, right);
    }

    /**
     * Erzeugt einen neuen Ausdruck mit einer binären Operation, die ein Oder-Verknüpfung darstellt.
     * 
     * @param left Der linke Operand.
     * @param right Der rechte Operand.
     * @pre left und right sind nicht null.
     * @return Ein neuer Disjunktionsausdruck.
     */
    public Expression makeOrExpression(Expression left, Expression right) {
        assert (left != null) && (right != null);

        return new OrExpression(left, right);
    }

    /**
     * Erzeugt einen neuen Ausdruck mit einer binären Operation, die ein Exklusiv-Oder-Verknüpfung
     * darstellt.
     * 
     * @param left Der linke Operand.
     * @param right Der rechte Operand.
     * @pre left und right sind nicht null.
     * @return Ein neuer Alternativausdruck.
     */
    public Expression makeXorExpression(Expression left, Expression right) {
        assert (left != null) && (right != null);

        return new XorExpression(left, right);
    }

    /**
     * Erzeugt einen neuen Ausdruck mit einer binären Operation, die ein Äquivalenz darstellt.
     * 
     * @param left Der linke Operand.
     * @param right Der rechte Operand.
     * @pre left und right sind nicht null.
     * @return Ein neuer Äquivalenzausdruck.
     */
    public Expression makeEquivalenceExpression(Expression left, Expression right) {
        assert (left != null) && (right != null);

        return new EqExpression(left, right);
    }

    /**
     * Erzeugt einen neuen Ausdruck mit einer binären Operation, die ein Implikation darstellt.
     * 
     * @param left Der linke Operand.
     * @param right Der rechte Operand.
     * @pre left und right sind nicht null.
     * @return Ein neuer Implikationsausdruck.
     */
    public Expression makeConsequenceExpression(Expression left, Expression right) {
        assert (left != null) && (right != null);

        return new ConsequenceExpression(left, right);
    }

}
