module fi.karelia.edu.maze {
    requires javafx.controls;
    requires javafx.fxml;


    opens fi.karelia.edu.maze to javafx.fxml;
    exports fi.karelia.edu.maze;
}