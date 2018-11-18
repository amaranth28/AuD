package simplex;

import simplex.LinearProgram.Restriction;
import simplex.LinearProgram.Restriction.Type;
import simplex.LinearProgram.SolveType;

/**
 * Ein Automat zum Lösen linearer Optimierungsprobleme
 * 
 * @author kar, mhe, Cedric Heinrich, Clemens Heinrich
 * 
 */
public class SimplexSolver {

    /** Ungültiger Index um Fehler anzudeuten: {@value #INVALID_INDEX} */
    private static final int INVALID_INDEX = -1;

    /**
     * Zustand der Lösung
     */
    public enum SimplexState {
        /** ungültige Lösung */
        INVALID_SOLUTION,
        /** gültige Lösung */
        VALID_SOLUTION,
        /** optimale Lösung */
        OPTIMAL,
        /** unlösbar */
        UNSOLVABLE
    }

    /** aktueller Zustand der Simplex-Tableaus */
    private SimplexState state;

    /** Simplex-Tableau mit den Zeilen in der ersten und den Spalten in der zweiten Dimension */
    private Fraction[][] table;

    /** Höhe des Tableaus */
    private int tableHeight;

    /** Breite des Tableaus */
    private int tableWidth;

    /** Indices des Basisvariablen */
    private int[] baseVars;

    /** Anzahl der Entscheidungsvariablen */
    private final int numDecisionVariables;

    /** Anzahl der Schlupfvariablen */
    private final int numSlackVariables;

    /** Anzahl der künstlichen Variablen */
    private final int numArtificialVariables;

    /** Optimierungsrichtung für den Wert den Zielfunktion */
    private final SolveType solveType;

    /**
     * Erstellt einen Automaten aus dem übergebenen linearer Optimierungsproblem. Der Automat
     * verbleibt im Ausgangstableau, d.h. es werden noch keine Optimierungsschritte durchgeführt.
     * 
     * @param lp lineares Problem, das optimiert werden soll
     * @pre lp != null
     */
    public SimplexSolver(LinearProgram lp) {
        assert lp != null;

        this.numDecisionVariables = lp.getObjectiveTerm().length;
        this.numSlackVariables = lp.getRestrictions().length;
        this.numArtificialVariables = lp.getRestrictions().length;
        // +1 für ObjectiveRow
        this.tableHeight = lp.getRestrictions().length + 1;
        // +1 für RHS
        this.tableWidth = this.numDecisionVariables + this.numSlackVariables
                + this.numArtificialVariables + 1;
        this.table = new Fraction[tableHeight][tableWidth];
        this.solveType = lp.getSolveType();

        initTableau(lp);
        initBaseVars();

        this.state = determineInitialState();
    }

    /**
     * Initialisiert die Basisvariablen
     */
    private void initBaseVars() {
        this.baseVars = new int[tableHeight - 1];

        for (int i = 0; i < tableHeight; i++) {
            Fraction[] row = getRow(i);
            for (int j = getOffsetSlackVariable(); j < getOffsetRHS(); j++) {
                Fraction cell = row[j];
                if (Fraction.ONE.equals(cell)) {
                    setBaseVar(i, j);
                }
            }
        }
    }

    /**
     * Initialisiert das Tableau mit dem LOP
     * 
     * @param lp das LOP
     */
    private void initTableau(LinearProgram lp) {
        initTableauWithFraction(Fraction.ZERO);
        initTableauWithRestrictions(lp.getRestrictions());
        initTableauWithObjectiveTerm(lp.getObjectiveTerm(), lp.getSolveType());
    }

    /**
     * Initialisiert alle Zellen des Tableaus mit einem übergebenen Wert
     * 
     * @param fraction der Wert mit dem alle Zellen initialisiert werden
     */
    private void initTableauWithFraction(Fraction fraction) {
        for (int i = 0; i < tableHeight; i++) {
            for (int j = 0; j < tableWidth; j++) {
                setEntry(i, j, fraction);
            }
        }
    }

    /**
     * Initialisiert das Tableau mit den Restriktionen des LOP
     * 
     * @param restrictions die Restriktionen des LOP
     */
    private void initTableauWithRestrictions(Restriction[] restrictions) {
        for (int i = 0; i < restrictions.length; i++) {
            Restriction restriction = restrictions[i];
            Fraction slackVar = getSlackVarByRestrictionType(restriction.getType());
            Fraction artificialVar = getArtificialVarByRestrictionType(restriction.getType());
            for (int j = 0; j < restriction.getTerm().length; j++) {
                setEntry(i, j, restriction.getTerm()[j]);
            }
            setEntry(i, getOffsetSlackVariable() + i, slackVar);
            setEntry(i, getOffsetArtificialVariable() + i, artificialVar);
            setEntry(i, getOffsetRHS(), restriction.getRightSide());
        }
    }

