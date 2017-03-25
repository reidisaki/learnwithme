package kalei.com.learnwithme.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Observer;
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
    protected static final float PITCH_VALUE = 1f;
    protected static final String CARTOON_FONT = "LDFComicSans.ttf";
    protected static final String IS_ENGLISH = "IS_ENGLISH";
    protected static final String IS_LETTER = "IS_LETTER";
    protected static boolean mIsReadMode = false;
    protected final String TAG = "lwm";//this.getClass().getSimpleName();

    protected Typeface custom_font;

    protected String[] mWordsArray = {"one", "two", "bat", "cat", "mat", "pat", "rat", "sat", "can", "fan", "man", "pan", "ran", "tan", "cap", "map", "nap",
            "tap", "sap", "bag", "wag", "tag", "rag", "dam", "ham", "jam", "ram", "yam", "bad", "dad", "had", "mad", "jar", "tar", "dry", "my", "all", "are",
            "ask", "mom", "and", "pad", "sad", "can", "fan", "pan", "ran", "van", "bed", "led", "red", "get", "let", "jet", "met", "net", "pet", "set", "wet",
            "den", "men", "pen", "ten", "beg", "leg", "peg", "keg", "egg", "bit", "fit", "hit", "kit", "mit", "pit", "sit", "big", "dig", "fig", "pig", "wig",
            "fin", "pin", "win", "bid", "did", "hid", "rid", "hip", "sip", "tip", "dip", "lip", "hop", "mop", "pop", "top", "dot", "got", "hot", "not", "pot",
            "rot", "box", "fox", "pox", "job", "mob", "rob", "sob", "bye", "bee", "see", "his", "her", "bye", "bee", "see", "cow", "how", "now", "bun", "fun",
            "run", "sun", "but", "cut", "gut", "hut", "nut", "rut", "bus", "cup", "pup", "cub", "rub", "tub", "bug", "dug", "hug", "mug", "rug", "tug", "see",
            "she", "bar", "car", "far", "hen"};

    protected String[] mKindergartenArray = {
            "was", "on", "are", "as", "hurt", "cat", "she", "fun", "the", "first", "be", "this", "from", "egg", "have", "not", "but", "all", "what", "four",
            "their", "if", "do", "how", "which", "them", "then", "ran", "so", "other", "has", "more", "her", "two", "like", "make", "than", "first", "been",
            "its", "over", "did", "down", "only", "long", "big", "blue", "very", "little", "after", "fur", "sir", "fly", "for", "shirt", "his", "they", "at",
            "he", "with", "go", "or", "by", "one", "said", "had", "up", "out", "come", "your", "when", "we", "can", "an", "there", "you", "and", "jump", "look",
            "each", "in", "is", "it", "came", "about", "him", "see", "time", "could", "know", "many", "some", "these", "into", "would", "who", "now", "made",
            "my", "people", "way", "may", "use", "find", "water", "words", "call", "just", "most", "where", "here", "am", "eat", "get", "funny", "ate", "good",
            "our", "any", "black", "say", "ask", "let", "sit", "brown", "help", "me", "not", "saw", "please", "were", "must", "no", "ride", "pretty", "old",
            "put", "stop", "open", "thank", "too", "well", "went", "wash", "yellow", "red", "run", "to", "new", "three"};

    protected Button mListenButton;
    protected int mIndex = 0, mCorrectSoundId, mAwwSoundId = 0;
    protected TextToSpeech mTTS;
    protected View mRootView;
    protected ImageView mLeftArrow, mRightArrow, mPuzzlePiece1, mPuzzlePiece2, mPuzzlePiece3, mPuzzlePiece4, mPuzzlePiece5, mPuzzlePiece6, mPuzzlePiece7, mPuzzlePiece8, mPuzzlePiece9, mCongratsImageView;
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
    protected View mCongratsView;

    protected Observable<String> wordObservable;
    protected Observer wordTextObserver;
    protected RelativeLayout mContentView;
    private static List<ImageView> mImageViewList;
    private static LinkedList<Integer> mImageIdLinkedList;
    private static List<Integer> mVisibleImageIdList; //when someone rotates the screen this keeps track of what views should be hidden/visible
    private boolean mIsHiragana = true;

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
                    loadHiraganaArray();
                    loadJapaneseMap();
                    break;
                case "katakana":
                    mIsHiragana = false;
                    loadKatakanaArray();
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

        if (mIsReadMode) {
            mRootView = inflater.inflate(R.layout.fragment_game, viewGroup);
        } else {
            mRootView = inflater.inflate(R.layout.fragment_game_spell, viewGroup);
        }
        initViews();
        //this will reset and hide all pieces the user has already hid
        resetShowCongratsPiece();
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
        mCongratsView = mRootView.findViewById(R.id.fragment_congrats);
        mContentView = (RelativeLayout) mRootView.findViewById(R.id.content_view);
        mPuzzlePiece1 = (ImageView) mRootView.findViewById(R.id.piece_1);
        mPuzzlePiece2 = (ImageView) mRootView.findViewById(R.id.piece_2);
        mPuzzlePiece3 = (ImageView) mRootView.findViewById(R.id.piece_3);
        mPuzzlePiece4 = (ImageView) mRootView.findViewById(R.id.piece_4);
        mPuzzlePiece5 = (ImageView) mRootView.findViewById(R.id.piece_5);
        mPuzzlePiece6 = (ImageView) mRootView.findViewById(R.id.piece_6);
        mPuzzlePiece7 = (ImageView) mRootView.findViewById(R.id.piece_7);
        mPuzzlePiece8 = (ImageView) mRootView.findViewById(R.id.piece_8);
        mPuzzlePiece9 = (ImageView) mRootView.findViewById(R.id.piece_9);
        mCongratsImageView = (ImageView) mRootView.findViewById(R.id.congrats_img);
        initImageViewList();
    }

    private void initImageViewList() {
        mImageViewList = new ArrayList<>();
        mImageViewList.add(mPuzzlePiece1);
        mImageViewList.add(mPuzzlePiece2);
        mImageViewList.add(mPuzzlePiece3);
        mImageViewList.add(mPuzzlePiece4);
        mImageViewList.add(mPuzzlePiece5);
        mImageViewList.add(mPuzzlePiece6);
        mImageViewList.add(mPuzzlePiece7);
        mImageViewList.add(mPuzzlePiece8);
        mImageViewList.add(mPuzzlePiece9);
    }

    private void shuffleIntegerArray() {
        Collections.shuffle(mImageIdLinkedList);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState == null) {
            mRootView = inflater.inflate(setLayout(), container, false);
            randomizeWords();
            initViews();
            mImageIdLinkedList = new LinkedList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
            mVisibleImageIdList = new ArrayList<>();
            shuffleIntegerArray();
            mRootView.setBackgroundColor(generateRandomColor());
        }
        return mRootView;
    }

    protected abstract int setLayout();

    protected void correctAnswer() {
        startAnimation();
    }

    private void startAnimation() {
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        mCongratsView.startAnimation(fadeIn);
        final Animation fadeInContentView = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_game);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCongratsView.setVisibility(View.VISIBLE);
                showCongratsPiece();

                mContentView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                        fadeOut.setAnimationListener(new AnimationListener() {
                            @Override
                            public void onAnimationStart(final Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(final Animation animation) {
                                mContentView.startAnimation(fadeInContentView);
                                loadNextWord();
                            }

                            @Override
                            public void onAnimationRepeat(final Animation animation) {

                            }
                        });
                        mCongratsView.startAnimation(fadeOut);
                    }
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        fadeInContentView.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                mCongratsView.setVisibility(View.GONE);
                mContentView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });
    }

    protected void resetShowCongratsPiece() {

        for (int i : mVisibleImageIdList) {
            mImageViewList.get(i).setVisibility(View.INVISIBLE);
        }
    }

    protected void showCongratsPiece() {
        if (mImageIdLinkedList.size() > 0) {
            int imageId = mImageIdLinkedList.remove(0);
            mVisibleImageIdList.add(imageId);
            mImageViewList.get(imageId).setVisibility(View.INVISIBLE);
        } else {
            resetCongratsViewPieces();
        }
    }

    protected abstract void wrongAnswer();

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
                //todo: undo this commenting this was only for testing
