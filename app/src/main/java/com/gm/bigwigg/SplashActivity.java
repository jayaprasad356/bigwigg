package com.gm.bigwigg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    ImageView Logo;
    Handler handler;
    Animation animFadeIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        Logo = findViewById(R.id.logo);
        animFadeIn = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.fade_in);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Logo.startAnimation(myFadeInAnimation);

        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                updateAlertDialog();
            }
            else {

                GotoActivity();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                GotoActivity();

            }
        });



    }
    private void updateAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yep! Here you can see a new update ");
        builder.setMessage(
                "We are working hard for your better experience " +
                        "\n" +
                        "So, Update it and enter in a new and updated version " +
                        "\n");
        builder.setCancelable(false);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    private void GotoActivity()
    {
        Map<String, String> params = new HashMap<>();
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Intent intent=new Intent(SplashActivity.this,TestLoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        Session session = new Session(SplashActivity.this);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (session.getBoolean("is_logged_in")){
                                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                }



                            }
                        },2000);

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }



            }
            else {
                Toast.makeText(this, String.valueOf(response) +String.valueOf(result), Toast.LENGTH_SHORT).show();

            }
            //pass url
        }, SplashActivity.this, Constant.LOGIN_TEST__URL, params,false);




    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}