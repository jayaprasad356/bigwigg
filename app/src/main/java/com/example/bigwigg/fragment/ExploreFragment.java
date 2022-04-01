package com.example.bigwigg.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.bigwigg.MainActivity;
import com.example.bigwigg.R;
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
        ((MainActivity)getActivity()).setExploreChecked();


       r1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               PostFragment postFragment = new PostFragment();
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, postFragment).addToBackStack("my_fragment").commit();

               ((MainActivity)getActivity()).SetBottomNavUnchecked();


           }
       });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostFragment postFragment = new PostFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, postFragment).addToBackStack("my_fragment").commit();

                ((MainActivity)getActivity()).SetBottomNavUnchecked();

            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostFragment postFragment = new PostFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, postFragment).addToBackStack("my_fragment").commit();

                ((MainActivity)getActivity()).SetBottomNavUnchecked();

            }
        });


       return rootview;

    }


}