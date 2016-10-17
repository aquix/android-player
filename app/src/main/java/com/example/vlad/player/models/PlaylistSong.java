package com.example.vlad.player.models;

public class PlaylistSong {
    public PlaylistSong(int songId, int playlistId) {
        SongId = songId;
        PlaylistId = playlistId;
    }

    public int SongId;
    public int PlaylistId;
}