//                loadNextWord();
                break;
        }
    }

    public abstract void loadNextWord();
    public abstract void getPrevWord();

    /**
     * Ask the current default engine to launch the matching INSTALL_TTS_DATA activity so the required TTS files are properly installed.
     */
    private void installVoiceData() {
        Intent intent = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.tts"/*replace with the package name of the target TTS engine*/);
        try {
            Log.v(TAG, "Installing voice data: " + intent.toUri(0));
            //todo: start activty for result and handle the new data. 
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Log.e(TAG, "Failed to install TTS data, no acitivty found for " + intent + ")");
        }
    }

    protected void setupVoice() {
        mTTS.setSpeechRate(SPEECH_RATE);
        Set<Voice> voices = mTTS.getVoices();

        if (voices != null) {
            for (Voice v : voices) {
                //could use a british voice here
                //todo: make users download a language pack when they open the app
                if (v.getLocale().equals(Locale.getDefault()) && v.getQuality() == Voice.QUALITY_HIGH) {
                    Log.i("reid", v.toString());
//                mTTS.setVoice(v);
                }
                if (v.getQuality() == Voice.QUALITY_HIGH && v.getLatency() == Voice.LATENCY_LOW && v.getLocale().equals(Locale.getDefault())) {
//                    mTTS.setVoice(v);
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
            mTTS.speak(mWordsArray[mIndex].toLowerCase(), TextToSpeech.QUEUE_FLUSH, null, "");
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
        if (BuildConfig.BUILD_TYPE.toString().equals("debug")) {
            return;
        }
        //show interstitial every 10 times
        if (mIndex != 0 && mIndex % NUM_TIMES_BEFORE_INTERSTITIAL_SHOWN == 0) {
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
        if (mIsHiragana) {
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
        } else {
            //katakana

            mJapaneseLetterMap.put("\u30A1", "a");
            mJapaneseLetterMap.put("\u30A2", "a");
            mJapaneseLetterMap.put("\u30A3", "i");
            mJapaneseLetterMap.put("\u30A4", "i");
            mJapaneseLetterMap.put("\u30A5", "oo");
            mJapaneseLetterMap.put("\u30A6", "oo");
            mJapaneseLetterMap.put("\u30A7", "eh");
            mJapaneseLetterMap.put("\u30A8", "eh");
            mJapaneseLetterMap.put("\u30A9", "oh");
            mJapaneseLetterMap.put("\u30AA", "oh");
            mJapaneseLetterMap.put("\u30AB", "ka");
            mJapaneseLetterMap.put("\u30AC", "ga");
            mJapaneseLetterMap.put("\u30AD", "ki");
            mJapaneseLetterMap.put("\u30AE", "gi");
            mJapaneseLetterMap.put("\u30AF", "ku");
            mJapaneseLetterMap.put("\u30B0", "gu");
            mJapaneseLetterMap.put("\u30B1", "ke");
            mJapaneseLetterMap.put("\u30B2", "ge");
            mJapaneseLetterMap.put("\u30B3", "ko");
            mJapaneseLetterMap.put("\u30B4", "go");
            mJapaneseLetterMap.put("\u30B5", "sa");
            mJapaneseLetterMap.put("\u30B6", "za");
            mJapaneseLetterMap.put("\u30B7", "si");
            mJapaneseLetterMap.put("\u30B8", "zi");
            mJapaneseLetterMap.put("\u30B9", "su");
            mJapaneseLetterMap.put("\u30BA", "zu");
            mJapaneseLetterMap.put("\u30BB", "se");
            mJapaneseLetterMap.put("\u30BC", "ze");
            mJapaneseLetterMap.put("\u30BD", "so");
            mJapaneseLetterMap.put("\u30BE", "zo");
            mJapaneseLetterMap.put("\u30BF", "ta");
            mJapaneseLetterMap.put("\u30C0", "da");
            mJapaneseLetterMap.put("\u30C1", "ti");
            mJapaneseLetterMap.put("\u30C2", "di");
            mJapaneseLetterMap.put("\u30C3", "tu");
            mJapaneseLetterMap.put("\u30C4", "tu");
            mJapaneseLetterMap.put("\u30C5", "du");
            mJapaneseLetterMap.put("\u30C6", "te");
            mJapaneseLetterMap.put("\u30C7", "de");
            mJapaneseLetterMap.put("\u30C8", "to");
            mJapaneseLetterMap.put("\u30C9", "do");
            mJapaneseLetterMap.put("\u30CA", "na");
            mJapaneseLetterMap.put("\u30CB", "ni");
            mJapaneseLetterMap.put("\u30CC", "nu");
            mJapaneseLetterMap.put("\u30CD", "ne");
            mJapaneseLetterMap.put("\u30CE", "no");
            mJapaneseLetterMap.put("\u30CF", "ha");
            mJapaneseLetterMap.put("\u30D0", "ba");
            mJapaneseLetterMap.put("\u30D1", "pa");
            mJapaneseLetterMap.put("\u30D2", "hi");
            mJapaneseLetterMap.put("\u30D3", "bi");
            mJapaneseLetterMap.put("\u30D4", "pi");
            mJapaneseLetterMap.put("\u30D5", "hu");
            mJapaneseLetterMap.put("\u30D6", "bu");
            mJapaneseLetterMap.put("\u30D7", "pu");
            mJapaneseLetterMap.put("\u30D8", "he");
            mJapaneseLetterMap.put("\u30D9", "be");
            mJapaneseLetterMap.put("\u30DA", "pe");
            mJapaneseLetterMap.put("\u30DB", "ho");
            mJapaneseLetterMap.put("\u30DC", "bo");
            mJapaneseLetterMap.put("\u30DD", "po");
            mJapaneseLetterMap.put("\u30DE", "ma");
            mJapaneseLetterMap.put("\u30DF", "mi");
            mJapaneseLetterMap.put("\u30E0", "mu");
            mJapaneseLetterMap.put("\u30E1", "me");
            mJapaneseLetterMap.put("\u30E2", "mo");
            mJapaneseLetterMap.put("\u30E3", "ya");
            mJapaneseLetterMap.put("\u30E4", "ya");
            mJapaneseLetterMap.put("\u30E5", "yu");
            mJapaneseLetterMap.put("\u30E6", "yu");
            mJapaneseLetterMap.put("\u30E7", "yo");
            mJapaneseLetterMap.put("\u30E8", "yo");
            mJapaneseLetterMap.put("\u30E9", "ra");
            mJapaneseLetterMap.put("\u30EA", "ri");
            mJapaneseLetterMap.put("\u30EB", "ru");
            mJapaneseLetterMap.put("\u30EC", "re");
            mJapaneseLetterMap.put("\u30ED", "ro");
            mJapaneseLetterMap.put("\u30EE", "wa");
            mJapaneseLetterMap.put("\u30EF", "wa");
            mJapaneseLetterMap.put("\u30F0", "wi");
            mJapaneseLetterMap.put("\u30F1", "we");
            mJapaneseLetterMap.put("\u30F2", "wo");
            mJapaneseLetterMap.put("\u30F3", "n");
            mJapaneseLetterMap.put("\u30F4", "vu");
            mJapaneseLetterMap.put("\u30F5", "ka");
            mJapaneseLetterMap.put("\u30F6", "ke");
            mJapaneseLetterMap.put("\u30F7", "va");
            mJapaneseLetterMap.put("\u30F8", "vi");
            mJapaneseLetterMap.put("\u30F9", "ve");
            mJapaneseLetterMap.put("\u30FA", "vo");
        }
    }

    protected void loadHiraganaArray() {
        mUnicodeArray = new String[]{"\u3042", "\u3044", "\u3046", "\u3048", "\u304a", "\u304b", "\u304c", "\u304d", "\u304e", "\u304f", "\u3050",
                "\u3051", "\u3052", "\u3053", "\u3054", "\u3055", "\u3056", "\u3057", "\u3058", "\u3059", "\u305a", "\u305b", "\u305c", "\u305d",
                "\u305e", "\u305f", "\u3060", "\u3061", "\u3062", "\u3064", "\u3065", "\u3066", "\u3067", "\u3068", "\u3069", "\u306a", "\u306b",
                "\u306c", "\u306d", "\u306e", "\u306f", "\u3070", "\u3071", "\u3072", "\u3073", "\u3074", "\u3075", "\u3076", "\u3077", "\u3078",
                "\u3079", "\u307a", "\u307b", "\u307c", "\u307d", "\u307e", "\u307f", "\u3080", "\u3081", "\u3082", "\u3084", "\u3086", "\u3088",
                "\u3089", "\u308a", "\u308b", "\u308c", "\u308d", "\u308f", "\u3090", "\u3091", "\u3091", "\u3093", "\u3094"};
    }

    protected void loadKatakanaArray() {
        mUnicodeArray = new String[]{"\u30A1", "\u30A2", "\u30A3", "\u30A4", "\u30A5", "\u30A6", "\u30A7", "\u30A8", "\u30A9", "\u30AA", "\u30AB",
                "\u30AC", "\u30AD", "\u30AE", "\u30AF", "\u30B0", "\u30B1", "\u30B2", "\u30B3", "\u30B4", "\u30B5", "\u30B6", "\u30B7", "\u30B8", "\u30B9",
                "\u30BA", "\u30BB", "\u30BC", "\u30BD", "\u30BE", "\u30BF", "\u30C0", "\u30C1", "\u30C2", "\u30C3", "\u30C4", "\u30C5", "\u30C6", "\u30C7",
                "\u30C8", "\u30C9", "\u30CA", "\u30CB", "\u30CC", "\u30CD", "\u30CE", "\u30CF", "\u30D0", "\u30D1", "\u30D2", "\u30D3", "\u30D4", "\u30D5",
                "\u30D6", "\u30D7", "\u30D8", "\u30D9", "\u30DA", "\u30DB", "\u30DC", "\u30DD", "\u30DE", "\u30DF", "\u30E0", "\u30E1", "\u30E2", "\u30E3",
                "\u30E4", "\u30E5", "\u30E6", "\u30E7", "\u30E8", "\u30E9", "\u30EA", "\u30EB", "\u30EC", "\u30ED", "\u30EE", "\u30EF", "\u30F0", "\u30F1",
                "\u30F2", "\u30F3", "\u30F4", "\u30F5", "\u30F6", "\u30F7", "\u30F8", "\u30F9", "\u30FA"};
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

    protected int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    protected void resetCongratsViewPieces() {
        Drawable drawable = getResources()
                .getDrawable(getResources().getIdentifier("congrats_" + getRandomNumber(1, 4), "drawable", getActivity().getPackageName()), null);

        mCongratsImageView.setImageDrawable(drawable);
        for (ImageView im : mImageViewList) {
            im.setVisibility(View.VISIBLE);
        }
        mImageIdLinkedList = new LinkedList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
        mVisibleImageIdList.clear();
        Collections.shuffle(mImageIdLinkedList);

        //show the first piece
        showCongratsPiece();
    }
}
