package db;

import util.Constants;
import java.sql.*;

public class DataBaseSetup {

    private static final String CREATE_TABLE_LANGUAGE = String.format("CREATE TABLE %s (" +
            "language_id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
            "language_name VARCHAR(64)" +
            ")", Constants.TABLE_NAME_LANGUAGES);

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


    public static void createDatabase() throws SQLException {
        System.out.println("db setup started");
        System.out.println("getting connection");
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(Constants.CONNECTION_STRING);
            System.out.println("connected");
            statement = connection.createStatement();
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }

        if (statement != null) {

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
        }

    }
}
