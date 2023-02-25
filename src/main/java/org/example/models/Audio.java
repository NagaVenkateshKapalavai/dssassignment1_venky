package org.example.models;

import java.util.Date;

public class Audio {
    public String name, path, singer, album, genre, year, duration;
    public int rating;
    public Date createdOn, updatedOn;

    public Audio() {
        this.createdOn = new Date();
        this.updatedOn = new Date();
    }

    public Audio(String name, String path, String singer, String album, String genre, String year, String duration, int rating) {
        this.name = name;
        this.path = path;
        this.singer = singer;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
        this.rating = rating;
        this.createdOn = new Date();
        this.updatedOn = new Date();
    }

    public String getName() {
        return this.name;
    }
}
