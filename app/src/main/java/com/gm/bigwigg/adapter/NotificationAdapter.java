package com.gm.bigwigg.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gm.bigwigg.R;
import com.gm.bigwigg.SinglePostActivity;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.model.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Notification> notifications;

    public NotificationAdapter(Activity activity, ArrayList<Notification> notifications) {
        this.activity = activity;
        this.notifications = notifications;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.notify_layout, parent, false);
        return new ItemHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
        final ItemHolder holder = (ItemHolder) holderParent;
        final Notification notification = notifications.get(position);
        holder.title.setText(notification.getTitle());
        holder.time.setText(notification.getDate_created());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SinglePostActivity.class);
                intent.putExtra(Constant.USER_ID,notification.getUser_id());
                if (notification.getVideo() != null ){
                    intent.putExtra(Constant.TYPE,"video");

                }
                else {
                    intent.putExtra(Constant.TYPE,"image");


                }
                intent.putExtra(Constant.POST_ID,notification.getNotify_post_id());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        final TextView title,time;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
        }
    }
}

