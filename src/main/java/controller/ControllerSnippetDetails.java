package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import models.CodeSnippet;
import models.Language;
import repository.CodeSnippetRepositoryJDBC;
import repository.LanguageRepositoryJDBC;
import util.Constants;
import view.TextCodeArea;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;


public class ControllerSnippetDetails {

    private TextCodeArea textCodeArea;
    private final LanguageRepositoryJDBC languageRepositoryJDBC = new LanguageRepositoryJDBC();
    private final CodeSnippetRepositoryJDBC codeSnippetRepositoryJDBC = new CodeSnippetRepositoryJDBC();
    ObservableList<Language> languages = FXCollections.observableArrayList(languageRepositoryJDBC.readAll());

    private int codeSnippetId = 0;

    public void setCodeSnippet(CodeSnippet codeSnippet) {
        this.codeSnippet = codeSnippet;
    }

    private CodeSnippet codeSnippet;

    @FXML
    private SVGPath svgIsFavorite;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;

    @FXML
    private Label labelTimesSeen;

    @FXML
    private Label labelDate;

    @FXML
    private TextField textFieldLink;

    @FXML
    private TextField textFieldTitle;

    @FXML
    private ChoiceBox<Language> choiceBoxLanguage;

    @FXML
    private Button btnEditLanguage;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextArea textFieldDescription;

    public ControllerSnippetDetails() throws SQLException {
    }

    @FXML
    void handlerBtnEditLanguage() throws SQLException {


        int languageId = choiceBoxLanguage.getSelectionModel().getSelectedItem() == null ? 0 : choiceBoxLanguage.getSelectionModel().getSelectedItem().getId();
        Language changedLanguage = languageRepositoryJDBC.read(languageId).orElse(choiceBoxLanguage.getSelectionModel().getSelectedItem());
        EditLanguageWindow editLanguageWindow = new EditLanguageWindow();
        editLanguageWindow.showLanguageEditWindow(changedLanguage);
        Stage stage = editLanguageWindow.getStage();
        stage.showAndWait();
        languages = FXCollections.observableArrayList(languageRepositoryJDBC.readAll());
        choiceBoxLanguage.setItems(languages);
        choiceBoxLanguage.getSelectionModel().select(changedLanguage);
        String textCodeAreaText = textCodeArea.getText();
        String[] keyWords = changedLanguage == null ? Constants.STANDARD_KEY_WORDS : changedLanguage.getKeyWords();
        System.out.println("KEYWORDS FROM CONTROLLER SNIPPET DETAIL: " + Arrays.toString(keyWords));

        createTextCodeArea(keyWords,textCodeAreaText);
    }


    @FXML
    void handleBtnCancel() {
        System.out.println(textCodeArea.getText());
        closeStage();
    }

