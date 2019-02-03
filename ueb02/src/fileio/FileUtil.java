package fileio;

import java.io.IOException;

import db.DBTable;

/**
 * Bietet Methoden für das Lesen und Schreiben von {@link DBTable}s aus bzw. in Dateien.
 *
 * @author kar, mhe
 *
 */
public interface FileUtil {

    /**
     * Liest eine Text-Datei und interpretiert diese als {@link DBTable}. Dabei wird das Quoting
     * innerhalb der einzelnen Felder wieder rückgängig gemacht, so dass sich die Inhalte der
     * Tabelle wieder im Originalzustand befinden. (siehe
     * {@link FileUtil#writeTableToFile(String,DBTable)}).
     *
     * Syntaktische Fehler in der Datei werden dabei über eine WrongSyntaxException signalisiert. Zu
     * den syntaktischen Fehlern zählen hierbei auch nicht valide Bezeichner und die Verletzung der
     * Konsistenzbedingungen für Bezeichner der Datenbank (also verletzte Vorbedingungen).
     *
     * @param filename Dateiname
     *
     * @return Tabelle als DBTable
     * @throws IOException Fehler beim Einlesen der Datei
     * @throws WrongSyntaxException Fehler in der Syntax der Eingabedatei
     */
    DBTable readTableFromFile(String filename) throws IOException, WrongSyntaxException;

    /**
     * Schreibt die {@link DBTable} in die Datei namens filename. Die Syntax und Semantik der
     * Ausgabe sind identisch zu {@link DBTable#toFile()}.
     *
     * @param filename Dateiname
     * @param table Tabelle, welche geschrieben werden soll
     *
     * @throws IOException Fehler beim Schreiben der Datei
     */
    void writeTableToFile(String filename, DBTable table) throws IOException;

}
