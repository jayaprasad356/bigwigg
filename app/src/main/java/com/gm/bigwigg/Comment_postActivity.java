package com.gm.bigwigg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gm.bigwigg.adapter.CommentAdapter;
import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;
import com.gm.bigwigg.model.Comment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Comment_postActivity extends AppCompatActivity {

    ImageView backArrow;
    EditText etComment;
    ImageView Send;
    public static RecyclerView recyclerView;
    public static CommentAdapter commentAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    Activity activity;
    String PostID;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_post);

        PostID = getIntent().getStringExtra(Constant.POST_ID);
        activity = Comment_postActivity.this;
        etComment = findViewById(R.id.etComment);
        Send = findViewById(R.id.send);
        recyclerView = findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        backArrow = findViewById(R.id.backArrow);
        session = new Session(activity);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etComment.getText().toString().trim().equals("")){
                    Toast.makeText(Comment_postActivity.this, "Enter Comment", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendComment();
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentList();
            }
        });
        commentList();
    }

    private void commentList()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.POST_ID, PostID);
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("COMMENT_RESPONSE",response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<Comment> comments = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                Comment group = g.fromJson(jsonObject1.toString(), Comment.class);
                                comments.add(group);
                            } else {
                                break;
                            }
                        }

                        commentAdapter = new CommentAdapter(activity, comments);
                        recyclerView.setAdapter(commentAdapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    else {
                        //Toast.makeText(activity, ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.LIST_COMMENT_URL, params, true);

    }

    private void sendComment()
    {
        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.POST_ID,PostID);
        params.put(Constant.USER_ID,session.getData(Constant.ID));
        params.put(Constant.MESSAGE,etComment.getText().toString().trim());

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        commentList();
                        etComment.getText().clear();
                    }
                    else {
                        Toast.makeText(this, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();

                }

            }
            else {
                Toast.makeText(this, String.valueOf(response) +String.valueOf(result), Toast.LENGTH_SHORT).show();

            }
            //pass url
        }, activity, Constant.SEND_COMMENT_URL, params,true);


    }
}