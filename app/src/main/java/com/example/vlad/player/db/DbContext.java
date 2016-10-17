package com.example.vlad.player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.vlad.player.models.Playlist;
import com.example.vlad.player.models.Song;

import java.util.ArrayList;
import java.util.List;

public class DbContext implements IDbContext {
    private SQLiteDatabase db;
    private DbOpenHelper dbHelper;

    public DbContext(Context context) {
        dbHelper = new DbOpenHelper(context);
        open();
        seed();
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    public List<Song> getSongs() {

        String selectQuery = "SELECT  * FROM " + D.SONGS_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Song> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Song(
                        cursor.getInt(cursor.getColumnIndex(D.SONG_ID)),
                        cursor.getString(cursor.getColumnIndex(D.SONG_TITLE)),
                        cursor.getString(cursor.getColumnIndex(D.SONG_ARTIST)),
                        cursor.getString(cursor.getColumnIndex(D.SONG_ARTIST))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @Override
    public List<Playlist> getPlaylists() {

        String selectQuery = "SELECT  * FROM " + D.PLAYLISTS_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Playlist> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Playlist(
                        cursor.getInt(cursor.getColumnIndex(D.PLAYLIST_ID)),
                        cursor.getString(cursor.getColumnIndex(D.PLAYLIST_NAME))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) {
        String selectQuery = "SELECT " + D.SONGS_TABLE + "." + D.SONG_ID + " AS " + D.SONG_ID +
                ", " + D.SONGS_TABLE + "." + D.SONG_TITLE + " AS " + D.SONG_TITLE +
                ", " + D.SONGS_TABLE + "." + D.SONG_ARTIST + " AS " + D.SONG_ARTIST +
                " FROM " + D.SONGS_TABLE +
                " JOIN " + D.PLAYLISTS_SONGS_TABLE + " ON " + D.SONGS_TABLE + "." + D.SONG_ID + "=" + D.PLAYLISTS_SONGS_TABLE + "." + D.PS_SONG_ID +
                " WHERE " + D.PLAYLISTS_SONGS_TABLE + "." + D.PS_PLAYLIST_ID + "=" + playlistId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Song> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Song(
                        cursor.getInt(cursor.getColumnIndex(D.SONG_ID)),
                        cursor.getString(cursor.getColumnIndex(D.SONG_TITLE)),
                        cursor.getString(cursor.getColumnIndex(D.SONG_ARTIST)),
                        cursor.getString(cursor.getColumnIndex(D.SONG_PATH))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @Override
    public void addSong(Song song, int playlistId) {

        ContentValues values = new ContentValues();
        values.put(D.SONG_TITLE, song.Title);
        values.put(D.SONG_ARTIST, song.Artist);

        long newIndex = db.insert(D.SONGS_TABLE, "", values);

        db.execSQL("INSERT INTO " + D.SONGS_TABLE +
                "(" + D.SONG_TITLE + ", " + D.SONG_ARTIST + ")" +
                " VALUES ('" + song.Title + "', '" + song.Artist + "');");

        values = new ContentValues();
        values.put(D.PS_SONG_ID, newIndex);
        values.put(D.PS_PLAYLIST_ID, playlistId);
        db.insert(D.PLAYLISTS_SONGS_TABLE, "", values);
    }

    @Override
    public void addPlaylist(Playlist playlist) {

        ContentValues values = new ContentValues();
        values.put(D.PLAYLIST_NAME, playlist.Name);

        db.insert(D.PLAYLISTS_TABLE, "", values);
    }

    @Override
    public void deleteSongById() {

    }

    private void seed() {
        addPlaylist(new Playlist(1, "Something"));
        addSong(new Song(1, "dfs", "kdsfaj", "fjdks"), 1);
    }
}
