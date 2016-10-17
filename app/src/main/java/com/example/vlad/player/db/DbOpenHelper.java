package com.example.vlad.player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

import com.example.vlad.player.models.Playlist;
import com.example.vlad.player.models.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "playlist.db";
    private static final int SCHEMA = 1;


    private SQLiteDatabase db;

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        db.execSQL("PRAGMA foreign_keys = ON");

        db.execSQL("CREATE TABLE " + D.PLAYLISTS_TABLE + " (" +
                D.PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                D.PLAYLIST_NAME + " NVARCHAR(50) " +
                ");");

        db.execSQL("CREATE TABLE " + D.SONGS_TABLE + " (" +
                D.SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                D.SONG_ARTIST + " NVARCHAR(50), " +
                D.SONG_TITLE + " NVARCHAR(50), " +
                D.SONG_PATH + " NVARCHAR(50) " +
                ");");

        db.execSQL("CREATE TABLE " + D.PLAYLISTS_SONGS_TABLE + " (" +
                D.PS_PLAYLIST_ID + " INTEGER, " +
                D.PS_SONG_ID + " INTEGER, " +
                "PRIMARY KEY (" + D.PS_PLAYLIST_ID + ", " + D.PS_SONG_ID + "), " +
                "FOREIGN KEY (" + D.PS_PLAYLIST_ID + ") REFERENCES " + D.PLAYLISTS_TABLE + "(" + D.PLAYLIST_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY (" + D.PS_SONG_ID + ") REFERENCES " + D.SONGS_TABLE + "(" + D.SONG_ID + ") ON DELETE CASCADE " +
                ");");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ D.PLAYLISTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ D.SONGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ D.PLAYLISTS_SONGS_TABLE);
        onCreate(db);
    }
}
