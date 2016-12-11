package kalei.com.learnwithme.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import kalei.com.learnwithme.BuildConfig;
import kalei.com.learnwithme.R;
import kalei.com.learnwithme.activities.LearnWithMeActivity.LearnWithMeAdListener;
import kalei.com.learnwithme.models.Letter;

/**
 * Created by risaki on 9/7/16.
 */
public abstract class GameFragment extends LearnWithMeFragment implements OnClickListener, OnLoadCompleteListener {
    protected static final int NUM_TIMES_BEFORE_INTERSTITIAL_SHOWN = 10;
    protected static final float SPEECH_RATE = 1f;
    protected static final float THRESHOLD_CORRECT_PERCENTAGE = .75f;
    protected static final float PITCH_VALUE = 1.1f;
    protected static final String CARTOON_FONT = "LDFComicSans.ttf";
    protected static final String IS_ENGLISH = "IS_ENGLISH";
    protected static final String IS_LETTER = "IS_LETTER";
    protected static boolean mIsReadMode = false;
    protected final String TAG = "lwm";//this.getClass().getSimpleName();

    protected Typeface custom_font;

    protected String[] mWordsArray = {"one", "two", "bat", "cat", "mat", "pat", "rat", "sat", "can", "fan", "man", "pan", "ran", "tan", "cap", "map", "nap",
            "tap", "sap", "bag", "wag", "tag", "rag", "dam", "ham", "jam", "ram", "yam", "bad", "dad", "had", "mad", "jar", "tar", "dry", "my,", "all", "are",
            "ask",
            "mom", "and", "pad", "sad", "can", "fan", "pan", "ran", "van", "bed", "led", "red", "get", "let", "jet", "met", "net", "pet", "set", "wet", "den",
            "hen", "men", "pen", "ten", "beg", "leg", "peg", "keg", "egg", "bit", "fit", "hit", "kit", "mit", "pit", "sit", "big", "dig", "fig", "pig", "wig",
            "fin", "pin", "win", "bid", "did", "hid", "rid", "hip", "sip", "tip", "dip", "lip", "hop", "mop", "pop", "top", "dot", "got", "hot", "not", "pot",
            "rot", "box", "fox", "pox", "job", "mob", "rob", "sob", "bye", "bee", "see", "his", "her", "bye", "bee", "see", "cow", "how", "now", "bun", "fun",
            "run", "sun", "but", "cut", "gut", "hut", "nut", "rut", "bus", "cup", "pup", "cub", "rub", "tub", "bug", "dug", "hug", "mug", "rug", "tug", "see",
            "she", "bar", "car", "far"};

    protected String[] mKindergartenArray = {
            "WAS", "ON", "ARE", "AS", "HURT", "CAT", "SHE", "FUN", "THE", "FIRST", "BE", "THIS", "FROM", "EGG", "HAVE", "NOT", "BUT", "ALL", "WHAT", "FOUR",
            "THEIR", "IF", "DO", "HOW", "WHICH", "THEM", "THEN", "RAN", "SO", "OTHER", "HAS", "MORE", "HER", "TWO", "LIKE", "MAKE", "THAN", "FIRST", "BEEN",
            "ITS", "OVER", "DID", "DOWN", "ONLY", "LONG", "BIG", "BLUE", "VERY", "LITTLE", "AFTER", "FUR", "SIR", "FLY", "FOR", "SHIRT", "HIS", "THEY", "AT",
            "HE", "WITH", "GO", "OR", "BY", "ONE", "SAID", "HAD", "UP", "OUT", "COME", "YOUR", "WHEN", "WE", "CAN", "AN", "THERE", "YOU", "AND", "JUMP", "LOOK",
            "EACH", "IN", "IS", "IT", "CAME", "ABOUT", "HIM", "SEE", "TIME", "COULD", "KNOW", "MANY", "SOME", "THESE", "INTO", "WOULD", "WHO", "NOW", "MADE",
            "MY", "PEOPLE", "WAY", "MAY", "USE", "FIND", "WATER", "WORDS", "CALL", "JUST", "MOST", "WHERE", "HERE", "AM", "EAT", "GET", "FUNNY", "ATE", "GOOD",
            "OUR", "ANY", "BLACK", "SAY", "ASK", "LET", "SIT", "BROWN", "HELP", "ME", "NOT", "SAW", "PLEASE", "WERE", "MUST", "NO", "RIDE", "PRETTY", "OLD",
            "PUT", "STOP", "OPEN", "THANK", "TOO", "WELL", "WENT", "WASH", "YELLOW", "RED", "RUN", "TO", "NEW", "THREE"};

