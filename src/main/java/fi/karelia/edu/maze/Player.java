package fi.karelia.edu.maze;

import java.util.Arrays;

public class Player {
    private String name;
    private double[] bestScores;

    Player(String name, double[] bestScores) {
        this.name = name;
        this.bestScores = bestScores;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getBestScores() {
        Arrays.sort(this.bestScores);
        return bestScores;
    }

    public void addScore(double score) {
        Arrays.sort(this.bestScores);
        if (score > this.bestScores[0]) {this.bestScores[0] = score;}
    }

}