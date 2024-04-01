module fi.karelia.edu.maze {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens fi.karelia.edu.maze to javafx.fxml;
    exports fi.karelia.edu.maze;
    exports external;
    opens external to javafx.fxml;
}