    /**
     * Initialisiert das Tableau mit der Zielfunktion des LOP
     * 
     * @param term der Term der Zielfunktion
     * @param solveType Optimierungsrichtung für den Wert den Zielfunktion
     */
    private void initTableauWithObjectiveTerm(Fraction[] term, SolveType solveType) {
        int objectiveRowIdx = tableHeight - 1;
        final boolean isMinSolveType = SolveType.MIN.equals(solveType);
        for (int i = 0; i < term.length; i++) {
            setEntry(objectiveRowIdx, i,
                    isMinSolveType ? term[i].multiplyBy(Fraction.MINUS_ONE) : term[i]);
        }
    }

    /**
     * Ermittelt den initialen State des Tableaus
     * 
     * @return der initiale State des Tableaus
     */
    private SimplexState determineInitialState() {
        SimplexState state = null;
        if (isValidSolution()) {
            // Initial gültige Lösung (Restriktionen nur mit <=)
            state = SimplexState.VALID_SOLUTION;
        } else if (restrictionsHaveNegativeRHS()) {
            // Verhalten für LOP, deren Restriktionen negative rechte Seiten haben, ist nicht
            // definiert.
            state = SimplexState.UNSOLVABLE;
        } else {
            // alles andere bildet eine ungültige Lösung (initial kann nicht Optimal sein?)
            state = SimplexState.INVALID_SOLUTION;
        }

        return state;
    }

    /**
     * @return Referenz auf die Indices der Basisvariablen
     */
    public int[] getBaseVars() {
        return this.baseVars;
    }

    /**
     * Versucht, das lineare Optimierungsproblem zu lösen. Wenn dies möglich ist, werden die
     * optimalen Koeffizienten und der Wert der Zielfunktion zurückgegeben, andernfalls die
     * null-Referenz.
     * 
     * @return optimale Koeffizienten und Wert der Zielfunktion (in gegebener Reihenfolge) oder
     *         null, wenn unlösbar
     */
    public Fraction[] getSolution() {

        solve();

        if (!SimplexState.UNSOLVABLE.equals(this.state)) {
            Fraction[] solution = new Fraction[numDecisionVariables + 1];
            // Iteration über Decision und Schlupfvariablen
            for (int i = 0; i < getOffsetSlackVariable(); i++) {
                solution[i] = Fraction.ZERO;
                for (int j = 0; j < this.baseVars.length; j++) {
                    if (getBaseVar(j) == i) {
                        solution[i] = getRow(j)[getOffsetRHS()];
                    }
                }
            }
            // Für Maximierungsproblem *(-1)
            Fraction objectiveValue = getObjectiveRow()[getOffsetRHS()];
            solution[solution.length - 1] = SolveType.MIN.equals(this.solveType) ? objectiveValue
                    : objectiveValue.multiplyBy(Fraction.MINUS_ONE);
            return solution;
        } else {
            return null;
        }
    }

    /**
     * @return aktueller Zustand des Simplex-Tableaus
     */
    public SimplexState getState() {
        return this.state;
    }

    /**
     * @return Referenz auf das Simplex-Tableau
     */
    public Fraction[][] getTable() {
        return this.table;
    }

    /**
     * Führt wiederholt einen Simplexschritt aus, bis das Tableau eine optimale Lösung anzeigt
     * (SimplexState.OPTIMAL) oder es sich als unlösbar erweist (SimplexState.UNSOLVABLE).
     * 
     * @return Der Endzustand des Simplex-Tableaus (SimplexState.OPTIMAL oder
     *         SimplexState.UNSOLVABLE)
     */
    public SimplexState solve() {
        while (!SimplexState.OPTIMAL.equals(this.state)
                && !SimplexState.UNSOLVABLE.equals(this.state)) {
            step();
        }

        return this.state;
    }

