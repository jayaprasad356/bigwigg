package com.example.bigwigg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

                    session.setBoolean("is_logged_in", true);
                    session.setUserData(String.valueOf(acct.getPhotoUrl()),acct.getDisplayName(), acct.getEmail());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(this, e -> Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }
}