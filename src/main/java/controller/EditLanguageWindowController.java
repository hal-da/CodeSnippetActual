package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Language;
import util.Constants;

import java.io.IOException;

public class EditLanguageWindowController {

    private Stage stage;


    void showLanguageEditWindow(Language selectedLanguage){

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(Constants.editLanguageFXML));
        try {
            Parent parent = fxmlLoader.load();
            EditLanguageController controllerEditLanguage = fxmlLoader.getController();
            controllerEditLanguage.setSelectedLanguage(selectedLanguage);
            Scene scene = new Scene(parent);
            stage = new Stage();
            stage.setTitle(Constants.editLanguagesTitle);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }
}
