package kalei.com.learnwithme.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.MainThread;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import kalei.com.learnwithme.R;
import kalei.com.learnwithme.models.Letter;
import me.grantland.widget.AutofitTextView;

/**
 * Created by risaki on 9/7/16.
 */
public class ReadGameFragment extends GameFragment implements OnClickListener, OnLoadCompleteListener {

    protected AutofitTextView mWordTextView;
    private SpannableString mContent;
    private static String GAME_TYPE = "game_type_label";
    private String mGameType;
    protected Button mSpeakButton;

    private int mASoundId, mBSoundId, mCSoundId, mDSoundId, mESoundId, mFSoundId, mGSoundId, mHSoundId, mISoundId, mJSoundId, mKSoundId, mLSoundId, mMSoundId, mNSoundId, mOSoundId, mPSoundId, mQSoundId, mRSoundId, mSSoundId, mTSoundId, mUSoundId, mVSoundId, mWSoundId, mXSoundId, mYSoundId, mZSoundId;

    protected void enableSpeakButton() {
        if (mCanContinue) {
            mSpeakButton.setEnabled(true);
        }
    }

    public static ReadGameFragment newInstance(String gameType) {
        ReadGameFragment fragment = new ReadGameFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GAME_TYPE, gameType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initViews();
        String word = mWordsArray[mIndex];
        //needs to run first to build up underline spans

        if (mContent == null || !mContent.toString().equals(word)) {
            mContent = new SpannableString(word);
        }
        mContent = setUnderLineText();

        //set the mContent span that was built above
        mWordTextView.setText(mContent);
    }

