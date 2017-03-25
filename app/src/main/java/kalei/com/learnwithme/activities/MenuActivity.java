package kalei.com.learnwithme.activities;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mMenuFragment = MenuFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_frame, mMenuFragment).commit();

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
        if (mAdView != null && !BuildConfig.BUILD_TYPE.toString().equals("debug")) {
            String android_id = Secure.getString(this.getContentResolver(),
                    Secure.ANDROID_ID);
//            AdRequest adRequest = new AdRequest.Builder().addTestDevice(android_id).build();
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
            mAdView.loadAd(adRequest);
        }
    }
}
