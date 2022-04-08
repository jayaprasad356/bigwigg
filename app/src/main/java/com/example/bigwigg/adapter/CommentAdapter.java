package com.example.bigwigg.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.fragment.PostFragment;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.model.Comment;
import com.example.bigwigg.model.Explore;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Comment> comments;

    public CommentAdapter(Activity activity, ArrayList<Comment> comments) {
        this.activity = activity;
        this.comments = comments;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.cmt_lyt, parent, false);
        return new ItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
        final ItemHolder holder = (ItemHolder) holderParent;
        final Comment comment = comments.get(position);

        Glide.with(activity).load(comment.getProfile()).into(holder.profile);
        holder.name.setText(comment.getName());
        holder.message.setText(comment.getMessage());


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        final ImageView profile;
        final TextView name,message;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);


        }
    }
}

