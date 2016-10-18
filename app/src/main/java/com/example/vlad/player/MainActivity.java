package com.example.vlad.player;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements AddPlaylistFragment.AddPlaylistDialogListener {
    private MainActivity activity;
    private List<Playlist> playlists;
    private ListView lvPlaylists;
    private ArrayAdapter<Playlist> adapter;
    private AddPlaylistFragment dlgAddPlaylist;


    DbContext dbContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = this;
        setContentView(R.layout.activity_main);

        this.dbContext = new DbContext(this);
        this.playlists = dbContext.getPlaylists();
        this.lvPlaylists = (ListView)findViewById(R.id.playlists);

        this.adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, playlists);

        this.lvPlaylists.setAdapter(adapter);
        this.lvPlaylists.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Intent intent = new Intent(activity, PlaylistActivity.class);
                intent.putExtra("playlistId", playlists.get(position).Id);
                startActivity(intent);
            }
        });

        this.dlgAddPlaylist = new AddPlaylistFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playlists_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_playlist) {
            this.dlgAddPlaylist.show(getFragmentManager(), "dlgAddPlaylist");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddPlaylist(DialogFragment dialog) {
        this.playlists = dbContext.getPlaylists();
        this.adapter.clear();
        this.adapter.addAll(this.playlists);
        this.adapter.notifyDataSetChanged();
    }
}
