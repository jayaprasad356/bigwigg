package com.example.bigwigg.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.bigwigg.PostActivity;
import com.example.bigwigg.R;
import com.example.bigwigg.TestActivity;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExploreFragment extends Fragment {
    RelativeLayout r1,r2,r3;
    PostFragment postFragment;
    View fragment;
    BottomNavigationView bottomNavigationView;




    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootview = inflater.inflate(R.layout.fragment_explore, container, false);
       r1 = rootview.findViewById(R.id.r1);
       r2 = rootview.findViewById(R.id.r2);
       r3 = rootview.findViewById(R.id.r3);
       bottomNavigationView = rootview.findViewById(R.id.bottom_nav_view);
       fragment = rootview.findViewById(R.id.nav_host_fragment);
        postFragment = new PostFragment();

       r1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Fragment fragmentA = new PostFragment();
               getFragmentManager().beginTransaction()
                       .replace(R.id.nav_host_fragment,fragmentA,"YOUR_TARGET_FRAGMENT_TAG")
                       .addToBackStack("YOUR_SOURCE_FRAGMENT_TAG").commit();
               ((TestActivity)getActivity()).SetBottomNavUnchecked();


           }
       });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragmentA = new PostFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment,fragmentA,"YOUR_TARGET_FRAGMENT_TAG")
                        .addToBackStack("YOUR_SOURCE_FRAGMENT_TAG").commit();
                ((TestActivity)getActivity()).SetBottomNavUnchecked();

            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragmentA = new PostFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment,fragmentA,"YOUR_TARGET_FRAGMENT_TAG")
                        .addToBackStack("YOUR_SOURCE_FRAGMENT_TAG").commit();
                ((TestActivity)getActivity()).SetBottomNavUnchecked();

            }
        });


       return rootview;

    }


}