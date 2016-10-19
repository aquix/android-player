package com.example.vlad.player.models;

public class Song {
    public Song(int id, String title, String artist, String path) {
        this(title, artist, path);
        this.Id = id;
    }

    public Song(String title, String artist, String path) {
        this.Title = title;
        this.Artist = artist;
        this.Path = path;
    }

    public int Id;
    public String Title;
    public String Artist;
    public String Path;
}
