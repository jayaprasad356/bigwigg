package com.gm.bigwigg.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gm.bigwigg.PlayVideoActivity;
import com.gm.bigwigg.R;
import com.gm.bigwigg.model.Business;
import com.gm.bigwigg.model.Comment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Business> businesses;

    public BusinessAdapter(Activity activity, ArrayList<Business> businesses) {
        this.activity = activity;
        this.businesses = businesses;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.business_lyt, parent, false);
        return new ItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
        final ItemHolder holder = (ItemHolder) holderParent;
        final Business business = businesses.get(position);


        Glide.with(activity).load(business.getProfile()).into(holder.profile);
        holder.name.setText(business.getName());
        holder.caption.setText(business.getCaption());

        if (business.getVideo() != null ){
            holder.play.setVisibility(View.VISIBLE);
            Glide.with(activity).asBitmap().load(business.getVideo()).into(holder.postimg);
        }
        else if (business.getFile() != null ){
            holder.postimg.setImageResource(R.drawable.fileholder);

        }
        else {
            Glide.with(activity).load(business.getImage()).into(holder.postimg);

        }
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PlayVideoActivity.class);
                intent.putExtra("videoUrl",business.getVideo());
                activity.startActivity(intent);
            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=+91"+business.getMobile();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                activity.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        final TextView name,caption;
        final CircleImageView profile,chat;
        final ImageView postimg,play;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            chat = itemView.findViewById(R.id.chat);
            caption = itemView.findViewById(R.id.caption);
            postimg = itemView.findViewById(R.id.postimg);
            play = itemView.findViewById(R.id.play);


        }
    }
}

