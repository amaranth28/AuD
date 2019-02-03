package fileio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
//import java.io.I
import java.util.function.Predicate;

import db.DBTable;

/**
 * @author Cedric Heinrich, Clemens Heinrich
 *
 */
public class FileUtilImpl implements FileUtil {

    /*
     * (non-Javadoc)
     * 
     * @see fileio.FileUtil#readTableFromFile(java.lang.String)
     */
    @Override
    public DBTable readTableFromFile(String filename) throws IOException, WrongSyntaxException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
            // 1. Zeile: Table-ID
            String tableId = validateSyntax(br.readLine(), DBTable::isValidIdentifier, "Table-ID");
            // 2. Zeile: Kommaseparierte Column-IDs
            String[] colIds = validateSyntax(br.readLine().split(","), DBTable::areValidIdentifiers,
                    "Col-IDs");

            DBTable fileTable = new DBTable(tableId, colIds);

            // 3. Zeile Kommaseparierte Row-Values
            String line = br.readLine();
            if (line != null) {
                String[] rows = line.split(",");
                for (int i = 0; i < rows.length / colIds.length; i++) {
                    String[] values = new String[colIds.length];
                    for (int j = 0; j < colIds.length; j++) {
                        values[j] = rows[(colIds.length * i) + j];
                    }
                    fileTable.appendRow(values);
                }

            }

            return fileTable;

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fileio.FileUtil#writeTableToFile(java.lang.String, db.DBTable)
     */
    /**
     * Liefert die Stringrepräsentation der Datenbanktabelle so wie sie in eine Datei geschrieben
     * wird.
     *
     * Diese Stringrepräsentation ist wie folgt aufgebaut:
     * <ul>
     * <li>In der ersten Zeile steht nur der Bezeichner der Datenbanktabelle.</li>
     * <li>In der zweiten Zeile stehen durch Kommata getrennt die Bezeichner der Spalten der
     * Datenbanktabelle.</li>
     * <li>Es folgen die Zeilen der Datenbanktabelle in der Reihenfolge, in der sie in der
     * Datenbanktabelle gespeichert sind, in einer einzigen Zeile. Die Felder der Zeilen werden
     * durch Kommata voneinander getrennt und hintereinander gereiht.</li>
     * </ul>
     *
     * Da in den Tabellenfeldern alle Zeichen erlaubt sind, werden sowohl Kommata (",") als auch
     * Backslashes ("\") bei dieser Ausgabe jeweils mit einem Backslash ("\") gequotet.
     *
     * Es werden stets Unix-Zeilenumbrüche (\n) verwendet.
     *
     * 
     */
    @Override
    public void writeTableToFile(String filename, DBTable table) throws IOException {
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        writer.println(table.toFile());
        writer.close();

    }

    /**
     * Prüft einen Testwert auf Validität anhand eines übergebenen Tests
     * 
     * @param <T> der Typ des Testobjektes
     * @param testObject Objekt, dass auf Validität geprüft werden soll
     * @param test der Test mit dem das Objekt auf Validität geprüft werden soll
     * @param message die Message, die im Falle einer WrongSyntaxException mit angegeben wird
     * @return das testObject
     * @throws WrongSyntaxException wenn der Test fehlschlägt
     */
    private static <T> T validateSyntax(T testObject, Predicate<T> test, String message)
            throws WrongSyntaxException {
        if (!test.test(testObject)) {
            throw new WrongSyntaxException(message);
        } else {
            return testObject;
        }
    }

}
