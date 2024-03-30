package fi.karelia.edu.maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CSVMethods {
    public static ArrayList<Player> CSVImport(String file) {
        var csvFile = new File(file);
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
    public static void CSVExport(String file, ArrayList<Player> playerList) {
        var csvFile = new File(file);
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
