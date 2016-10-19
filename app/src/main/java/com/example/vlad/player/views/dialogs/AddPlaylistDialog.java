package com.example.vlad.player.views.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.vlad.player.R;
import com.example.vlad.player.models.Playlist;

public class AddPlaylistDialog extends DialogFragment implements View.OnClickListener {
    public interface AddPlaylistDialogListener {
        public void onAddPlaylist(Playlist playlist);
    }
    AddPlaylistDialogListener listener;

    Button btnAddPlaylist;
    EditText txtNewPlaylistName;

    public static AddPlaylistDialog newInstance() {
        Bundle args = new Bundle();

        AddPlaylistDialog fragment = new AddPlaylistDialog();
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

    public void setOnAddPlaylistListener(AddPlaylistDialogListener listener) {
        this.listener = listener;
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
}
