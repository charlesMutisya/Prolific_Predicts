package com.mully.prolificsoccerpredictions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;

public class PostDetails extends AppCompatActivity {
    DatabaseReference mRef;
    String postKey;
    TextView tvTitle, tvBody, tvTime;
    ProgressDialog pd;
    String selection;
    AutoLinkTextView autoLinkTextView;
    InterstitialAd interstitialAd_fb;
    AdView mbappe00;

    private final String TAG = PostDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_post_details);
        postKey = getIntent().getExtras().getString("postkey");
        selection = getIntent().getExtras().getString("selection");
        tvBody = findViewById(R.id.tvBody);
        tvTitle = findViewById(R.id.tvTitle);
        tvTime = findViewById(R.id.post_time);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        autoLinkTextView = findViewById(R.id.autoLinkrate);
        autoLinkTextView.addAutoLinkMode(AutoLinkMode.MODE_CUSTOM);
        autoLinkTextView.setCustomRegex("\\sHere\\b");


        autoLinkTextView.setAutoLinkText("Motivate us by rating us five stars : Here");

        autoLinkTextView.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
            @Override
            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                if (autoLinkMode == AutoLinkMode.MODE_CUSTOM)
                    try {
                        Intent RateIntent =
                                new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName()));
                        startActivity(RateIntent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Unable to connect try again later...",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

            }
        });


        if (postKey != null) {

            mRef = FirebaseDatabase.getInstance().getReference().child("Tips1").child(postKey);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String title = dataSnapshot.child("tipsTitle").getValue().toString();
                    String body = dataSnapshot.child("tipsDetails").getValue().toString();
                    String date1 = dataSnapshot.child("tipsDate").getValue().toString();

                    if (title != null) {
                        tvTitle.setText(title.toUpperCase());
                        pd.dismiss();
                    } else {
                        Toast.makeText(PostDetails.this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                    if (body != null) {
                        tvBody.setText(body);

                    }
                    if (date1 != null) {
                        tvTime.setText(date1);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
    private void showBaners() {
        mbappe00 = new com.facebook.ads.AdView(this, "245389397006094_245403817004652", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.banner_container2);
        adContainer.addView(mbappe00);

        mbappe00.loadAd();
    }

    private void loadingInt() {
        interstitialAd_fb = new InterstitialAd(PostDetails.this, "245389397006094_245400907004943");
        interstitialAd_fb.loadAd(interstitialAd_fb.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.e(TAG, "Interstitial ad displayed.");

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.e(TAG, "Interstitial ad dismissed.");

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());

            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd_fb.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "Interstitial ad clicked!");


            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "Interstitial ad impression logged!");

            }
        }).build());
        interstitialAd_fb.loadAd();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menub, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.feedback) {
            startActivity(new Intent(this, FeedBack.class));

        } else if (id == R.id.menu_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = " We deliver daily wins to you all for FREE. Download here https://play.google.com/store/apps/details?id=com.mully.prolificsoccerpredictions";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " TopPredict Tips");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(sharingIntent);
        } else if (id == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.privacyP) {
            Uri uri = Uri.parse("https://prolificpredictionz.blogspot.com/p/privacy-policy.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}