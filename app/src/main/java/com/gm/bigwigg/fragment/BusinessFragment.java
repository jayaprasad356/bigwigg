package com.gm.bigwigg.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gm.bigwigg.MainActivity;
import com.gm.bigwigg.R;
import com.gm.bigwigg.adapter.BusinessAdapter;
import com.gm.bigwigg.adapter.NotificationAdapter;
import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;
import com.gm.bigwigg.model.Business;
import com.gm.bigwigg.model.Notification;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BusinessFragment extends Fragment {

    View root;
    public static Activity activity;
    public static RecyclerView recyclerView;
    public static BusinessAdapter businessAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    Session session;
    String UserID;



    public BusinessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_business, container, false);
        ((MainActivity)getActivity()).setBusinessChecked();
        recyclerView = root.findViewById(R.id.recyclerView);
        activity = getActivity();
        session = new Session(activity);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeLayout);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            UserID = bundle.getString(Constant.USER_ID);

        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                businessList();
            }
        });
        businessList();



        return  root;


    }

    private void businessList()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("BUSINESSFRAGMENTLIST",response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        businessRead();
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<Business> businesses = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                Business group = g.fromJson(jsonObject1.toString(), Business.class);
                                businesses.add(group);
                            } else {
                                break;
                            }
                        }

                        businessAdapter = new BusinessAdapter(activity, businesses);
                        recyclerView.setAdapter(businessAdapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.LIST_BUSINESS_URL, params, true);
    }

    private void businessRead()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        session.setData(Constant.BUSINESS_COUNT,jsonObject.getString(Constant.BUSINESS_COUNT));
                        ((MainActivity)getActivity()).removeBusinessBadge();

                    }
                    else {
                        //Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }



            }
            else {

            }
            //pass url
        }, activity, Constant.BUSINESS_READ_COUNT_URL, params,true);

    }
}