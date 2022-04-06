package com.example.bigwigg;

import static com.example.bigwigg.ImagePickerActivity.REQUEST_IMAGE_CAPTURE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.bigwigg.fragment.BusinessFragment;
import com.example.bigwigg.fragment.ExploreFragment;
import com.example.bigwigg.fragment.FavouriteFragment;
import com.example.bigwigg.fragment.NotificationFragment;
import com.example.bigwigg.fragment.PostFragment;
import com.example.bigwigg.fragment.ProfileFragment;
import com.example.bigwigg.fragment.SettingsFragment;
import com.example.bigwigg.fragment.TestExploreFragment;
import com.example.bigwigg.fragment.TestPostFragment;
import com.example.bigwigg.helper.ApiConfig;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.helper.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    ExploreFragment exploreFragment;
    BusinessFragment businessFragment;
    FavouriteFragment favouriteFragment;
    NotificationFragment notificationFragment;
    CircleImageView Profile;
    ImageButton Video;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    boolean doubleBackToExitPressedOnce = false;
    FloatingActionButton Post;
    public static final int REQUEST_IMAGE = 100;
    Bitmap bitmap;
    Activity activity;
    Session session;
    public static final int SELECT_FILE = 110;
    Uri imageUri;
    String filePath = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;
        session = new Session(activity);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Post = findViewById(R.id.post);
        Profile = findViewById(R.id.profile_image);
        Video = findViewById(R.id.video);

        bottomNavigationView.setSelectedItemId(R.id.explore);
        bottomNavigationView.setOnItemSelectedListener(this);
        exploreFragment = new ExploreFragment();
        businessFragment = new BusinessFragment();
        favouriteFragment = new FavouriteFragment();
        notificationFragment = new NotificationFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, exploreFragment,"EXPLORE").commit();
        setProfileImage();
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(activity)
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

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();


            }
        });
        Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoFragment videoFragment = new VideoFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, videoFragment,TAG_FRAGMENT).addToBackStack("my_fragment").commit();

                SetBottomNavUnchecked();


            }
        });
    }

    public void setProfileImage() {
        Glide.with(activity).load(Uri.parse(session.getData(Constant.PROFILE))).into(Profile);

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
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
    private void launchCameraIntent() {
        Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
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

    private void launchGalleryIntent() {
        Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        TextView profile = bottomSheetDialog.findViewById(R.id.profile);
        TextView settings = bottomSheetDialog.findViewById(R.id.settings);
        TextView search = bottomSheetDialog.findViewById(R.id.search);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, profileFragment).addToBackStack("my_fragment").commit();

                SetBottomNavUnchecked();
                bottomSheetDialog.dismiss();

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment settingsFragment = new SettingsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, settingsFragment).addToBackStack("my_fragment").commit();

                SetBottomNavUnchecked();
                bottomSheetDialog.dismiss();

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();

            }
        });



        bottomSheetDialog.show();
    }
    private void showPostBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.post_sheet);
        ImageView postimg = bottomSheetDialog.findViewById(R.id.postimg);
        Button postbtn = bottomSheetDialog.findViewById(R.id.postbtn);
        TextView caption = bottomSheetDialog.findViewById(R.id.caption);
        Glide.with(activity).load(filePath).into(postimg);

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                UploadPost(caption.getText().toString().trim());

            }
        });

        bottomSheetDialog.show();

    }
    public void SetBottomNavUnchecked() {
        bottomNavigationView.getMenu().findItem(R.id.placeholder).setChecked(true);
    }
    public void setExploreChecked() {
        bottomNavigationView.getMenu().findItem(R.id.explore).setChecked(true);
    }
    public void setBusinessChecked() {
        bottomNavigationView.getMenu().findItem(R.id.business).setChecked(true);
    }
    public void setFavouriteChecked() {
        bottomNavigationView.getMenu().findItem(R.id.favourite).setChecked(true);
    }
    public void setNotificationChecked() {
        bottomNavigationView.getMenu().findItem(R.id.notification).setChecked(true);
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.explore:
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, exploreFragment,"EXPLORE").commit();
                return true;

            case R.id.business:
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,businessFragment,"BUSINESS" ).commit();
                return true;



            case R.id.favourite:
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,favouriteFragment,"FAVOURITE").commit();
                return true;


            case R.id.notification:

                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,notificationFragment,"NOTIFICATION").commit();
                return true;
        }

        return false;
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else{

            ExploreFragment exploreFragment = (ExploreFragment) getSupportFragmentManager().findFragmentByTag("EXPLORE");
            BusinessFragment businessFragment = (BusinessFragment) getSupportFragmentManager().findFragmentByTag("BUSINESS");
            FavouriteFragment favouriteFragment = (FavouriteFragment) getSupportFragmentManager().findFragmentByTag("FAVOURITE");
            NotificationFragment notificationFragment = (NotificationFragment) getSupportFragmentManager().findFragmentByTag("NOTIFICATION");
            PostFragment postFragment = (PostFragment) getSupportFragmentManager().findFragmentByTag("POST");
            if ((exploreFragment != null && exploreFragment.isVisible()) || (businessFragment != null && businessFragment.isVisible()) || (favouriteFragment != null && favouriteFragment.isVisible()) || (notificationFragment != null && notificationFragment.isVisible()) || (postFragment != null && postFragment.isVisible())) {
                if (doubleBackToExitPressedOnce) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
            else{
                super.onBackPressed();

            }

        }
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
                        .start(activity);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setOutputCompressQuality(90)
                        .setRequestedSize(300, 300)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setAspectRatio(1, 1)
                        .start(activity);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                assert result != null;
                filePath = result.getUriFilePath(activity, true);
                showPostBottomSheetDialog();



            }
        }
    }

    private void UploadPost(String caption)
    {
        Map<String, String> params = new HashMap<>();
        Map<String, String> fileParams = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        fileParams.put(Constant.IMAGE, filePath);
        params.put(Constant.CAPTION, caption);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.UPLOAD_POST_URL, params, fileParams);

    }

}