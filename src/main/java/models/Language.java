package models;

import util.Constants;

import java.util.Arrays;
import java.util.Objects;

public class Language {

    int id;
    String name;
    String[] keyWords = Constants.STANDARD_KEY_WORDS;


    public Language(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Language(String name, String keyWords) {
        this.name = name;
        this.keyWords = keyWords.split(",");
    }

    public Language(int id, String name, String[] keyWords) {
        this.id = id;
        this.name = name;
        this.keyWords = keyWords;
    }

    public Language(String name) {
        this.name = name;
    }

    public Language() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String[] keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return id == language.id && Objects.equals(name, language.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

