package com.gm.bigwigg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PostActivity extends AppCompatActivity {

    ImageView comment_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        comment_btn = findViewById(R.id.comment_btn);

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, Comment_postActivity.class);
                startActivity(intent);
            }
        });
    }
}