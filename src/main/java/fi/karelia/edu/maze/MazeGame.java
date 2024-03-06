package fi.karelia.edu.maze;

/* TODO:
    - player class with position
    - add scale for size of rectangle in gridPane
    - *check position with gridpane
    - think about using AnchorPane to resize GridPane
    - game idea with changing colors
    - *make multi keys pressed events
    - *make an exit and next level
*/
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

/**
 * The type Maze game.
 */
public class MazeGame extends Application {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    int mazeSize;
    int squareSide;
    Circle playerCircle;
    Label playerXY;
    Label playerGrid;
    LevelMap maze;
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * <BorderPane root.Center>
     *     <StackPane pane>
     *         <GridPane grid0></GridPane>
     *         <GridPane grid1></GridPane>
     *      </StackPane>
     * </BorderPane>
     * @param primaryStage
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
        grid0.setGridLinesVisible(true);
        GridPane grid1 = new GridPane();
        squareSide = 800 / mazeSize;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                if (maze.getMazeMatrix()[i][j] == 2) grid0.add(new Rectangle(squareSide,squareSide, Color.BLACK), j, i);
                else grid0.add(new Rectangle(squareSide,squareSide, Color.TRANSPARENT), j, i);
                grid0.add(new Text(Integer.toString(maze.getMazeMatrix()[i][j])), j, i);
            }
        }
        long endTime = System.currentTimeMillis();

        Text textBottom = new Text("maze created in: " + (endTime - startTime));
        playerXY = new Label();
        playerGrid = new Label();
        vBoxBottom.getChildren().addAll(textBottom, playerXY, playerGrid);


        int circleRadius = 200 / mazeSize;
        playerCircle = new Circle(circleRadius, Color.GREENYELLOW);
        StackPane.setAlignment(playerCircle, Pos.TOP_LEFT);
        playerCircle.setCenterX(squareSide + 1);
        playerCircle.setCenterY(squareSide + 1);
        playerXY.setText("X: " + playerCircle.getCenterX() + " Y: " + playerCircle.getCenterY());
        playerGrid.setText("Grid: " + playerCircle.getCenterY() / squareSide
                + ", " + playerCircle.getCenterY() / squareSide);

        stackPane.getChildren().addAll(grid0, grid1);
        pane.getChildren().add(playerCircle);



        Scene scene = new Scene(root);
        primaryStage.setTitle("Maze Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(new playerMoveHandler());


        //SceneSizeChangeListener.letterbox(scene, stackPane);
    }
    class playerMoveHandler implements EventHandler<KeyEvent> {
        long step = 200 / mazeSize;
        @Override
        public void handle(KeyEvent e) {
            switch (e.getCode()) {
                case UP: playerCircle.setCenterY(playerCircle.getCenterY() - (isPlayerMovable(-1, 0, step))); break;
                case DOWN: playerCircle.setCenterY(playerCircle.getCenterY() + (isPlayerMovable(1, 0, step))); break;
                case LEFT: playerCircle.setCenterX(playerCircle.getCenterX() - (isPlayerMovable(0, -1, step))); break;
                case RIGHT: playerCircle.setCenterX(playerCircle.getCenterX() + (isPlayerMovable(0, 1, step))); break;
            }
            playerXY.setText("X: " + playerCircle.getCenterX() + " Y: " + playerCircle.getCenterY());
            playerGrid.setText("Grid: " + (int)(playerCircle.getCenterY() / squareSide)
                    + ", " + (int)(playerCircle.getCenterX() / squareSide));
        }

        long isPlayerMovable(int up, int left, long step) {
            int i = (int) ((playerCircle.getCenterY() + (playerCircle.getRadius() + step) * up) / squareSide);
            int j = (int) ((playerCircle.getCenterX() + (playerCircle.getRadius() + step) * left) / squareSide);
            if (maze.getMazeMatrix()[i][j] == 2) step = isPlayerMovable(up, left, step - 1);
            return step;
        }
    }
}
