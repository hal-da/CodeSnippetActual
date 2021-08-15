package controller;

import java.sql.SQLException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Language;
import repository.LanguageRepositoryJDBC;

public class ControllerEditLanguage {

    @FXML
    private ChoiceBox<Language> choiceBoxLanguages;

    @FXML
    private TextField textFieldLanguage;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    private final LanguageRepositoryJDBC languageRepositoryJDBC = new LanguageRepositoryJDBC();
    private final ObservableList<Language> languages = FXCollections.observableArrayList(languageRepositoryJDBC.readAll());

    public ControllerEditLanguage() throws SQLException {


    }

    @FXML
    void handleBtnCancel(ActionEvent event) {
        closeStage();
    }



    @FXML
    void handleBtnDelete(ActionEvent event) throws SQLException {
        int choiceBoxSelectedIndex = choiceBoxLanguages.getSelectionModel().getSelectedIndex();
        if(choiceBoxSelectedIndex != 0){

            //SHow Dialog - only delete if button pressed OK
            //TODO: on delete CASCADE

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setContentText("Are you really really sure?");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                int languageId = choiceBoxLanguages.getSelectionModel().getSelectedItem().getId();
                String languageName = choiceBoxLanguages.getSelectionModel().getSelectedItem().getName();
                languageRepositoryJDBC.delete(new Language(languageId, languageName));
                closeStage();
            }
        }
    }

    @FXML
    void handleBtnSave(ActionEvent event) throws SQLException {
        int choiceBoxSelectedIndex = choiceBoxLanguages.getSelectionModel().getSelectedIndex();
        if(choiceBoxSelectedIndex == 0){
            languageRepositoryJDBC.insert(textFieldLanguage.getText());
        } else {
            int languageId = choiceBoxLanguages.getSelectionModel().getSelectedItem().getId();
            String languageName = textFieldLanguage.getText();
            languageRepositoryJDBC.update(new Language(languageId,languageName));
        }


        System.out.println(choiceBoxLanguages.getSelectionModel().getSelectedIndex());
        System.out.println(textFieldLanguage.getText());
        System.out.println(choiceBoxLanguages.getSelectionModel().getSelectedItem().getId());

        closeStage();
    }

    private void closeStage(){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() throws SQLException {
        assert choiceBoxLanguages != null : "fx:id=\"choiceBoxLanguages\" was not injected: check your FXML file 'editLanguage.fxml'.";
        assert textFieldLanguage != null : "fx:id=\"textFieldLanguage\" was not injected: check your FXML file 'editLanguage.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'editLanguage.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'editLanguage.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'editLanguage.fxml'.";

        fillChoiceBox();

        choiceBoxLanguages.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldLanguage, newLanguage) -> textFieldLanguage.setText(newLanguage.getName()));

    }

    private void fillChoiceBox() throws SQLException {
        FXCollections.observableArrayList(languageRepositoryJDBC.readAll());
        choiceBoxLanguages.setItems(languages);
        choiceBoxLanguages.getItems().add(0,new Language("New Language"));
        choiceBoxLanguages.getSelectionModel().select(0);
    }
}
