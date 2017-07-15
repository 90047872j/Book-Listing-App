package com.example.juan.booklistingapp;

import android.graphics.Bitmap;

public class Book {

    private String title;
    private String author;
    private String description;
    private String publishDate;
    private Bitmap image;
    private String bookUrl;

    public Book(Bitmap image,String title,String author,String publishDate, String description,String bookUrl){
        this.image = image;
        this.title = title;
        this.author= author;
        this.publishDate = publishDate;
        this.description = description;
        this.bookUrl = bookUrl;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public String getAuthor() {
        return author;
    }
}

