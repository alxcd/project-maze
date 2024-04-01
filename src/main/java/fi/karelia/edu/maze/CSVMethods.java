package fi.karelia.edu.maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Methods to import/export from/to CSV file of Players.
 */
public abstract class CSVMethods {
    /**
     * Import from CSV-file to ArrayList of Player class.
     *
     * @param filePath the file path
     * @return the ArrayList of Player
     */
    public static ArrayList<Player> CSVImport(String filePath) {
        var csvFile = new File(filePath);
        var arrayList = new ArrayList<Player>();
        try (Scanner input = new Scanner(csvFile)) {
            while (input.hasNext()) {
                String[] line = input.nextLine().split(",", 2);
                var playerName = line[0].replaceAll("\"","");
                var bestScores = Arrays.stream(line[1].replaceAll("\"","").split(",")).mapToDouble(Double::parseDouble).toArray();
                var player = new Player(playerName, bestScores);
                arrayList.add(player);
            }
        }
        catch (FileNotFoundException e) {
            return arrayList;
        }
        return arrayList;
    }

    /**
     * Export from ArrayList of Player class to CSV-file.
     *
     * @param playerList the ArrayList of Player
     * @param filePath   the file path
     */
    public static void CSVExport(ArrayList<Player> playerList, String filePath) {
        var csvFile = new File(filePath);
        try (PrintWriter output = new PrintWriter(csvFile)) {
            for (var player : playerList) {
                output.print(player.getName());
                for (var score : player.getBestScores()) {
                    output.print("," + score);
                }
                output.print("\n");
            }
        }
        catch (FileNotFoundException e) {}
    }
}
