package com.bd.bizhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.switchmaterial.SwitchMaterial;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class OnboardingActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mdots;
    private Button mNextBtn;
    private Button mNextBtn2;
    private Button mBackBtn;
    private int mCurrentPage;
    SwitchMaterial theme_switch;

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage = i;
            if (i == 0) {

                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mNextBtn2.setEnabled(false);
                theme_switch.setEnabled(false);
                theme_switch.setVisibility(View.INVISIBLE);
                mBackBtn.setVisibility(View.INVISIBLE);
                mNextBtn2.setVisibility(View.INVISIBLE);
                mNextBtn.setText("Get Started");
                mBackBtn.setText("");
                mNextBtn.setOnClickListener(view -> mSlideViewPager.setCurrentItem(mCurrentPage + 1));

            } else if (i == mdots.length - 1) {
                mNextBtn.setEnabled(true);
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mNextBtn2.setVisibility(View.VISIBLE);
                mNextBtn2.setEnabled(true);
                theme_switch.setEnabled(false);


                theme_switch.setVisibility(View.INVISIBLE);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setText("No");
                mNextBtn2.setText("Yes");
                mBackBtn.setText("Back");

                //yes - ask for login
                mNextBtn2.setOnClickListener(view -> {

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("Type", "Login");
                    startActivity(intent);


                    //We need this to go back
                    //OnboardingActivity.this.finish();
                });

                //no - new user , register
                mNextBtn.setOnClickListener(view -> {

                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    intent.putExtra("Type", "Register");
                    startActivity(intent);

                    //We need this to go back
                    //OnboardingActivity.this.finish();
                });


            } else {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mNextBtn2.setEnabled(false);
                theme_switch.setEnabled(true);
                theme_switch.setVisibility(View.VISIBLE);
                mNextBtn2.setVisibility(View.INVISIBLE);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setText("Next");
                mBackBtn.setText("Back");
                mNextBtn.setOnClickListener(view -> mSlideViewPager.setCurrentItem(mCurrentPage + 1));


            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        setContentView(R.layout.activity_intro);
        ConstraintLayout intro_layout = findViewById(R.id.intro_constraint);
        if(useDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //intro_layout.setBackgroundResource(R.drawable.bg);
            intro_layout.setBackgroundColor(android.R.color.black);
            setContentView(intro_layout);


        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //intro_layout.setBackgroundResource(R.drawable.bg_white);
            intro_layout.setBackgroundColor(android.R.color.white);
            setContentView(intro_layout);

        }



        theme_switch = findViewById(R.id.switch1);

        if(useDarkTheme){
            theme_switch.setText("Dark");
        }else{
            theme_switch.setText("Light");
        }

        theme_switch.setChecked(useDarkTheme);



       theme_switch.setOnCheckedChangeListener((view, isChecked) -> toggleTheme(isChecked));


       // mAuth = FirebaseAuth.getInstance();
        mSlideViewPager = findViewById(R.id.ViewPager);
        mDotLayout = findViewById(R.id.LinearLayout);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        mNextBtn2 = findViewById(R.id.next);
        mNextBtn = findViewById(R.id.next2);
        mBackBtn = findViewById(R.id.previous);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);


        mNextBtn.setOnClickListener(view -> mSlideViewPager.setCurrentItem(mCurrentPage + 1));

    //    mNextBtn2.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), FirebaseActivity.class)));

        mBackBtn.setOnClickListener(view -> mSlideViewPager.setCurrentItem(mCurrentPage - 1));
    }

    public void addDotsIndicator(int position) {

        mdots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mdots.length; i++) {
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(25);
            //mdots[i].setTextColor(getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(mdots[i]);
        }
        if (mdots.length > 0) {
            mdots[position].setTextColor(getColor(R.color.navyblue));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        App app = new App(new AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .build());
        User user = app.currentUser();
        if ( user!= null) {
            startActivity(new Intent(this, NavigationActivity.class));
            OnboardingActivity.this.finish();
        }
    }

    public void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getApplication().getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        Log.d("Theme", String.valueOf(darkTheme));
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();

        //Intent intent = getIntent();

        OnboardingActivity.this.finishAffinity();
        startActivity(getIntent());

    }
}
