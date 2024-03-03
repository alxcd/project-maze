package fi.karelia.edu.maze;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MazeGame extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        VBox vBoxLeft = new VBox(new Button("New game"));
        root.setLeft(vBoxLeft);

        LevelMap maze = new LevelMap(10);

        GridPane grid = new GridPane();
        root.setCenter(grid);

        for (int i = 0; i < maze.getMazeMatrix().length; i++) {
            for (int j = 0; j < maze.getMazeMatrix()[i].length; j++) {
                if (maze.getMazeMatrix()[i][j] == 2) grid.add(new Rectangle(20,20, Color.BLACK), j, i);
                else grid.add(new Rectangle(20,20, Color.TRANSPARENT), j, i);
            }
        }

        Scene scene = new Scene(root);
        primaryStage.setTitle("Maze Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        //SceneSizeChangeListener.letterbox(scene, root);
    }
}
