package moe.nyamori.arenaofvalor;

import android.app.Activity;
import android.content.ContentResolver;
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
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private ContentResolver mResolver;

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

        mDateButton = (Button) v.findViewById(R.id.hero_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mHero.getDate());
                dialog.setTargetFragment(HeroFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.hero_solved);
        mSolvedCheckBox.setChecked(mHero.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mHero.setSolved(isChecked);
            }
        });

        mTitleField = (EditText) v.findViewById(R.id.hero_title);
        mTitleField.setText(mHero.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
                mHero.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        mSuspectButton = (Button) v.findViewById(R.id.hero_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mHero.getSuspect() != null) {
            mSuspectButton.setText(mHero.getSuspect());
        }

        //Check if system has a responsible app to contact activity
        final PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                packageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.hero_camera);
        //final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //final Intent selectImageFromGallery = new Intent(Intent.ACTION_PICK);
        final Intent captureImage = new Intent(Intent.ACTION_PICK);
        captureImage.setType("image/*");

        //Check if system has a camera app or other photo provider
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
//
//                List<ResolveInfo> cameraActivities = getActivity()
//                        .getPackageManager().queryIntentActivities(captureImage,
//                                PackageManager.MATCH_DEFAULT_ONLY);
//
//                for (ResolveInfo activity : cameraActivities) {
//                    getActivity().grantUriPermission(activity.activityInfo.packageName,
//                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                }

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
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mHero.setDate(date);
            updateDate();
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
                mHero.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = data.getData();
//            Uri uri = FileProvider.getUriForFile(getActivity(),
//                    "moe.nyamori.arenaofvalor.fileprovider",
//                    mPhotoFile);

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

//        MenuItem menuItem = menu.findItem(R.id.delete_hero);
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

    private void updateDate() {
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        mDateButton.setText(dateFormat.format(mHero.getDate()));
    }

    private String getHeroReport() {
        String solvedString = null;
        if (mHero.isSolved()) {
            solvedString = getString(R.string.hero_report_solved);
        } else {
            solvedString = getString(R.string.hero_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mHero.getDate()).toString();

        String suspect = mHero.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.hero_report_no_suspect);
        } else {
            suspect = getString(R.string.hero_report_suspect, suspect);
        }

        String report = getString(R.string.hero_report,
                mHero.getTitle(), dateString, solvedString, suspect);

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

