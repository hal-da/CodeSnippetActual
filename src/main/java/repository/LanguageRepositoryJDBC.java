package repository;

import models.Language;
import util.Consts;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LanguageRepositoryJDBC implements LanguageRepository{

    private static final String INSERT_LANGUAGE = String.format("INSERT INTO %s (language_name) VALUES (?)", Consts.TABLE_NAME_LANGUAGES);

    Connection connection;

    public LanguageRepositoryJDBC() throws SQLException {
        connection = DriverManager.getConnection(Consts.CONNECTION_STRING);
    }

    @Override
    public int insert(String languageName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LANGUAGE, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, languageName);
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public List<Language> readAll() throws SQLException {
        List<Language> languages = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s",Consts.TABLE_NAME_LANGUAGES));

        while (resultSet.next()){
            int languageId = resultSet.getInt(1);
            String languageName = resultSet.getString(2);
            Language language = new Language(languageId,languageName);
            languages.add(language);
        }

        return languages;
    }

    @Override
    public Optional<Language> read(int id) throws SQLException {
        Language language = null;
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE language_id = ?",Consts.TABLE_NAME_LANGUAGES));
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int langId = resultSet.getInt(1);
            String langName = resultSet.getString(2);
            language = new Language(langId,langName);
        }
        return Optional.ofNullable(language);
    }

    @Override
    public Language update(Language language) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("UPDATE %s SET language_name=? WHERE language_id=? ",Consts.TABLE_NAME_LANGUAGES));
        preparedStatement.setString(1,language.getName());
        preparedStatement.setInt(2,language.getId());
        preparedStatement.executeUpdate();
        System.out.println(language);
        return language;
    }

    @Override
    public void delete(Language language) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("DELETE FROM %s WHERE LANGUAGE_ID = ?",Consts.TABLE_NAME_LANGUAGES));
        preparedStatement.setInt(1,language.getId());
        int rowsChanged = preparedStatement.executeUpdate();
        System.out.println(rowsChanged + " row deleted, name: " + language.getName());

    }
}
