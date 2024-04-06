package fi.karelia.edu.maze;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Collections.shuffle;

/**
 * The type LevelMap.
 */
public class LevelMap {
    /**
     * The size of the maze.
     */
    final private int size;
    /**
     * The maze matrix.
     */
    private final int[][] mazeMatrix;
    /**
     * Directions for carving the passage.
     */
    private final ArrayList<Character> directions = new ArrayList<>(Arrays.asList('N', 'S', 'W', 'E'));

    /**
     * Instantiates a new LevelMap.
     * Creates a specific sized maze matrix.
     * Each matrix cell corresponds either to a wall or a maze position/cell<p>
     * 0 - unvisited cell (at first all of them are)<p>
     * 1 - visited cell<p>
     * 2 - wall<p>
     * 4 - no wall<p>
     * 5 - exit<p>
     * In this constructor maze matrix is populated with walls and unvisited cells everywhere.
     *
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
        carvePassageInMaze(randX,randY);

        //making an exit
        int iExit, jExit;
        iExit = Math.random() > 0.5 ? 0 : mazeMatrix.length - 1;
        jExit = Math.random() > 0.5 ? 1 : mazeMatrix.length - 2;
        mazeMatrix[iExit][jExit] = 5;
    }

    /**
     * Carve passage in maze using recursive backtracking method with 2 int attributes as
     * starting position for carving. Both should be odd - method doesn't check if they are.
     * Algorithm goes like this:<p>
     * 1. Choose starting position(we do this beforehand)<p>
     * 2. Randomly determine where to go(North/Up, South/Down, West/Left, East/Right)
     * It shouldn't be another wall or visited cell. So we always carve new cells.<p>
     * 3. If we have nowhere to go, then we go back one cell.<p>
     * 4. Once we have nowhere to go, and we're back to the starting position, the maze is done.<p>
     * Recursion part starts at 2nd step by calling 1st step again, but with new position of a cell we carved.<p>
     * For algorithm's description @see <a href="https://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking"></a>
     *
     * @param cx current X coordinate in a maze
     * @param cy current Y coordinate in a maze
     */
    private void carvePassageInMaze(int cx, int cy) {
        int nx = cx; // cx, cy - current coordinates;
        int ny = cy; // nx, ny = new coordinates for recursion call
        mazeMatrix[cy][cx] = 1;
        ArrayList<Character> tempDirections = new ArrayList<>(directions); // shuffle in what direction try going first
        shuffle(tempDirections);
        for (char direction : tempDirections) { // 2nd step. Checking in what direction we can go
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
     * @return the size of maze
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the maze array.
     *
     * @return the int[][]
     */
    public int[][] getMazeMatrix() {
        return mazeMatrix;
    }
}
