package fi.karelia.edu.maze;

public class Score implements Comparable<Score> {
    private String name;
    private Double score;

    Score (String name, Double score) {
        this.name = name;
        this.score = score;
    }
    public String getName() {
        return name;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public int compareTo(Score score) {
        Double compareScore = score.getScore();
        return (int) (compareScore - this.score);
    }
}
