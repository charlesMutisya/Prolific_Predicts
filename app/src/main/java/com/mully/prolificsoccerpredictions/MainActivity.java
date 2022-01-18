package com.mully.prolificsoccerpredictions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;



import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter sectionsPagerAdapter;
    private boolean doubleBack = false;
    private final String TAG = MainActivity.class.getSimpleName();
    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FirebaseMessaging.getInstance().subscribeToTopic("Tips1");






        FabSpeedDial fabSpeedDial= findViewById(R.id.fabspeed);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int idc = menuItem.getItemId();
                    if (idc== R.id.action_mail){
                        Intent feedback = new Intent(MainActivity.this, FeedBack.class);

                        startActivity(feedback);

                    }else if (idc== R.id.action_rate){
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
                        }

                    }else  if (idc==R.id.action_share){
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "We deliver daily wins to you all for FREE. Download here https://play.google.com/store/apps/details?id=com.mully.prolificsoccerpredictions";
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " The Winning Stars");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(sharingIntent);

                    }



                return super.onMenuItemSelected(menuItem);
            }
        });





    }

     class  SectionsPagerAdapter extends FragmentPagerAdapter{

        public SectionsPagerAdapter(MainActivity mainActivity, @NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return  new DailyTips();
                case 1:
                    return  new Livescore();
            }
            return new DailyTips();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Daily Tips";
                case 1:
                    return  "Livescore";
            }
            return "Daily Tips";
        }


    }

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            super.onBackPressed();
            return;
        }
        this.doubleBack = true;
        Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2000);
    }
}



