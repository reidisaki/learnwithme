package kalei.com.learnwithme.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import kalei.com.learnwithme.R;
import kalei.com.learnwithme.activities.LearnWithMeActivity;
import kalei.com.learnwithme.activities.LearnWithMeActivity.LearnWithMeAdListener;

/**
 * Created by risaki on 9/7/16.
 */
public class GameFragment extends LearnWithMeFragment implements OnClickListener {
    private static final int NUM_TIMES_BEFORE_INTERSTITIAL_SHOWN = 10;
    private static final float SPEECH_RATE = .1f;
    private static final float THRESHOLD_CORRECT_PERCENTAGE = .5f;
    private static final float PITCH_VALUE = 1.1f;
    private final String TAG = this.getClass().getSimpleName();

    private String[] mWordsArray = {"one", "two", "bat", "cat", "mat", "pat", "rat", "sat", "can", "fan", "man", "pan", "ran", "tan", "cap", "map", "nap",
            "tap",
            "sap", "bag", "wag", "tag", "rag", "dam", "ham", "jam", "ram", "yam", "bad", "dad", "had", "mad", "jar", "tar", "dry", "my,", "all", "are", "ask",
            "mom", "and", "pad", "sad", "can", "fan", "pan", "ran", "van", "bed", "led", "red", "get", "let", "jet", "met", "net", "pet", "set", "wet", "den",
            "hen", "men", "pen", "ten", "beg", "leg", "peg", "keg", "egg", "bit", "fit", "hit", "kit", "mit", "pit", "sit", "big", "dig", "fig", "pig", "wig",
            "fin", "pin", "win", "bid", "did", "hid", "rid", "hip", "sip", "tip", "dip", "lip", "hop", "mop", "pop", "top", "dot", "got", "hot", "not", "pot",
            "rot", "box", "fox", "pox", "job", "mob", "rob", "sob", "bye", "bee", "see", "his", "her", "bye", "bee", "see", "cow", "how", "now", "bun", "fun",
            "run", "sun", "but", "cut", "gut", "hut", "nut", "rut", "bus", "cup", "pup", "cub", "rub", "tub", "bug", "dug", "hug", "mug", "rug", "tug", "see",
            "she", "bar", "car", "far"};

    private Button mPlayButton, mListenButton;
    private TextView mWordTextView;
    private int mIndex = 0;
    private TextToSpeech mTTS;
    private View mRootView;
    private static final int REQ_CODE_SPEECH_INPUT = 1234;
    private LearnWithMeAdListener adListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            adListener = (LearnWithMeAdListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTTS.setLanguage(Locale.US);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViewsInLayout();
        mRootView = inflater.inflate(R.layout.fragment_game, viewGroup);
        mPlayButton = (Button) mRootView.findViewById(R.id.play_btn);
        mListenButton = (Button) mRootView.findViewById(R.id.speak_btn);
        mWordTextView = (TextView) mRootView.findViewById(R.id.word_txt);
        mWordTextView.setText(mWordsArray[mIndex]);
        mPlayButton.setOnClickListener(this);
        mListenButton.setOnClickListener(this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            mRootView = inflater.inflate(R.layout.fragment_game, container, false);
            randomizeWords();
//        ((MainActivity) getActivity()).getActionBar().hide();
            mPlayButton = (Button) mRootView.findViewById(R.id.play_btn);
            mListenButton = (Button) mRootView.findViewById(R.id.speak_btn);
            mWordTextView = (TextView) mRootView.findViewById(R.id.word_txt);
            mRootView.setBackgroundColor(generateRandomColor());
            mPlayButton.setOnClickListener(this);
            mListenButton.setOnClickListener(this);
        }
        mWordTextView.setText(mWordsArray[mIndex]);
        return mRootView;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.play_btn:
                promptSpeechInput();
                break;
            case R.id.speak_btn:
                sayWord();
                break;
        }
    }

    private void sayWord() {
        mTTS.setSpeechRate(SPEECH_RATE);
        Set<Voice> voices = mTTS.getVoices();

        for (Voice v : voices) {
            //could use a british voice here
            if (v.getLocale().equals(Locale.getDefault()) && v.getQuality() == Voice.QUALITY_HIGH) {
                Log.i("reid", v.toString());
            }
            if (v.getQuality() == Voice.QUALITY_HIGH && v.getLatency() == Voice.LATENCY_LOW && v.getLocale().equals(Locale.getDefault())) {
                mTTS.setVoice(v);
            }
        }

        mTTS.setPitch(PITCH_VALUE);
        mTTS.speak(mWordsArray[mIndex], TextToSpeech.QUEUE_FLUSH, null, "");
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

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String theirAnswer = result.get(0);

                    String resultText = answerIsCloseEnough(mWordsArray[mIndex].toLowerCase(), theirAnswer.toLowerCase()) ? "good job you said it right" :
                            "sorry the word was:" + mWordsArray[mIndex] + " you said:" + result.get(0);

                    Toast.makeText(getActivity(),
                            resultText,
                            Toast.LENGTH_LONG).show();
                    loadNextWord();
                }
            }
            break;
        }
    }

    private void loadNextWord() {
        mIndex++;
        //show interstitial every 10 times
        if (mIndex % NUM_TIMES_BEFORE_INTERSTITIAL_SHOWN == 0) {
            adListener.createAndShowAd();
        }
        if (mIndex >= mWordsArray.length) {
            mIndex = 0;
        }
        mRootView.setBackgroundColor(generateRandomColor());
        mWordTextView.setText(mWordsArray[mIndex]);
    }

    public void randomizeWords() {
        int currentIndex = mWordsArray.length, randomIndex;
        String temporaryValue;

        // While there remain elements to shuffle...
        while (0 != currentIndex) {

            // Pick a remaining element...
            randomIndex = (int) Math.floor(Math.random() * currentIndex);
            currentIndex -= 1;

            // And swap it with the current element.
            temporaryValue = mWordsArray[currentIndex];
            mWordsArray[currentIndex] = mWordsArray[randomIndex];
            mWordsArray[randomIndex] = temporaryValue;
        }
    }

    public int generateRandomColor() {
        final Random mRandom = new Random(System.currentTimeMillis());
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.rgb(red, green, blue);
    }

    public boolean answerIsCloseEnough(String answer, String theirAnswer) {
        String[] answerArray = answer.split("");
        String[] theirAnswerArray = theirAnswer.split("");
        int numLettersFound = 0;
        for (int i = 0; i < answerArray.length; i++) {
            if (theirAnswer.contains(answerArray[i])) {
                numLettersFound++;
            }
        }
        float percentage = numLettersFound / answer.length();
        return percentage >= THRESHOLD_CORRECT_PERCENTAGE ? true : false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTTS != null) {
            mTTS.shutdown();
        }
    }
}
