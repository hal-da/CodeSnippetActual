package util;

public class Constants {
    public static final String USER_NAME = "usr";
    public static final String USER_PW = "123";
    public static final String TABLE_NAME_LANGUAGES = "languages";
    public static final String TABLE_NAME_SNIPPETS = "snippets";
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

    public static final String CONNECTION_STRING = String.format("jdbc:derby:codeSnippetDB; user=%s; password=%s; create=true",USER_NAME,USER_PW);

}
