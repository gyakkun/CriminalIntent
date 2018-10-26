package moe.nyamori.criminalintent;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;

import java.io.File;

public class PhotoDetailDialog extends DialogFragment {

    private static final String ARG_FILE = "file";

    public static PhotoDetailDialog newInstance(File photoFile) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILE, photoFile);

        PhotoDetailDialog dialog = new PhotoDetailDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        File photoFile = (File) getArguments().getSerializable(ARG_FILE);

        return  null;
    }
}