    /**
     * Führt einen Schritt des Simplex-Algorithmus auf dem Simplex-Tableau gemäß Aufgabenstellung
     * aus: 1. es wird eine Pivotspalte ausgewählt, 2. es wird eine Pivotzeile ausgewählt, 3. die
     * Variable der Pivotspalte wird gegen die bisherige der Pivotzeile getauscht, 4. die Pivotzeile
     * wird normiert, 5. die übrigen Zeilen werden reduziert. <br>
     *
     * Für Tableaus im Zustand SimplexState.OPTIMAL oder SimplexState.UNSOLVABLE werden keine
     * Berechnungen durchgeführt und der bisherige Zustand bleibt unverändert. <br>
     *
     * Befindet sich das Tableau im Zustand SimplexState.INVALID_SOLUTION und kann keine Pivotspalte
     * mit Koeffizientensumme (siehe Aufgabenstellung) &gt; 0 identifiziert werden, so wechselt das
     * Tableau in den Zustand SimplexState.UNSOLVABLE und es werden keine weiteren Berechnungen
     * ausgeführt. <br>
     *
     * Befindet sich das Tableau im Zustand SimplexState.VALID_SOLUTION und kann keine Pivotspalte
     * mit Zielfunktionszeilenwert &gt; 0 identifiziert werden, so wechselt das Tableau in den
     * Zustand SimplexState.OPTIMAL und es werden keine weiteren Berechnungen ausgeführt. <br>
     *
     * Kann keine Pivotzeile identifiziert werden (d.h. kein Quotient Q hat einen Wert &ge; 0), so
     * wechselt das Tableau in den Zustand SimplexState.UNSOLVABLE und es werden keine weiteren
     * Berechnungen ausgeführt. <br>
     *
     * Wird der Schritt erfolgreich ausgeführt und befinden sich noch künstliche Variablen in der
     * Basis, so erhält das Tableau den Zustand SimplexState.INVALID_SOLUTION. Wird der Schritt
     * erfolgreich ausgeführt und befinden sich keine künstlichen Variablen in der Basis, so erhält
     * das Tableau den Zustand SimplexState.VALID_SOLUTION. <br>
     *
     * @return Der Zustand des Simplex-Tableaus am Ende der Methode
     */
    public SimplexState step() {

        if (!SimplexState.OPTIMAL.equals(this.state)
                && !SimplexState.UNSOLVABLE.equals(this.state)) {

            // 1. Auswahl der Pivotspalte
            int pivotColIndex = getPivotColIndex();
            // 2. Auswahl der Pivotzeile
            int pivotRowIndex = getPivotRowIndex(pivotColIndex);

            if (pivotColIndex == INVALID_INDEX) {
                if (SimplexState.VALID_SOLUTION.equals(this.state)) {
                    // Tableau bildet gültige Lösung ab und Pivotspalte nicht findbar
                    // => optimal
                    this.state = SimplexState.OPTIMAL;
                } else {
                    // Tableau bildet ungültige Lösung ab und Pivotspalte nicht findbar
                    // => unlösbar
                    this.state = SimplexState.UNSOLVABLE;
                }
            } else if (pivotRowIndex == INVALID_INDEX) {
                // Pivotzeile nicht findbar
                // => unlösbar
                this.state = SimplexState.UNSOLVABLE;
            } else {
                Fraction pivotElement = getEntry(pivotRowIndex, pivotColIndex);
                // 3. Austausch der Variablen / Normierung der Pivotzeile
                setBaseVar(pivotRowIndex, pivotColIndex);
                scalePivotRow(pivotRowIndex, pivotElement);

                // 4. Reduktion der anderen Zeilen
                reduceNonPivotRows(pivotRowIndex, pivotColIndex);

                this.state = isValidSolution() ? SimplexState.VALID_SOLUTION
                        : SimplexState.INVALID_SOLUTION;
            }
        }

        return this.state;
    }

    /**
     * Reduziert alle Zeilen, außer der Pivotzeile, sodass in der Pivotspalte eine 0 entsteht
     * 
     * @param pivotRowIndex Index der Pivotzeile
     * @param pivotColIndex Index der Pivotspalte
     */
    private void reduceNonPivotRows(int pivotRowIndex, int pivotColIndex) {
        for (int i = 0; i < this.tableHeight; i++) {
            // Pivotzeile auslassen
            if (i != pivotRowIndex) {
                // Faktor ermitteln mit dem die Pivotzeile multipliziert werden muss, sodass in der
                // Pivotspalte eine 0 entsteht
                Fraction factor = getEntry(i, pivotColIndex);
                for (int j = 0; j < this.tableWidth; j++) {
                    // Zelle mit einem Vielfachen von dem korrespondierenden Wert in der Pivotzeile
                    // subtrahieren
                    setEntry(i, j,
                            getEntry(i, j).subtract(getEntry(pivotRowIndex, j).multiplyBy(factor)));
                }
            }
        }

    }

