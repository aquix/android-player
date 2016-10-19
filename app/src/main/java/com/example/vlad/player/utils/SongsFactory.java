package com.example.vlad.player.utils;

import android.media.MediaMetadataRetriever;

import com.example.vlad.player.models.Song;

public class SongsFactory {
    public static Song createFromPath(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);

        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        return new Song(title, artist, path);
    }
}
