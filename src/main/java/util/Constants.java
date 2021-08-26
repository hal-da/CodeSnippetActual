package util;

public class Constants {
    public static final String USER_NAME = "usr";
    public static final String USER_PW = "123";
    public static final String TABLE_NAME_LANGUAGES = "languages";
    public static final String TABLE_NAME_SNIPPETS = "snippets";

    public static final String CONNECTION_STRING = String.format("jdbc:derby:codeSnippetDB; user=%s; password=%s; create=true",USER_NAME,USER_PW);

}
