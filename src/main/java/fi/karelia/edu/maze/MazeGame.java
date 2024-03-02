package fi.karelia.edu.maze;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MazeGame extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Maze Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        SceneSizeChangeListener.letterbox(scene, root);
    }
}
