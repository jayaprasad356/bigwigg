package com.example.bigwigg.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bigwigg.R;
import com.example.bigwigg.adapter.ExploreAdapter;
import com.example.bigwigg.helper.ApiConfig;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.model.Explore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestExploreFragment extends Fragment {
    View root;

    public static Activity activity;
    public static RecyclerView recyclerView;
    public static ExploreAdapter exploreAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public TestExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_test_explore, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        activity = getActivity();
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeLayout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        exploreList();



        return root;
    }
    private void exploreList()
    {
        Map<String, String> params = new HashMap<>();
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
                        Toast.makeText(getActivity(), ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.USER_LIST_URL, params, true);
    }
}