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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.BUILD_TYPE.toString().equals("debug")) {
            startActivity();
            return;
        }
        // Create the next level button, which tries to show an interstitial when clicked.
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        newInterstitialAd();
        loadInterstitial();
    }

    protected void newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);

//        if (!BuildConfig.BUILD_TYPE.toString().equals("debug")) {
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

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
        mInterstitialAd = interstitialAd;
//        }
    }

    private void startActivity() {
        startActivity(new Intent(MainActivity.this, MenuActivity.class));
    }
}
