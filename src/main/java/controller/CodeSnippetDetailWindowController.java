package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.CodeSnippet;
import java.io.IOException;
import java.util.Objects;

public class CodeSnippetDetailWindowController {
    private CodeSnippet codeSnippet;

    void showSnippetInStage(CodeSnippet codeSnippet){

        if(codeSnippet == null) return;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/snippetDetail.fxml"));
        try {
            Parent root = fxmlLoader.load();
            SnippetDetailsController snippetDetailsController = fxmlLoader.getController();
            snippetDetailsController.setCodeSnippet(codeSnippet);
            snippetDetailsController.populateViewWithExistingCodeSnippet();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/java-keywords.css")).toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Create new CodeSnippet");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public CodeSnippet getCodeSnippet() {
        return codeSnippet;
    }

    public void setCodeSnippet(CodeSnippet codeSnippet) {
        this.codeSnippet = codeSnippet;
    }
}
