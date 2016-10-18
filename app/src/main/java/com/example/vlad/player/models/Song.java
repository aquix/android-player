package com.example.vlad.player.models;

public class Song {
    public Song(int id, String title, String artist, String path) {
        this(title, artist, path);
        Id = id;
    }

    public Song(String title, String artist, String path) {
        Title = title;
        Artist = artist;
        Path = path;
    }

    public int Id;
    public String Title;
    public String Artist;
    public String Path;
}
