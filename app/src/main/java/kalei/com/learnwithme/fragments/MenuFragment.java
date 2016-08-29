package kalei.com.learnwithme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kalei.com.learnwithme.R;

/**
 * Created by risaki on 8/28/16.
 */
public class MenuFragment extends LearnWithMeFragment {

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
//        mSettingsImage = (ImageView) rootView.findViewById(R.id.settings_image);
        return rootView;
    }
}
