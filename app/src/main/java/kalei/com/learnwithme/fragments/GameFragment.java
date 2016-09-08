package kalei.com.learnwithme.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import kalei.com.learnwithme.R;

/**
 * Created by risaki on 9/7/16.
 */
public class GameFragment extends LearnWithMeFragment implements OnClickListener {
    private String[] mWordsArray = {"one", "two", "bat", "cat", "mat", "pat", "rat", "sat", "can", "fan", "man", "pan", "ran", "tan", "cap", "map", "nap",
            "tap",
            "sap", "bag", "wag", "tag", "rag", "dam", "ham", "jam", "ram", "yam", "bad", "dad", "had", "mad", "jar", "tar", "dry", "my,", "all", "are", "ask",
            "mom", "and", "pad", "sad", "can", "fan", "pan", "ran", "van", "bed", "led", "red", "get", "let", "jet", "met", "net", "pet", "set", "wet", "den",
            "hen", "men", "pen", "ten", "beg", "leg", "peg", "keg", "egg", "bit", "fit", "hit", "kit", "mit", "pit", "sit", "big", "dig", "fig", "pig", "wig",
            "fin", "pin", "win", "bid", "did", "hid", "rid", "hip", "sip", "tip", "dip", "lip", "hop", "mop", "pop", "top", "dot", "got", "hot", "not", "pot",
            "rot", "box", "fox", "pox", "job", "mob", "rob", "sob", "bye", "bee", "see", "his", "her", "bye", "bee", "see", "cow", "how", "now", "bun", "fun",
            "run", "sun", "but", "cut", "gut", "hut", "nut", "rut", "bus", "cup", "pup", "cub", "rub", "tub", "bug", "dug", "hug", "mug", "rug", "tug", "see",
            "she", "bar", "car", "far" };

    private Button mPlayButton;
    private TextView mWordTextView;
    private int mIndex = 0;
    private static final int REQ_CODE_SPEECH_INPUT = 1234;

    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        randomizeWords();
//        ((MainActivity) getActivity()).getActionBar().hide();
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        mPlayButton = (Button) rootView.findViewById(R.id.play_btn);
        mWordTextView = (TextView) rootView.findViewById(R.id.word_txt);
        mWordTextView.setText(mWordsArray[mIndex]);
//        mSettingsImage = (ImageView) rootView.findViewById(R.id.settings_image);
        mPlayButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.play_btn) {
            promptSpeechInput();
        }
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
                    Toast.makeText(getActivity(),
                            result.get(0),
                            Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
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
}
