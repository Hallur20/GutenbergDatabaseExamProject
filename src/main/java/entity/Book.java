package entity;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private int id;
    private String title;
    private List<String> authors;
    private List<String> cities;

    public Book(int id, String title, List<String> authors) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        cities = new ArrayList();
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

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title=" + title + ", authors=" + authors + ", cities=" + cities + '}';
    }

}
