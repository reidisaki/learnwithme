package kalei.com.learnwithme.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import kalei.com.learnwithme.R;
import kalei.com.learnwithme.activities.LearnWithMeActivity.LearnWithMeAdListener;
import me.grantland.widget.AutofitTextView;

/**
 * Created by risaki on 9/7/16.
 */
public class GameFragment extends LearnWithMeFragment implements OnClickListener {
    private static final int NUM_TIMES_BEFORE_INTERSTITIAL_SHOWN = 10;
    private static final float SPEECH_RATE = .1f;
    private static final float THRESHOLD_CORRECT_PERCENTAGE = .75f;
    private static final float PITCH_VALUE = 1.1f;
    private static final String CARTOON_FONT = "LDFComicSans.ttf";
    private final String TAG = this.getClass().getSimpleName();

    private Typeface custom_font;
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
    private AutofitTextView mWordTextView;
    private int mIndex = 0;
    private TextToSpeech mTTS;
    private View mRootView;
    private static final int REQ_CODE_SPEECH_INPUT = 1234;
    private LearnWithMeAdListener adListener;
    private HashMap<String, String> mLetterMap;
    private HashMap<String, Boolean> mLetterCheckedMap;
    private SpannableString mContent;
    private Boolean mCanContinue = false;

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
        mLetterCheckedMap = new HashMap<String, Boolean>();
        initLetterMap();
        custom_font = Typeface.createFromAsset(getResources().getAssets(), CARTOON_FONT);
    }

    private void initLetterMap() {
        mLetterMap = new HashMap<>();
        mLetterMap.put("a", "ahh");
        mLetterMap.put("b", "bay");
        mLetterMap.put("c", "ka");
        mLetterMap.put("d", "dah");
        mLetterMap.put("e", "ae");
        mLetterMap.put("f", "f");
        mLetterMap.put("g", "gah");
        mLetterMap.put("h", "ha");
        mLetterMap.put("i", "the letter i");
        mLetterMap.put("j", "gee");
        mLetterMap.put("k", "ka");
        mLetterMap.put("l", "ll");
        mLetterMap.put("m", "maw");
        mLetterMap.put("n", "naw");
        mLetterMap.put("o", "ooh");
        mLetterMap.put("p", "pah");
        mLetterMap.put("q", "kwa");
        mLetterMap.put("r", "ra");
        mLetterMap.put("s", "saw");
        mLetterMap.put("t", "tah");
        mLetterMap.put("u", "ooo");
        mLetterMap.put("v", "vay");
        mLetterMap.put("w", "wuh");
        mLetterMap.put("x", "ex");
        mLetterMap.put("y", "yu");
        mLetterMap.put("z", "za");
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
        initViews();
        //needs to run first to build up underline spans
        setUnderLineText();
        //set the mContent span that was built above
        mWordTextView.setText(mContent);
    }

    private void initViews() {
        mPlayButton = (Button) mRootView.findViewById(R.id.play_btn);
        mPlayButton.setEnabled(false);
        mListenButton = (Button) mRootView.findViewById(R.id.speak_btn);
        mWordTextView = (AutofitTextView) mRootView.findViewById(R.id.word_txt);
        mWordTextView.setTypeface(custom_font);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mWordTextView.setScaleX(1.5f);
        } else {
            mWordTextView.setScaleX(1f);
        }
        mWordTextView.setText(mWordsArray[mIndex]);

        mWordTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {

                String word = mWordTextView.getText().toString();
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int deviceTotalWidth = metrics.widthPixels;
                int sectionWidth = deviceTotalWidth / word.length();
                String letter = "a";
                mContent = new SpannableString(mWordsArray[mIndex]);
                for (int i = 0; i < word.length(); i++) {
                    int j = i;
                    j++;
                    if (i == 0) {
                        if (event.getRawX() <= sectionWidth) {
                            //its the first section
                            letter = word.split("(?!^)")[0];

                            checkForLetterInMap(letter);
                            break;
                        }
                    } else {
                        if (event.getRawX() < sectionWidth * j) {

                            //its in section i
                            letter = word.split("(?!^)")[i];

                            int index = word.indexOf(letter);
                            checkForLetterInMap(letter);
                            break;
                        }
                    }
                }

                mWordTextView.setText(mContent);
                sayLetter(letter);

                return false;
            }
        });
        mPlayButton.setOnClickListener(this);
        mListenButton.setOnClickListener(this);
    }

    private void checkForLetterInMap(final String letter) {
        if (mLetterCheckedMap.get(letter) != null) {
            mLetterCheckedMap.put(letter, true);
        }

        mCanContinue = true;
        setUnderLineText();

        if (mCanContinue) {
            mPlayButton.setEnabled(true);
        }
    }

    //sets underline text and if user can continue
    private void setUnderLineText() {
        Iterator it = mLetterCheckedMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (!(boolean) pair.getValue()) {
                mCanContinue = false;
            } else {
                int index = mWordsArray[mIndex].indexOf(pair.getKey().toString());
                mContent.setSpan(new UnderlineSpan(), index, index + 1, 0);
            }
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            mRootView = inflater.inflate(R.layout.fragment_game, container, false);
            randomizeWords();
//        ((MainActivity) getActivity()).getActionBar().hide();
            initViews();
            mRootView.setBackgroundColor(generateRandomColor());
        }

