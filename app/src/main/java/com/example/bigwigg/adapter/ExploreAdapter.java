package com.example.bigwigg.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.fragment.PostFragment;
import com.example.bigwigg.fragment.TestPostFragment;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.model.Explore;
import com.example.bigwigg.model.Post;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
                Bundle bundle = new Bundle();
                bundle.putString(Constant.USER_ID, explore.getId());
                PostFragment postFragment = new PostFragment();
                postFragment.setArguments(bundle);
                ((MainActivity)activity).SetBottomNavUnchecked();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,postFragment,"POST" ).commit();

            }
        });
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

