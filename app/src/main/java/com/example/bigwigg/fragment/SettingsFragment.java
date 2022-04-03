package com.example.bigwigg.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bigwigg.ImagePickerActivity;
import com.example.bigwigg.LoginActivity;
import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.SplashActivity;
import com.example.bigwigg.helper.ApiConfig;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.helper.Session;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

                update_profile();

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
                    Bitmap lastBitmap = null;
                    lastBitmap = bitmap;
                    filePath = getStringImage(lastBitmap);
                    uploadprofile();



                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadprofile()
    {
        Map<String, String> params = new HashMap<>();
        Map<String, String> fileParams = new HashMap<>();
        params.put(Constant.USER_ID, "1");
        params.put(Constant.TYPE, "upload_profile");
        fileParams.put(Constant.PROFILE, filePath);
        ApiConfig.RequestToVolley((result, response) -> {
            Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        //session.setData(Constant.PROFILE);
                        Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getActivity(), ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }



            }
            else {
                Toast.makeText(getActivity(), String.valueOf(response) +String.valueOf(result), Toast.LENGTH_SHORT).show();

            }
        }, getActivity(), Constant.UPDATE_PROFILE_URL, params,fileParams);


    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

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