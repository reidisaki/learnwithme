package kalei.com.learnwithme.activities;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.provider.Settings.Secure;

import kalei.com.learnwithme.R;
import kalei.com.learnwithme.fragments.GameFragment;

/**
 * Created by risaki on 8/28/16.
 */
public class GameActivity extends LearnWithMeActivity {

    public GameFragment mGameFragment;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_game);
        mGameFragment = GameFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_frame, mGameFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAds();
    }

    public void loadAds() {
//        adView = (AdLayout) findViewById(R.id.adview);
//
//        AdTargetingOptions adOptions = new AdTargetingOptions();
//        // Optional: Set ad targeting options here.
//        adView.loadAd(adOptions); // Retrieves an ad on background thread
//
        AdView mAdView = (AdView) findViewById(R.id.adView);
        if (mAdView != null) {
            String android_id = Secure.getString(this.getContentResolver(),
                    Secure.ANDROID_ID);
//            AdRequest adRequest = new AdRequest.Builder().addTestDevice(android_id).build();
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }
}