    protected Button mListenButton;
    protected int mIndex = 0, mCorrectSoundId, mAwwSoundId = 0;
    protected TextToSpeech mTTS;
    protected View mRootView;
    protected ImageView mLeftArrow, mRightArrow;
    protected static final int REQ_CODE_SPEECH_INPUT = 1234;
    protected LearnWithMeAdListener adListener;
    protected HashMap<String, String> mLetterMap;
    protected HashMap<String, String> mJapaneseLetterMap;
    protected ArrayList<Letter> mLetterList;
    protected String[] mUnicodeArray;
    protected SpannableString mContent;
    protected Boolean mCanContinue = false;
    protected static String GAME_TYPE = "game_type_label";
    protected String mGameType;
    protected SoundPool mSound;
    protected TextView mEnglishWord;

    protected int mASoundId, mBSoundId, mCSoundId, mDSoundId, mESoundId, mFSoundId, mGSoundId, mHSoundId, mISoundId, mJSoundId, mKSoundId, mLSoundId, mMSoundId, mNSoundId, mOSoundId, mPSoundId, mQSoundId, mRSoundId, mSSoundId, mTSoundId, mUSoundId, mVSoundId, mWSoundId, mXSoundId, mYSoundId, mZSoundId;
    protected float mPitch;
    protected boolean mIsEnglish;
    protected boolean mIsLetter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            adListener = (LearnWithMeAdListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        Bundle args = getArguments();
        if (args != null) {
            mGameType = args.getString(GAME_TYPE);
            mIsEnglish = args.getBoolean(IS_ENGLISH, true);
            mIsLetter = args.getBoolean(IS_LETTER, false);
            mIsReadMode = args.getBoolean("MODE", false);
            Log.i(TAG, "gameType: " + mGameType);
            switch (mGameType) {
                case "kindergarten":
                    mWordsArray = mKindergartenArray;
                    break;
                case "hiragana":
                    loadJapaneseArray();
                    loadJapaneseMap();
                    break;
            }
        }

        setRetainInstance(true);
        mPitch = 1.f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSound = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(6).build();
        } else {

            mSound = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        mSound.setOnLoadCompleteListener(this);

        mCorrectSoundId = mSound.load(getActivity(), R.raw.correct, 1);
        mAwwSoundId = mSound.load(getActivity(), R.raw.aww, 1);
        mLetterList = new ArrayList<>();
        initLetterMap();
        custom_font = Typeface.createFromAsset(getResources().getAssets(), CARTOON_FONT);
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
    }

    protected void initViews() {

        mLeftArrow = (ImageView) mRootView.findViewById(R.id.left_arrow_img);
        if (mIndex == 0) {
            mLeftArrow.setVisibility(View.GONE);
        }
        mListenButton = (Button) mRootView.findViewById(R.id.listen_btn);
        mRightArrow = (ImageView) mRootView.findViewById(R.id.right_arrow_img);
        mRightArrow.setOnClickListener(this);
        mLeftArrow.setOnClickListener(this);
        mEnglishWord = (TextView) mRootView.findViewById(R.id.english_word);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            mRootView = inflater.inflate(setLayout(), container, false);
            randomizeWords();
            initViews();
            mRootView.setBackgroundColor(generateRandomColor());
        }

