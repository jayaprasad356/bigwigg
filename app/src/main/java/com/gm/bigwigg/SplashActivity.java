package com.gm.bigwigg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.gm.bigwigg.helper.Session;

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


    }

    @Override
    protected void onStart() {
        super.onStart();
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
        },3000);
    }
}