package com.example.bigwigg.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bigwigg.Comment_postActivity;
import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.fragment.OtherProfileFragment;
import com.example.bigwigg.helper.ApiConfig;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.helper.Session;
import com.example.bigwigg.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Post> posts;
    Session session;
    ImageView delete;
    int totalstars = 0;

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
                sendToOtherProfileFragment(view,post.getUser_id(),post.getName(),post.getRole(),post.getDescription(),post.getProfile());

            }
        });


        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToOtherProfileFragment(view,post.getUser_id(),post.getName(),post.getRole(),post.getDescription(),post.getProfile());

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
        getRateUs(holder.rate,post.getUser_id(),post.getId(),post.getImage(),false);
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
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                String shareBody = "Here my Post "+post.getImage()+" \n Download BigWigg App Now";
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                activity.startActivity(Intent.createChooser(intent, "Share via"));
            }
        });

        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRateUs(holder.rate,post.getUser_id(),post.getId(),post.getImage(),true);

//                if (holder.rate.getTag().equals("fill"))
//                {
//                    int ratecount = Integer.parseInt(holder.tvRatecount.getText().toString()) - 1;
//                    holder.tvRatecount.setText(""+ratecount);
//
//                }
//                else {
//                    int ratecount = Integer.parseInt(holder.tvRatecount.getText().toString()) + 1;
//                    holder.tvRatecount.setText(""+ratecount);
//
//                }
//                RateToPost(holder.rate,session.getData(Constant.ID),post.getId());


            }
        });

        if (post.getUser_id().equals(session.getData(Constant.ID))){
            holder.delete.setVisibility(View.VISIBLE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openalertdialog(post.getId());

            }
        });
    }

    private void openalertdialog(String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Are you sure want to delete this post")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deletePost(id);

                    }
                })

        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void deletePost(String id) {

        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.POST_ID,id);
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("FAVOURITE_RESPONSE",response);

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
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
        }, activity, Constant.DELETE_POST_URL, params,true);


    }


    private void sendToOtherProfileFragment(View view, String user_id, String name, String role, String description, String profile)
    {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.USER_ID, user_id);
        bundle.putString(Constant.NAME, name);
        bundle.putString(Constant.ROLE, role);
        bundle.putString(Constant.DESCRIPION, description);
        bundle.putString(Constant.PROFILE, profile);
        OtherProfileFragment otherProfileFragment = new OtherProfileFragment();
        otherProfileFragment.setArguments(bundle);
        ((MainActivity)activity).SetBottomNavUnchecked();
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,otherProfileFragment,"OTHER_PROFILE" ).commit();

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

    private void getRateUs(ImageView rate, String user_id, String post_id, String image, boolean b) {
        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.USER_ID,session.getData(Constant.ID));
        params.put(Constant.POST_ID,post_id);
        ApiConfig.RequestToVolley((result, response) -> {

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        totalstars = Integer.parseInt(jsonObject.getString(Constant.TOTAL));
                        if(b){
                            //getRateUs(holder.rate,session.getData(Constant.ID),post.getId(),true);

                            ratedialog(image,rate,session.getData(Constant.ID),user_id,post_id);
                        }
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
    private void ratedialog(String image, ImageView rate, String id, String user_id ,String post_id) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.star_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView postimage = (ImageView) dialog.findViewById(R.id.postimage);
        Glide.with(activity).load(image).into(postimage);
        RatingBar star = (RatingBar) dialog.findViewById(R.id.star);
        Button ratenow = (Button) dialog.findViewById(R.id.ratenow);



        star.setRating(totalstars);

        ratenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                RateToPost(rate,id,post_id,String.valueOf(star.getRating()));
            }
        });



        dialog.show();

    }




    private void RateToPost(ImageView rate, String user_id, String post_id,String totalrating)
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,user_id);
        params.put(Constant.POST_ID,post_id);
        params.put(Constant.TOTAL,totalrating);
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

        final ImageView postimage,comment_btn,rate,favourite,share,delete;
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
            delete = itemView.findViewById(R.id.delete);
            rate = itemView.findViewById(R.id.rate);
            favourite = itemView.findViewById(R.id.favourite);
            tvRatecount = itemView.findViewById(R.id.tvRatecount);
            tvViewcomments = itemView.findViewById(R.id.tvViewcomments);


        }
    }
}

