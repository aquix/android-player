package com.example.vlad.player.models;

public class Playlist {
    public Playlist(int id, String name) {
        Id = id;
        Name = name;
    }

    public int Id;
    public String Name;

    @Override
    public String toString() {
        return Name;
    }
}
