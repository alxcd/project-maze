package fi.karelia.edu.maze;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
        StackPane stackPane = new StackPane();
        Pane pane = new Pane();
        pane.getChildren().add(stackPane);

//        AnchorPane root = new AnchorPane();
//        AnchorPane.setTopAnchor(pane, 10.0);
//        AnchorPane.setRightAnchor(pane, 10.0);
//        AnchorPane.setLeftAnchor(pane, 10.0);


        root.setCenter(pane);
        root.setPrefSize(500, 500);

        VBox vBoxLeft = new VBox();
        vBoxLeft.setPadding(new Insets(15));
//        AnchorPane.setLeftAnchor(vBoxLeft, 10.0);
//        AnchorPane.setBottomAnchor(vBoxLeft, 10.0);

        //root.getChildren().addAll(pane, vBoxLeft);

        root.setBottom(vBoxLeft);

        LevelMap maze = new LevelMap(10);

        GridPane grid0 = new GridPane();
        for (int i = 0; i < maze.getMazeMatrix().length; i++) {
            for (int j = 0; j < maze.getMazeMatrix()[i].length; j++) {
                if (maze.getMazeMatrix()[i][j] == 2) grid0.add(new Rectangle(20,20, Color.BLACK), j, i);
                else grid0.add(new Rectangle(20,20, Color.TRANSPARENT), j, i);
            }
        }

        GridPane grid1 = new GridPane();
        //grid1.add(new Circle(5, Color.YELLOW), 1, 1);
        //grid1.add(new Rectangle(10,10, Color.YELLOW), 11, 11);

        Circle playerCircle = new Circle(5, Color.YELLOW);
        StackPane.setAlignment(playerCircle, Pos.TOP_LEFT);
        playerCircle.setCenterX(50);
        playerCircle.setCenterY(50);

        stackPane.getChildren().addAll(grid0, grid1);
        pane.getChildren().add(playerCircle);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Maze Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DOWN: playerCircle.setCenterY(playerCircle.getCenterY() + 10); break;
                case UP: playerCircle.setCenterY(playerCircle.getCenterY() - 10); break;
                case LEFT: playerCircle.setCenterX(playerCircle.getCenterX() - 10); break;
                case RIGHT: playerCircle.setCenterX(playerCircle.getCenterX() + 10); break;
            }
        });


        SceneSizeChangeListener.letterbox(scene, root);
    }
}
