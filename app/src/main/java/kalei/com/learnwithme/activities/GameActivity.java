package kalei.com.learnwithme.activities;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.provider.Settings.Secure;

import kalei.com.learnwithme.BuildConfig;
import kalei.com.learnwithme.R;
import kalei.com.learnwithme.activities.LearnWithMeActivity.LearnWithMeAdListener;
import kalei.com.learnwithme.fragments.GameFragment;

/**
 * Created by risaki on 8/28/16.
 */
public class GameActivity extends LearnWithMeActivity implements LearnWithMeAdListener {

    public GameFragment mGameFragment;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_game);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mGameFragment = GameFragment.newInstance(args.getString(getString(R.string.game_type_label)));
            mGameFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_frame, mGameFragment).commit();
        }
        loadAds();
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
        if (mAdView != null && !BuildConfig.DEBUG) {
            String android_id = Secure.getString(this.getContentResolver(),
                    Secure.ANDROID_ID);
//            AdRequest adRequest = new AdRequest.Builder().addTestDevice("1227AC999E49F1FE325D0EA5E2E4E604").build();
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

            mAdView.loadAd(adRequest);
        }
    }

    @Override
    public void createAndShowAd() {
        newInterstitialAd();
        loadInterstitial();
    }
}
