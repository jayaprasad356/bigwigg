package com.example.bigwigg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bigwigg.Fragment.BusinessFragment;
import com.example.bigwigg.Fragment.ExploreFragment;
import com.example.bigwigg.Fragment.FavouriteFragment;
import com.example.bigwigg.Fragment.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    ExploreFragment exploreFragment;
    BusinessFragment businessFragment;
    FavouriteFragment favouriteFragment;
    NotificationFragment notificationFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.explore);
        bottomNavigationView.setOnItemSelectedListener(this);


        exploreFragment = new ExploreFragment();
        businessFragment = new BusinessFragment();
        favouriteFragment = new FavouriteFragment();
        notificationFragment = new NotificationFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, exploreFragment).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.explore:
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, exploreFragment).commit();
                return true;

            case R.id.business:
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,businessFragment ).commit();
                return true;



            case R.id.favourite:
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,favouriteFragment ).commit();
                return true;


            case R.id.notification:

                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,notificationFragment ).commit();
                return true;






        }

        return false;
    }
}