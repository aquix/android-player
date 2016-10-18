package com.example.vlad.player;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vlad.player.db.DbContext;
import com.example.vlad.player.models.Song;
import com.example.vlad.player.utils.OpenFileDialog;
import com.example.vlad.player.utils.SongListAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaylistActivity extends ListActivity implements OpenFileDialog.OpenDialogListener {
    private TextView textPlaylist;
    private Button btn;
    private DialogFragment dlgSongInfo;
    private OpenFileDialog openFileDialog;

    private int playlistId;

    private DbContext dbContext;

    private List<Song> songs = Arrays.asList(new Song(1, "Extreme ways", "Moby", ""),
        new Song(2, "Выхода нет", "Сплин", ""));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        dbContext = new DbContext(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.playlistId = extras.getInt("playlistId");
        }

        this.songs = this.dbContext.getSongsInPlaylist(this.playlistId);

        setListAdapter(new SongListAdapter(this, songs));
        dlgSongInfo = new SongInfoFragment();

        this.openFileDialog = new OpenFileDialog(this);
        this.openFileDialog.setOpenDialogListener(this);

        btn = (Button)findViewById(R.id.btnAddPlaylist);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileDialog.show();
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Bundle fragmentData = new Bundle();
        fragmentData.putInt("songId", songs.get(position).Id);
        dlgSongInfo.setArguments(fragmentData);
        dlgSongInfo.show(getFragmentManager(), "dlgSongInfo");
    }

    @Override
    public void OnSelectedFile(String fileName) {
        Song song = new Song("Anton", "Gena", fileName);
        dbContext.addSong(song, this.playlistId);
        this.updateSongList();
    }

    public void updateSongList() {
        this.songs = this.dbContext.getSongsInPlaylist(this.playlistId);
    }
}