    /**
     * Normiert die Pivotzeile
     * 
     * @param pivotRowIndex Index der Pivotzeile
     * @param pivotElement der Wert des Pivotelements
     */
    private void scalePivotRow(int pivotRowIndex, Fraction pivotElement) {
        for (int i = 0; i < this.tableWidth; i++) {
            setEntry(pivotRowIndex, i, getEntry(pivotRowIndex, i).divideBy(pivotElement));
        }
    }

    /**
     * Liefert den Index der Pivotspalte oder {@link #INVALID_INDEX} wenn keine gültige Pivotspalte
     * gefunden werden konnte
     * 
     * @return den Index der Pivotspalte oder {@link #INVALID_INDEX}
     */
    private int getPivotColIndex() {
        if (SimplexState.VALID_SOLUTION.equals(this.state)) {
            return getPivotColForValidSolution();
        } else {
            return getPivotColForInvalidSolution();
        }
    }

    /**
     * Liefert den Index der Pivotspalte für ein LOP im {@link SimplexState#INVALID_SOLUTION} Status
     * oder {@link #INVALID_INDEX} wenn keine gültige Pivotspalte gefunden werden konnte
     * 
     * @return den Index der Pivotspalte oder {@link #INVALID_INDEX} wenn keine gültige Pivotspalte
     *         gefunden werden konnte
     */
    private int getPivotColForInvalidSolution() {
        int maxIndex = INVALID_INDEX;
        Fraction maxValue = Fraction.ZERO;
        // Iteration über alle möglichen Spalten
        for (int i = 0; i < getOffsetArtificialVariable(); i++) {
            Fraction colSum = Fraction.ZERO;
            for (int j = 0; j < this.baseVars.length; j++) {
                // Prüfung ob baseVar eine künstliche Variable ist
                if (getBaseVar(j) >= getOffsetArtificialVariable()) {
                    colSum = colSum.add(getEntry(j, i));
                }
            }
            // Prüfung auf neues Maximum
            if (colSum.compareTo(maxValue) > 0) {
                maxIndex = i;
                maxValue = colSum;
            }
        }
        return maxIndex;
    }

    /**
     * Liefert den Index der Pivotspalte für ein LOP im {@link SimplexState#VALID_SOLUTION} Status
     * oder {@link #INVALID_INDEX} wenn keine gültige Pivotspalte gefunden werden konnte
     * 
     * @return den Index der Pivotspalte oder {@link #INVALID_INDEX} wenn keine gültige Pivotspalte
     *         gefunden werden konnte
     */
    private int getPivotColForValidSolution() {
        int idx = INVALID_INDEX;
        Fraction max = Fraction.ZERO;
        Fraction[] objectiveRow = getObjectiveRow();
        // Iteration über alle möglichen Spalten
        for (int i = 0; i < getOffsetArtificialVariable(); i++) {
            Fraction other = objectiveRow[i];
            // Prüfung auf neues Maximum
            if (max.compareTo(other) < 0) {
                max = other;
                idx = i;
            }
        }
        // wenn der größte Koeffizient nicht >0 ist, dann -1 zurückliefen um ungültige Pivotspalte
        // anzudeuten
        return max.compareTo(Fraction.ZERO) > 0 ? idx : INVALID_INDEX;
    }

    /**
     * Liefert den Index der Pivotzeile oder {@link #INVALID_INDEX}, wenn keine gültige Pivotzeile
     * gefunden werden konnte
     * 
     * @param pivotCol der Index der Pivotspalte
     * @return den Index der Pivotzeile oder {@link #INVALID_INDEX}, wenn keine gültige Pivotzeile
     *         gefunden werden konnte
     */
    private int getPivotRowIndex(int pivotCol) {
        Fraction minValue = null;
        int minIndex = INVALID_INDEX;

        // Prüfung ob es eine gültige Pivotspalte gibt
        if (pivotCol != INVALID_INDEX) {
            // Iteration über alle möglichen Reihen
            for (int i = 0; i < getOffsetObjectiveRow(); i++) {
                Fraction cell = getEntry(i, pivotCol);
                Fraction quotient = null;
                // Division durch 0 abfangen
                if (!Fraction.ZERO.equals(cell)) {
                    quotient = getEntry(i, getOffsetRHS()).divideBy(cell);
                }

                // Prüfung auf neues Minimum das > 0 ist
                if (quotient != null && quotient.compareTo(Fraction.ZERO) > 0
                        && (minValue == null || minValue.compareTo(quotient) > 0)) {
                    minValue = quotient;
                    minIndex = i;
                }

            }
        }

        return minIndex;
    }

