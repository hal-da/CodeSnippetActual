package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Language;

import java.io.IOException;

public class EditLanguageWindow {


    void showLanguageEditWindow(Language selectedLanguage){

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/editLanguage.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            ControllerEditLanguage controllerEditLanguage = fxmlLoader.getController();
            controllerEditLanguage.setSelectedLanguage(selectedLanguage);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Edit Languages");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
