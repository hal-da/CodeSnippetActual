package repository;

import models.CodeSnippet;

import java.sql.SQLException;
import java.util.List;

public interface CodeSnippetRepository {
    void insert(CodeSnippet codeSnippet) throws SQLException;
    List<CodeSnippet> readAll() throws SQLException;
    void update(CodeSnippet codeSnippet) throws SQLException;
    void delete(CodeSnippet codeSnippet) throws SQLException;
}
