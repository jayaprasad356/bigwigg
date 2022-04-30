package com.gm.bigwigg.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.gm.bigwigg.R;
import com.gm.bigwigg.adapter.ExploreAdapter;
import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;
import com.gm.bigwigg.model.Explore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchFragment extends Fragment {
    public static Activity activity;
    public static RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    Session session;
    EditText etSearch;
    View root;
    public static ExploreAdapter exploreAdapter;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        etSearch = root.findViewById(R.id.etSearch);
        activity = getActivity();
        session = new Session(activity);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeLayout);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userList();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!etSearch.getText().toString().equals("")){
                    userList();

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return root;
    }
    private void userList()
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
                        ArrayList<Explore> explores = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                Explore group = g.fromJson(jsonObject1.toString(), Explore.class);
                                explores.add(group);
                            } else {
                                break;
                            }
                        }

                        exploreAdapter = new ExploreAdapter(activity, explores);
                        recyclerView.setAdapter(exploreAdapter);
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
        }, activity, Constant.SEARCH_LIST_URL, params, true);
    }
}