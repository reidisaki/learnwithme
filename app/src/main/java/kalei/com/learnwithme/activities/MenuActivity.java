package kalei.com.learnwithme.activities;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.flurry.android.FlurryAgent;
import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdInterstitial;
import com.flurry.android.ads.FlurryAdInterstitialListener;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;

import kalei.com.learnwithme.BuildConfig;
import kalei.com.learnwithme.R;
import kalei.com.learnwithme.fragments.MenuFragment;

/**
 * Created by risaki on 8/28/16.
 */
public class MenuActivity extends LearnWithMeActivity {

    public MenuFragment mMenuFragment;
    FlurryAdInterstitialListener interstitialAdListener = new FlurryAdInterstitialListener() {

        @Override
        public void onFetched(FlurryAdInterstitial adInterstitial) {
            adInterstitial.displayAd();
        }

        @Override
        public void onRendered(final FlurryAdInterstitial flurryAdInterstitial) {
            int x = 9;
        }

        @Override
        public void onDisplay(final FlurryAdInterstitial flurryAdInterstitial) {
            int x = 9;
        }

        @Override
        public void onClose(final FlurryAdInterstitial flurryAdInterstitial) {
            int x = 9;
        }

        @Override
        public void onAppExit(final FlurryAdInterstitial flurryAdInterstitial) {
            int x = 9;
        }

        @Override
        public void onClicked(final FlurryAdInterstitial flurryAdInterstitial) {
            int x = 9;
        }

        @Override
        public void onVideoCompleted(final FlurryAdInterstitial flurryAdInterstitial) {
            int x = 9;
        }

        @Override
        public void onError(FlurryAdInterstitial adInterstitial, FlurryAdErrorType adErrorType, int errorCode) {
            adInterstitial.destroy();
        }
        //..
        //the remainder of listener callbacks
    };
    private FlurryAdInterstitial mFlurryAdInterstitial = null;
    private String mAdSpaceName = "interstitial";
    private String mApiKey = "WJBSGT9ZWK89H3Y86STR";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mMenuFragment = MenuFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_frame, mMenuFragment).commit();
        mFlurryAdInterstitial = new FlurryAdInterstitial(this, mAdSpaceName);
        mFlurryAdInterstitial.setListener(interstitialAdListener);
        mFlurryAdInterstitial.fetchAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void loadAds() {
//        adView = (AdLayout) findViewById(R.id.adview);
//
//        AdTargetingOptions adOptions = new AdTargetingOptions();
//        // Optional: Set ad targeting options here.
//        adView.loadAd(adOptions); // Retrieves an ad on background thread
//
        AdView mAdView = (AdView) findViewById(R.id.adView);
        if (mAdView != null && !BuildConfig.BUILD_TYPE.toString().equals("debug")) {
            String android_id = Secure.getString(this.getContentResolver(),
                    Secure.ANDROID_ID);
//            AdRequest adRequest = new AdRequest.Builder().addTestDevice(android_id).build();
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
            mAdView.loadAd(adRequest);
        }
    }

    public void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, mApiKey);
        // fetch and prepare ad for this ad space. won’t render one yet

    }

    public void onStop() {
        FlurryAgent.onEndSession(this);
        //do NOT call mFlurryAdInterstitial.destroy() here.
        //it will destroy the object prematurely and prevent certain listener callbacks form fireing
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        mFlurryAdInterstitial.destroy();
    }
}