    /**
     * Liefert einen Wert für eine Schlupfvariable korrespondierend zum übergebenen
     * {@link Restriction.Type}
     * <ul>
     * <li>= -> 0</li>
     * <li>>= -> -1</li>
     * <li><= -> 1</li>
     * </ul>
     * 
     * @param restrictionType der {@link Restriction.Type} der Restriktion
     * @return den korrespondierenden Wert für die Schlupfvariable
     */
    private static Fraction getSlackVarByRestrictionType(Type restrictionType) {
        switch (restrictionType) {
            case EQ:
                return Fraction.ZERO;
            case GE:
                return Fraction.MINUS_ONE;
            case LE:
                return Fraction.ONE;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Liefert einen Wert für eine künstliche Variable korrespondierend zum übergebenen
     * {@link Restriction.Type}
     * <ul>
     * <li>= -> 1</li>
     * <li>>= -> 1</li>
     * <li><= -> 0</li>
     * </ul>
     * 
     * @param restrictionType restrictionType der {@link Restriction.Type} der Restriktion
     * @return den korrespondierenden Wert für die künstliche Variable
     */
    private static Fraction getArtificialVarByRestrictionType(Type restrictionType) {
        switch (restrictionType) {
            case EQ:
            case GE:
                return Fraction.ONE;
            case LE:
                return Fraction.ZERO;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * @return den Offset für die Schlupfvariablen
     */
    private int getOffsetSlackVariable() {
        return this.numDecisionVariables;
    }

    /**
     * @return den Offset für die künstlichen Variablen
     */
    private int getOffsetArtificialVariable() {
        return this.numDecisionVariables + this.numSlackVariables;
    }

    /**
     * @return den Offset für die RHS
     */
    private int getOffsetRHS() {
        return tableWidth - 1;
    }

    /**
     * Liefert eine Zeile des Tableaus zu einem übergebenen Index
     * 
     * @param index der Index der Zeile
     * @return die Zeile des Tableaus
     */
    private Fraction[] getRow(int index) {
        return this.table[index];
    }

    /**
     * @return den Offset der Zielfunktion
     */
    private int getOffsetObjectiveRow() {
        return tableHeight - 1;
    }

    /**
     * @return die Zeile der Zielfunktion
     */
    private Fraction[] getObjectiveRow() {
        return getRow(getOffsetObjectiveRow());
    }

    /**
     * Ermittelt, ob das Tableau eine gültige Lösung abbildet
     * 
     * @return true wenn das Tableau eine gültige Lösung abbildet, ansonsten false
     */
    private boolean isValidSolution() {
        return !hasArtificialVarAsBaseVar();
    }

    /**
     * @return true, wenn eine künstliche Variable als Basis benutz wird
     */
    private boolean hasArtificialVarAsBaseVar() {
        for (int i : this.baseVars) {
            if (i >= getOffsetArtificialVariable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true, wenn eine Restriktion eine negative RHS hat
     */
    private boolean restrictionsHaveNegativeRHS() {
        for (int i = 0; i < getOffsetObjectiveRow(); i++) {
            if (Fraction.ZERO.compareTo(getEntry(i, getOffsetRHS())) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Liefert den Wert einer Zelle des Tableaus der übergebenen Indexen
     * 
     * @param row Index der Zeile
     * @param col Index der Spalte
     * @return der Wert der Zelle
     */
    private Fraction getEntry(int row, int col) {
        return this.table[row][col];
    }

    /**
     * Setzt den Wert einer Zelle des Tableaus der übergebenen Indexe
     * 
     * @param row Index der Zeile
     * @param col Index der Spalte
     * @param value der Wert, der in der Zelle gesetzt werden soll
     */
    private void setEntry(int row, int col, Fraction value) {
        this.table[row][col] = value;
    }

    /**
     * Liefert den Wert der Basisvariablen am übergebenen Index
     * 
     * @param idx Index der Basisvariablen
     * @return den Wert der Basisvariablen am übergebenen Index
     */
    private int getBaseVar(int idx) {
        return this.baseVars[idx];
    }

    /**
     * Setzt den Wert einer Basisvariablen am übergebenen Index mit
     * 
     * @param idx Index der Basisvariablen
     * @param baseVarIdx neuer Wert der Basisvariablen
     */
    private void setBaseVar(int idx, int baseVarIdx) {
        this.baseVars[idx] = baseVarIdx;
    }

}
