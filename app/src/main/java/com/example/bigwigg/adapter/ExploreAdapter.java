package com.example.bigwigg.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.fragment.PostFragment;
import com.example.bigwigg.helper.ApiConfig;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.model.Explore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExploreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Explore> explores;

    public ExploreAdapter(Activity activity, ArrayList<Explore> explores) {
        this.activity = activity;
        this.explores = explores;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.explore_lyt, parent, false);
        return new ExploreItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
        final ExploreItemHolder holder = (ExploreItemHolder) holderParent;
        final Explore explore = explores.get(position);

        Glide.with(activity).load(explore.getProfile()).into(holder.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkpost(explore.getId(),view);
            }
        });
    }

    private void checkpost(String id, View view) {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, id);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        if (jsonObject.getString(Constant.POST_COUNT).equals("0")  ){

                        }

                    }
                    else {
                        Toast.makeText(activity, ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.USER_DETAILS_COUNT_URL, params, true);


        Bundle bundle = new Bundle();
        bundle.putString(Constant.USER_ID, id);
        PostFragment postFragment = new PostFragment();
        postFragment.setArguments(bundle);
        ((MainActivity)activity).SetBottomNavUnchecked();
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,postFragment,"POST" ).addToBackStack("my_fragment").commit();
    }

    @Override
    public int getItemCount() {
        return explores.size();
    }

    static class ExploreItemHolder extends RecyclerView.ViewHolder {

        final ImageView profile;
        public ExploreItemHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);


        }
    }
}

