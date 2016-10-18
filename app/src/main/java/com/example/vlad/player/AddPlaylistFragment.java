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
        public void onAddPlaylist(DialogFragment dialog);
    }

    AddPlaylistDialogListener listener;
    Button btnAddPlaylist;
    EditText txtNewPlaylistName;

    DbContext dbContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_playlist, container, false);

        btnAddPlaylist = (Button)view.findViewById(R.id.btnAddPlaylist);
        btnAddPlaylist.setOnClickListener(this);
        txtNewPlaylistName = (EditText)view.findViewById(R.id.txtNewPlaylistName);

        dbContext = new DbContext(getActivity().getApplicationContext());

        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (AddPlaylistDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnAddPlaylist:
                String newPlaylistName = txtNewPlaylistName.getText().toString();
                dbContext.addPlaylist(new Playlist(newPlaylistName));
                listener.onAddPlaylist(this);
                dismiss();
                break;
        }
    }
}
