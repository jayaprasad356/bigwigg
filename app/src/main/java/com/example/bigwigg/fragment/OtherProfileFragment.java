package com.example.bigwigg.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bigwigg.R;
import com.example.bigwigg.helper.Constant;
import com.example.bigwigg.helper.Session;

public class OtherProfileFragment extends Fragment {
    TextView Name,Role,Description;
    View root;
    Session session;
    ImageView Profile;



    public OtherProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_other_profile, container, false);
        Name = root.findViewById(R.id.name);
        Role = root.findViewById(R.id.role);
        Description = root.findViewById(R.id.description);
        Profile = root.findViewById(R.id.profile);

        session = new Session(getActivity());

        Name.setText(session.getData(Constant.NAME));
        Role.setText(session.getData(Constant.ROLE));
        Description.setText(session.getData(Constant.DESCRIPION));

        Glide.with(getActivity()).load(Uri.parse(session.getData(Constant.PROFILE))).into(Profile);



        return root;
    }
}