//        mWordTextView.setText(Html.fromHtml("<a href='#'>c</a><a href='#'>a</a><a href='#'>t</a>"));
        if (mWordsArray[mIndex] != null) {
            mWordTextView.setText(mWordsArray[mIndex]);
            loadLetterMap(mWordTextView.getText().toString());
        }
        return mRootView;
    }

    private void loadLetterMap(String s) {
        mLetterCheckedMap = new HashMap<>();
        for (String letter : s.split("(?!^)")) {
            mLetterCheckedMap.put(letter, false);
        }
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

    private void sayLetter(final String letter) {
        setupVoice();
        mTTS.speak(letterSound(letter.toLowerCase()), TextToSpeech.QUEUE_FLUSH, null, "");
    }

    private void setupVoice() {
        mTTS.setSpeechRate(SPEECH_RATE);
        Set<Voice> voices = mTTS.getVoices();

        if (voices != null) {
            for (Voice v : voices) {
                //could use a british voice here
//            if (v.getLocale().equals(Locale.getDefault()) && v.getQuality() == Voice.QUALITY_HIGH) {
//                Log.i("reid", v.toString());
//            }
                if (v.getQuality() == Voice.QUALITY_HIGH && v.getLatency() == Voice.LATENCY_LOW && v.getLocale().equals(Locale.getDefault())) {
                    mTTS.setVoice(v);
                }
            }
        }
        mTTS.setPitch(PITCH_VALUE);
//        mTTS.speak("ka", TextToSpeech.QUEUE_FLUSH, null, "");

    }

    private void sayWord() {
        setupVoice();
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

                    boolean isAnswerGoodEnough = false;
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    for (String answer : result) {
                        Log.i(TAG, "result: " + answer);
                        if (!isAnswerGoodEnough) {
                            isAnswerGoodEnough = answerIsCloseEnough(mWordsArray[mIndex].toLowerCase(), answer.toLowerCase());
                        }
                    }

                    String resultText = isAnswerGoodEnough ? "good job you said it right" :
                            "sorry the word was:" + mWordsArray[mIndex] + " you said:" + result.get(0);

                    Toast.makeText(getActivity(),
                            resultText,
                            Toast.LENGTH_LONG).show();
                    if (isAnswerGoodEnough) {
                        loadNextWord();
                    }
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
        String word = mWordsArray[mIndex];

        mCanContinue = false;
        loadLetterMap(word);
        mWordTextView.setText(word);
        mPlayButton.setEnabled(false);
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

        return baseColor;
//        return Color.rgb(red, green, blue);
    }

    public boolean answerIsCloseEnough(String answer, String theirAnswer) {
        String[] answerArray = answer.split("(?!^)");
        String[] theirAnswerArray = theirAnswer.split("(?!^)");
        int numLettersFound = 0;
        for (int i = 0; i < theirAnswerArray.length; i++) {
            if (answer.contains(theirAnswerArray[i])) {
                numLettersFound++;
            }
        }
        double percentage = (double) numLettersFound / theirAnswer.length();
        return percentage >= THRESHOLD_CORRECT_PERCENTAGE ? true : false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTTS != null) {
            mTTS.shutdown();
        }
    }

    private String letterSound(String letter) {
        return mLetterMap.get(letter);
    }
}
