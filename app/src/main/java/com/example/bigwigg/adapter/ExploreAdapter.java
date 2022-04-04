package com.example.bigwigg.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.example.bigwigg.R;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.model.Explore;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ExploreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<Explore> explores;

    public ExploreAdapter(Activity activity, ArrayList<Explore> explores) {
        this.activity = activity;
        this.explores = explores;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.explore_lyt, parent, false);
        return new ExploreItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
        final ExploreItemHolder holder = (ExploreItemHolder) holderParent;
        final Explore explore = explores.get(position);

        Glide.with(activity).load(explore.getProfile()).into(holder.profile);

//        try {
//            URL url = new URL("https://images.pexels.com/photos/2253275/pexels-photo-2253275.jpeg");
//            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            BitmapDrawable background = new BitmapDrawable(activity.getResources(), image);
//            holder.profile.setBackground(background);
//        } catch(IOException e) {
//            System.out.println(e);
//        }
        //Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.g2);


//        new LoadBackground(explore.getProfile(),
//                "androidfigure",holder.profile).execute();


    }
    private class LoadBackground extends AsyncTask<String, Void, Drawable> {

        private String imageUrl , imageName;
        RelativeLayout profile;

        public LoadBackground(String url, String file_name, RelativeLayout profile) {
            this.imageUrl = url;
            this.imageName = file_name;
            this.profile = profile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Drawable doInBackground(String... urls) {

            try {
                InputStream is = (InputStream) this.fetch(this.imageUrl);
                Drawable d = Drawable.createFromStream(is, this.imageName);
                return d;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        private Object fetch(String address) throws MalformedURLException,IOException {
            URL url = new URL(address);
            Object content = url.getContent();
            return content;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            //profile.setBackground(result);
        }
    }

    @Override
    public int getItemCount() {
        return explores.size();
    }

    static class ExploreItemHolder extends RecyclerView.ViewHolder {

        final ImageView profile;
        public ExploreItemHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);


        }
    }
}

