package com.example.vlad.player;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlad.player.db.DbContext;
import com.example.vlad.player.db.IDbContext;
import com.example.vlad.player.models.Song;
import com.example.vlad.player.services.AlbumArtService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SongInfoFragment extends DialogFragment implements View.OnClickListener {
    private ImageView imageAlbumArt;
    private Button btnPrevSong;
    private Button btnNextSong;
    private TextView textTitle;
    private TextView textArtist;

    private ArrayList<Integer> songIds;
    private IDbContext sqliteContext;

    private Song currentSong;
    private int songIndex;
    private ArrayList<Song> songs;

    public static SongInfoFragment newInstance(ArrayList<Integer> songIds, int songIndex) {
        
        Bundle args = new Bundle();
        args.putIntegerArrayList("songIds", songIds);
        args.putInt("songIndex", songIndex);
        
        SongInfoFragment fragment = new SongInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_info, container, false);

        this.imageAlbumArt = (ImageView)view.findViewById(R.id.album_full_image);
        this.btnPrevSong = (Button)view.findViewById(R.id.btn_prev_song);
        this.btnPrevSong.setOnClickListener(this);
        this.btnNextSong = (Button)view.findViewById(R.id.btn_next_song);
        this.btnNextSong.setOnClickListener(this);
        this.textTitle = (TextView)view.findViewById(R.id.song_info_title);
        this.textArtist = (TextView)view.findViewById(R.id.song_info_artist);

        this.songIds = this.getArguments().getIntegerArrayList("songIds");
        this.songIndex = this.getArguments().getInt("songIndex");

        this.sqliteContext = new DbContext(this.getActivity().getApplicationContext());
        this.songs = this.sqliteContext.getSongsByIds(this.songIds);
        this.currentSong = this.songs.get(this.songIndex);
        this.renderView();
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.btn_prev_song:
            this.songIndex -= 1;
            if (this.songIndex < 0) {
                this.songIndex += this.songIds.size();
            }
            this.currentSong = this.songs.get(this.songIndex);
            this.renderView();
            break;
        case R.id.btn_next_song:
            this.songIndex += 1;
            if (this.songIndex >= this.songIds.size()) {
                this.songIndex -= this.songIds.size();
            }
            this.currentSong = this.songs.get(this.songIndex);
            this.renderView();
            break;
        }
    }

    private void renderView() {
        Context context = this.getActivity().getApplicationContext();

        String searchQuery = this.currentSong.Artist + " " + this.currentSong.Title;
        String url = AlbumArtService.getInstance().getAlbumArtUrl(searchQuery);
        Picasso.with(context)
                .load(url)
                .error(R.drawable.album_art_default)
                .into(this.imageAlbumArt);
        this.textTitle.setText(this.currentSong.Title);
        this.textArtist.setText(this.currentSong.Artist);
    }
}
