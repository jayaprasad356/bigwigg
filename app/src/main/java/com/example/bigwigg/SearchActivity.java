package com.example.bigwigg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bigwigg.R;
import com.example.bigwigg.adapter.PostAdapter;
import com.example.bigwigg.helper.ApiConfig;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.helper.Session;
import com.example.bigwigg.model.Post;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    public static Activity activity;
    public static RecyclerView recyclerView;
    public static PostAdapter postAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    Session session;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        activity = SearchActivity.this;
        session = new Session(activity);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postList();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!etSearch.getText().toString().equals("")){
                    postList();

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    private void postList()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.SEARCH, etSearch.getText().toString().trim());
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
                                posts.add(group);
                            } else {
                                break;
                            }
                        }

                        postAdapter = new PostAdapter(activity, posts);
                        recyclerView.setAdapter(postAdapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    else {
                        Toast.makeText(SearchActivity.this, ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SearchActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.SEARCH_LIST_URL, params, true);
    }
}