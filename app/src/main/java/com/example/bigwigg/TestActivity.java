package com.example.bigwigg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class TestActivity extends AppCompatActivity {
    private static final float END_SCALE = 0.85f;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private DrawerLayout drawer;
    private BottomNavigationView bottomNavView;
    private CoordinatorLayout contentView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        initToolbar();

        initNavigation();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    public void SetBottomNavUnchecked() {
        bottomNavView.getMenu().findItem(R.id.placeholder).setChecked(true);
    }



    private void initNavigation() {

        drawer = findViewById(R.id.drawer_layout);
        bottomNavView = findViewById(R.id.bottom_nav_view);
        contentView = findViewById(R.id.content_view);
        navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_profile,
                R.id.explore, R.id.business, R.id.favourite,
                R.id.notification,  R.id.nav_send,
                R.id.bottom_home, R.id.bottom_dashboard, R.id.bottom_notifications)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        //NavigationUI.setupWithNavController(navigationView, navController);




        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // You need this line to handle the navigation
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (handled) {
                    ViewParent parent = navigationView.getParent();
                    if (parent instanceof DrawerLayout) {
                        ((DrawerLayout) parent).closeDrawer(navigationView);
                    }
                }


                int itemId = item.getItemId();
                if (itemId == R.id.nav_logout) {
                    Intent intent =  new Intent(TestActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                //return true;
                return handled;
            }
        });





        animateNavigationDrawer();
    }


    private void animateNavigationDrawer() {
//        drawerLayout.setScrimColor(getResources().getColor(R.color.text_brown));
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.video:
              //  Toast.makeText(this, "search clicked", Toast.LENGTH_SHORT).show();
                VideoFragment fragmentA = new VideoFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.nav_host_fragment,fragmentA,"YOUR_TARGET_FRAGMENT_TAG")
                        .addToBackStack("YOUR_SOURCE_FRAGMENT_TAG").commit();
                SetBottomNavUnchecked();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }
}