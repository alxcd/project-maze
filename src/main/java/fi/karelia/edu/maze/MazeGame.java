package fi.karelia.edu.maze;

/* TODO:
    - player class with position
    - *check position with gridpane
    - think about using AnchorPane to resize GridPane
    - game idea with changing colors
    - *make an exit and next level
    - rewrite Application class for level restart
*/
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Timer;

/**
 * The type Maze game.
 */
public class MazeGame extends Application {
    private int mazeSize;
    private int squareSide;
    private Circle playerCircle;
    private Label playerXY;
    private Label playerGrid;
    private LevelMap maze;
    private HashMap<String, Integer> keyPressedDirection = new HashMap<>();
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * <BorderPane root.Center>
     * <StackPane pane>
     * <GridPane grid0></GridPane>
     * <GridPane grid1></GridPane>
     * </StackPane>
     * </BorderPane>
     *
     * @param primaryStage the primary stage
     */
    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        Pane pane = new Pane();
        StackPane stackPane = new StackPane();
        pane.getChildren().add(stackPane);

//        stackPane.prefHeightProperty().bind(root.heightProperty());
//        stackPane.prefWidthProperty().bind(root.widthProperty());


        root.setCenter(pane);

        VBox vBoxBottom = new VBox();
        vBoxBottom.setPadding(new Insets(15));

        root.setBottom(vBoxBottom);


        int size = 5;
        maze = new LevelMap(size);
        mazeSize = maze.getMazeMatrix().length;

        GridPane grid0 = new GridPane();
        grid0.setGridLinesVisible(false);
        GridPane grid1 = new GridPane();
        squareSide = 800 / mazeSize;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                if (maze.getMazeMatrix()[i][j] == 2) grid0.add(new Rectangle(squareSide,squareSide, Color.BLACK), j, i);
                else grid0.add(new Rectangle(squareSide,squareSide, Color.TRANSPARENT), j, i);
                //grid0.add(new Text(Integer.toString(maze.getMazeMatrix()[i][j])), j, i);
            }
        }
        long endTime = System.currentTimeMillis();

        Text textBottom = new Text("maze created in: " + (endTime - startTime));
        playerXY = new Label();
        playerGrid = new Label();
        vBoxBottom.getChildren().addAll(textBottom, playerXY, playerGrid);


        int circleRadius = 200 / mazeSize;
        int centerGrid = mazeSize / 2;
        playerCircle = new Circle(circleRadius, Color.GREENYELLOW);
        StackPane.setAlignment(playerCircle, Pos.TOP_LEFT);
        playerCircle.setCenterX(squareSide * centerGrid + playerCircle.getRadius() + squareSide / 4.0);
        playerCircle.setCenterY(squareSide * centerGrid + playerCircle.getRadius() + squareSide / 4.0);
        playerXY.setText("X: " + playerCircle.getCenterX() + " Y: " + playerCircle.getCenterY());
        playerGrid.setText("Grid: " + playerCircle.getCenterY() / squareSide
                + ", " + playerCircle.getCenterY() / squareSide);

        stackPane.getChildren().addAll(grid0, grid1);
        pane.getChildren().add(playerCircle);



        Scene scene = new Scene(root);
        primaryStage.setTitle("Maze Game");
        primaryStage.setScene(scene);
        primaryStage.show();


        keyPressedDirection.put("UP", 0);
        keyPressedDirection.put("LEFT", 0);

//        playerMoveHandler keyPressed = new playerMoveHandler();
//        scene.setOnKeyPressed(keyPressed);
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

        AnimationTimer moving = new Moving();
        moving.start();

        //SceneSizeChangeListener.letterbox(scene, stackPane);
    }


    class Moving extends AnimationTimer {
        @Override
        public void handle(long now) {
            if ((keyPressedDirection.get("UP") != 0 || keyPressedDirection.get("LEFT") != 0)) {
                double speed = 10.0 / mazeSize;
                if (isPlayerMovableY())
                    playerCircle.setCenterY(playerCircle.getCenterY() + speed * keyPressedDirection.get("UP"));
                if (isPlayerMovableX())
                    playerCircle.setCenterX(playerCircle.getCenterX() + speed * keyPressedDirection.get("LEFT"));
            }
        }

        private boolean isPlayerMovableY() {
            int i = (int) ((playerCircle.getCenterY() + (playerCircle.getRadius() + 1) * keyPressedDirection.get("UP")) / squareSide);
            int j = (int) ((playerCircle.getCenterX() + (playerCircle.getRadius())) / squareSide);
            if (maze.getMazeMatrix()[i][j] == 2) return false;
            return true;
        }
        private boolean isPlayerMovableX() {
            int i = (int) ((playerCircle.getCenterY() + (playerCircle.getRadius())) / squareSide);
            int j = (int) ((playerCircle.getCenterX() + (playerCircle.getRadius() + 1) * keyPressedDirection.get("LEFT")) / squareSide);
            if (maze.getMazeMatrix()[i][j] == 2) return false;
            return true;
        }
    }
    /**
     * The type Player move handler.
     */
//    class playerMoveHandler implements EventHandler<KeyEvent> {
//        private long step = 200 / mazeSize;
//
//        /**
//         * Handle.
//         *
//         * @param e the e
//         */
//        @Override
//        public void handle(KeyEvent e) {
//            switch (e.getCode()) {
//                case UP:
//                    playerCircle.setCenterY(playerCircle.getCenterY() - (isPlayerMovable(-1, 0, step)));
//                    break;
//                case DOWN:
//                    playerCircle.setCenterY(playerCircle.getCenterY() + (isPlayerMovable(1, 0, step)));
//                    break;
//                case LEFT:
//                    playerCircle.setCenterX(playerCircle.getCenterX() - (isPlayerMovable(0, -1, step)));
//                    break;
//                case RIGHT:
//                    playerCircle.setCenterX(playerCircle.getCenterX() + (isPlayerMovable(0, 1, step)));
//                    break;
//            }
//            playerXY.setText("X: " + playerCircle.getCenterX() + " Y: " + playerCircle.getCenterY());
//            playerGrid.setText("Grid: " + (int) (playerCircle.getCenterY() / squareSide)
//                    + ", " + (int) (playerCircle.getCenterX() / squareSide));
//        }
//
//        public long isPlayerMovable(int up, int left, long step) {
//            int i = (int) ((playerCircle.getCenterY() + (playerCircle.getRadius() + step) * up) / squareSide);
//            int j = (int) ((playerCircle.getCenterX() + (playerCircle.getRadius() + step) * left) / squareSide);
//            if (maze.getMazeMatrix()[i][j] == 2) step = isPlayerMovable(up, left, step - 1);
//            return step;
//        }
//    }
}
