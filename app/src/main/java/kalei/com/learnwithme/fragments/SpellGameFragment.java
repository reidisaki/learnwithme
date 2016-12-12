package kalei.com.learnwithme.fragments;

import com.aigestudio.wheelpicker.WheelPicker;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kalei.com.learnwithme.R;
import kalei.com.learnwithme.activities.LearnWithMeActivity.LearnWithMeAdListener;

/**
 * Created by risaki on 9/7/16.
 */
public class SpellGameFragment extends GameFragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            adListener = (LearnWithMeAdListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    public static SpellGameFragment newInstance(String gameType) {
        SpellGameFragment fragment = new SpellGameFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GAME_TYPE, gameType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mRootView = super.onCreateView(inflater, container, savedInstanceState);

        String testWord = "Test";
        LinearLayout flContainer = (LinearLayout) mRootView.findViewById(R.id.wheel_container);
        LinearLayout.LayoutParams flParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.CENTER;

        int WHEEL_TEXT_SIZE = 56;
        WheelPicker wheelPicker = null;
        for (int i = 0; i < testWord.length(); i++) {
            wheelPicker = new WheelPicker(getActivity());
            wheelPicker.setPadding(100, 50, 0, 50);
            wheelPicker.setData(new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g")));
            wheelPicker.setAtmospheric(true);
            wheelPicker.setCurved(true);
            wheelPicker.setItemTextSize(Math.round(WHEEL_TEXT_SIZE * getResources().getDisplayMetrics().scaledDensity));
            wheelPicker.setSelectedItemTextColor(R.color.colorAccent);

            flContainer.addView(wheelPicker, flParams);
        }

        return mRootView;
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_game_spell;
    }

    @Override
    public void loadNextWord() {

    }

    @Override
    public void getPrevWord() {

    }
}
