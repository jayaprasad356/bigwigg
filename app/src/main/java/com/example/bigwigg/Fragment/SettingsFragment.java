package com.example.bigwigg.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.bigwigg.LoginActivity;
import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.SearchActivity;


public class SettingsFragment extends Fragment {

    ImageButton logout;


    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_settings, container, false);

        logout=rootview.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();


            }
        });




        return rootview;
    }
}