package app;

import db.DataBaseSetup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Constants;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DataBaseSetup.createDatabase();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.startFXML)));
        primaryStage.setTitle(Constants.startTitle);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(Constants.listViewCSS)).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
