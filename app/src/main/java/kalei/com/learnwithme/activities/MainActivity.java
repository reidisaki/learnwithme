package kalei.com.learnwithme.activities;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import kalei.com.learnwithme.BuildConfig;
import kalei.com.learnwithme.R;

public class MainActivity extends LearnWithMeActivity {

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the next level button, which tries to show an interstitial when clicked.
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);

        if (BuildConfig.DEBUG) {
            interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        } else {
            interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        }
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

                //start new activity  show menu
                startActivity();
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                //start new activity  show menu
                startActivity();
            }
        });
        return interstitialAd;
    }

    private void startActivity() {
        startActivity(new Intent(MainActivity.this, MenuActivity.class));
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }
}
