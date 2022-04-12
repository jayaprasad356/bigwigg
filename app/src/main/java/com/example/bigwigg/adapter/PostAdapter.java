package com.example.bigwigg.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import java.io.File;
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
        holder.tvRatecount.setText(post.getRating_count());
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

         getFavourites(holder.favourite,session.getData(Constant.ID),post.getId());

         holder.tvViewcomments.setText(post.getComment_count()+" Comments");

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteToPost(holder.favourite,session.getData(Constant.ID),post.getId());


            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Create an ACTION_SEND Intent*/
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                String shareBody = "Here my Post "+post.getImage()+" \n Download BigWigg App Now";
                /*The type of the content is text, obviously.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                /*Fire!*/
                activity.startActivity(Intent.createChooser(intent, "Share via"));
            }
        });


        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.rate.getTag().equals("fill"))
                {
                    int ratecount = Integer.parseInt(holder.tvRatecount.getText().toString()) - 1;
                    holder.tvRatecount.setText(""+ratecount);

                }
                else {
                    int ratecount = Integer.parseInt(holder.tvRatecount.getText().toString()) + 1;
                    holder.tvRatecount.setText(""+ratecount);

                }



                RateToPost(holder.rate,session.getData(Constant.ID),post.getId());


            }
        });

    }

    private void getFavourites(ImageView favourite, String user_id, String post_id)
    {
        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.USER_ID,user_id);
        params.put(Constant.POST_ID,post_id);
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("FAVOURITE_RESPONSE",response);

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        if (jsonObject.getString(Constant.STATUS).equals(Constant.TRUE)){
                            favourite.setImageResource(R.drawable.ic_heart_fill);
                        }
                        else {
                            favourite.setImageResource(R.drawable.ic_heart);


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
        }, activity, Constant.CHECK_POST_FAVOURITE_URL, params,true);



    }

    private void FavouriteToPost(ImageView favourite, String user_id, String post_id)
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,user_id);
        params.put(Constant.POST_ID,post_id);
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("FAVOURITE_POST_RESPONSE",response);

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        if (jsonObject.getString(Constant.STATUS).equals(Constant.TRUE)){
                            favourite.setImageResource(R.drawable.ic_heart_fill);


                        }
                        else {
                            favourite.setImageResource(R.drawable.ic_heart);


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
        }, activity, Constant.SEND_FAVOURITE_URL, params,true);



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
                            rate.setTag("fill");
                            rate.setImageResource(R.drawable.ic_fill_star);
                        }
                        else {
                            rate.setTag("unfill");
                            rate.setImageResource(R.drawable.ic_ei_star);


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
                            rate.setTag("fill");

                        }
                        else {
                            rate.setImageResource(R.drawable.ic_ei_star);
                            rate.setTag("unfill");

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

        final ImageView postimage,comment_btn,rate,favourite,share;
        final CircleImageView profile;

        final TextView name,caption,tvRatecount,tvViewcomments;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            postimage = itemView.findViewById(R.id.postimg);
            comment_btn = itemView.findViewById(R.id.comment_btn);
            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            caption = itemView.findViewById(R.id.caption);
            share = itemView.findViewById(R.id.share);
            rate = itemView.findViewById(R.id.rate);
            favourite = itemView.findViewById(R.id.favourite);
            tvRatecount = itemView.findViewById(R.id.tvRatecount);
            tvViewcomments = itemView.findViewById(R.id.tvViewcomments);


        }
    }
}

