package fi.karelia.edu.maze;

/* TODO:
    - make GameOver condition
    - correct bug with player moving out of bounds
    - speed is still an issue
    - make game fun
*/

import external.GameLoopTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * The Maze Game.
 */
public class MazeGame extends Application {
    /**
     * The size of maze.
     */
    private int size;
    /**
     * The size of square side for the cell in the maze.
     */
    private int squareSide;
    /**
     * The Circle for drawing player.
     */
    private Circle circlePlayer;
    /**
     * The label for printing player's coordinates.
     */
    private Label lbPlayerXY;
    /**
     * The label for printing player's position on a grid.
     */
    private Label lbPlayerGrid;
    /**
     * The label for printing game's timer.
     */
    private Label lbGameTimer;
    /**
     * The Maze.
     */
    private LevelMap maze;
    /**
     * The Key pressed direction.
     */
    private final HashMap<String, Integer> keyPressedDirection = new HashMap<>();
    /**
     * The score label.
     */
    private Label lbScore;
    /**
     * The copy of primaryStage.
     */
    private Stage pStage;
    /**
     * The Decimal format.
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    /**
     * The Player.
     */
    private Player player;
    /**
     * The Current score.
     */
    private double currentScore;
    /**
     * The ArrayList of Player.
     */
    private ArrayList<Player> playerList;
    /**
     * The file path to CSV-file of players and their scores.
     */
    private final String filePath = "src/main/resources/fi/karelia/edu/maze/players.csv";
    /**
     * The Image for exit.
     */
    ImageView imageExit = new ImageView(new Image("file:src/main/resources/fi/karelia/edu/maze/exit.png"));
    /**
     * The Image for keyboard keys.
     */
    ImageView imageKeys = new ImageView(new Image("file:src/main/resources/fi/karelia/edu/maze/keys.png"));
    /**
     * The Center grid.
     */
    private int centerGrid;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start of the program.
     * Copies primaryStage to pStage and calls startGame method
     *
     * @param primaryStage the primary stage
     * @see #startGame(Stage) #startGame(Stage)#startGame(Stage)
     */
    @Override
    public void start(Stage primaryStage) {
        size = 3;
        pStage = primaryStage;
        startGame(pStage);
    }

    /**
     * Called when player advances to the next level.
     *
     * @param stage          the stage
     * @param elapsedSeconds the elapsed seconds
     */
    void nextLevel(Stage stage, double elapsedSeconds) {
        currentScore += (60 - elapsedSeconds) * (size - 2);
        size += 1;
        startGame(stage);
    }

    /**
     * Called by pressing "New game" button.
     *
     * @param stage the stage
     */
    void newGame(Stage stage) {
        size = 3;
        startGame(stage);
        currentScore = 0;
    }

    /**
     * Called by pressing "Save score" button.
     * Asks player to enter their name.
     */
    void saveScoreStage() {
        Stage stage = new Stage();

        var hBoxSave = new HBox();
        var textArea = new TextField("Player1");
        if (player != null) {textArea.setText(player.getName());}
        var btnSave = new Button("Save");
        hBoxSave.getChildren().addAll(textArea, btnSave);
        Scene scene = new Scene(hBoxSave);
        stage.setTitle("Save Score");
        stage.setScene(scene);

        btnSave.setOnAction(e -> {
            saveScoresToFile(textArea);
            stage.close();
        });

        stage.show();
    }

    /**
     * Saves scores in CSV file.
     *
     * @param textArea the text area
     */
    private void saveScoresToFile(TextField textArea) {
        for (var p : playerList) {
            if (p.getName().equals(textArea.getText())) {
                player = p;
                break;
            }
        }
        if (player == null) {
            player = new Player(textArea.getText(), new double[5]);
            playerList.add(player);
        }
        player.addScore(currentScore);
        CSVMethods.CSVExport(playerList, filePath);
    }

    /**
     * Show best scores stage.
     */
    void showScoresStage() {
        var vBoxPlayers = new VBox();
        var textPlayers = new Text();
        textPlayers.setFont(new Font(20));
        vBoxPlayers.getChildren().add(textPlayers);
        var vBoxScores = new VBox();
        var textScores = new Text();
        textScores.setFont(new Font(20));
        vBoxScores.getChildren().add(textScores);
        var hBoxScores = new HBox();
        hBoxScores.getChildren().addAll(vBoxPlayers, vBoxScores);

        var scores = new ArrayList<Score>();
        for (Player p : playerList) {
            for (double score : p.getBestScores()) {
                if (score > 0) {
                    scores.add(new Score(p.getName(), score));
                }
            }
        }
        Collections.sort(scores);

        var textScoresString = "";
        var textPlayersString = "";
        for (int i = 0; i < scores.size(); i++) {
                textPlayersString += i + 1 + ". " + scores.get(i).getName() + "\n";
                textScoresString += " " + decimalFormat.format(scores.get(i).getScore()) + "\n";
        }
        textPlayers.setText(textPlayersString);
        textScores.setText(textScoresString);

        Stage stage = new Stage();
        Scene scene = new Scene(hBoxScores);
        stage.setTitle("Best scores");
        stage.setScene(scene);

        stage.show();
    }


