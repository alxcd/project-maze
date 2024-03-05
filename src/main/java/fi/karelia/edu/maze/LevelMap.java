package fi.karelia.edu.maze;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Collections.shuffle;

/**
 * The type Level map.
 */
public class LevelMap {
    /**
     * The Size.
     */
    final private int size;
    /**
     * The Maze matrix.
     */
    private final int[][] mazeMatrix;
    private final ArrayList<Character> directions = new ArrayList<>(Arrays.asList('N', 'S', 'W', 'E'));

    /**
     * Instantiates a new Level map.
     * Creates a specific sized mazeMatrix.
     * Each matrix cell corresponds either to a wall or a maze position/cell
     * 0 - unvisited cell (at first all of them are)
     * 1 - visited cell
     * 2 - wall
     * 4 - no wall
     * In this constructor mazeMatrix is populated with walls and unvisited cells everywhere.
     * @param size the size
     */
    public LevelMap(int size) {
        this.size = size;
        this.mazeMatrix = new int[this.size * 2 + 1][this.size * 2 + 1];
        for (int i = 0; i < mazeMatrix.length; i++) {
            for (int j = 0; j < mazeMatrix[i].length; j++) {
                mazeMatrix[i][j] = (i % 2 == 0 || j % 2 == 0) ? 2 : 0;
            }
        }
        int range = this.size - 1;
        int randX = (int)(Math.random() * range - 1) * 2 + 1;
        int randY = (int)(Math.random() * range - 1) * 2 + 1;
        System.out.println(randY + " " + randX);
        carvePassageInMaze(randX,randY);
    }

    /**
     * Carve passage in maze using recursive backtracking method with 2 int attributes as
     * starting position for carving. Both should be odd - method doesn't check if they are.
     * Algorithm goes like this:
     * 1. Choose starting position(we do this beforehand)
     * 2. Randomly determine where to go(North/Up, South/Down, West/Left, East/Right)
     *    It shouldn't be another wall or visited cell. So we always carve new cells.
     * 3. If we have nowhere to go, then we go back one cell.
     * 4. Once we have nowhere to go, and we're back to the starting position, the maze is done.
     * Recursion part starts at 2nd step by calling 1st step again, but with new position of a cell we carved.
     *
     * @param cx the cx
     * @param cy the cy
     *
     */
    private void carvePassageInMaze(int cx, int cy) {
        int nx = cx; // cx, cy - current coordinates;
        int ny = cy; // nx, ny = new coordinates for recursion call
        mazeMatrix[cy][cx] = 1;
        ArrayList<Character> tempDirections = new ArrayList<>(directions); // shuffle in what direction try going first
        shuffle(tempDirections);
        for (char direction : tempDirections) { // 2nd step. Checking where we can go
            if (direction == 'N') {
                if (cy - 2 >= 0 && mazeMatrix[cy - 2][cx] == 0) {
                    mazeMatrix[cy - 1][cx] = 4;
                    mazeMatrix[cy - 2][cx] = 1;
                    nx = cx;
                    ny = cy - 2;
                    carvePassageInMaze(nx, ny);
                }
            } else if (direction == 'S') {
                if (cy + 2 < mazeMatrix.length && mazeMatrix[cy + 2][cx] == 0) {
                    mazeMatrix[cy + 1][cx] = 4;
                    mazeMatrix[cy + 2][cx] = 1;
                    nx = cx;
                    ny = cy + 2;
                    carvePassageInMaze(nx, ny);
                }
            } else if (direction == 'W') {
                if (cx - 2 >= 0 && mazeMatrix[cy][cx - 2] == 0) {
                    mazeMatrix[cy][cx - 1] = 4;
                    mazeMatrix[cy][cx - 2] = 1;
                    nx = cx - 2;
                    ny = cy;
                    carvePassageInMaze(nx, ny);
                }
            } else if (direction == 'E') {
                if (cx + 2 < mazeMatrix.length && mazeMatrix[cy][cx + 2] == 0) {
                    mazeMatrix[cy][cx + 1] = 4;
                    mazeMatrix[cy][cx + 2] = 1;
                    nx = cx + 2;
                    ny = cy;
                    carvePassageInMaze(nx, ny);
                }
            }
        }
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Get maze matrix int [ ] [ ].
     *
     * @return the int [ ] [ ]
     */
    public int[][] getMazeMatrix() {
        return mazeMatrix;
    }
}
