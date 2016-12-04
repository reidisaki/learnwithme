package kalei.com.learnwithme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import kalei.com.learnwithme.R;
import kalei.com.learnwithme.activities.GameActivity;

/**
 * Created by risaki on 8/28/16.
 */
public class MenuFragment extends LearnWithMeFragment implements OnClickListener {

    private Button threeLetterButton;
    private Button kindergartenButton;
    private Button japaneseButton;
    private Button katakanaButton;

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

//        ((MainActivity) getActivity()).getActionBar().hide();
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        threeLetterButton = (Button) rootView.findViewById(R.id.three_letter_button);
        kindergartenButton = (Button) rootView.findViewById(R.id.kindergarten_button);
        japaneseButton = (Button) rootView.findViewById(R.id.japanese_button);
        katakanaButton = (Button) rootView.findViewById(R.id.katakana_button);
        threeLetterButton.setOnClickListener(this);
        kindergartenButton.setOnClickListener(this);
        japaneseButton.setOnClickListener(this);
        katakanaButton.setOnClickListener(this);
//        mSettingsImage = (ImageView) rootView.findViewById(R.id.settings_image);
        return rootView;
    }

    @Override
    public void onClick(final View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.three_letter_button:
                i = new Intent(getActivity(), GameActivity.class);
                i.putExtra(getString(R.string.game_type_label), getString(R.string.three_letter_words_label));
                i.putExtra("IS_ENGLISH", true);
                i.putExtra("IS_LETTER", false);
                startActivity(i);
                break;
            case R.id.kindergarten_button:
                i = new Intent(getActivity(), GameActivity.class);
                i.putExtra(getString(R.string.game_type_label), getString(R.string.kindergarten));
                i.putExtra("IS_ENGLISH", true);
                i.putExtra("IS_LETTER", false);
                startActivity(i);
                break;
            case R.id.japanese_button:
                i = new Intent(getActivity(), GameActivity.class);
                i.putExtra(getString(R.string.game_type_label), getString(R.string.hiragana));
                i.putExtra("IS_ENGLISH", false);
                i.putExtra("IS_LETTER", true);
                startActivity(i);
                break;
            case R.id.katakana_button:
                i = new Intent(getActivity(), GameActivity.class);
                i.putExtra(getString(R.string.game_type_label), getString(R.string.katakana));
                i.putExtra("IS_ENGLISH", false);
                i.putExtra("IS_LETTER", true);
                startActivity(i);
                break;
        }
    }
}