    /**
     * Start game.
     *
     * @param stage the stage
     */
    void startGame(Stage stage) {

        playerList = CSVMethods.CSVImport(filePath);

        /* ========================================Drawing JavaFX elements======================================== */
        var root = new BorderPane();
        var pane = new Pane();
        var stackPane = new StackPane();
        pane.getChildren().add(stackPane);
        root.setCenter(pane);

        //vBoxBottom1
        lbPlayerXY = new Label();
        lbPlayerGrid = new Label();
        var vBoxBottom1 = new VBox();
        vBoxBottom1.setPadding(new Insets(15));
        vBoxBottom1.getChildren().addAll(lbPlayerXY, lbPlayerGrid);

        //vBoxBottom2
        lbGameTimer = new Label("60");
        lbGameTimer.setFont(new Font(30));
        var vBoxBottom2 = new VBox();
        vBoxBottom2.setPadding(new Insets(15));
        vBoxBottom2.getChildren().add(lbGameTimer);

        //vBoxBottom3
        var vBoxBottom3 = new VBox();
        var btnNewGame = new Button("New game");
        btnNewGame.setFocusTraversable(false);
        var btnSaveScore = new Button("Save score");
        btnSaveScore.setFocusTraversable(false);
        var btnShowScores = new Button("Show scores");
        btnShowScores.setFocusTraversable(false);
        vBoxBottom3.setPadding(new Insets(15));
        vBoxBottom3.setSpacing(5);
        vBoxBottom3.getChildren().addAll(btnNewGame, btnSaveScore, btnShowScores);

        //vBoxBottom4
        var vBoxBottom4 = new VBox();
        lbScore = new Label();
        lbScore.setFont(new Font(18));
        vBoxBottom4.setPadding(new Insets(15));
        vBoxBottom4.setSpacing(5);
        vBoxBottom4.getChildren().add(lbScore);

        //hBoxBottom
        var hBoxBottom = new HBox();
        root.setBottom(hBoxBottom);
        imageKeys.setPreserveRatio(true);
        imageKeys.setFitHeight(100);
        hBoxBottom.getChildren().addAll(vBoxBottom3, vBoxBottom1, vBoxBottom2, vBoxBottom4, imageKeys);
        hBoxBottom.setBackground(new Background(new BackgroundFill(Color.TEAL, null, null)));
        hBoxBottom.setPadding(new Insets(5));

/* ========================================Making a maze======================================== */
        maze = new LevelMap(size);
        int mazeSize = maze.getMazeMatrix().length;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        squareSide = (int) (screenBounds.getHeight() - 30) / (mazeSize + 3);

        var grid0 = new GridPane();
        grid0.setGridLinesVisible(false);
        imageExit.setFitHeight(squareSide);
        imageExit.setFitWidth(squareSide);
        // drawing maze from matrix on GridPane
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                if (maze.getMazeMatrix()[i][j] == 2) grid0.add(new Rectangle(squareSide, squareSide, Color.SLATEGRAY), j, i);
                else if (maze.getMazeMatrix()[i][j] == 5) grid0.add(imageExit, j, i);
                else grid0.add(new Rectangle(squareSide, squareSide, Color.BEIGE), j, i);
                //grid0.add(new Text(Integer.toString(maze.getMazeMatrix()[i][j])), j, i);
            }
        }

        // drawing the player circle in the center
        int circleRadius = squareSide / 3;
        centerGrid = (size % 2 != 0) ? (mazeSize / 2) : (mazeSize / 2 + 1);
        circlePlayer = new Circle(circleRadius, Color.GREEN);
        StackPane.setAlignment(circlePlayer, Pos.TOP_LEFT);
        circlePlayer.setCenterX(squareSide * centerGrid + circlePlayer.getRadius() + squareSide / 4.0);
        circlePlayer.setCenterY(squareSide * centerGrid + circlePlayer.getRadius() + squareSide / 4.0);
        lbPlayerXY.setText("X: " + circlePlayer.getCenterX() + " Y: " + circlePlayer.getCenterY());
        lbPlayerGrid.setText("Grid: " + circlePlayer.getCenterY() / squareSide
                + ", " + circlePlayer.getCenterY() / squareSide);

        stackPane.getChildren().addAll(grid0);
        pane.getChildren().add(circlePlayer);

        Scene scene = new Scene(root);
        stage.setTitle("Maze Game");
        stage.setScene(scene);
        stage.show();

