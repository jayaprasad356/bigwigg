package com.example.bigwigg.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.SinglePostActivity;
import com.example.bigwigg.fragment.PostFragment;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.helper.Session;
import com.example.bigwigg.model.Explore;
import com.example.bigwigg.model.Post;

import java.util.ArrayList;

public class ProfilePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Post> posts;
    Session session;

    public ProfilePostAdapter(Activity activity, ArrayList<Post> posts) {
        this.activity = activity;
        this.posts = posts;
        session = new Session(activity);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.profile_post_lyt, parent, false);
        return new ItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
        final ItemHolder holder = (ItemHolder) holderParent;
        final Post post = posts.get(position);

        Glide.with(activity).load(post.getImage()).into(holder.postimg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SinglePostActivity.class);
                intent.putExtra(Constant.USER_ID,post.getUser_id());
                intent.putExtra(Constant.POST_ID,post.getId());
                activity.startActivity(intent);
//                Bundle bundle = new Bundle();
//                bundle.putString(Constant.USER_ID, session.getData(Constant.ID));
//                PostFragment postFragment = new PostFragment();
//                postFragment.setArguments(bundle);
//                ((MainActivity)activity).SetBottomNavUnchecked();
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,postFragment,"POST" ).commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        final ImageView postimg;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            postimg = itemView.findViewById(R.id.postimg);
        }
    }
}

