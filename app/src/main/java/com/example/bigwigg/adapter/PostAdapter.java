package com.example.bigwigg.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bigwigg.Comment_postActivity;
import com.example.bigwigg.PostActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.model.Explore;
import com.example.bigwigg.model.Post;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Post> posts;

    public PostAdapter(Activity activity, ArrayList<Post> posts) {
        this.activity = activity;
        this.posts = posts;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.post_lyt, parent, false);
        return new ItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
        final ItemHolder holder = (ItemHolder) holderParent;
        final Post post = posts.get(position);

        Glide.with(activity).load(post.getImage()).into(holder.postimage);
        Glide.with(activity).load(post.getProfile()).into(holder.profile);
        holder.name.setText(post.getName());
        holder.caption.setText(post.getCaption());

        holder.comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Comment_postActivity.class);
                activity.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        final ImageView postimage,comment_btn;
        final CircleImageView profile;
        final TextView name,caption;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            postimage = itemView.findViewById(R.id.postimg);
            comment_btn = itemView.findViewById(R.id.comment_btn);
            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            caption = itemView.findViewById(R.id.caption);


        }
    }
}

