package repository;

import models.CodeSnippet;
import models.Language;
import util.Consts;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CodeSnippetRepositoryJDBC implements CodeSnippetRepository{
Connection connection;
LanguageRepositoryJDBC languageRepositoryJDBC = new LanguageRepositoryJDBC();

    public CodeSnippetRepositoryJDBC() throws SQLException {
        connection = DriverManager.getConnection(Consts.CONNECTION_STRING);
    }

    @Override
    public int insert(CodeSnippet codeSnippet) throws SQLException {
        String prepStatement = "INSERT INTO snippets(title,description,favourite,snippet,language_id,url,last_change,times_seen) VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(prepStatement, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1,codeSnippet.getTitle());
        preparedStatement.setString(2,codeSnippet.getDescription());
        preparedStatement.setBoolean(3,codeSnippet.isFavourite());
        preparedStatement.setString(4,codeSnippet.getSnippet());

        int langId = getFKLang(codeSnippet);

        preparedStatement.setInt(5,langId);
        preparedStatement.setString(6, String.valueOf(codeSnippet.getUrl()));
        preparedStatement.setDate(7, Date.valueOf(codeSnippet.getLastChange()));
        preparedStatement.setInt(8,codeSnippet.getTimesSeen());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public List<CodeSnippet> readAll() throws SQLException{

        List<CodeSnippet> codeSnippets = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM snippets");

        while (resultSet.next()){
            codeSnippets.add(getCodeSnippetFromResultSet(resultSet));
        }
        return codeSnippets;
    }

    @Override
    public Optional<CodeSnippet> read(int codeSnippetId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM snippets WHERE snippet_id = ?");
        preparedStatement.setInt(1,codeSnippetId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        CodeSnippet codeSnippet = getCodeSnippetFromResultSet(resultSet);

        System.out.println(codeSnippet);

        return Optional.of(codeSnippet);
    }


    @Override
    public CodeSnippet update(CodeSnippet codeSnippet) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement("UPDATE snippets SET title=?,description=?,favourite=?,snippet=?,language_id=?,url=?,last_change=?,times_seen=? WHERE snippet_id = ?");

        prepStatement.setString(1,codeSnippet.getTitle());
        prepStatement.setString(2,codeSnippet.getDescription());
        prepStatement.setBoolean(3, codeSnippet.isFavourite());
        prepStatement.setString(4, codeSnippet.getSnippet());
        prepStatement.setInt(5, getFKLang(codeSnippet));
        prepStatement.setString(6,String.valueOf(codeSnippet.getUrl()));
        prepStatement.setDate(7,Date.valueOf(codeSnippet.getLastChange()));
        prepStatement.setInt(8,codeSnippet.getTimesSeen());

        prepStatement.setInt(9,codeSnippet.getId());

        prepStatement.executeUpdate();
        System.out.println("update executed");
        return null;
    }

    @Override
    public void delete(CodeSnippet codeSnippet) throws SQLException {
        String deleteOne = "DELETE FROM snippets WHERE snippet_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteOne);
        preparedStatement.setInt(1,codeSnippet.getId());
        preparedStatement.executeUpdate();
    }

    private int getFKLang(CodeSnippet codeSnippet) throws SQLException {
        String getLanguageIdQuery = "SELECT language_id FROM languages WHERE language_name = ?";
        PreparedStatement getLangIdPrepStatement = connection.prepareStatement(getLanguageIdQuery);
        getLangIdPrepStatement.setString(1,codeSnippet.getLanguage().getName());
        ResultSet r = getLangIdPrepStatement.executeQuery();
        r.next();
        return r.getInt(1);
    }

    private CodeSnippet getCodeSnippetFromResultSet(ResultSet resultSet) throws SQLException {

        int snippetId = resultSet.getInt(1);
        String title = resultSet.getString(2);
        String description = resultSet.getString(3);
        boolean favourite = resultSet.getBoolean(4);
        String snippet = resultSet.getString(5);
        Language language = languageRepositoryJDBC.read(resultSet.getInt(6)).orElse(new Language("unreadable, should not have happened"));

        URL url = null;
        String urlFromDB = resultSet.getString(7);
        if(urlFromDB != null ){
            System.out.println(urlFromDB);
            try {
                url = new URL(urlFromDB);
            } catch (MalformedURLException e){
                System.out.println("url is null, not a problem");
            }
        }

        Date date = resultSet.getDate(8);
        int timesSeen = resultSet.getInt(9);

        return new CodeSnippet(
                snippetId,
                title,
                description,
                favourite,
                snippet,
                language,
                url,
                date.toLocalDate(),
                timesSeen
        );
    }
}
