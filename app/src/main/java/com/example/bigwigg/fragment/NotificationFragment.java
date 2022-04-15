package com.example.bigwigg.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.adapter.NotificationAdapter;
import com.example.bigwigg.adapter.PostAdapter;
import com.example.bigwigg.helper.ApiConfig;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.helper.Session;
import com.example.bigwigg.model.Notification;
import com.example.bigwigg.model.Post;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NotificationFragment extends Fragment {
    View root;
    public static Activity activity;
    public static RecyclerView recyclerView;
    public static NotificationAdapter notificationAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    Session session;
    String UserID;



    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_notification, container, false);
        ((MainActivity)getActivity()).setNotificationChecked();
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
                notifyList();
            }
        });
        notifyList();


        return root;
    }
    private void notifyList()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        notificationRead();
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<Notification> notifications = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                Notification group = g.fromJson(jsonObject1.toString(), Notification.class);
                                notifications.add(group);
                            } else {
                                break;
                            }
                        }

                        notificationAdapter = new NotificationAdapter(activity, notifications);
                        recyclerView.setAdapter(notificationAdapter);
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
        }, activity, Constant.LIST_NOTIFICATIONS_URL, params, true);
    }
    private void notificationRead()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        session.setData(Constant.NOTIFICATIONS_COUNT,jsonObject.getString(Constant.NOTIFICATIONS_COUNT));
                        ((MainActivity)getActivity()).removeBadge();

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
        }, activity, Constant.NOTIFICATIONS_READ_COUNT_URL, params,true);
    }
}