    protected void initViews() {
        super.initViews();
        mWordTextView = (AutofitTextView) mRootView.findViewById(R.id.word_txt);
        mWordTextView.setTypeface(custom_font);
        mEnglishWord = (TextView) mRootView.findViewById(R.id.english_word);
        mWordTextView.setText(mIsEnglish ? mWordsArray[mIndex] : mUnicodeArray[mIndex]);

        mSpeakButton = (Button) mRootView.findViewById(R.id.play_btn);
        mSpeakButton.setEnabled(false);
        mSpeakButton.setOnClickListener(this);

        if (mIsEnglish) {
            mListenButton.setEnabled(false);
        } else {
            mSpeakButton.setText("Next");
        }
        mWordTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {

                //only english words can be spelled and sounded out
                if (mIsEnglish) {
                    String word = mWordsArray[mIndex];
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    int screenWidth = metrics.widthPixels;
                    int wordTotalWidth = mWordTextView.getMeasuredWidth();
                    int sectionWidth = wordTotalWidth / word.length();
                    int wordPadding = (screenWidth - wordTotalWidth) / 2;

                    String letter = "a";
                    mContent = new SpannableString(word);
                    for (int i = 0; i < word.length(); i++) {
                        int j = i;
                        j++;
                        if (event.getRawX() < (sectionWidth * j) + wordPadding) {
                            //its in section i
                            letter = word.split("(?!^)")[i];
                            checkForLetterInMap(letter, i);
                            break;
                        }
                    }

                    mWordTextView.setText(mContent);
                    sayLetter(letter);
                } else {
                    mWordTextView.setText(mUnicodeArray[mIndex]);
                    sayWord();
                }
                return false;
            }
        });
        mListenButton.setOnClickListener(this);
    }

    protected void sayWord() {
        super.sayWord();
        enableSpeakButton();
    }

    private void checkForLetterInMap(final String letter, int index) {

        Letter letterObj = mLetterList.get(index);
        if (letterObj.getLetterCharacter().equals(letter)) {
            letterObj.setUnderlined(true);
        }

        setUnderLineText();
        mListenButton.setEnabled(mCanContinue);

        enableSpeakButton();
    }

    //sets underline text and if user can continue
    protected SpannableString setUnderLineText() {
        mCanContinue = true;
        for (int j = 0; j < mLetterList.size(); j++) {
            Letter letter = mLetterList.get(j);
            if (!letter.isUnderlined()) {
                mCanContinue = false;
            } else {
                int index = letter.getIndexOfLetter();
                mContent.setSpan(new UnderlineSpan(), index, index + 1, 0);
            }
        }
        return mContent;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_game;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mRootView = super.onCreateView(inflater, container, savedInstanceState);
        loadSounds();
        String word = mIsEnglish ? mWordsArray[mIndex] : mUnicodeArray[mIndex];
        if (word != null) {
            if (mWordTextView == null) {
                initViews();
            }
            if (mIsEnglish) {
                loadLetterMap(word);
            } else {
                mWordTextView.setText(word);
            }
        }
        return mRootView;
    }

    public void getPrevWord() {
        if (mIndex > 0) {
            mIndex--;
            mLeftArrow.setVisibility(mIndex > 1 ? View.VISIBLE : View.GONE);
            if (mIsEnglish) {
                setupWord();
            } else {
                setupUnicodeLetter();
            }
        }
    }

    @Override
    public void onClick(final View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.play_btn:
                if (mIsEnglish) {
                    promptSpeechInput();
                } else {
                    loadNextWord();
                }
                break;
            case R.id.listen_btn:
                sayWord();
                break;
            case R.id.right_arrow_img:
                //todo: delete this later its for testing reid
                correctAnswer();
//                wordObservable = Observable.create(new ObservableOnSubscribe<String>() {
//                    @Override
//                    public void subscribe(final ObservableEmitter<String> e) throws Exception {
//
//                        e.onNext(mWordTextView.getText().toString());
//                        e.onComplete();
//                    }
//                }).map(new Function<String, String>() {
//                    @Override
//                    public String apply(final String s) throws Exception {
//                        return s + " this is a test";
//                    }
//                });
//
//                wordTextObserver = new Observer<String>() {
//                    @Override
//                    public void onSubscribe(final Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(final String value) {
//                        Log.i("lwm", "word changed to:" + value);
//                    }
//
//                    @Override
//                    public void onError(final Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                };
//                wordObservable.subscribe(wordTextObserver);
                break;
        }
    }

    private void sayLetter(String letter) {

        letter = letter.toLowerCase();
        switch (letter) {
            case "a":
                mSound.play(mASoundId, 1, 1, 10, 0, mPitch);
                break;
            case "b":
                mSound.play(mBSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "c":
                mSound.play(mCSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "d":
                mSound.play(mDSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "e":
                mSound.play(mESoundId, 1, 1, 10, 0, mPitch);
                break;
            case "f":
                mSound.play(mFSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "g":
                mSound.play(mGSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "h":
                mSound.play(mHSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "i":
                mSound.play(mISoundId, 1, 1, 10, 0, mPitch);
                break;
            case "j":
                mSound.play(mJSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "k":
                mSound.play(mKSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "l":
                mSound.play(mLSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "m":
                mSound.play(mMSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "n":
                mSound.play(mNSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "o":
                mSound.play(mOSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "p":
                mSound.play(mPSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "q":
                mSound.play(mQSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "r":
                mSound.play(mRSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "s":
                mSound.play(mSSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "t":
                mSound.play(mTSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "u":
                mSound.play(mUSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "v":
                mSound.play(mVSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "w":
                mSound.play(mWSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "x":
                mSound.play(mXSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "y":
                mSound.play(mYSoundId, 1, 1, 10, 0, mPitch);
                break;
            case "z":
                mSound.play(mZSoundId, 1, 1, 10, 0, mPitch);
                break;
        }

//        mTTS.speak(letterSound(letter.toLowerCase()), TextToSpeech.QUEUE_FLUSH, null, "");

        /*if we use alexas voice

        //set filenames as letter sound file
        final MediaPlayer mp = MediaPlayer.create(getActivity(), getResources().getIdentifier(letter,
                "raw", getActivity().getPackageName()));
        mp.start();
        */
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak now");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    "speech not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == getActivity().RESULT_OK && null != data) {

                    boolean isAnswerGoodEnough = false;
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    for (String answer : result) {
                        Log.i(TAG, "result: " + answer);
                        if (!isAnswerGoodEnough) {
                            isAnswerGoodEnough = answerIsCloseEnough(mWordsArray[mIndex].toLowerCase(), answer.toLowerCase());
                        }
                    }
                    //todo: make this more of a fun thing for kids showing animations and explosions

                    String resultText = isAnswerGoodEnough ? "good job you said it right" :
                            "sorry the word was:" + mWordsArray[mIndex] + " you said:" + result.get(0);
                    if (isAnswerGoodEnough) {
                        correctAnswer();
                    } else {
                        wrongAnswer();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void correctAnswer() {
        super.correctAnswer();
        mSound.play(mCorrectSoundId, 1, 1, 10, 0, mPitch);
        loadNextWord();
    }

    @Override
    protected void wrongAnswer() {
        mSound.play(mAwwSoundId, 1, 1, 10, 0, mPitch);
    }

    public void loadNextWord() {
        mIndex++;
        mEnglishWord.setVisibility(View.GONE);
        mLeftArrow.setVisibility(mIndex > 0 ? View.VISIBLE : View.GONE);

        //tod: handle other unicode languages
        if (mIsEnglish) {
            mListenButton.setEnabled(false);
            setupWord();
        } else {
            mListenButton.setEnabled(true);
            setupUnicodeLetter();
        }
    }

    private void setupWord() {
        //show interstitial every 10 times
        showInterstitial();

        String word = mWordsArray[mIndex];

        mCanContinue = false;
        loadLetterMap(word);

        mWordTextView.setText(word);
    }

    private void setupUnicodeLetter() {
        showInterstitial();

        if (mIndex >= mUnicodeArray.length) {
            mIndex = 0;
        }

        //weird hack i need to show every x number of words
        mWordTextView.setText(mUnicodeArray[mIndex] + " ");
        Log.i(TAG, "setup word: " + mWordTextView.getText().toString() + " index: " + mIndex + " real value: " + mUnicodeArray[mIndex]);
        mCanContinue = false;
    }

    public boolean answerIsCloseEnough(String answer, String theirAnswer) {
        String[] theirAnswerArray = theirAnswer.split("(?!^)");
        int numLettersFound = 0;
        for (String aTheirAnswerArray : theirAnswerArray) {
            if (answer.contains(aTheirAnswerArray)) {
                numLettersFound++;
            }
        }
        double percentage = (double) numLettersFound / theirAnswer.length();
        return percentage >= THRESHOLD_CORRECT_PERCENTAGE;
    }

    private String letterSound(String letter) {
        return mLetterMap.get(letter);
    }

    @Override
    public void onLoadComplete(final SoundPool soundPool, final int sampleId, final int status) {

//        mSoundsLoaded++;
//        if (mSoundsLoaded == 26) {
//            mProgress.dismiss();
//        }
    }

    protected void loadSounds() {

        mASoundId = mSound.load(getActivity(), getResources().getIdentifier("a",
                "raw", getActivity().getPackageName()), 1);
        mBSoundId = mSound.load(getActivity(), getResources().getIdentifier("b",
                "raw", getActivity().getPackageName()), 1);
        mCSoundId = mSound.load(getActivity(), getResources().getIdentifier("c",
                "raw", getActivity().getPackageName()), 1);
        mDSoundId = mSound.load(getActivity(), getResources().getIdentifier("d",
                "raw", getActivity().getPackageName()), 1);
        mESoundId = mSound.load(getActivity(), getResources().getIdentifier("e",
                "raw", getActivity().getPackageName()), 1);
        mFSoundId = mSound.load(getActivity(), getResources().getIdentifier("f",
                "raw", getActivity().getPackageName()), 1);
        mGSoundId = mSound.load(getActivity(), getResources().getIdentifier("g",
                "raw", getActivity().getPackageName()), 1);
        mHSoundId = mSound.load(getActivity(), getResources().getIdentifier("h",
                "raw", getActivity().getPackageName()), 1);
        mISoundId = mSound.load(getActivity(), getResources().getIdentifier("i",
                "raw", getActivity().getPackageName()), 1);
        mJSoundId = mSound.load(getActivity(), getResources().getIdentifier("j",
                "raw", getActivity().getPackageName()), 1);
        mKSoundId = mSound.load(getActivity(), getResources().getIdentifier("k",
                "raw", getActivity().getPackageName()), 1);
        mLSoundId = mSound.load(getActivity(), getResources().getIdentifier("l",
                "raw", getActivity().getPackageName()), 1);
        mMSoundId = mSound.load(getActivity(), getResources().getIdentifier("m",
                "raw", getActivity().getPackageName()), 1);
        mNSoundId = mSound.load(getActivity(), getResources().getIdentifier("n",
                "raw", getActivity().getPackageName()), 1);
        mOSoundId = mSound.load(getActivity(), getResources().getIdentifier("o",
                "raw", getActivity().getPackageName()), 1);
        mPSoundId = mSound.load(getActivity(), getResources().getIdentifier("p",
                "raw", getActivity().getPackageName()), 1);
        mQSoundId = mSound.load(getActivity(), getResources().getIdentifier("q",
                "raw", getActivity().getPackageName()), 1);
        mRSoundId = mSound.load(getActivity(), getResources().getIdentifier("r",
                "raw", getActivity().getPackageName()), 1);
        mSSoundId = mSound.load(getActivity(), getResources().getIdentifier("s",
                "raw", getActivity().getPackageName()), 1);
        mTSoundId = mSound.load(getActivity(), getResources().getIdentifier("t",
                "raw", getActivity().getPackageName()), 1);
        mUSoundId = mSound.load(getActivity(), getResources().getIdentifier("u",
                "raw", getActivity().getPackageName()), 1);
        mVSoundId = mSound.load(getActivity(), getResources().getIdentifier("v",
                "raw", getActivity().getPackageName()), 1);
        mWSoundId = mSound.load(getActivity(), getResources().getIdentifier("w",
                "raw", getActivity().getPackageName()), 1);
        mXSoundId = mSound.load(getActivity(), getResources().getIdentifier("x",
                "raw", getActivity().getPackageName()), 1);
        mYSoundId = mSound.load(getActivity(), getResources().getIdentifier("y",
                "raw", getActivity().getPackageName()), 1);
        mZSoundId = mSound.load(getActivity(), getResources().getIdentifier("z",
                "raw", getActivity().getPackageName()), 1);
    }
}
