package fi.karelia.edu.maze;

/**
 * The type Score.
 * Used for sorting "Player name - Score" pair.
 * Implements Comparable for sorting.
 * @see java.lang.Comparable
 */
public class Score implements Comparable<Score> {
    /**
     * The name.
     */
    private final String name;
    /**
     * The score.
     */
    private final Double score;

    /**
     * Instantiates a new Score.
     *
     * @param name  the name
     * @param score the score
     */
    Score (String name, Double score) {
        this.name = name;
        this.score = score;
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
     * Gets score.
     *
     * @return the score
     */
    public Double getScore() {
        return score;
    }

    /**
     * Compare to int. Implemented so class could sort entries based on score,
     * which would sort names as well.
     *
     * @param score the score
     * @return the int
     */
    @Override
    public int compareTo(Score score) {
        Double compareScore = score.getScore();
        return (int) (compareScore - this.score);
    }
}
