package com.example.vlad.player;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vlad.player.models.Song;
import com.example.vlad.player.utils.SongListAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaylistActivity extends ListActivity {
    private TextView textPlaylistId;
    private DialogFragment dlgSongInfo;

    private List<Song> songs = Arrays.asList(new Song(1, "Extreme ways", "Moby", ""),
        new Song(2, "Выхода нет", "Сплин", ""));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            int playlistId = extras.getInt("playlistId");
//            textPlaylistId.setText(String.valueOf(playlistId));
//        }

        setListAdapter(new SongListAdapter(this, songs));
        dlgSongInfo = new SongInfoFragment();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Bundle fragmentData = new Bundle();
        fragmentData.putInt("songId", songs.get(position).Id);
        dlgSongInfo.setArguments(fragmentData);
        dlgSongInfo.show(getFragmentManager(), "dlgSongInfo");
    }
}
