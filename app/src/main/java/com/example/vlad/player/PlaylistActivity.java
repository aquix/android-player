package com.example.vlad.player;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.vlad.player.db.DbContext;
import com.example.vlad.player.db.IDbContext;
import com.example.vlad.player.models.Song;
import com.example.vlad.player.utils.OpenFileDialog;
import com.example.vlad.player.utils.SongListAdapter;
import com.example.vlad.player.utils.SongsFactory;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends ListActivity
        implements OpenFileDialog.OpenDialogListener, DeleteSongDialog.RemoveSongDialogListener,
        View.OnClickListener {

    private Button btnAddSong;

    private List<Song> songs;
    private SongListAdapter adapter;

    private IDbContext dbContext;

    private int playlistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_playlist);

        this.btnAddSong = (Button)this.findViewById(R.id.btnAddSong);
        this.btnAddSong.setOnClickListener(this);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.playlistId = extras.getInt("playlistId");
        }

        this.dbContext = new DbContext(this.getApplicationContext());

        // Initialize list
        this.songs = this.dbContext.getSongsInPlaylist(this.playlistId);
        this.adapter = new SongListAdapter(this, this.songs);
        this.setListAdapter(this.adapter);
        this.registerForContextMenu(this.getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ArrayList<Integer> songIds = new ArrayList<>(this.songs.size());
        for (Song song : this.songs) {
            songIds.add(song.Id);
        }

        SongInfoFragment dlgSongInfo = SongInfoFragment.newInstance(songIds, position);
        dlgSongInfo.show(this.getFragmentManager(), "dlgSongInfo");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.song_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_song_item) {
            int index = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
            int songId = this.songs.get(index).Id;
            DeleteSongDialog deleteDialog = DeleteSongDialog.newInstance(songId);
            deleteDialog.show(this.getFragmentManager(), "deleteDialog");
        }

        return true;
    }

    @Override
    public void onSelectedFile(String fileName) {
        Song song = SongsFactory.createFromPath(fileName);
        this.dbContext.addSong(song, this.playlistId);
        this.updateSongList();
        this.updateListView();
    }

    @Override
    public void onDeleteSong(int songId, boolean removeFromDisk) {
        this.dbContext.deleteSong(songId);
        this.updateSongList();
        this.updateListView();
    }

    private void updateSongList() {
        this.songs = this.dbContext.getSongsInPlaylist(this.playlistId);
    }

    private void updateListView() {
        this.adapter.clear();
        this.adapter.addAll(this.songs);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnAddSong) {
            OpenFileDialog openFileDialog = new OpenFileDialog(this);
            openFileDialog.setOpenDialogListener(this);
            openFileDialog.show();
        }
    }
}
