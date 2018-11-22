package moe.nyamori.arenaofvalor;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class PhotoDetailDialog extends DialogFragment {

    private static final String ARG_FILE = "file";
    private ImageView mImageView;

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

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_photo_detail, null);

        mImageView = (ImageView) view.findViewById(R.id.photo_detail);

        Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(),
                getActivity());
        mImageView.setImageBitmap(bitmap);

        final Dialog dialog =  new Dialog(getActivity(), R.style.SingleImageDialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
