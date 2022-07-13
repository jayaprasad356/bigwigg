package com.gm.bigwigg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.gm.bigwigg.helper.Session;

public class PrivacyActivity extends AppCompatActivity {
    WebView webView;
    Button btnTerms;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        session = new Session(PrivacyActivity.this);
        webView = findViewById(R.id.webView);
        btnTerms = findViewById(R.id.btnTerms);
        webView.loadUrl("https://bigwigg.app/admin/privacy-policy.html");

        btnTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setData("privacy","seen");
                Intent intent = new Intent(PrivacyActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}