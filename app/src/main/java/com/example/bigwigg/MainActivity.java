package com.example.bigwigg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigwigg.Fragment.BusinessFragment;
import com.example.bigwigg.Fragment.ExploreFragment;
import com.example.bigwigg.Fragment.FavouriteFragment;
import com.example.bigwigg.Fragment.NotificationFragment;
import com.example.bigwigg.Fragment.ProfileFragment;
import com.example.bigwigg.Fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    ExploreFragment exploreFragment;
    BusinessFragment businessFragment;
    FavouriteFragment favouriteFragment;
    NotificationFragment notificationFragment;
    ImageView Profile;
    ImageButton Video;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Profile = findViewById(R.id.profile);
        Video = findViewById(R.id.video);

        bottomNavigationView.setSelectedItemId(R.id.explore);
        bottomNavigationView.setOnItemSelectedListener(this);


        exploreFragment = new ExploreFragment();
        businessFragment = new BusinessFragment();
        favouriteFragment = new FavouriteFragment();
        notificationFragment = new NotificationFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, exploreFragment,"EXPLORE").commit();

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();


            }
        });
        Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoFragment videoFragment = new VideoFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, videoFragment,TAG_FRAGMENT).addToBackStack("my_fragment").commit();

                SetBottomNavUnchecked();


            }
        });



    }
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        TextView profile = bottomSheetDialog.findViewById(R.id.profile);
        TextView settings = bottomSheetDialog.findViewById(R.id.settings);
        TextView search = bottomSheetDialog.findViewById(R.id.search);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, profileFragment).addToBackStack("my_fragment").commit();

                SetBottomNavUnchecked();
                bottomSheetDialog.dismiss();

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment settingsFragment = new SettingsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, settingsFragment).addToBackStack("my_fragment").commit();

                SetBottomNavUnchecked();
                bottomSheetDialog.dismiss();

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                finish();
                bottomSheetDialog.dismiss();

            }
        });



        bottomSheetDialog.show();
    }
    public void SetBottomNavUnchecked() {
        bottomNavigationView.getMenu().findItem(R.id.placeholder).setChecked(true);
    }
    public void setExploreChecked() {
        bottomNavigationView.getMenu().findItem(R.id.explore).setChecked(true);
    }
    public void setBusinessChecked() {
        bottomNavigationView.getMenu().findItem(R.id.business).setChecked(true);
    }
    public void setFavouriteChecked() {
        bottomNavigationView.getMenu().findItem(R.id.favourite).setChecked(true);
    }
    public void setNotificationChecked() {
        bottomNavigationView.getMenu().findItem(R.id.notification).setChecked(true);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.explore:
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment, exploreFragment,"EXPLORE").commit();
                return true;

            case R.id.business:
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,businessFragment,"BUSINESS" ).commit();
                return true;



            case R.id.favourite:
                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,favouriteFragment,"FAVOURITE").commit();
                return true;


            case R.id.notification:

                getSupportFragmentManager().beginTransaction().replace(R.id.f1fragment,notificationFragment,"NOTIFICATION").commit();
                return true;
        }

        return false;
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else{

            ExploreFragment exploreFragment = (ExploreFragment) getSupportFragmentManager().findFragmentByTag("EXPLORE");
            BusinessFragment businessFragment = (BusinessFragment) getSupportFragmentManager().findFragmentByTag("BUSINESS");
            FavouriteFragment favouriteFragment = (FavouriteFragment) getSupportFragmentManager().findFragmentByTag("FAVOURITE");
            NotificationFragment notificationFragment = (NotificationFragment) getSupportFragmentManager().findFragmentByTag("NOTIFICATION");
            if ((exploreFragment != null && exploreFragment.isVisible()) || (businessFragment != null && businessFragment.isVisible()) || (favouriteFragment != null && favouriteFragment.isVisible()) || (notificationFragment != null && notificationFragment.isVisible())) {
                if (doubleBackToExitPressedOnce) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
            else{
                super.onBackPressed();

            }

        }
    }

}