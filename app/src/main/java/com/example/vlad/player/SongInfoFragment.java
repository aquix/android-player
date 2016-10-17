package com.example.vlad.player;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlad.player.models.Song;
import com.example.vlad.player.services.AlbumArtService;
import com.squareup.picasso.Picasso;


public class SongInfoFragment extends DialogFragment implements View.OnClickListener {
    private ImageView imageAlbumArt;
    private Button btnPrevSong;
    private Button btnNextSong;
    private TextView textTitle;
    private TextView textArtist;

    private Song song;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_info, container, false);


        imageAlbumArt = (ImageView)view.findViewById(R.id.album_full_image);
        btnPrevSong = (Button)view.findViewById(R.id.btn_prev_song);
        btnNextSong = (Button)view.findViewById(R.id.btn_next_song);
        textTitle = (TextView)view.findViewById(R.id.song_info_title);
        textArtist = (TextView)view.findViewById(R.id.song_info_artist);

        btnNextSong.setOnClickListener(this);
        btnPrevSong.setOnClickListener(this);

        song = new Song(1, "Title", "Author", "");
        renderView();
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.btn_prev_song:
            song.Id--;
            song.Title = song.Title + "2";
            renderView();
            break;
        case R.id.btn_next_song:
            song.Id++;
            song.Title = song.Title + "1";
            renderView();
            break;
        }
    }

    private void renderView() {
        Context context = getActivity().getApplicationContext();

        String searchQuery = song.Artist + " " + song.Title;
        String url = AlbumArtService.getInstance().getAlbumArtUrl(searchQuery);
        Picasso.with(context)
                .load(url)
                .error(R.drawable.album_art_default)
                .into(imageAlbumArt);
        textTitle.setText(song.Title);
        textArtist.setText(song.Artist);
    }
}
