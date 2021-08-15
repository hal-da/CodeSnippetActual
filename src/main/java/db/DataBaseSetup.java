package db;

import models.CodeSnippet;
import models.Language;
import util.Consts;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;


public class DataBaseSetup {

    private static final String CREATE_TABLE_LANGUAGE = String.format("CREATE TABLE %s (" +
            "language_id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
            "language_name VARCHAR(64)" +
            ")", Consts.TABLE_NAME_LANGUAGES);

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
            "FOREIGN KEY (language_id) REFERENCES languages(language_id)" +
            ")";


/*    private static final String CREATE_TABLE_LINKS = String.format("CREATE TABLE %s (" +
            "id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
            "link VARCHAR(2048)," +
            "snippet_id INTEGER NOT NULL," +
            "FOREIGN KEY (snippet_id) REFERENCES %s(snippet_id)" +
            ")", Consts.TABLE_NAME_LINKS, Consts.TABLE_NAME_SNIPPETS);*/

    public static void startDatabase() throws SQLException {
        System.out.println("db setup started");
        System.out.println("getting connection");
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(Consts.CONNECTION_STRING);
            System.out.println("connected");
            statement = connection.createStatement();
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }

        if (statement != null) {
  //      statement.execute("DROP table links");
//        statement.execute("DROP table snippets");
//        statement.execute("DROP table languages");
            try {
                statement.execute(CREATE_TABLE_LANGUAGE);
                System.out.println("lang created");
            } catch (SQLException sqlException) {
                System.out.println(sqlException);
            }
            try {
                statement.execute(CREATE_TABLE_SNIPPETS);
                System.out.println("snippets created");
            } catch (SQLException sqlException) {
                System.out.println(sqlException);
            }
//            try {
////                statement.execute(CREATE_TABLE_LINKS);
////                System.out.println("links created");
//
//                //TEST PURPOSE DELETE!
//                statement.execute("INSERT INTO languages (language_name) VALUES ('JAVA')");
//                statement.execute("INSERT INTO languages (language_name) VALUES ('JavaScript')");
//                statement.execute("INSERT INTO languages (language_name) VALUES ('PHP')");
//
//                System.out.println("TEST INSERTS OK");
//
//
//
//            } catch (SQLException  e) {
//                System.out.println(e);
//            }
//            try {
//                putASnippet(connection);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
        }

    }

    private static void putASnippet(Connection connection) throws MalformedURLException, SQLException {
        URL url = new URL("https://www.google.com/search?q=google&rlz=1C1CHBD_deAT906AT906&oq=go&aqs=chrome.1.69i57j0i131i433l3j0i433j0i131i433l5.1572j0j4&sourceid=chrome&ie=UTF-8");
        CodeSnippet codeSnippet = new CodeSnippet(
                "my first CodeSnippet",
                "its really my first",
                true,
                "package app;\n" +
                        "\n" +
                        "import db.DataBaseSetup;\n" +
                        "import javafx.application.Application;\n" +
                        "import javafx.fxml.FXMLLoader;\n" +
                        "import javafx.scene.Parent;\n" +
                        "import javafx.scene.Scene;\n" +
                        "import javafx.stage.Stage;\n" +
                        "\n" +
                        "import java.util.Objects;\n" +
                        "\n" +
                        "public class Main extends Application {\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void start(Stage primaryStage) throws Exception {\n" +
                        "        DataBaseSetup.startDatabase();\n" +
                        "        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(\"/start.fxml\")));\n" +
                        "        primaryStage.setTitle(\"CodeSnippet SingleUser\");\n" +
                        "        Scene scene = new Scene(root);\n" +
                        "        primaryStage.setScene(scene);\n" +
                        "        primaryStage.show();\n" +
                        "\n" +
                        "    }\n" +
                        "    public static void main(String[] args) {\n" +
                        "        launch(args);\n" +
                        "    }\n" +
                        "}\n",
                new Language("JAVA"),
                url,
                0);

        String prepStatement = "INSERT INTO snippets(title,description,favourite,snippet,language_id,url,last_change, times_seen) VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);

        preparedStatement.setString(1,codeSnippet.getTitle());
        preparedStatement.setString(2,codeSnippet.getDescription());
        preparedStatement.setBoolean(3,codeSnippet.isFavourite());
        preparedStatement.setString(4,codeSnippet.getSnippet());

        int langId = getFKLang(connection, codeSnippet);

        preparedStatement.setInt(5,langId);
        preparedStatement.setString(6, url.toExternalForm());
        preparedStatement.setDate(7, Date.valueOf(codeSnippet.getLastChange()));
        preparedStatement.setInt(8,codeSnippet.getTimesSeen());

        preparedStatement.executeUpdate();

        System.out.println("inserted test snippet");


    }

    private static int getFKLang(Connection connection, CodeSnippet codeSnippet) throws SQLException {
        String getLanguageIdQuery = "SELECT language_id FROM languages WHERE language_name = ?";
        PreparedStatement getLangIdPrepStatement = connection.prepareStatement(getLanguageIdQuery);
        getLangIdPrepStatement.setString(1,codeSnippet.getLanguage().getName());
        ResultSet r = getLangIdPrepStatement.executeQuery();
        r.next();
        return r.getInt(1);
    }

}
