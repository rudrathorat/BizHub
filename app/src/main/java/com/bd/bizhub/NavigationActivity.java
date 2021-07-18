package com.bd.bizhub;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bd.bizhub.ui.faq.FaqActivity;
import com.bd.bizhub.ui.settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class NavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private AppBarConfiguration mAppBarConfiguration2;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navBottomView = findViewById(R.id.bottom_navigation_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navBottomView.getMaxItemCount();

        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,R.id.navigation_profile)
                .setDrawerLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(navBottomView, navController);


        Menu menu = navigationView.getMenu();
        Menu top = menu.addSubMenu("More");


        top.add("Notifications").setCheckable(true).setIcon(R.drawable.ic_baseline_notifications_active_24).setOnMenuItemClickListener(item -> {

            //   Intent intent = new Intent(this, RegisterActivity.class);
            //   startActivity(intent);

            return false;
        });

        top.add("FAQ").setCheckable(true).setIcon(R.drawable.faq).setOnMenuItemClickListener(item -> {

               Intent intent = new Intent(this, FaqActivity.class);
               startActivity(intent);

            return false;
        });


        top.add("Chat").setCheckable(true).setIcon(R.drawable.ic_baseline_chat_24).setOnMenuItemClickListener(item -> {

            //   Intent intent = new Intent(this, RegisterActivity.class);
            //   startActivity(intent);


                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
               // sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }


            return false;
        });

        top.add("Settings").setCheckable(true).setIcon(R.drawable.ic_round_settings_24).setOnMenuItemClickListener(item -> {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return false;
        });

        top.add("Logout").setCheckable(true).setIcon(R.drawable.ic_baseline_exit_to_app_24).setOnMenuItemClickListener(item -> {

            App app = new App(new AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                    .build());
            User user = app.currentUser();
            user.logOutAsync( result -> {
                if (result.isSuccess()) {
                    Log.v("AUTH", "Successfully logged out.: ID:"+ user.getId());
                    startActivity(new Intent(this, OnboardingActivity.class));
                    NavigationActivity.this.finish();

                } else {
                    Log.e("AUTH", "log out failed! Error: " + result.getError().toString());
                }
            });

            return false;
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme){
            int positionOfMenuItem = 0;
            MenuItem item = menu.getItem(positionOfMenuItem);
            SpannableString s = new SpannableString("Settings");
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
            item.setTitle(s);

        }

        return true;
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout layout = findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);

        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }



}