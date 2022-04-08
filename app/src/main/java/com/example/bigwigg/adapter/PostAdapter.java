package com.example.bigwigg.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bigwigg.Comment_postActivity;
import com.example.bigwigg.LoginActivity;
import com.example.bigwigg.MainActivity;
import com.example.bigwigg.PostActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.fragment.OtherProfileFragment;
import com.example.bigwigg.fragment.PostFragment;
import com.example.bigwigg.helper.ApiConfig;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.helper.Session;
import com.example.bigwigg.model.Explore;
import com.example.bigwigg.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Post> posts;
    boolean rate_status = false;
    Session session;

    public PostAdapter(Activity activity, ArrayList<Post> posts) {
        this.activity = activity;
        this.posts = posts;
        session = new Session(activity);
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
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.USER_ID, post.getUser_id());
                bundle.putString(Constant.NAME, post.getName());
                bundle.putString(Constant.ROLE, post.getRole());
                bundle.putString(Constant.DESCRIPION, post.getDescription());
                bundle.putString(Constant.PROFILE, post.getProfile());
                OtherProfileFragment otherProfileFragment = new OtherProfileFragment();
                otherProfileFragment.setArguments(bundle);
                ((MainActivity)activity).SetBottomNavUnchecked();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,otherProfileFragment,"OTHER_PROFILE" ).commit();

            }
        });


        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.USER_ID, post.getUser_id());
                bundle.putString(Constant.NAME, post.getName());
                bundle.putString(Constant.ROLE, post.getRole());
                bundle.putString(Constant.DESCRIPION, post.getDescription());
                bundle.putString(Constant.PROFILE, post.getProfile());
                OtherProfileFragment otherProfileFragment = new OtherProfileFragment();
                otherProfileFragment.setArguments(bundle);
                ((MainActivity)activity).SetBottomNavUnchecked();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,otherProfileFragment,"OTHER_PROFILE" ).commit();

            }
        });

        holder.comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Comment_postActivity.class);
                intent.putExtra(Constant.POST_ID,post.getId());
                activity.startActivity(intent);
            }
        });
        getRateUs(holder.rate,session.getData(Constant.ID),post.getId());


        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RateToPost(holder.rate,session.getData(Constant.ID),post.getId());


            }
        });

    }

    private void getRateUs(ImageView rate, String user_id, String post_id) {
        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.USER_ID,user_id);
        params.put(Constant.POST_ID,post_id);
        ApiConfig.RequestToVolley((result, response) -> {

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        if (jsonObject.getString(Constant.STATUS).equals(Constant.TRUE)){
                            rate.setImageResource(R.drawable.ic_fill_star);

                            rate_status = true;
                        }
                        else {
                            rate.setImageResource(R.drawable.ic_ei_star);

                            rate_status = false;

                        }
                    }
                    else {
                        //Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }



            }
            else {
                Toast.makeText(activity, String.valueOf(response) +String.valueOf(result), Toast.LENGTH_SHORT).show();

            }
            //pass url
        }, activity, Constant.CHECK_POST_RATING_URL, params,true);


    }


    private void RateToPost(ImageView rate, String user_id, String post_id)
    {
        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.USER_ID,user_id);
        params.put(Constant.POST_ID,post_id);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        if (jsonObject.getString(Constant.STATUS).equals(Constant.TRUE)){
                            rate.setImageResource(R.drawable.ic_fill_star);

                            rate_status = true;
                        }
                        else {
                            rate.setImageResource(R.drawable.ic_ei_star);

                            rate_status = false;

                        }
                    }
                    else {
                        Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }



            }
            else {
                Toast.makeText(activity, String.valueOf(response) +String.valueOf(result), Toast.LENGTH_SHORT).show();

            }
            //pass url
        }, activity, Constant.RATING_URL, params,true);

    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        final ImageView postimage,comment_btn,rate;
        final CircleImageView profile;
        final TextView name,caption;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            postimage = itemView.findViewById(R.id.postimg);
            comment_btn = itemView.findViewById(R.id.comment_btn);
            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            caption = itemView.findViewById(R.id.caption);
            rate = itemView.findViewById(R.id.rate);


        }
    }
}

