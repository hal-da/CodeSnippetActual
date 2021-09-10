package controller;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import models.CodeSnippet;
import models.Language;
import repository.CodeSnippetRepositoryJDBC;
import repository.LanguageRepositoryJDBC;

public class ControllerStart {


    @FXML
    private CheckBox checkBoxSearchTitle;
    @FXML
    private CheckBox checkBoxSearchCode;
    @FXML
    private CheckBox checkBoxSearchDescription;
    @FXML
    private TextField textFieldSearchBar;

    @FXML
    private ChoiceBox<Language> choiceBoxLanguages;

    @FXML
    private ChoiceBox<String> choiceBoxSortBy;

    @FXML
    private ListView<CodeSnippet> listViewSnippets;

    @FXML
    private Button buttonNewSnippet;

    private final String[] sortOptionsArr = new String[] {"Last change", "Language", "Title", "Times seen", "Description"};
    private final ObservableList<String> sortOptions = FXCollections.observableArrayList(sortOptionsArr);
    private final LanguageRepositoryJDBC languageRepositoryJDBC = new LanguageRepositoryJDBC();
    private final CodeSnippetRepositoryJDBC codeSnippetRepositoryJDBC = new CodeSnippetRepositoryJDBC();
    private ObservableList<CodeSnippet> codeSnippetObservableList = FXCollections.observableArrayList(codeSnippetRepositoryJDBC.readAll());


    public ControllerStart() throws SQLException {
        System.out.println("controller started");
    }

    @FXML
    public void handleInputSort(){
        System.out.println(choiceBoxSortBy.getValue());

        switch (choiceBoxSortBy.getValue()) {
            case "Last change" -> codeSnippetObservableList.sort(Comparator.comparing(CodeSnippet::getLastChange).reversed());
            case "Language" -> codeSnippetObservableList.sort(Comparator.comparing(x -> x.getLanguage().getName()));
            case "Title" -> codeSnippetObservableList.sort(Comparator.comparing(CodeSnippet::getTitle));
            case "Times seen" -> codeSnippetObservableList.sort(Comparator.comparing(CodeSnippet::getTimesSeen).reversed());
            case "Description" -> codeSnippetObservableList.sort(Comparator.comparing(CodeSnippet::getDescription));
            default -> System.out.println("default");
        }
    }

    @FXML
    void handleInputTextChangedSearch() {
        System.out.println("handle input search");

        String searchTerm = textFieldSearchBar.getText().trim().toLowerCase();
        if(searchTerm.equals("search...") || searchTerm.equals("")) {
            filterSnippetListByLanguage();
            return;
        }

        ObservableList<CodeSnippet> searchResult = FXCollections.observableArrayList();

        if(checkBoxSearchTitle.isSelected()){
            ObservableList<CodeSnippet> searchTitleResultList = codeSnippetObservableList
                    .stream()
                    .filter(x -> x.getTitle().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            searchResult.addAll(searchTitleResultList);
        }
        if(checkBoxSearchDescription.isSelected()){
            ObservableList<CodeSnippet> searchDescriptionResultList = codeSnippetObservableList
                    .stream()
                    .filter(x -> x.getDescription().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            searchResult.addAll(searchDescriptionResultList);
        }
        if(checkBoxSearchCode.isSelected()){
            ObservableList<CodeSnippet> searchCodeResultList = codeSnippetObservableList
                    .stream()
                    .filter(x -> x.getSnippet().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            searchResult.addAll(searchCodeResultList);
        }
        if(!choiceBoxLanguages.getSelectionModel().isSelected(0)){
            searchResult = searchResult.filtered(codeSnippet -> codeSnippet.getLanguage().equals(choiceBoxLanguages.getSelectionModel().getSelectedItem()));
        }
        ObservableList<CodeSnippet> distinctResult = searchResult.stream().distinct().collect(Collectors.toCollection(FXCollections::observableArrayList));

        fillListView(distinctResult);
    }

    @FXML
    void handleNewSnippet() {
        openSnippetDetail(new CodeSnippet());
    }

    private void openSnippetDetail(CodeSnippet codeSnippet){

        if(codeSnippet == null) return;

        CodeSnippetDetailWindow codeSnippetDetailWindow = new CodeSnippetDetailWindow();
        codeSnippetDetailWindow.showSnippetInStage(codeSnippet);

        try {
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listViewSnippets.getSelectionModel().clearSelection();
    }

    @FXML
    void initialize() throws SQLException {
        System.out.println("init");
        assert textFieldSearchBar != null : "fx:id=\"textFieldSearchBar\" was not injected: check your FXML file 'start.fxml'.";
        assert choiceBoxLanguages != null : "fx:id=\"choiceBoxLanguage\" was not injected: check your FXML file 'start.fxml'.";
        assert choiceBoxSortBy != null : "fx:id=\"choiceBoxSortBy\" was not injected: check your FXML file 'start.fxml'.";
        assert listViewSnippets != null : "fx:id=\"listSnippets\" was not injected: check your FXML file 'start.fxml'.";
        assert buttonNewSnippet != null : "fx:id=\"buttonNewSnippet\" was not injected: check your FXML file 'start.fxml'.";

        checkBoxSearchDescription.setSelected(true);
        checkBoxSearchCode.setSelected(true);
        checkBoxSearchTitle.setSelected(true);

        listViewSnippets.setCellFactory(cell -> new CustomListCell());
        listViewSnippets.setOnMouseClicked(click -> openSnippetDetail(listViewSnippets.getSelectionModel().getSelectedItem()));

        fillChoiceBox();
        fillSortBox();
        try {
            codeSnippetObservableList = FXCollections.observableArrayList(codeSnippetRepositoryJDBC.readAll());
        } catch (Exception e){
            e.printStackTrace();
        }
        fillListView(codeSnippetObservableList);
        codeSnippetObservableList.sort(Comparator.comparing(CodeSnippet::getLastChange).reversed());
    }

    private void fillListView(ObservableList<CodeSnippet> codeSnippetObservableList){
        listViewSnippets.setItems(codeSnippetObservableList);
    }

    private void fillSortBox(){
        choiceBoxSortBy.setItems(sortOptions);
        choiceBoxSortBy.getSelectionModel().select(0);
    }

    private void fillChoiceBox() throws SQLException {
        ObservableList<Language> languages = FXCollections.observableArrayList(languageRepositoryJDBC.readAll());
        choiceBoxLanguages.setItems(languages);
        choiceBoxLanguages.getItems().add(0,new Language("All"));
        choiceBoxLanguages.getSelectionModel().select(0);
    }

    public void handleMousePressedSearchBar() {
        textFieldSearchBar.setText("");
        handleInputTextChangedSearch();
    }

    @FXML
    private void filterSnippetListByLanguage() {
        System.out.println("filter lang");
        if(!choiceBoxLanguages.getSelectionModel().isSelected(0)){
            ObservableList<CodeSnippet> languageFilteredList  = codeSnippetObservableList
                    .stream()
                    .filter(codeSnippet -> codeSnippet.getLanguage().equals(choiceBoxLanguages.getSelectionModel().getSelectedItem()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            fillListView(languageFilteredList);
        } else fillListView(codeSnippetObservableList);
    }

}