package com.gm.bigwigg.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gm.bigwigg.MainActivity;
import com.gm.bigwigg.R;
import com.gm.bigwigg.adapter.PostAdapter;
import com.gm.bigwigg.adapter.ProfilePostAdapter;
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

public class ProfileFragment extends Fragment {
    TextView Name,Role,Description;
    View root;
    Session session;
    ImageView Profile;
    public static RecyclerView recyclerView;
    public static ProfilePostAdapter profilePostAdapter;
    TextView follow,post_count,followers_count,following_count,videoes_count,tVRating;

    Activity activity;
    public static PostAdapter postAdapter;
    RelativeLayout videoes_rl;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_profile, container, false);
        Name = root.findViewById(R.id.name);
        Role = root.findViewById(R.id.role);
        Description = root.findViewById(R.id.description);
        Profile = root.findViewById(R.id.profile);
        recyclerView = root.findViewById(R.id.recyclerView);
        follow = root.findViewById(R.id.follow);
        post_count = root.findViewById(R.id.post_count);
        followers_count = root.findViewById(R.id.followers_count);
        following_count = root.findViewById(R.id.following_count);
        videoes_count = root.findViewById(R.id.videoes_count);
        videoes_rl = root.findViewById(R.id.videoes_rl);
        tVRating = root.findViewById(R.id.tVRating);

        activity = getActivity();

        session = new Session(activity);
        Name.setText(session.getData(Constant.NAME));
        Role.setText(session.getData(Constant.ROLE));
        Description.setText(session.getData(Constant.DESCRIPION));

        Glide.with(getActivity()).load(Uri.parse(session.getData(Constant.PROFILE))).into(Profile);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity,3);
        recyclerView.setLayoutManager(gridLayoutManager);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
//        recyclerView.setLayoutManager(linearLayoutManager);
        postList();
        //mypostList();
        getUserDetailsCount();

        videoes_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.USER_ID, session.getData(Constant.ID));
                VideoFragment videoFragment = new VideoFragment();
                videoFragment.setArguments(bundle);
                ((MainActivity)activity).SetBottomNavUnchecked();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,videoFragment,"VIDEO" ).addToBackStack("my_fragment").commit();

            }
        });



        return root;
    }

    private void mypostList()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
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

                        postAdapter = new PostAdapter(activity, posts,"image");
                        recyclerView.setAdapter(postAdapter);

//                        Log.d("POSTFRAGMENT_RESPONSE",""+recyclerView.getAdapter().getItemCount());
//                        //recyclerView.getLayoutManager().scrollToPosition(4);
                    }
                    else {
                        Toast.makeText(getActivity(), ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.POST_LIST_URL, params, true);
    }

    private void getUserDetailsCount()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        post_count.setText(jsonObject.getString(Constant.POST_COUNT));
                        followers_count.setText(jsonObject.getString(Constant.FOLLOWERS_COUNT));
                        following_count.setText(jsonObject.getString(Constant.FOLLOWING_COUNT));
                        videoes_count.setText(jsonObject.getString(Constant.VIDEOES_COUNT));
                        tVRating.setText(jsonObject.getString(Constant.RATING_COUNT));

                    }
                    else {
                        Toast.makeText(getActivity(), ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.USER_DETAILS_COUNT_URL, params, true);

    }
    private void postList()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
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

                        profilePostAdapter = new ProfilePostAdapter(activity, posts);
                        recyclerView.setAdapter(profilePostAdapter);
                    }
                    else {
                        Toast.makeText(getActivity(), ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.POST_LIST_URL, params, true);
    }
}