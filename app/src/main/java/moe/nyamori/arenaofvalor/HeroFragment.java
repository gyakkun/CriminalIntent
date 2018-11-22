package moe.nyamori.arenaofvalor;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class HeroFragment extends Fragment {

    private static final String ARG_CRIME_ID = "hero_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_PHOTO = "DialogPhoto";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;


    private Hero mHero;
    private File mPhotoFile;

    private EditText mNameField;
    private EditText mNicknameField;
    private EditText mPositionField;

    private CheckBox mStarCheckbox;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    private TextView mLivenessValue;
    private TextView mAttackValue;
    private TextView mAffectionValue;
    private TextView mHardnessValue;

    private SeekBar mLivenessSeekbar;
    private SeekBar mAttackSeekbar;
    private SeekBar mAffectionSeekbar;
    private SeekBar mHardnessSeekbar;

    public static HeroFragment newInstance(UUID heroId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, heroId);

        HeroFragment fragment = new HeroFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        mHero = new Hero();
        UUID heroId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mHero = HeroLab.get(getActivity()).getHero(heroId);
        mPhotoFile = HeroLab.get(getActivity()).getPhotoFile(mHero);
    }

    @Override
    public void onPause() {
        super.onPause();

        HeroLab.get(getContext())
                .updateHero(mHero);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_hero, container, false);

        mStarCheckbox = (CheckBox) v.findViewById(R.id.hero_starred);
        mStarCheckbox.setChecked(mHero.isStarred());
        mStarCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mHero.setStarred(isChecked);
            }
        });

        mNameField = (EditText) v.findViewById(R.id.hero_title);
        mNameField.setText(mHero.getName());
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
                mHero.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mNicknameField = (EditText) v.findViewById(R.id.hero_argv1);
        mNicknameField.setText(mHero.getNickname());
        mNicknameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
                mHero.setNickname(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPositionField = (EditText) v.findViewById(R.id.hero_argv2);
        mPositionField.setText(mHero.getPosition());
        mPositionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
                mHero.setPosition(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // Start Seekbar and Value Textview part
        mAffectionValue = (TextView) v.findViewById(R.id.hero_affection_value);
        mAffectionValue.setText(String.valueOf(mHero.getAffection()));
        mAffectionSeekbar = (SeekBar) v.findViewById(R.id.hero_affection);
        mAffectionSeekbar.setProgress(mHero.getAffection());
        mAffectionSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                mAffectionValue.setText(String.valueOf(value));
                mHero.setAffection(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mLivenessValue = (TextView) v.findViewById(R.id.hero_liveness_value);
        mLivenessValue.setText(String.valueOf(mHero.getLiveness()));
        mLivenessSeekbar = (SeekBar) v.findViewById(R.id.hero_liveness);
        mLivenessSeekbar.setProgress(mHero.getLiveness());
        mLivenessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                mLivenessValue.setText(String.valueOf(value));
                mHero.setLiveness(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mAttackValue = (TextView) v.findViewById(R.id.hero_attack_value);
        mAttackValue.setText(String.valueOf(mHero.getAttack()));
        mAttackSeekbar = (SeekBar) v.findViewById(R.id.hero_attack);
        mAttackSeekbar.setProgress(mHero.getAttack());
        mAttackSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                mAttackValue.setText(String.valueOf(value));
                mHero.setAttack(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mReportButton = (Button) v.findViewById(R.id.hero_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getHeroReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.hero_report_subject));
                //Always choose an activity to launch.
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);


        //Check if system has a responsible app to contact activity

        mPhotoButton = (ImageButton) v.findViewById(R.id.hero_camera);
        final Intent captureImage = new Intent(Intent.ACTION_PICK);
        captureImage.setType("image/*");

        //Check if system has a camera app or other photo provider
        final PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "moe.nyamori.arenaofvalor.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.hero_photo);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoFile == null || !mPhotoFile.exists()) {
                    return;
                } else {
                    FragmentManager manager = getFragmentManager();
                    PhotoDetailDialog dialog = PhotoDetailDialog.newInstance(mPhotoFile);
                    dialog.setTargetFragment(HeroFragment.this, REQUEST_PHOTO);
                    dialog.show(manager, DIALOG_PHOTO);
                }
            }
        });
        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else if (resultCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();

            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            try {

                if (c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();
                String suspect = c.getString(0);
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = data.getData();

            try {
                // 使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                String imgName = mPhotoFile.getName();
                savePhotoToSDCard("/data/data/" + getContext().getPackageName() + "/files", imgName, photo);
                if (photo != null) {
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_hero, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_hero:
                HeroLab.get(getContext()).deleteHero(mHero);
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private String getHeroReport() {
        String solvedString = null;
        if (mHero.isStarred()) {
            solvedString = getString(R.string.hero_report_solved);
        } else {
            solvedString = getString(R.string.hero_report_unsolved);
        }


        String report = getString(R.string.hero_report,
                mHero.getName(), solvedString, "hero");

        return report;

    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }


    private void savePhotoToSDCard(String path, String photoName, Bitmap photoBitmap) {
        FileOutputStream fileOutputStream = null;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File photoFile = new File(path, photoName);
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null) {
                if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
        } catch (FileNotFoundException e) {
            photoFile.delete();
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

