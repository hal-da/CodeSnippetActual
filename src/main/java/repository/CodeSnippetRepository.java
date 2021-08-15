package repository;

import models.CodeSnippet;
import models.Language;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CodeSnippetRepository {
    int insert(CodeSnippet codeSnippet) throws SQLException;
    List<CodeSnippet> readAll() throws SQLException;
    Optional<CodeSnippet> read(int codeSnippetId) throws SQLException;
    CodeSnippet update(CodeSnippet codeSnippet) throws SQLException;
    void delete(CodeSnippet codeSnippet) throws SQLException;
}
