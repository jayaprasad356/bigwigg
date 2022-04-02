package com.example.bigwigg.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bigwigg.ImagePickerActivity;
import com.example.bigwigg.LoginActivity;
import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.SplashActivity;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.helper.Session;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;


public class SettingsFragment extends Fragment {

    ImageButton logout;
    Session session;
    TextView changeprofile;
    ImageView profileimg;
    public static final int REQUEST_IMAGE = 100;


    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_settings, container, false);
        session = new Session(getActivity());

        logout=rootview.findViewById(R.id.logout_btn);
        changeprofile=rootview.findViewById(R.id.changeprofile);
        profileimg=rootview.findViewById(R.id.profileimg);

        profileimg.setImageURI(Uri.parse(session.getData(Constant.PROFILE)));
        Glide.with(getActivity()).load(Uri.parse(session.getData(Constant.PROFILE))).into(profileimg);

        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(getActivity())
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.logoutUser(getActivity());


            }
        });




        return rootview;
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE){
            if (resultCode == Activity.RESULT_OK){
                Uri uri = data.getParcelableExtra("path");
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                    profileimg.setImageBitmap(bitmap);



                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }
}