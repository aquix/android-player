package com.example.vlad.player.services;

import com.example.vlad.player.models.Song;

import java.util.LinkedList;
import java.util.List;

public class LastSongsService {
    // Singleton
    private static LastSongsService ourInstance = new LastSongsService();
    public static LastSongsService getInstance() {
        return ourInstance;
    }
    private LastSongsService() {
        this.songs = new LinkedList<>();
    }

    private final int MAX_LIST_SIZE = 5;
    private LinkedList<Song> songs;

    public void addSong(Song openedSong) {
        // Check if song is already in list
        for (Song song : this.songs) {
            if (song.Id == openedSong.Id) {
                this.songs.remove(song);
                break;
            }
        }

        if (this.songs.size() >= this.MAX_LIST_SIZE) {
            this.songs.removeLast();
        }
        this.songs.addFirst(openedSong);
    }

    public List<Song> getSongs() {
        return this.songs;
    }
}
