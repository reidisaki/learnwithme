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
        threeLetterButton.setOnClickListener(this);
//        mSettingsImage = (ImageView) rootView.findViewById(R.id.settings_image);
        return rootView;
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.three_letter_button) {
            Intent i = new Intent(getActivity(), GameActivity.class);
            startActivity(i);
        }
    }
}
