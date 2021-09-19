package models;

import java.net.URL;
import java.time.LocalDate;


public class CodeSnippet {

    int id = 0;
    String title = "";
    String description = "";
    boolean isFavourite = false;
    String snippet = "";
    Language language = null;
    URL url = null;
    LocalDate lastChange = LocalDate.now();
    LocalDate createdAt = LocalDate.now();
    int timesSeen = 0;

    //only for unit test
    public CodeSnippet() {
    }




    //new from DB
    public CodeSnippet(int id, String title, String description, boolean isFavourite, String snippet, Language language, URL url, LocalDate lastChange, int timesSeen, LocalDate createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isFavourite = isFavourite;
        this.snippet = snippet;
        this.language = language;
        this.url = url;
        this.lastChange = lastChange;
        this.timesSeen = timesSeen;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public URL getUrl() {
        return url;
    }

    public LocalDate getLastChange() {
        return lastChange;
    }

    public void setLastChange(LocalDate lastChange) {
        this.lastChange = lastChange;
    }

    public int getTimesSeen() {
        return timesSeen;
    }

    public void setTimesSeen(int timesSeen) {
        this.timesSeen = timesSeen;
    }

    public LocalDate getCreatedAt() {return createdAt;}

    public void setCreatedAt(LocalDate createdAt) {this.createdAt = createdAt;}

    @Override
    public String toString() {
        return "CodeSnippet{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isFavourite=" + isFavourite +
                ", snippet='" + snippet + '\'' +
                ", language=" + language +
                ", url=" + url +
                ", lastChange=" + lastChange +
                '}';
    }
}
