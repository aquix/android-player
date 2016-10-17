package com.example.vlad.player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vlad.player.db.DbContext;
import com.example.vlad.player.models.Playlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MainActivity activity;
    List<Playlist> playlists;
    ListView lvPlaylists;

    DbContext dbContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Replace with Db data
        dbContext = new DbContext(this);
        playlists = dbContext.getPlaylists();
        lvPlaylists = (ListView)findViewById(R.id.playlists);

        ArrayAdapter<Playlist> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, playlists);

        lvPlaylists.setAdapter(adapter);
        lvPlaylists.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Intent intent = new Intent(activity, PlaylistActivity.class);
                intent.putExtra("playlistId", playlists.get(position).Id);
                startActivity(intent);
            }
        });
    }
}
