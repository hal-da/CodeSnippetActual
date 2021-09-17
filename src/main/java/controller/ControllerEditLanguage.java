package controller;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Language;
import repository.LanguageRepositoryJDBC;

public class ControllerEditLanguage {



    private Language selectedLanguage;

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

    @FXML
    private TextArea textAreaKeyWords;

    private final LanguageRepositoryJDBC languageRepositoryJDBC = new LanguageRepositoryJDBC();
    private ObservableList<Language> languages;



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
            languageRepositoryJDBC.insert(new Language(textFieldLanguage.getText(), textAreaKeyWords.getText()));
        } else {
            int languageId = choiceBoxLanguages.getSelectionModel().getSelectedItem().getId();
            String languageName = textFieldLanguage.getText();
            String[] keyWords = textAreaKeyWords.getText().split(",");
            keyWords = Arrays.stream(keyWords).map(String::trim).toArray(String[]::new);
            languageRepositoryJDBC.update(new Language(languageId,languageName, keyWords));
        }



        closeStage();
    }

    private void closeStage(){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        Parent parent = btnCancel.getParent();
        //parent.
        stage.close();
    }

    @FXML
    void initialize() throws SQLException {
        assert choiceBoxLanguages != null : "fx:id=\"choiceBoxLanguages\" was not injected: check your FXML file 'editLanguage.fxml'.";
        assert textFieldLanguage != null : "fx:id=\"textFieldLanguage\" was not injected: check your FXML file 'editLanguage.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'editLanguage.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'editLanguage.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'editLanguage.fxml'.";
        assert textAreaKeyWords != null : "fx:id=\"textAreaKeyWords\" was not injected: check your FXML file 'editLanguage.fxml'.";

        fillChoiceBox();
        disableBtnSaveAtStart();
        disableDeleteBtn();

        choiceBoxLanguages.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldLanguage, newLanguage) -> {
                    textFieldLanguage.setText(newLanguage.getName());
                    textAreaKeyWords.setText(String.join(", ",newLanguage.getKeyWords()));
                });
    }

    private void disableBtnSaveAtStart(){
        btnSave.setDisable(true);
        textFieldLanguage.textProperty().addListener( (observable, o, n) -> {
            btnSave.setDisable(false);
            if(n.equals("new language")) btnSave.setDisable(true);
        });
    }

    private void disableDeleteBtn(){

        btnDelete.setDisable(true);
        choiceBoxLanguages.getSelectionModel().selectedItemProperty().addListener(o -> {
            btnDelete.setDisable(false);
            if(choiceBoxLanguages.getSelectionModel().getSelectedItem().getId() == 0)btnDelete.setDisable(true);
        });
    }

    private void fillChoiceBox() throws SQLException {
        languages = FXCollections.observableArrayList(languageRepositoryJDBC.readAll());
        Language newLanguage = new Language("new language");
        languages.add(0, newLanguage);
        choiceBoxLanguages.setItems(languages);
        setFormToSelectedLang(newLanguage);
    }

    private void setFormToSelectedLang(Language language){
        choiceBoxLanguages.getSelectionModel().select(language);
        textFieldLanguage.setText(language.getName());
        textAreaKeyWords.setText(String.join(", ",language.getKeyWords()));
    }

    public void setSelectedLanguage(Language selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
        if(selectedLanguage != null) setFormToSelectedLang(selectedLanguage);
        System.out.println(selectedLanguage +  " selected lang");
    }
}
