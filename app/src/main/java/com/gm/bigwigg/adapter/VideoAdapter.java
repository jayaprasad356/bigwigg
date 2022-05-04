package com.gm.bigwigg.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.artjimlop.altex.AltexImageDownloader;
import com.bumptech.glide.Glide;
import com.gm.bigwigg.Comment_postActivity;
import com.gm.bigwigg.MainActivity;
import com.gm.bigwigg.PlayVideoActivity;
import com.gm.bigwigg.R;
import com.gm.bigwigg.fragment.OtherProfileFragment;
import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;
import com.gm.bigwigg.model.Post;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Post> posts;
    Session session;
    ImageView delete;
    int totalstars = 0;

    public VideoAdapter(Activity activity, ArrayList<Post> posts) {
        this.activity = activity;
        this.posts = posts;
        session = new Session(activity);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.video_lyt, parent, false);
        return new ItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, @SuppressLint("RecyclerView") int position) {
        final ItemHolder holder = (ItemHolder) holderParent;
        final Post post = posts.get(position);

        Glide.with(activity).asBitmap().load(post.getVideo()).into(holder.postimg);
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PlayVideoActivity.class);
                intent.putExtra("videoUrl",post.getVideo());
                activity.startActivity(intent);
            }
        });


    }






    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        final ImageView postimg,comment,rate,rupee,menu,play;
        final TextView name,tvRatecount;
        CircleImageView profile;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            postimg = itemView.findViewById(R.id.postimg);
            comment = itemView.findViewById(R.id.comment_btn);
            rate = itemView.findViewById(R.id.rate);
            rupee = itemView.findViewById(R.id.rupee);
            menu = itemView.findViewById(R.id.menu);
            name = itemView.findViewById(R.id.name);
            tvRatecount = itemView.findViewById(R.id.tvRatecount);
            profile = itemView.findViewById(R.id.profile);
            play = itemView.findViewById(R.id.play);



        }
    }
}

