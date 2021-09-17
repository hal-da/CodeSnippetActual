package repository;

import models.Language;
import util.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LanguageRepositoryJDBC implements LanguageRepository{

    private static final String INSERT_LANGUAGE = String.format("INSERT INTO %s (language_name, key_words) VALUES (?,?)", Constants.TABLE_NAME_LANGUAGES);

    Connection connection;
    public LanguageRepositoryJDBC() throws SQLException {
        connection = DriverManager.getConnection(Constants.CONNECTION_STRING);
    }

    @Override
    public int insert(Language language) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LANGUAGE, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, language.getName());
        String keyWords = language.getKeyWords().length == 0 ? String.join(",",Constants.STANDARD_KEY_WORDS) : String.join(",",language.getKeyWords());
        System.out.println("last words: " + keyWords);
        preparedStatement.setString(2,keyWords);
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public List<Language> readAll() throws SQLException {
        List<Language> languages = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s", Constants.TABLE_NAME_LANGUAGES));

        while (resultSet.next()){
            int languageId = resultSet.getInt(1);
            String languageName = resultSet.getString(2);
            String[] keyWords = resultSet.getString(3).split(",");
            Language language = new Language(languageId,languageName, keyWords);
            System.out.println(language.getName() + " language loaded from table - languagerepo");
            languages.add(language);
        }

        return languages;
    }

    @Override
    public Optional<Language> read(int id) throws SQLException {
        Language language = null;
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE language_id = ?", Constants.TABLE_NAME_LANGUAGES));
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int langId = resultSet.getInt(1);
            String langName = resultSet.getString(2);
            String[] keyWords = resultSet.getString(3).split(",");
            language = new Language(langId,langName,keyWords);
        }
        return Optional.ofNullable(language);
    }

    @Override
    public Language update(Language language) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("UPDATE %s SET language_name=?, key_words=? WHERE language_id=? ", Constants.TABLE_NAME_LANGUAGES));
        preparedStatement.setString(1,language.getName());
        String keyWords = language.getKeyWords().length == 0 ?  String.join(",",Constants.STANDARD_KEY_WORDS) : String.join(",",language.getKeyWords());
        preparedStatement.setString(2, keyWords);
        preparedStatement.setInt(3,language.getId());

        preparedStatement.executeUpdate();
        System.out.println(language + " UPDATED IN REPO");
        return language;
    }

    @Override
    public void delete(Language language) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("DELETE FROM %s WHERE LANGUAGE_ID = ?", Constants.TABLE_NAME_LANGUAGES));
        preparedStatement.setInt(1,language.getId());
        int rowsChanged = preparedStatement.executeUpdate();
        System.out.println(rowsChanged + " row deleted, name: " + language.getName());
    }
}
