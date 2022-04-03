package com.example.bigwigg.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.bigwigg.LoginActivity;
import com.example.bigwigg.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class Session {
    public static final String PREFER_NAME = "bigwigg";
    final int PRIVATE_MODE = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _activity;
    GoogleSignInClient mSignInClient;


    public Session(Context activity) {
        try {
            this._activity = activity;
            pref = _activity.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
            editor = pref.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void setData(String id, String val) {
        editor.putString(id, val);
        editor.commit();
    }

    public void setBoolean(String id, boolean val) {
        editor.putBoolean(id, val);
        editor.commit();
    }
    public void setUserData(String id,String profile, String name, String email) {
        editor.putString(Constant.ID, id);
        editor.putString(Constant.NAME, name);
        editor.putString(Constant.EMAIL, email);
        editor.putString(Constant.PROFILE, profile);
        editor.commit();
    }
    public String getData(String id) {
        return pref.getString(id, "");
    }


    public void logoutUser(Activity activity) {
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.webclient))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(activity,gso);
        editor.clear();
        editor.commit();

        new Session(_activity).setBoolean("is_logged_in", false);
        firebaseAuth.signOut();
        mSignInClient.signOut().addOnCompleteListener(activity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(activity, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(i);
                        activity.finish();
                    }
                });
    }
    public boolean getBoolean(String id) {
        return pref.getBoolean(id, false);
    }
}