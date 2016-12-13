package kalei.com.learnwithme.fragments;

import com.aigestudio.wheelpicker.WheelPicker;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kalei.com.learnwithme.R;

/**
 * Created by risaki on 9/7/16.
 */
public class SpellGameFragment extends GameFragment {

    String mCurrentWord;
    Button mSubmitBtn;
    LinearLayout mFlContainer;
    Animation mFadeIn;
    List<Character> mEnglishLetterList = new ArrayList<>(Arrays
            .asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
                    'Z'));

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
        mFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        mListenButton.setOnClickListener(this);
        mSubmitBtn = (Button) mRootView.findViewById(R.id.submit_btn);
        mSubmitBtn.setOnClickListener(this);

        setupWord();

        return mRootView;
    }

    private void initWheel() {
        mFlContainer = (LinearLayout) mRootView.findViewById(R.id.wheel_container);
        LinearLayout.LayoutParams flParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.CENTER;
        int WHEEL_TEXT_SIZE = 56;
        WheelPicker wheelPicker = null;
        for (int i = 0; i < mCurrentWord.length(); i++) {
            wheelPicker = new WheelPicker(getActivity());
            //todo: make this better

            wheelPicker.setPadding((i == 0 ? 0 : 100), 0, 0, 0);
            wheelPicker.setData(mEnglishLetterList);

            wheelPicker.setAtmospheric(true);
            wheelPicker.setCurved(true);
            wheelPicker.setVisibleItemCount(3);

            wheelPicker.setItemSpace(100);
//            wheelPicker.setSelectedItemPosition(mEnglishLetterList.indexOf(mCurrentWord.charAt(i)));
            wheelPicker.setItemTextSize(Math.round(WHEEL_TEXT_SIZE * getResources().getDisplayMetrics().scaledDensity));
            wheelPicker.setSelectedItemTextColor(R.color.colorAccent);

            mFlContainer.addView(wheelPicker, flParams);
        }
    }

    @Override
    public void onClick(final View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.submit_btn:
                if (mIsEnglish) {
                    Log.i("lwm", "word is: " + mCurrentWord);
                    //only spelling english words for now
                    if (checkSpelling()) {
                        correctAnswer();
                    } else {
                        wrongAnswer();
                    }
                } else {
//                    loadNextWord();
                }
                break;
            case R.id.right_arrow_img:
                loadNextWord();
                break;
            case R.id.listen_btn:
                sayWord();
                break;
        }
    }

    private boolean checkSpelling() {
        String word = "";
        for (int i = 0; i < mFlContainer.getChildCount(); i++) {
            word += mEnglishLetterList.get(((WheelPicker) mFlContainer.getChildAt(i)).getCurrentItemPosition()).toString();
        }

        return word.toUpperCase().equals(mCurrentWord.toUpperCase());
    }

    @Override
    protected void correctAnswer() {
        //animate the right answer blink in and out
        mEnglishWord.setText(mCurrentWord.toUpperCase());
        mEnglishWord.startAnimation(mFadeIn);

        mFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                mEnglishWord.setTextColor(getResources().getColor(R.color.darkGreenColor));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                fadeOut.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(final Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(final Animation animation) {
                        loadNextWord();
                    }

                    @Override
                    public void onAnimationRepeat(final Animation animation) {

                    }
                });
                mEnglishWord.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mSound.play(mCorrectSoundId, 1, 1, 10, 0, mPitch);
    }

    @Override
    protected void wrongAnswer() {
        mEnglishWord.setTextColor(Color.RED);
        mEnglishWord.setText("NOPE");
        Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        mEnglishWord.setAnimation(fadeOut);
        fadeOut.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                mEnglishWord.setText("");
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });
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
        mIndex++;
        mFlContainer.removeAllViews();
        setupWord();
    }

    @Override
    public void getPrevWord() {

    }

    private void setupWord() {
        //show interstitial every 10 times
        showInterstitial();

        mCurrentWord = mWordsArray[mIndex];

        mEnglishWord.setText("");
        mCanContinue = false;

        initWheel();
    }
}
