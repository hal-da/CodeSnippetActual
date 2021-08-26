package app;

import db.DataBaseSetup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DataBaseSetup.createDatabase();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/start.fxml")));
        primaryStage.setTitle("CodeSnippet SingleUser");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/listView.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
