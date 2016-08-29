package kalei.com.learnwithme.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

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
}
