package controller;

import java.sql.SQLException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.CodeSnippet;
import models.Language;
import repository.CodeSnippetRepositoryJDBC;
import repository.LanguageRepositoryJDBC;

public class EditLanguageController {

    private final LanguageRepositoryJDBC languageRepositoryJDBC = new LanguageRepositoryJDBC();

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

    public EditLanguageController() throws SQLException {}

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

    @FXML
    void handleBtnCancel() {
        closeStage();
    }

    @FXML
    void handleBtnDelete() throws SQLException {
        int choiceBoxSelectedIndex = choiceBoxLanguages.getSelectionModel().getSelectedIndex();

        if(choiceBoxSelectedIndex != 0){
            CodeSnippetRepositoryJDBC codeSnippetRepositoryJDBC = new CodeSnippetRepositoryJDBC();
            Language languageFromChoiceBox = choiceBoxLanguages.getSelectionModel().getSelectedItem();

            Optional<CodeSnippet> codeSnippetOptional = codeSnippetRepositoryJDBC.readAll()
                    .stream()
                    .filter(codeSnippet -> codeSnippet.getLanguage().getId() == languageFromChoiceBox.getId())
                            .findFirst();

            if (codeSnippetOptional.isPresent()) {
                deleteSnippetsBeforeLanguageShowDialog();
            } else readyToDeleteShowDialog();
        }
    }

    private void deleteSnippetsBeforeLanguageShowDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setContentText("There are snippets associated with this language. \n Please delete or change these snippets first");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.showAndWait();
    }

    private void readyToDeleteShowDialog() throws SQLException {
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

    @FXML
    void handleBtnSave() throws SQLException {
        int choiceBoxSelectedIndex = choiceBoxLanguages.getSelectionModel().getSelectedIndex();
        if(choiceBoxSelectedIndex == 0){

            languageRepositoryJDBC.insert(new Language(textFieldLanguage.getText(), textAreaKeyWords.getText()));
        } else {
            languageRepositoryJDBC.update(new Language(choiceBoxLanguages.getSelectionModel().getSelectedItem().getId(),textFieldLanguage.getText(), textAreaKeyWords.getText()));
        }
        closeStage();
    }

    private void closeStage(){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
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
        ObservableList<Language> languages = FXCollections.observableArrayList(languageRepositoryJDBC.readAll());
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
        if(selectedLanguage != null) setFormToSelectedLang(selectedLanguage);
    }
}
