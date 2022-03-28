package com.example.bigwigg.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bigwigg.LoginActivity;
import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.TestActivity;


public class LogoutFragment extends Fragment {


    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_logout, container, false);

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        return rootview;
    }
}