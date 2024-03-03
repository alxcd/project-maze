package fi.karelia.edu.maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private int[][] mazeMatrix;
    private ArrayList<Character> directions = new ArrayList<>(Arrays.asList('N', 'S', 'W', 'E'));

    /**
     * Instantiates a new Level map.
     * Creates a specific sized mazeMatrix filled with 0s.
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
        carvePassageInMaze(1,1);
    }

//    void maze () {
//        DX.put('E', 1);
//        DX.put('W', -1);
//        DX.put('N', 0);
//        DX.put('S', 0);
//        DY.put('E', 0);
//        DY.put('W', 0);
//        DY.put('N', -1);
//        DY.put('S', 1);
//        OPPOSITE.put('E', 'W');
//        OPPOSITE.put('W', 'E');
//        OPPOSITE.put('N', 'S');
//        OPPOSITE.put('S', 'N');
//    }
    // cx, cy - current; nx, ny = new
    void carvePassageInMaze(int cx, int cy) {
        shuffle(directions);
        int nx = cx;
        int ny = cy;
        for (char direction : directions) {
            switch (direction) {
                case 'N':
                    if (cy - 2 >= 0 && mazeMatrix[cy-2][cx] == 0) {
                        mazeMatrix[cy-1][cx] = 4;
                        mazeMatrix[cy-2][cx] = 1;
                        nx = cx;
                        ny = cy - 2;
                        carvePassageInMaze(nx, ny);
                    }
                    break;
                case 'S':
                    if (cy + 2 < mazeMatrix.length && mazeMatrix[cy+2][cx] == 0) {
                        mazeMatrix[cy+1][cx] = 4;
                        mazeMatrix[cy+2][cx] = 1;
                        nx = cx;
                        ny = cy + 2;
                        carvePassageInMaze(nx, ny);
                    }
                    break;
                case 'W':
                    if (cx - 2 >= 0 && mazeMatrix[cy][cx-2] == 0) {
                        mazeMatrix[cy][cx-1] = 4;
                        mazeMatrix[cy][cx-2] = 1;
                        nx = cx - 2;
                        ny = cy;
                        carvePassageInMaze(nx, ny);
                    }
                    break;
                case 'E':
                    if (cx + 2 < mazeMatrix.length && mazeMatrix[cy][cx+2] == 0) {
                        mazeMatrix[cy][cx+1] = 4;
                        mazeMatrix[cy][cx+2] = 1;
                        nx = cx + 2;
                        ny = cy;
                        carvePassageInMaze(nx, ny);
                    }
                    break;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int[][] getMazeMatrix() {
        return mazeMatrix;
    }
}
