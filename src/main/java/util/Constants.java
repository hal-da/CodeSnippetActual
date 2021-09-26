package util;

public class Constants {

    //DB

    public static final String USER_NAME = "usr";
    public static final String USER_PW = "123";
    public static final String TABLE_NAME_LANGUAGES = "languages";
    public static final String TABLE_NAME_SNIPPETS = "snippets";
    public static final String CONNECTION_STRING = String.format("jdbc:derby:codeSnippetDB; user=%s; password=%s; create=true",USER_NAME,USER_PW);

    // Utils

    public static final String[] STANDARD_KEY_WORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte", "requires","Override",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "null"
    };



    // FXML
    public static final String snippetDetailFXML = "/fxml/snippetDetail.fxml";
    public static final String startFXML = "/fxml/start.fxml";
    public static final String customListCellFXML = "/fxml/customListCell.fxml";
    public static final String editLanguageFXML = "/fxml/editLanguage.fxml";

    // CSS
    public static final String javaKeywordsCSS = "/css/java-keywords.css";
    public static final String listViewCSS = "/css/listView.css";

    //Titles
    public static final String createSnippetTitle = "Create new CodeSnippet";
    public static final String startTitle = "CodeSnippet SingleUser";
    public static final String editLanguagesTitle = "Edit Languages";
    public static final String deleteTitle = "Delete CodeSnippet";


    //Dialogs
    public static final String[] sortOptionsArr = new String[] {"Last change", "Language", "Title", "Times seen", "Description", "created at", "favourites"};
    public static final String dateFormat = "dd.MM.yyyy";
    public static final String errorMessageDeleteLangWithAssociatedSnippet = "There are snippets associated with this language. \n Please delete or change these snippets first";
    public static final String confirmation = "Are you really really sure?";
    public static final String confirmationDeleteSnippet = "That Snippet will be lost, like tears in rain. \nAre you sure?";
    public static final String newLanguageName = "new language";
    public static final String searchBarStandardText = "search...";



}
