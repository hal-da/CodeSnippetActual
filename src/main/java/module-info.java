module CodeSnippetSU {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;
    requires java.sql.rowset;
    requires flowless;
    requires reactfx;
    requires org.fxmisc.richtext;
    requires org.junit.jupiter.api;
    requires junit;

    opens controller;
    opens app;
    opens models;
}