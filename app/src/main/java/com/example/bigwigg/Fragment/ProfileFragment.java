package com.example.bigwigg.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bigwigg.R;
import com.example.bigwigg.TestActivity;

public class ProfileFragment extends Fragment {



    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_profile, container, false);
        ((TestActivity)getActivity()).SetBottomNavUnchecked();

        return rootview;
    }
}