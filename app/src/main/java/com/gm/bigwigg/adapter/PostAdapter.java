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
import android.widget.EditText;
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
import com.gm.bigwigg.ReportActivity;
import com.gm.bigwigg.fragment.OtherProfileFragment;
import com.gm.bigwigg.helper.ApiConfig;
import com.gm.bigwigg.helper.Constant;
import com.gm.bigwigg.helper.Session;
import com.gm.bigwigg.model.Post;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    String type;
    int totalstars = 0;
    String fileUri = "";

    public PostAdapter(Activity activity, ArrayList<Post> posts, String type) {
        this.activity = activity;
        this.posts = posts;
        this.type = type;
        session = new Session(activity);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (type.equals("image")){
            view = LayoutInflater.from(activity).inflate(R.layout.post_lyt, parent, false);

        }else {
            view = LayoutInflater.from(activity).inflate(R.layout.video_lyt, parent, false);

        }

        return new ItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, @SuppressLint("RecyclerView") int position) {
        final ItemHolder holder = (ItemHolder) holderParent;
        final Post post = posts.get(position);
        if (post.getUser_id().equals(session.getData(Constant.ID))){
            holder.rate.setVisibility(View.GONE);
        }


        Glide.with(activity).load(post.getProfile()).into(holder.profile);
        holder.name.setText(post.getName());
        holder.caption.setText(post.getCaption());
        holder.tvRatecount.setText(post.getRating_count());
        if (post.getVideo() != null ){
            fileUri = post.getVideo();
            holder.play.setVisibility(View.VISIBLE);
            Glide.with(activity).asBitmap().load(post.getVideo()).into(holder.postimage);

        }
        else if (post.getFile() != null){
            fileUri = post.getFile();
            Glide.with(activity).load(post.getThumbnail()).into(holder.postimage);

            holder.postimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = post.getFile();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    activity.startActivity(i);

                }
            });


        }
        else {
            fileUri = post.getImage();
            holder.play.setVisibility(View.GONE);
            Glide.with(activity).load(post.getImage()).into(holder.postimage);
        }
        holder.rupee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.business_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                EditText etMobile = (EditText) dialog.findViewById(R.id.etMobile);
                Button interestedBtn = (Button) dialog.findViewById(R.id.interestedBtn);
                interestedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etMobile.getText().toString().equals("")){
                            etMobile.setError("empty");
                        }
                        else if (etMobile.getText().length() != 10){
                            etMobile.setError("Invalid");
                        }
                        else {
                            interetToBusiness(post.getId(),etMobile.getText().toString().trim(),dialog);
                        }

                    }
                });
                dialog.show();
            }
        });
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PlayVideoActivity.class);
                intent.putExtra("videoUrl",post.getVideo());
                activity.startActivity(intent);
            }
        });
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
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
                bottomSheetDialog.setContentView(R.layout.post_menu_layout);

                LinearLayout delete = (LinearLayout) bottomSheetDialog.findViewById(R.id.delete);
                ImageView cancel = (ImageView) bottomSheetDialog.findViewById(R.id.cancel);
                LinearLayout share = (LinearLayout) bottomSheetDialog.findViewById(R.id.share);
                LinearLayout download = (LinearLayout) bottomSheetDialog.findViewById(R.id.download);
                LinearLayout report = (LinearLayout) bottomSheetDialog.findViewById(R.id.report);

                if (post.getUser_id().equals(session.getData(Constant.ID))){
                    delete.setVisibility(View.VISIBLE);
                }
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deletePost(post.getId(),position);
                        bottomSheetDialog.dismiss();

                    }
                });
                report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity,ReportActivity.class);
                        intent.putExtra(Constant.POST_ID,post.getId());
                        activity.startActivity(intent);

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        if (post.getVideo() != null){
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            String shareBody = "Here my Video "+post.getVideo()+" \n Download BigWigg App Now";
                            intent.setType("text/plain");
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            activity.startActivity(Intent.createChooser(intent, "Share via"));

                        }else if (post.getFile() != null){
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            String shareBody = "Here my File "+post.getFile()+" \n Download BigWigg App Now";
                            intent.setType("text/plain");
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            activity.startActivity(Intent.createChooser(intent, "Share via"));

                        }else {
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            String shareBody = "Here my Post "+post.getImage()+" \n Download BigWigg App Now";
                            intent.setType("text/plain");
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            activity.startActivity(Intent.createChooser(intent, "Share via"));

                        }

                    }
                });
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (post.getVideo() != null){
                            AltexImageDownloader.writeToDisk(activity, post.getVideo(), "Bigwigg");


                        }else if (post.getFile() != null){
                            AltexImageDownloader.writeToDisk(activity, post.getFile(), "Bigwigg");


                        }else {
                            AltexImageDownloader.writeToDisk(activity, post.getImage(), "Bigwigg");

                        }

                        bottomSheetDialog.dismiss();
                        Toast.makeText(activity, "Downloading...", Toast.LENGTH_SHORT).show();

                    }
                });




                bottomSheetDialog.show();
            }
        });
        getRateUs(holder.rate,post.getUser_id(),post.getId(),post.getImage(),false, post.getVideo());
        getFavourites(holder.favourite,session.getData(Constant.ID),post.getId());
        if (post.getVideo() != null ){
            holder.tvViewcomments.setText(post.getComment_count());

        }
        else {
            holder.tvViewcomments.setText(post.getComment_count()+" Comments");
        }

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteToPost(holder.favourite,session.getData(Constant.ID),post.getId());


            }
        });


        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRateUs(holder.rate,post.getUser_id(),post.getId(),post.getImage(),true,post.getVideo());

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


    }

    private void interetToBusiness(String post_id, String mobile, Dialog dialog) {
        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.POST_ID,post_id);
        params.put(Constant.MOBILE,mobile);
        params.put(Constant.USER_ID,session.getData(Constant.ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        dialog.dismiss();
                        Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else {

            }
            //pass url
        }, activity, Constant.ADD_BUSINESS_URL, params,true);



    }


    private void deletePost(String id, int position) {

        Map<String, String> params = new HashMap<>();
        //request
        params.put(Constant.POST_ID,id);
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("FAVOURITE_RESPONSE",response);

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
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

                } catch (JSONException e){
                    e.printStackTrace();
                }
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

            //pass url
        }, activity, Constant.SEND_FAVOURITE_URL, params,true);



    }

    private void getRateUs(ImageView rate, String user_id, String post_id, String image, boolean b, String video) {
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

                            ratedialog(image,rate,session.getData(Constant.ID),user_id,post_id,video);
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

            //pass url
        }, activity, Constant.CHECK_POST_RATING_URL, params,true);


    }
    private void ratedialog(String image, ImageView rate, String id, String user_id ,String post_id,String video) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.star_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView postimage = (ImageView) dialog.findViewById(R.id.postimage);

        if (video != null ){
            postimage.setVisibility(View.GONE);
        }
        if (image == null || image.equals("")){
            postimage.setVisibility(View.GONE);
        }
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

            //pass url
        }, activity, Constant.RATING_URL, params,true);

    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        final ImageView postimage,comment_btn,rate,favourite,menu,play,rupee;
        final CircleImageView profile;

        final TextView name,caption,tvRatecount,tvViewcomments;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            postimage = itemView.findViewById(R.id.postimg);
            comment_btn = itemView.findViewById(R.id.comment_btn);
            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            caption = itemView.findViewById(R.id.caption);
            rate = itemView.findViewById(R.id.rate);
            favourite = itemView.findViewById(R.id.favourite);
            tvRatecount = itemView.findViewById(R.id.tvRatecount);
            tvViewcomments = itemView.findViewById(R.id.tvViewcomments);
            menu = itemView.findViewById(R.id.menu);
            play = itemView.findViewById(R.id.play);
            rupee = itemView.findViewById(R.id.rupee);


        }
    }
}

