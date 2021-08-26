package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import models.CodeSnippet;

import java.io.IOException;

public class CustomListCell extends ListCell<CodeSnippet> {

    public CustomListCell() {
        if (fxmlLoader == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/customListCell.fxml"));
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private FXMLLoader fxmlLoader;

    @FXML
    private Label labelDate;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelLanguage;
    @FXML
    private SVGPath svgFavStar;
    @FXML
    private Label labelDescription;
    @FXML
    private AnchorPane anchorPane;

    @Override
    protected void updateItem(CodeSnippet codeSnippet, boolean empty){

        super.updateItem(codeSnippet,empty);

        if(empty || codeSnippet == null){
            setText(null);
            setGraphic(null);
        } else {
            labelDate.setText(String.valueOf(codeSnippet.getLastChange()));
            labelTitle.setText(codeSnippet.getTitle());
            labelDescription.setText(codeSnippet.getDescription());
            labelLanguage.setText(codeSnippet.getLanguage().getName());
            svgFavStar.opacityProperty().setValue(codeSnippet.isFavourite() ? 1 : 0);
            setGraphic(anchorPane);
        }
    }
}