        return mRootView;
    }

    protected abstract int setLayout();

    protected void loadLetterMap(String s) {
        mLetterList = new ArrayList<>();
        int index = 0;
        for (String letter : s.split("(?!^)")) {
            mLetterList.add(new Letter(letter, false, index));
            index++;
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.left_arrow_img:
                getPrevWord();
                break;
            case R.id.right_arrow_img:
                Log.i(TAG, "right arrow clicked");
                loadNextWord();
                break;
        }
    }

    public abstract void loadNextWord();
    public abstract void getPrevWord();

    protected void setupVoice() {
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

    protected void sayWord() {
        setupVoice();

        //todo: handle other languages
        if (mIsEnglish) {
            mTTS.setLanguage(Locale.ENGLISH);
            mTTS.speak(mWordsArray[mIndex], TextToSpeech.QUEUE_FLUSH, null, "");
        } else {
            mTTS.setLanguage(Locale.JAPANESE);
            mTTS.speak(mUnicodeArray[mIndex], TextToSpeech.QUEUE_FLUSH, null, "");
            mEnglishWord.setVisibility(View.VISIBLE);
            mEnglishWord.setText(mJapaneseLetterMap.get(mUnicodeArray[mIndex]));
            mCanContinue = true;
        }
    }

    protected void showInterstitial() {
        //bypass ads for dev build
        if (BuildConfig.DEBUG) {
            return;
        }
        //show interstitial every 10 times
        if (mIndex % NUM_TIMES_BEFORE_INTERSTITIAL_SHOWN == 0) {
            adListener.createAndShowAd();
        }
        if (mIndex >= mWordsArray.length) {
            mIndex = 0;
        }
        mRootView.setBackgroundColor(generateRandomColor());
    }

    public void randomizeWords() {
        int currentIndex = mWordsArray.length - 1, randomIndex;
        String temporaryValue;

        if (mIsEnglish) {
            // While there remain elements to shuffle...
            while (0 < currentIndex) {

                // Pick a remaining element...
                randomIndex = (int) Math.floor(Math.random() * currentIndex);
                currentIndex -= 1;

                // And swap it with the current element.
                temporaryValue = mWordsArray[currentIndex];
                mWordsArray[currentIndex] = mWordsArray[randomIndex];
                mWordsArray[randomIndex] = temporaryValue;
            }
        } else {
            //Randomize japanese words
            currentIndex = mUnicodeArray.length - 1;
            while (0 < currentIndex) {

                // Pick a remaining element...
                randomIndex = (int) Math.floor(Math.random() * currentIndex);
                currentIndex -= 1;

                // And swap it with the current element.
                temporaryValue = mUnicodeArray[currentIndex];
                mUnicodeArray[currentIndex] = mUnicodeArray[randomIndex];
                mUnicodeArray[randomIndex] = temporaryValue;
            }
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

    @Override
    public void onPause() {
        super.onPause();
        if (mTTS != null) {
            mTTS.shutdown();
        }
    }

    protected void loadJapaneseMap() {
        mJapaneseLetterMap = new HashMap<>();
        mJapaneseLetterMap.put("\u3042", "a");
        mJapaneseLetterMap.put("\u3044", "i");
        mJapaneseLetterMap.put("\u3046", "oo");
        mJapaneseLetterMap.put("\u3048", "eh");
        mJapaneseLetterMap.put("\u304a", "oh");
        mJapaneseLetterMap.put("\u304b", "ka");
        mJapaneseLetterMap.put("\u304c", "ga");
        mJapaneseLetterMap.put("\u304d", "ki");
        mJapaneseLetterMap.put("\u304e", "gi");
        mJapaneseLetterMap.put("\u304f", "ku");
        mJapaneseLetterMap.put("\u3050", "gu");
        mJapaneseLetterMap.put("\u3051", "ke");
        mJapaneseLetterMap.put("\u3052", "ge");
        mJapaneseLetterMap.put("\u3053", "ko");
        mJapaneseLetterMap.put("\u3054", "go");
        mJapaneseLetterMap.put("\u3055", "sa");
        mJapaneseLetterMap.put("\u3056", "za");
        mJapaneseLetterMap.put("\u3057", "shi");
        mJapaneseLetterMap.put("\u3058", "gee");
        mJapaneseLetterMap.put("\u3059", "su");
        mJapaneseLetterMap.put("\u305a", "zu");
        mJapaneseLetterMap.put("\u305b", "se");
        mJapaneseLetterMap.put("\u305c", "ze");
        mJapaneseLetterMap.put("\u305d", "so");
        mJapaneseLetterMap.put("\u305e", "zo");
        mJapaneseLetterMap.put("\u305f", "ta");
        mJapaneseLetterMap.put("\u3060", "da");
        mJapaneseLetterMap.put("\u3061", "ti");
        mJapaneseLetterMap.put("\u3062", "di");
        mJapaneseLetterMap.put("\u3064", "tsu");
        mJapaneseLetterMap.put("\u3065", "du");
        mJapaneseLetterMap.put("\u3066", "te");
        mJapaneseLetterMap.put("\u3067", "de");
        mJapaneseLetterMap.put("\u3068", "to");
        mJapaneseLetterMap.put("\u3069", "do");
        mJapaneseLetterMap.put("\u306a", "na");
        mJapaneseLetterMap.put("\u306b", "ni");
        mJapaneseLetterMap.put("\u306c", "nu");
        mJapaneseLetterMap.put("\u306d", "ne");
        mJapaneseLetterMap.put("\u306e", "no");
        mJapaneseLetterMap.put("\u306f", "ha");
        mJapaneseLetterMap.put("\u3070", "ba");
        mJapaneseLetterMap.put("\u3071", "pa");
        mJapaneseLetterMap.put("\u3072", "hi");
        mJapaneseLetterMap.put("\u3073", "bi");
        mJapaneseLetterMap.put("\u3074", "pi");
        mJapaneseLetterMap.put("\u3075", "fu");
        mJapaneseLetterMap.put("\u3076", "bu");
        mJapaneseLetterMap.put("\u3077", "pu");
        mJapaneseLetterMap.put("\u3078", "he");
        mJapaneseLetterMap.put("\u3079", "be");
        mJapaneseLetterMap.put("\u307a", "pe");
        mJapaneseLetterMap.put("\u307b", "ho");
        mJapaneseLetterMap.put("\u307c", "bo");
        mJapaneseLetterMap.put("\u307d", "po");
        mJapaneseLetterMap.put("\u307e", "ma");
        mJapaneseLetterMap.put("\u307f", "mi");
        mJapaneseLetterMap.put("\u3080", "mu");
        mJapaneseLetterMap.put("\u3081", "me");
        mJapaneseLetterMap.put("\u3082", "mo");
        mJapaneseLetterMap.put("\u3084", "ya");
        mJapaneseLetterMap.put("\u3086", "yu");
        mJapaneseLetterMap.put("\u3088", "yo");
        mJapaneseLetterMap.put("\u3089", "ra");
        mJapaneseLetterMap.put("\u308a", "ri");
        mJapaneseLetterMap.put("\u308b", "ru");
        mJapaneseLetterMap.put("\u308c", "re");
        mJapaneseLetterMap.put("\u308d", "ro");
        mJapaneseLetterMap.put("\u308f", "wa");
        mJapaneseLetterMap.put("\u3090", "wi");
        mJapaneseLetterMap.put("\u3091", "we");
        mJapaneseLetterMap.put("\u3092", "wo");
        mJapaneseLetterMap.put("\u3093", "n");
        mJapaneseLetterMap.put("\u3094", "vu");

//        mJapaneseLetterMap = new HashMap<>();
//        mJapaneseLetterMap.put("a", "\u3042");
//        mJapaneseLetterMap.put("i", "\u3044");
//        mJapaneseLetterMap.put("oo", "\u3046");
//        mJapaneseLetterMap.put("eh", "\u3048");
//        mJapaneseLetterMap.put("oh", "\u304a");
//        mJapaneseLetterMap.put("ka", "\u304b");
//        mJapaneseLetterMap.put("ga", "\u304c");
//        mJapaneseLetterMap.put("ki", "\u304d");
//        mJapaneseLetterMap.put("gi", "\u304e");
//        mJapaneseLetterMap.put("ku", "\u304f");
//        mJapaneseLetterMap.put("gu", "\u3050");
//        mJapaneseLetterMap.put("ke", "\u3051");
//        mJapaneseLetterMap.put("ge", "\u3052");
//        mJapaneseLetterMap.put("ko", "\u3053");
//        mJapaneseLetterMap.put("go", "\u3054");
//        mJapaneseLetterMap.put("sa", "\u3055");
//        mJapaneseLetterMap.put("za", "\u3056");
//        mJapaneseLetterMap.put("shi", "\u3057");
//        mJapaneseLetterMap.put("gee", "\u3058");
//        mJapaneseLetterMap.put("su", "\u3059");
//        mJapaneseLetterMap.put("zu", "\u305a");
//        mJapaneseLetterMap.put("se", "\u305b");
//        mJapaneseLetterMap.put("ze", "\u305c");
//        mJapaneseLetterMap.put("so", "\u305d");
//        mJapaneseLetterMap.put("zo", "\u305e");
//        mJapaneseLetterMap.put("ta", "\u305f");
//        mJapaneseLetterMap.put("da", "\u3060");
//        mJapaneseLetterMap.put("ti", "\u3061");
//        mJapaneseLetterMap.put("di", "\u3062");
//        mJapaneseLetterMap.put("tu", "\u3064");
//        mJapaneseLetterMap.put("du", "\u3065");
//        mJapaneseLetterMap.put("te", "\u3066");
//        mJapaneseLetterMap.put("de", "\u3067");
//        mJapaneseLetterMap.put("to", "\u3068");
//        mJapaneseLetterMap.put("do", "\u3069");
//        mJapaneseLetterMap.put("na", "\u306a");
//        mJapaneseLetterMap.put("ni", "\u306b");
//        mJapaneseLetterMap.put("nu", "\u306c");
//        mJapaneseLetterMap.put("ne", "\u306d");
//        mJapaneseLetterMap.put("no", "\u306e");
//        mJapaneseLetterMap.put("ha", "\u306f");
//        mJapaneseLetterMap.put("ba", "\u3070");
//        mJapaneseLetterMap.put("pa", "\u3071");
//        mJapaneseLetterMap.put("hi", "\u3072");
//        mJapaneseLetterMap.put("bi", "\u3073");
//        mJapaneseLetterMap.put("pi", "\u3074");
//        mJapaneseLetterMap.put("hu", "\u3075");
//        mJapaneseLetterMap.put("bu", "\u3076");
//        mJapaneseLetterMap.put("pu", "\u3077");
//        mJapaneseLetterMap.put("he", "\u3078");
//        mJapaneseLetterMap.put("be", "\u3079");
//        mJapaneseLetterMap.put("pe", "\u307a");
//        mJapaneseLetterMap.put("ho", "\u307b");
//        mJapaneseLetterMap.put("bo", "\u307c");
//        mJapaneseLetterMap.put("po", "\u307d");
//        mJapaneseLetterMap.put("ma", "\u307e");
//        mJapaneseLetterMap.put("mi", "\u307f");
//        mJapaneseLetterMap.put("mu", "\u3080");
//        mJapaneseLetterMap.put("me", "\u3081");
//        mJapaneseLetterMap.put("mo", "\u3082");
//        mJapaneseLetterMap.put("ya", "\u3084");
//        mJapaneseLetterMap.put("yu", "\u3086");
//        mJapaneseLetterMap.put("yo", "\u3088");
//        mJapaneseLetterMap.put("ra", "\u3089");
//        mJapaneseLetterMap.put("ri", "\u308a");
//        mJapaneseLetterMap.put("ru", "\u308b");
//        mJapaneseLetterMap.put("re", "\u308c");
//        mJapaneseLetterMap.put("ro", "\u308d");
//        mJapaneseLetterMap.put("wa", "\u308f");
//        mJapaneseLetterMap.put("wi", "\u3090");
//        mJapaneseLetterMap.put("we", "\u3091");
//        mJapaneseLetterMap.put("wo", "\u3091");
//        mJapaneseLetterMap.put("n", "\u3093");
//        mJapaneseLetterMap.put("vu", "\u3094");
    }

    protected void loadJapaneseArray() {
        mUnicodeArray = new String[]{"\u3042", "\u3044", "\u3046", "\u3048", "\u304a", "\u304b", "\u304c", "\u304d", "\u304e", "\u304f", "\u3050",
                "\u3051", "\u3052", "\u3053", "\u3054", "\u3055", "\u3056", "\u3057", "\u3058", "\u3059", "\u305a", "\u305b", "\u305c", "\u305d",
                "\u305e", "\u305f", "\u3060", "\u3061", "\u3062", "\u3064", "\u3065", "\u3066", "\u3067", "\u3068", "\u3069", "\u306a", "\u306b",
                "\u306c", "\u306d", "\u306e", "\u306f", "\u3070", "\u3071", "\u3072", "\u3073", "\u3074", "\u3075", "\u3076", "\u3077", "\u3078",
                "\u3079", "\u307a", "\u307b", "\u307c", "\u307d", "\u307e", "\u307f", "\u3080", "\u3081", "\u3082", "\u3084", "\u3086", "\u3088",
                "\u3089", "\u308a", "\u308b", "\u308c", "\u308d", "\u308f", "\u3090", "\u3091", "\u3091", "\u3093", "\u3094"};
    }

    @Override
    public void onLoadComplete(final SoundPool soundPool, final int sampleId, final int status) {

//        mSoundsLoaded++;
//        if (mSoundsLoaded == 26) {
//            mProgress.dismiss();
//        }
    }

    protected void initLetterMap() {
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
}
