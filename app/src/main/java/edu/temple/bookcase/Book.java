package edu.temple.bookcase;

public class Book {
    int id;
    String title;
    String author;
    int published;
    String coverURL;

    public void Book(int id, String title, String author, int published, String coverURL){
        this.id = id;
        this.author = author;
        this.title = title;
        this.published = published;
        this.coverURL = coverURL;
    }
}
