package com.soa.data;

import com.soa.entity.Author;
import com.soa.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public static List<Author> authors = new ArrayList<>(List.of(
            new Author("Victor Hugo", "1"),
            new Author("J.K. Rowling", "2")
    ));

    public static List<Book> books = new ArrayList<>(List.of(
            new Book("1", "Les Misérables", "1"),
            new Book("2", "Notre-Dame de Paris", "1"),
            new Book("3", "Harry Potter", "2")
    ));

}
