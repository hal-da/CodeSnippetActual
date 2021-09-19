package db;

import models.CodeSnippet;
import models.Language;
import repository.CodeSnippetRepositoryJDBC;
import repository.LanguageRepositoryJDBC;
import util.Constants;
import java.sql.*;

public class DataBaseSetup {

    private static final String CREATE_TABLE_LANGUAGE = "CREATE TABLE languages (" +
            "language_id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
            "language_name VARCHAR(64)," +
            "key_words VARCHAR(2550)" +
            ")";

    private static final String CREATE_TABLE_SNIPPETS = "CREATE TABLE snippets (" +
            "snippet_id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
            "title VARCHAR(255)," +
            "description LONG VARCHAR," +
            "favourite BOOLEAN," +
            "snippet LONG VARCHAR," +
            "language_id INTEGER NOT NULL," +
            "url VARCHAR(2550),"+
            "last_change DATE," +
            "times_seen INTEGER," +
            "created_at DATE," +
            "FOREIGN KEY (language_id) REFERENCES languages(language_id)" +
            ")";

    public static void createDatabase() {
        System.out.println("db setup started");
        System.out.println("getting connection");
        Connection connection;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(Constants.CONNECTION_STRING);
            System.out.println("connected");
            statement = connection.createStatement();
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }

        if (statement != null) {
//            statement.execute("DROP table snippets");
//            statement.execute("DROP table languages");
            try {
                statement.execute(CREATE_TABLE_LANGUAGE);
                System.out.println("table lang created");
                statement.execute(CREATE_TABLE_SNIPPETS);
                System.out.println("table snippets created");
                createAndInsertLanguageJavaAndFirstCodeSnippetIntoDB();
            } catch (SQLException sqlException) {
                System.out.println(sqlException);
            }
        }
    }


    private static void createAndInsertLanguageJavaAndFirstCodeSnippetIntoDB() throws  SQLException {
        CodeSnippetRepositoryJDBC codeSnippetRepositoryJDBC = new CodeSnippetRepositoryJDBC();
        LanguageRepositoryJDBC languageRepositoryJDBC = new LanguageRepositoryJDBC();

        //create and insert language

        Language java = new Language("Java");
        java.setKeyWords(Constants.STANDARD_KEY_WORDS);
        java.setId(languageRepositoryJDBC.insert(java));

        //create and insert codesnippet

        CodeSnippet codeSnippet = new CodeSnippet();
        codeSnippet.setTitle("TestSnippet");
        codeSnippet.setDescription("This is a TestSnippet. Change me, delete me, play with me");
        codeSnippet.setFavourite(true);
        codeSnippet.setLanguage(java);
        codeSnippet.setSnippet("""
                public class Main {
                \t
                \taFunction(5);
                \t

                \tprivate void aFunction(int a){
                \t\twhile(a < 10){
                \t\t\tSystem.out.println("This is testsnippet");
                \t\t}\t
                \t}
                \t/*
                \t\tcolor palette is inspired by intellij darcula theme
                \t*/
                }""");

        codeSnippetRepositoryJDBC.insert(codeSnippet);
        System.out.println("inserted test snippet");
    }
}
