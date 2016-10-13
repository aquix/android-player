package com.example.vlad.player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

import com.example.vlad.player.models.Playlist;
import com.example.vlad.player.models.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class SqliteContext extends SQLiteOpenHelper implements IDbContext {
    private static final String DATABASE_NAME = "playlist.db";
    private static final int SCHEMA = 1;
    private static final String PLAYLISTS_TABLE = "playlists";
    private static final String SONGS_TABLE = "songs";
    private static final String PLAYLISTS_SONGS_TABLE = "plylists_songs";

    private static final String PLAYLIST_ID = "id";
    private static final String PLAYLIST_NAME = "name";
    private static final String SONG_ID = "id";
    private static final String SONG_TITLE = "title";
    private static final String SONG_ARTIST = "artist";

    private static final String PS_SONG_ID = "song_id";
    private static final String PS_PLAYLIST_ID = "playlist_id";

    private SQLiteDatabase db;


    public SqliteContext(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        db.execSQL("PRAGMA foreign_keys = ON");

        db.execSQL("CREATE TABLE " + PLAYLISTS_TABLE + " (" +
                PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLAYLIST_NAME + " NVARCHAR(50) " +
                ");");

        db.execSQL("CREATE TABLE " + SONGS_TABLE + " (" +
                SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SONG_ARTIST + " NVARCHAR(50), " +
                SONG_TITLE + " NVARCHAR(50) " +
                ");");

        db.execSQL("CREATE TABLE " + PLAYLISTS_SONGS_TABLE + " (" +
                PS_PLAYLIST_ID + " INTEGER, " +
                PS_SONG_ID + " INTEGER " +
                "PRIMARY KEY (" + PS_PLAYLIST_ID + ", " + PS_SONG_ID + ") " +
                "FOREIGN KEY (" + PS_PLAYLIST_ID + ") REFERENCES " + PLAYLISTS_TABLE + "(" + PLAYLIST_ID + ") ON DELETE CASCADE" +
                "FOREIGN KEY (" + PS_SONG_ID + ") REFERENCES " + SONGS_TABLE + "(" + SONG_ID + ") ON DELETE CASCADE" +
                ");");

        // Seed
        db.execSQL("INSERT INTO "+ PLAYLISTS_TABLE +
                "(" + PLAYLIST_NAME + ")" +
                " VALUES ('For night');");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ PLAYLISTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ SONGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ PLAYLISTS_SONGS_TABLE);
        onCreate(db);
    }

    @Override
    public List<Song> getSongs() {

        String selectQuery = "SELECT  * FROM " + SONGS_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Song> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Song(
                    cursor.getInt(cursor.getColumnIndex(SONG_ID)),
                    cursor.getString(cursor.getColumnIndex(SONG_TITLE)),
                    cursor.getString(cursor.getColumnIndex(SONG_ARTIST))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @Override
    public List<Playlist> getPlaylists() {

        String selectQuery = "SELECT  * FROM " + SONGS_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Playlist> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Playlist(
                        cursor.getInt(cursor.getColumnIndex(PLAYLIST_ID)),
                        cursor.getString(cursor.getColumnIndex(PLAYLIST_NAME))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) {
        String selectQuery = "SELECT " + SONGS_TABLE + "." + SONG_ID + " AS " + SONG_ID +
            ", " + SONGS_TABLE + "." + SONG_TITLE + " AS " + SONG_TITLE +
            ", " + SONGS_TABLE + "." + SONG_ARTIST + " AS " + SONG_ARTIST +
            " FROM " + SONGS_TABLE +
            " JOIN " + PLAYLISTS_SONGS_TABLE + " ON " + SONGS_TABLE + "." + SONG_ID + "=" + PLAYLISTS_SONGS_TABLE + "." + PS_SONG_ID +
            " WHERE " + PLAYLISTS_SONGS_TABLE + "." + PS_PLAYLIST_ID + "=" + playlistId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Song> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(new Song(
                        cursor.getInt(cursor.getColumnIndex(SONG_ID)),
                        cursor.getString(cursor.getColumnIndex(SONG_TITLE)),
                        cursor.getString(cursor.getColumnIndex(SONG_ARTIST))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    @Override
    public void addSong(Song song, int playlistId) {

        ContentValues values = new ContentValues();
        values.put(SONG_TITLE, song.Title);
        values.put(SONG_ARTIST, song.Artist);

        long newIndex = db.insert(SONGS_TABLE, "", values);

        db.execSQL("INSERT INTO "+ SONGS_TABLE +
                "(" + SONG_TITLE + ", " + SONG_ARTIST + ")" +
                " VALUES ('" + song.Title + ", " + song.Artist + "');");

        values = new ContentValues();
        values.put(PS_SONG_ID, newIndex);
        values.put(PS_PLAYLIST_ID, playlistId);
        db.insert(PLAYLISTS_SONGS_TABLE, "", values);
    }

    @Override
    public void addPlaylist(Playlist playlist) {

        ContentValues values = new ContentValues();
        values.put(PLAYLIST_NAME, playlist.Name);

        db.insert(PLAYLISTS_TABLE, "", values);
    }

    @Override
    public void deleteSongById() {

    }
}
