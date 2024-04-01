package fi.karelia.edu.maze;

import java.util.Arrays;

/**
 * The type Player.
 */
public class Player {
    /**
     * The name.
     */
    private String name;
    /**
     * Best scores.
     */
    private double[] bestScores;

    /**
     * Instantiates a new Player.
     *
     * @param name       the name
     * @param bestScores best scores
     */
    Player(String name, double[] bestScores) {
        this.name = name;
        this.bestScores = bestScores;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get best scores double[].
     *
     * @return the double[]
     */
    public double[] getBestScores() {
        Arrays.sort(this.bestScores);
        return bestScores;
    }

    /**
     * Add score to bestScores.
     * If the score is better than the worst score, then it is added.
     * bestScores array is sorted.
     * @param score the score
     */
    public void addScore(double score) {
        Arrays.sort(this.bestScores);
        if (score > this.bestScores[0]) {
            this.bestScores[0] = score;
            Arrays.sort(this.bestScores);
        }
    }

}