package com.example.bigwigg.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.bigwigg.LoginActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.SplashActivity;
import com.example.bigwigg.helper.Session;


public class SettingsFragment extends Fragment {

    ImageButton logout;
    Session session;


    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_settings, container, false);
        session = new Session(getActivity());

        logout=rootview.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.logoutUser(getActivity());


            }
        });




        return rootview;
    }
}