package repository;

import models.Language;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LanguageRepository {
    int insert(Language language) throws SQLException;
    List<Language> readAll() throws SQLException;
    Optional<Language> read(int id) throws SQLException;
    Language update(Language language) throws SQLException;
    void delete(Language language) throws SQLException;
}
