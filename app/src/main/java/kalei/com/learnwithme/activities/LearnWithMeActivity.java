package kalei.com.learnwithme.activities;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import com.crashlytics.android.Crashlytics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import io.fabric.sdk.android.Fabric;
import kalei.com.learnwithme.BuildConfig;
import kalei.com.learnwithme.R;

/**
 * Created by risaki on 8/28/16.
 */
public class LearnWithMeActivity extends FragmentActivity {
    protected InterstitialAd mInterstitialAd;

    public interface LearnWithMeAdListener {
        public void createAndShowAd();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.BUILD_TYPE.toString().equals("debug")) {
            Fabric.with(this, new Crashlytics());
        }
    }

    protected void newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);

        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
            }
        });
        mInterstitialAd = interstitialAd;
    }

    protected void loadInterstitial() {
        if (!BuildConfig.BUILD_TYPE.toString().equals("debug")) {
            // Disable the next level button and load the ad.
            AdRequest adRequest = new AdRequest.Builder()
                    .setRequestAgent("android_studio:ad_template").build();
            mInterstitialAd.loadAd(adRequest);
        }
    }
}
