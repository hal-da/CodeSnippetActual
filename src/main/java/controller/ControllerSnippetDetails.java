package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.CodeSnippet;
import models.Language;
import repository.CodeSnippetRepositoryJDBC;
import repository.LanguageRepositoryJDBC;
import view.TextCodeArea;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;


public class ControllerSnippetDetails {

    private TextCodeArea textCodeArea;
    private final LanguageRepositoryJDBC languageRepositoryJDBC = new LanguageRepositoryJDBC();
    private final CodeSnippetRepositoryJDBC codeSnippetRepositoryJDBC = new CodeSnippetRepositoryJDBC();

    private int codeSnippetId = 0;
    private CodeSnippet codeSnippet;

    @FXML
    private SVGPath svgIsFavorite;

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
    void handlerBtnEditLanguage() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/editLanguage.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Edit Languages");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            fillCHeckBox();
        }catch (IOException | SQLException e){
            e.printStackTrace();
        }

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

        labelDate.setText(String.valueOf(LocalDate.now()));
        textCodeArea = new TextCodeArea(anchorPane, "");

        disableButtonsAtStart(btnSave);
        disableButtonsAtStart(btnDelete);
        fillCHeckBox();
    }

    public void populateViewWithExistingCodeSnippet(CodeSnippet c){
        codeSnippet = c;
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

        //populate view
        textFieldTitle.setText(codeSnippet.getTitle());
        choiceBoxLanguage.getSelectionModel().select(codeSnippet.getLanguage());
        String url = String.valueOf(codeSnippet.getUrl());
        textFieldLink.setText(url.isEmpty() || url.equals("null") ? "" : url);
        labelDate.setText(String.valueOf(codeSnippet.getLastChange()));
        textFieldDescription.setText(codeSnippet.getDescription());
        textCodeArea = new TextCodeArea(anchorPane,codeSnippet.getSnippet());
        svgIsFavorite.opacityProperty().setValue(codeSnippet.isFavourite() ? 1 : 0);
        labelTimesSeen.setText(String.valueOf(codeSnippet.getTimesSeen()));
    }

    private void fillCHeckBox() throws SQLException {
        ObservableList<Language> languages = FXCollections.observableArrayList(languageRepositoryJDBC.readAll());
        choiceBoxLanguage.setItems(languages);
        choiceBoxLanguage.getSelectionModel().select(0);
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
        getCodeSnippetFromView();
        if (codeSnippetId == 0) {
            codeSnippetRepositoryJDBC.insert(codeSnippet);
        } else {
            codeSnippetRepositoryJDBC.update(codeSnippet);
        }
        closeStage();
    }

    private void getCodeSnippetFromView() {
        if(codeSnippet == null){
            codeSnippet = new CodeSnippet();
        }
        codeSnippet.setTitle(textFieldTitle.getText());
        codeSnippet.setDescription(textFieldDescription.getText());
        codeSnippet.setSnippet(textCodeArea.getText());
        codeSnippet.setLanguage(choiceBoxLanguage.getValue());
        try {
            codeSnippet.setUrl(new URL(textFieldLink.getText()));
        } catch (MalformedURLException e) {
            try {
                if(textFieldLink.getText().isEmpty()) codeSnippet.setUrl(null);
                //todo better malformedurlhandling
                else codeSnippet.setUrl(new URL("http://www."+textFieldLink.getText()));
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }
        }
        codeSnippet.setLastChange(LocalDate.now());
    }


    public void handleSVGMouseClick() {
        codeSnippet.setFavourite(!codeSnippet.isFavourite());
        System.out.println(codeSnippet.isFavourite());
        svgIsFavorite.opacityProperty().setValue(codeSnippet.isFavourite() ? 1 : 0);

    }



    private void disableButtonsAtStart(Button button){

        //Callable<Boolean> callableTextField = new CallableTextField(textCodeArea);

/*
        textCodeArea.getCodeArea().textProperty().addListener((ov, oldText, newText) -> {
            System.out.println(newText);
        });
        textCodeArea.getCodeArea().textProperty().
*/
        button.disableProperty().bind(
                textFieldDescription.textProperty().isEmpty().or(
                        textFieldTitle.textProperty().isEmpty().or(
                                choiceBoxLanguage.valueProperty().isNull()
                                       //TODO.or(Bindings.createBooleanBinding(callableTextField))
                        )
                )
        );
    }

/*    public Callable<TextCodeArea> CallableTextField(TextCodeArea textCodeArea) implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            return (textCodeArea.getCodeArea().);
        }
    }*/
}
