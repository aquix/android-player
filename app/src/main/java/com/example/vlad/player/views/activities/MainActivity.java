package com.example.vlad.player.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vlad.player.views.dialogs.AddPlaylistDialog;
import com.example.vlad.player.R;
import com.example.vlad.player.db.DbContext;
import com.example.vlad.player.db.IDbContext;
import com.example.vlad.player.models.Playlist;
import com.example.vlad.player.models.Song;
import com.example.vlad.player.services.LastSongsService;
import com.example.vlad.player.utils.SongListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddPlaylistDialog.AddPlaylistDialogListener {
    private MainActivity activity;

    private ListView lvPlaylists;
    private ListView lvLastOpenedSongs;

    private List<Playlist> playlists;
    private ArrayAdapter<Playlist> playlistsAdapter;

    private List<Song> lastOpenedSongs;
    private SongListAdapter lastOpenedSongsAdapter;

    private IDbContext db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        this.setContentView(R.layout.activity_main);

        this.db = new DbContext(this);

        this.playlists = this.db.getPlaylists();
        this.playlistsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, this.playlists);

        this.lastOpenedSongs = new ArrayList<>();
        this.lastOpenedSongsAdapter = new SongListAdapter(this, this.lastOpenedSongs);

        // Initialize ListView
        this.lvPlaylists = (ListView)this.findViewById(R.id.playlists);
        this.registerForContextMenu(this.lvPlaylists);
        this.lvPlaylists.setAdapter(this.playlistsAdapter);
        this.lvPlaylists.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Intent intent = new Intent(MainActivity.this.activity, PlaylistActivity.class);
                intent.putExtra("playlistId", MainActivity.this.playlists.get(position).Id);
                MainActivity.this.startActivity(intent);
            }
        });

        this.lvLastOpenedSongs = (ListView)this.findViewById(R.id.lastOpenedSongs);
        this.lvLastOpenedSongs.setAdapter(this.lastOpenedSongsAdapter);
    }

    @Override
    protected void onResume() {
        this.updateLastOpenedList();
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.playlist_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_playlist_item) {
            int index = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
            int playlistId = this.playlists.get(index).Id;
            this.db.deletePlaylist(playlistId);
            this.playlists = this.db.getPlaylists();
            this.reloadList();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.playlists_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_playlist) {
            AddPlaylistDialog dlgAddPlaylist = AddPlaylistDialog.newInstance();
            dlgAddPlaylist.setOnAddPlaylistListener(this);
            dlgAddPlaylist.show(this.getFragmentManager(), "dlgAddPlaylist");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddPlaylist(Playlist playlist) {
        this.db.addPlaylist(playlist);
        this.playlists = this.db.getPlaylists();
        this.reloadList();
    }

    private void reloadList() {
        this.playlistsAdapter.clear();
        this.playlistsAdapter.addAll(this.playlists);
        this.playlistsAdapter.notifyDataSetChanged();
    }

    private void updateLastOpenedList() {
        this.lastOpenedSongs = LastSongsService.getInstance().getSongs();
        this.lastOpenedSongsAdapter.clear();
        this.lastOpenedSongsAdapter.addAll(this.lastOpenedSongs);
        this.lastOpenedSongsAdapter.notifyDataSetChanged();
    }
}
