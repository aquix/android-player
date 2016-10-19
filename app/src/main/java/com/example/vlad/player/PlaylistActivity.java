package com.example.vlad.player;

import android.app.DialogFragment;
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

import java.util.Arrays;
import java.util.List;

public class PlaylistActivity extends ListActivity
        implements OpenFileDialog.OpenDialogListener, DeleteSongDialog.RemoveSongDialogListener {

    private Button btnAddSong;
    private OpenFileDialog openFileDialog;

    private List<Song> songs;
    private SongListAdapter adapter;

    private IDbContext dbContext;

    private int playlistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        btnAddSong = (Button) findViewById(R.id.btnAddSong);
        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileDialog.show();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.playlistId = extras.getInt("playlistId");
        }

        this.dbContext = new DbContext(getApplicationContext());

        // Initialize list
        this.songs = this.dbContext.getSongsInPlaylist(this.playlistId);
        this.adapter = new SongListAdapter(this, songs);
        setListAdapter(adapter);
        registerForContextMenu(getListView());

        // Initialize fragments
        this.openFileDialog = new OpenFileDialog(this);
        this.openFileDialog.setOpenDialogListener(this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SongInfoFragment dlgSongInfo = new SongInfoFragment();
        Bundle fragmentData = new Bundle();
        fragmentData.putInt("songId", songs.get(position).Id);
        dlgSongInfo.setArguments(fragmentData);
        dlgSongInfo.show(getFragmentManager(), "dlgSongInfo");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.song_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_song_item) {
            int index = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
            int songId = songs.get(index).Id;
            DeleteSongDialog deleteDialog = DeleteSongDialog.newInstance(songId);
            deleteDialog.show(getFragmentManager(), "deleteDialog");
        }

        return true;
    }

    @Override
    public void onSelectedFile(String fileName) {
        Song song = new Song("Anton", "Gena", fileName);
        this.dbContext.addSong(song, this.playlistId);
        this.updateSongList();
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
}
