package com.example.bigwigg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bigwigg.helper.ApiConfig;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.helper.Session;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button googlebtn;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient mSignInClient;
    private static int RC_SIGN_IN = 100;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googlebtn = findViewById(R.id.googlebtn);
        firebaseAuth = FirebaseAuth.getInstance();
        session = new Session(LoginActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.webclient))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this,gso);
        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  signIn();
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    public void googleicon(View view) {
//        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("LOGIN", "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                  registerEmail(acct.getDisplayName(),acct.getEmail(),acct.getPhotoUrl());

                })
                .addOnFailureListener(this, e -> Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }


    private void registerEmail(String name, String email, Uri profile) {
        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.EMAIL,email);
        params.put(Constant.NAME,name);
        params.put(Constant.PROFILE,String.valueOf(profile));

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);


                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        session.setBoolean("is_logged_in", true);
                        session.setUserData(jsonArray.getJSONObject(0).getString(Constant.ID),jsonArray.getJSONObject(0).getString(Constant.PROFILE),jsonArray.getJSONObject(0).getString(Constant.NAME), jsonArray.getJSONObject(0).getString(Constant.EMAIL));

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(this, "Failed"+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }



            }
            else {
                Toast.makeText(this, String.valueOf(response) +String.valueOf(result), Toast.LENGTH_SHORT).show();

            }
            //pass url
        }, LoginActivity.this, Constant.EMAIL_REGISTER_URL, params,true);



    }

}