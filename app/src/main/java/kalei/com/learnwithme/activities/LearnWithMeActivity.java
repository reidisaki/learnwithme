package kalei.com.learnwithme.activities;

import com.crashlytics.android.Crashlytics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import io.fabric.sdk.android.Fabric;

/**
 * Created by risaki on 8/28/16.
 */
public class LearnWithMeActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
    }
}
