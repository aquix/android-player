package com.example.vlad.player;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.vlad.player.db.DbContext;
import com.example.vlad.player.models.Playlist;

public class AddPlaylistFragment extends DialogFragment implements View.OnClickListener {
    public interface AddPlaylistDialogListener {
        public void onAddPlaylist(Playlist playlist);
    }
    AddPlaylistDialogListener listener;

    Button btnAddPlaylist;
    EditText txtNewPlaylistName;

    public static AddPlaylistFragment newInstance() {
        Bundle args = new Bundle();

        AddPlaylistFragment fragment = new AddPlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_playlist, container, false);

        this.btnAddPlaylist = (Button)view.findViewById(R.id.btnAddPlaylist);
        this.btnAddPlaylist.setOnClickListener(this);
        this.txtNewPlaylistName = (EditText)view.findViewById(R.id.txtNewPlaylistName);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.btnAddPlaylist:
            String newPlaylistName = this.txtNewPlaylistName.getText().toString();
            Playlist newPlaylist = new Playlist(newPlaylistName);
            this.listener.onAddPlaylist(newPlaylist);
            this.dismiss();
            break;
        }
    }

    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            this.listener = (AddPlaylistDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AddPlaylistDialogListener");
        }
    }
}
