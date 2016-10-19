package com.example.vlad.player.views.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.vlad.player.R;

public class DeleteSongDialog extends DialogFragment implements View.OnClickListener {
    public interface RemoveSongDialogListener {
        public void onDeleteSong(int songId, boolean removeFromDisk);
    }
    private RemoveSongDialogListener listener;

    private Button btnYes;
    private Button btnNo;
    private CheckBox cbRemoveFromDisk;

    int songId;

    public static DeleteSongDialog newInstance(int songId) {

        Bundle args = new Bundle();
        args.putInt("songId", songId);

        DeleteSongDialog fragment = new DeleteSongDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnDeleteSongListener(RemoveSongDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            savedInstanceState = this.getArguments();
        }
        this.songId = savedInstanceState.getInt("songId");

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_delete_song, container, false);

        this.btnYes = (Button)view.findViewById(R.id.btnYes);
        this.btnYes.setOnClickListener(this);
        this.btnNo = (Button)view.findViewById(R.id.btnNo);
        this.btnNo.setOnClickListener(this);
        this.cbRemoveFromDisk = (CheckBox) view.findViewById(R.id.cbRemoveFromDisk);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.btnYes:
            boolean removeFromDisk = this.cbRemoveFromDisk.isChecked();
            this.listener.onDeleteSong(this.songId, removeFromDisk);
            this.dismiss();
            break;
        case R.id.btnNo:
            this.dismiss();
            break;
        }
    }
}
