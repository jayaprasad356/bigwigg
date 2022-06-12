package com.gm.bigwigg.fragment;

import static android.app.Activity.RESULT_OK;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.gm.bigwigg.MainActivity;
import com.gm.bigwigg.R;
import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SettingsFragment extends Fragment {

    ImageButton logout,update_btn;
    Session session;
    TextView changeprofile,describtion,role;
    ImageView profileimg;

    EditText email,fullname;
    public static final int REQUEST_IMAGE = 100;
    String filePath = null;
    public static final int SELECT_FILE = 110;
    Uri imageUri;
    String currentPhotoPath;
    public static final int REQUEST_IMAGE_CAPTURE = 111;


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
        update_btn=rootview.findViewById(R.id.update_btn);
        role=rootview.findViewById(R.id.role);
        describtion=rootview.findViewById(R.id.describtion);
        fullname=rootview.findViewById(R.id.fullname);
        fullname.setText(session.getData(Constant.NAME));
        email=rootview.findViewById(R.id.email);
        email.setText(session.getData(Constant.EMAIL));
        describtion.setText(session.getData(Constant.DESCRIPION));
        role.setText(session.getData(Constant.ROLE));
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullname.getText().toString().equals("")){
                    fullname.setError("Name is Empty");
                    fullname.requestFocus();
                }
                else {
                    update_profile();

                }



            }
        });


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
                                    //showImagePickerOptions();
                                    //selectDialog();
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, SELECT_FILE);
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
//        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
//            @Override
//            public void onTakeCameraSelected() {
//                launchCameraIntent();
//            }
//
//            @Override
//            public void onChooseGallerySelected() {
//                launchGalleryIntent();
//            }
//        });
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "ECART_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public void selectDialog() {
        final CharSequence[] items = {getString(R.string.from_library), getString(R.string.from_camera), getString(R.string.cancel)};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(getString(R.string.from_library))) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            } else if (items[item].equals(getString(R.string.from_camera))) {
                dispatchTakePictureIntent();
            } else if (items[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_FILE) {

                imageUri = data.getData();
                CropImage.activity(imageUri)
                       .setGuidelines(CropImageView.Guidelines.ON)
                        .setOutputCompressQuality(90)
                        .setRequestedSize(300, 300)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setAspectRatio(1, 1)
                        .start(getContext(), this);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setOutputCompressQuality(90)
                        .setRequestedSize(300, 300)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setAspectRatio(1, 1)
                        .start(getContext(), this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                assert result != null;
                filePath = result.getUriFilePath(getActivity(), true);
                UpdateProfile();
            }
        }
    }

    private void UpdateProfile()
    {
        Map<String, String> params = new HashMap<>();
        Map<String, String> fileParams = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        fileParams.put(Constant.PROFILE, filePath);
        params.put(Constant.TYPE, Constant.UPLOAD_PROFILE);

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);

                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        Glide.with(getActivity()).load(jsonArray.getJSONObject(0).getString(Constant.PROFILE)).into(profileimg);
                        Toast.makeText(getActivity(), jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        session.setData(Constant.PROFILE, jsonArray.getJSONObject(0).getString(Constant.PROFILE));
                        ((MainActivity)getActivity()).setProfileImage();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), Constant.UPDATE_PROFILE_URL, params, fileParams);

    }


    private void uploadprofile()
    {
        Map<String, String> params = new HashMap<>();
        Map<String, String> fileParams = new HashMap<>();
        params.put(Constant.USER_ID, "1");
        fileParams.put(Constant.PROFILE, "/data/user/0/com.gm.blackkite/cache/temp_file_20220403_224105.jpg");
        params.put(Constant.TYPE, Constant.UPLOAD_PROFILE);
        ApiConfig.RequestToVolley((result, response) -> {
            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getActivity(), "Hi"+jsonObject, Toast.LENGTH_SHORT).show();
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        //session.setData(Constant.PROFILE);
                        Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getActivity(), ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }



            }
            else {
                Toast.makeText(getActivity(), String.valueOf(response) +String.valueOf(result), Toast.LENGTH_SHORT).show();

            }
        }, getActivity(), "http://192.168.43.38/bigwigg/api/update_profile.php", params,fileParams);


    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    private void update_profile() {
        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.EMAIL,email.getText().toString());
        params.put(Constant.NAME, fullname.getText().toString());
        params.put(Constant.TYPE, "register");
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        params.put(Constant.ROLE, role.getText().toString());
        params.put(Constant.DESCRIPION, describtion.getText().toString());
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);

                        session.setBoolean("is_logged_in", true);
                        session.setUserData(jsonArray.getJSONObject(0).getString(Constant.ID),session.getData(Constant.PROFILE),jsonArray.getJSONObject(0).getString(Constant.NAME), jsonArray.getJSONObject(0).getString(Constant.EMAIL));
                        session.setData(Constant.DESCRIPION,jsonArray.getJSONObject(0).getString(Constant.DESCRIPION));
                        session.setData(Constant.ROLE,jsonArray.getJSONObject(0).getString(Constant.ROLE));
                        Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), ""+jsonObject, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }



            }
            else {
                Toast.makeText(getActivity(), String.valueOf(response) +String.valueOf(result), Toast.LENGTH_SHORT).show();

            }
            //pass url
        },getActivity(), Constant.UPDATE_PROFILE_URL, params,true);



    }

}