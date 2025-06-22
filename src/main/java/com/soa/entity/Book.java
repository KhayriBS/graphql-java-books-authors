package com.soa.entity;

public class Book {
    private String authorId;
    private String title;
    private String id;

    public Book() {
    }

    public Book(String authorId, String title, String id) {
        this.authorId = authorId;
        this.title = title;
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
