package com.gm.bigwigg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.gm.bigwigg.adapter.PostAdapter;
import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;
import com.gm.bigwigg.model.Post;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SinglePostActivity extends AppCompatActivity {
    public static Activity activity;
    public static RecyclerView recyclerView;
    public static PostAdapter postAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    Session session;
    String UserID;
    String PostID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        recyclerView = findViewById(R.id.recyclerView);
        activity = SinglePostActivity.this;
        session = new Session(activity);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        UserID = getIntent().getStringExtra(Constant.USER_ID);
        PostID = getIntent().getStringExtra(Constant.POST_ID);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postList();
            }
        });
        postList();


    }
    private void postList()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, UserID);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<Post> posts = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                Post group = g.fromJson(jsonObject1.toString(), Post.class);
                                if(group.getId().equals(PostID)){
                                    posts.add(group);

                                }

                            } else {
                                break;
                            }
                        }

                        postAdapter = new PostAdapter(activity, posts);
                        recyclerView.setAdapter(postAdapter);

//                        Log.d("POSTFRAGMENT_RESPONSE",""+recyclerView.getAdapter().getItemCount());
//                        //recyclerView.getLayoutManager().scrollToPosition(4);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    else {
                        Toast.makeText(activity, ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.POST_LIST_URL, params, true);
    }
}