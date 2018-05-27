package kalei.com.learnwithme;

import com.flurry.android.FlurryAgent;

import android.app.Application;

/**
 * Created by risaki on 5/25/18.
 */

public class LearnWithMeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "WJBSGT9ZWK89H3Y86STR");
        FlurryAgent.init(this, "WJBSGT9ZWK89H3Y86STR");
    }
}