    private void closeStage(){
        textCodeArea.cleanUp();
        Stage stage = (Stage) btnEditLanguage.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() throws SQLException {
        assert textFieldTitle != null : "fx:id=\"textFieldTitle\" was not injected: check your FXML file 'snippetDetail.fxml'.";
        assert choiceBoxLanguage != null : "fx:id=\"choiceBoxLanguage\" was not injected: check your FXML file 'snippetDetail.fxml'.";
        assert btnEditLanguage != null : "fx:id=\"btnEditLanguage\" was not injected: check your FXML file 'snippetDetail.fxml'.";
        assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'snippetDetail.fxml'.";
        assert textFieldDescription != null : "fx:id=\"textFieldDescription\" was not injected: check your FXML file 'snippetDetail.fxml'.";

        textFieldTitle.setPromptText("Title");
        textFieldDescription.setPromptText("Description");

        labelDate.setText(String.valueOf(LocalDate.now()));
        languages = FXCollections.observableArrayList(languageRepositoryJDBC.readAll());
        fillLanguageList();

        disableButtonsAtStart(btnSave);
        disableButtonsAtStart(btnDelete);
    }

    private void fillLanguageList(){
        choiceBoxLanguage.setItems(languages);
    }

    public void populateViewWithExistingCodeSnippet(){
        codeSnippetId = codeSnippet.getId();
        //raise timesSeen in CodeSnippet & save it
        if(codeSnippetId != 0){
            codeSnippet.setTimesSeen(codeSnippet.getTimesSeen()+1);
            try {
                codeSnippetRepositoryJDBC.update(codeSnippet);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        } else {
            btnDelete.setVisible(false);
        }
        populateView();
    }

    private void populateView(){
        String[] keywords = codeSnippet.getLanguage() == null ?  Constants.STANDARD_KEY_WORDS : codeSnippet.getLanguage().getKeyWords();

        textFieldTitle.setText(codeSnippet.getTitle());
        choiceBoxLanguage.getSelectionModel().select(codeSnippet.getLanguage());
        labelDate.setText(String.valueOf(codeSnippet.getLastChange()));
        textFieldDescription.setText(codeSnippet.getDescription());
        System.out.println(Arrays.toString(keywords));
        createTextCodeArea(keywords, codeSnippet.getSnippet());

        svgIsFavorite.opacityProperty().setValue(codeSnippet.isFavourite() ? 1 : 0);
        labelTimesSeen.setText(String.valueOf(codeSnippet.getTimesSeen()));
    }

    private void createTextCodeArea(String[] keywords, String snippet) {
        TextCodeArea.KEYWORDS = keywords;
        TextCodeArea.setKeywordPattern();
        textCodeArea = new TextCodeArea(anchorPane,snippet, keywords);
    }

    @FXML
    private void handleBtnDelete() throws SQLException {
        if(codeSnippetId!=0){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setContentText("That Snippet will be lost, like tears in rain. \nAre you sure?");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
            dialog.setTitle("Delete CodeSnippet");
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                codeSnippetRepositoryJDBC.delete(codeSnippet);
            }
        }
        closeStage();

    }

    @FXML
    private void handleBtnSave() throws SQLException {
        System.out.println("click save btn" + codeSnippetId);
        createCodeSnippetFromFormData();
        if (codeSnippetId == 0) {
            codeSnippetRepositoryJDBC.insert(codeSnippet);
        } else {
            codeSnippetRepositoryJDBC.update(codeSnippet);
        }
        closeStage();
    }

    private void createCodeSnippetFromFormData() {
        if(codeSnippet == null){
            codeSnippet = new CodeSnippet();
        }
        codeSnippet.setTitle(textFieldTitle.getText());
        codeSnippet.setDescription(textFieldDescription.getText());
        codeSnippet.setSnippet(textCodeArea.getText());
        codeSnippet.setLanguage(choiceBoxLanguage.getValue());
        codeSnippet.setLastChange(LocalDate.now());
    }


    public void handleSVGMouseClick() {
        codeSnippet.setFavourite(!codeSnippet.isFavourite());
        System.out.println(codeSnippet.isFavourite());
        svgIsFavorite.opacityProperty().setValue(codeSnippet.isFavourite() ? 1 : 0);
    }

    private void disableButtonsAtStart(Button button){

        button.disableProperty().bind(
                textFieldDescription.textProperty().isEmpty().or(
                        textFieldTitle.textProperty().isEmpty().or(
                                choiceBoxLanguage.valueProperty().isNull()
                        )
                )
        );
    }

    public void choiceBoxLanguageChanged(ActionEvent actionEvent) {
        if(textCodeArea == null) return;
        System.out.println(choiceBoxLanguage.getSelectionModel().getSelectedItem());
        Language selectedLanguage = choiceBoxLanguage.getSelectionModel().getSelectedItem();
        String text = textCodeArea.getText();
        String[] keywords = selectedLanguage == null ? Constants.STANDARD_KEY_WORDS : selectedLanguage.getKeyWords();
        createTextCodeArea(keywords, text );
        choiceBoxLanguage.getSelectionModel().select(selectedLanguage);
    }
}
