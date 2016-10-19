package com.example.vlad.player.models;

public class Playlist {
    public Playlist(int id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public Playlist(String name) {
        this.Name = name;
    }

    public int Id;
    public String Name;

    @Override
    public String toString() {
        return this.Name;
    }
}