/* ========================================Actions and moving======================================== */
        keyPressedDirection.put("UP", 0);
        keyPressedDirection.put("LEFT", 0);

        btnNewGame.setOnAction(e -> newGame(stage));
        btnSaveScore.setOnAction(e -> saveScoreStage());
        btnShowScores.setOnAction(e -> showScoresStage());

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> keyPressedDirection.put("UP", -1);
                case DOWN -> keyPressedDirection.put("UP", 1);
                case LEFT -> keyPressedDirection.put("LEFT", -1);
                case RIGHT -> keyPressedDirection.put("LEFT", 1);
            }
        });
        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case UP -> {
                    if (keyPressedDirection.get("UP") != 1) keyPressedDirection.put("UP", 0);
                }
                case DOWN -> {
                    if (keyPressedDirection.get("UP") != -1) keyPressedDirection.put("UP", 0);
                }
                case LEFT -> {
                    if (keyPressedDirection.get("LEFT") != 1) keyPressedDirection.put("LEFT", 0);
                }
                case RIGHT -> {
                    if (keyPressedDirection.get("LEFT") != -1) keyPressedDirection.put("LEFT", 0);
                }
            }
        });

        GameLoopTimer moving = new Moving();
        moving.start();
    }

    /**
     * The Moving type which extends external class GameLoopTimer.
     *
     * @see external.GameLoopTimer
     */
    class Moving extends GameLoopTimer {
        /**
         * The elapsed seconds.
         */
        private double elapsedSeconds;

        /**
         * Instantiates a new Moving.
         */
        Moving () {}

        /**
         * Tick method overriding method from GameLoopTimer.
         * Is used to control the speed of animation.
         *
         * @param secondsSinceLastFrame the seconds since last frame
         */
        @Override
        public void tick(float secondsSinceLastFrame) {
            if (secondsSinceLastFrame < 60) {
                elapsedSeconds += secondsSinceLastFrame;
            }
            int i = (int) ((circlePlayer.getCenterY() + (circlePlayer.getRadius())) / squareSide);
            int j = (int) ((circlePlayer.getCenterX() + (circlePlayer.getRadius())) / squareSide);
            if (!(0 <= i && i < maze.getMazeMatrix().length && 0 <= j && j < maze.getMazeMatrix().length)) {
                circlePlayer.setCenterX(squareSide * centerGrid + circlePlayer.getRadius() + squareSide / 4.0);
                circlePlayer.setCenterY(squareSide * centerGrid + circlePlayer.getRadius() + squareSide / 4.0);
            }
            if (keyPressedDirection.get("UP") != 0 || keyPressedDirection.get("LEFT") != 0) {
                double speed = 2 * secondsSinceLastFrame * squareSide;
                if (isExit()) {
                    nextLevel(pStage, elapsedSeconds);
                }
                else {
                    if (isPlayerMovableY()) {
                    circlePlayer.setCenterY(circlePlayer.getCenterY() + speed * keyPressedDirection.get("UP"));
                    }
                    if (isPlayerMovableX()) {
                        circlePlayer.setCenterX(circlePlayer.getCenterX() + speed * keyPressedDirection.get("LEFT"));
                    }
                }
            }
            displayInfo();
        }

        /**
         * Is player movable in Y-axis boolean.
         *
         * @return the boolean
         */
        private boolean isPlayerMovableY() {
            int i = (int) ((circlePlayer.getCenterY() + (circlePlayer.getRadius() + 1) * keyPressedDirection.get("UP")) / squareSide);
            int j = (int) ((circlePlayer.getCenterX() + (circlePlayer.getRadius())) / squareSide);
            return 0 <= i && i < maze.getMazeMatrix().length &&
                   0 <= j && j < maze.getMazeMatrix().length &&
                   maze.getMazeMatrix()[i][j] != 2;
        }

        /**
         * Is player movable in X-axis boolean.
         *
         * @return the boolean
         */
        private boolean isPlayerMovableX() {
            int i = (int) ((circlePlayer.getCenterY() + (circlePlayer.getRadius())) / squareSide);
            int j = (int) ((circlePlayer.getCenterX() + (circlePlayer.getRadius() + 1) * keyPressedDirection.get("LEFT")) / squareSide);
            return 0 <= i && i < maze.getMazeMatrix().length &&
                   0 <= j && j < maze.getMazeMatrix().length &&
                   (maze.getMazeMatrix()[i][j] == 1 || maze.getMazeMatrix()[i][j] == 4);
        }

        /**
         * Has player reached exit cell boolean.
         *
         * @return the boolean
         */
        private boolean isExit() {
            int i = (int) ((circlePlayer.getCenterY() + (circlePlayer.getRadius())) / squareSide);
            int j = (int) ((circlePlayer.getCenterX() + (circlePlayer.getRadius())) / squareSide);
            return maze.getMazeMatrix()[i][j] == 5;
        }

        /**
         * Display info.
         */
        private void displayInfo() {
            lbPlayerXY.setText("X: " + decimalFormat.format(circlePlayer.getCenterX()) +
                    "\nY: " + decimalFormat.format(circlePlayer.getCenterY()));
            lbPlayerGrid.setText("Grid: " + (int) (circlePlayer.getCenterY() / squareSide)
                    + ", " + (int) (circlePlayer.getCenterX() / squareSide));
            lbGameTimer.setText("" + (int) (60 - elapsedSeconds));
            lbScore.setText("Score: " + decimalFormat.format(currentScore));
        }
    }
}
