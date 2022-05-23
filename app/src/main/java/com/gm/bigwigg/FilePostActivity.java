package com.gm.bigwigg;

import static com.gm.bigwigg.ImagePickerActivity.REQUEST_IMAGE_CAPTURE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FilePostActivity extends AppCompatActivity {
    String filePath = null;
    Activity activity;
    Session session;
    ImageView postimg;
    EditText caption;
    Button postbtn;
    public static final int SELECT_FILE = 110;
    Uri imageUri;
    boolean thumbnailexist = false;
    public static final int PICKFILE_REQUEST_CODE = 120;
    Button btnChooseFile;
    TextView file_uploded;
    Uri fileuri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_post);
        activity = FilePostActivity.this;
        session = new Session(activity);
        postimg = findViewById(R.id.postimg);
        caption = findViewById(R.id.caption);
        postbtn = findViewById(R.id.postbtn);
        file_uploded = findViewById(R.id.file_uploded);
        btnChooseFile = findViewById(R.id.btnChooseFile);

        postimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            }
        });
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thumbnailexist){
                    UploadFile(caption.getText().toString().trim(),"file");
                }
                else {
                    Toast.makeText(activity, "Upload Thumbnail", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    private String getfiletype(Uri videouri) {
        ContentResolver r = getContentResolver();
        // get the file type ,in this case its mp4
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(r.getType(videouri));
    }
    private void UploadFile(String caption, String type)
    {
        if (fileuri != null) {
            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            // save the selected video in Firebase storage
            final StorageReference reference = FirebaseStorage.getInstance().getReference("Files/" + System.currentTimeMillis() + "." + getfiletype(fileuri));
            reference.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    // get the link of video
                    String downloadUri = uriTask.getResult().toString();
                    sendDatattoDatabase(caption,downloadUri,type);
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(activity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    // show the progress bar
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
        else {
            Toast.makeText(activity, "File Should Uploaded", Toast.LENGTH_SHORT).show();
        }
    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    private void sendDatattoDatabase(String caption, String downloadUri, String type)
    {
        Map<String, String> params = new HashMap<>();
        Map<String, String> fileParams = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        params.put(Constant.TYPE, type);
        params.put(Constant.FILE, downloadUri);
        params.put(Constant.CAPTION, caption);
        fileParams.put(Constant.THUMBNAIL, filePath);
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("FILEPOST_RES",response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.UPLOAD_POST_URL,params,  fileParams);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICKFILE_REQUEST_CODE) {
                fileuri = data.getData();
                file_uploded.setVisibility(View.VISIBLE);
                btnChooseFile.setVisibility(View.GONE);


            }
            if (requestCode == SELECT_FILE) {

                imageUri = data.getData();
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setRequestedSize(300, 300)
                        .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        .setAspectRatio(1, 1)
                        .start(activity);
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setRequestedSize(300, 300)
                        .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        .setAspectRatio(1, 1)
                        .start(activity);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                assert result != null;
                filePath = result.getUriFilePath(activity, true);
                Glide.with(activity).load(filePath).into(postimg);
                thumbnailexist = true;


            }
        }
    }
}