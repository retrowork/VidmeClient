package com.example.vidme.login;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vidme.R;
import com.example.vidme.login.LoginFragment;
import com.example.vidme.videolist.VideoListFragment;

import static com.example.vidme.navigation.MainActivity.FEED;
import static com.example.vidme.navigation.MainActivity.LIST_TYPE;

public class ContainerFragment extends Fragment {
    private VideoListFragment mVideoListFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_container, container, false);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (authenticated()) {
            mVideoListFragment = new VideoListFragment();
            Bundle args = new Bundle();
            args.putInt(LIST_TYPE, FEED);
            mVideoListFragment.setArguments(args);
            transaction.replace(R.id.container, mVideoListFragment)
                    .commit();

        } else {
            transaction.replace(R.id.container, new LoginFragment())
                    .commit();
        }

       return view;
    }

    private boolean authenticated() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return !sp.getString("ACCESS_TOKEN", "").equals("");
    }


    public void showVideosList() {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putInt(LIST_TYPE, FEED);
        fragment.setArguments(args);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    public void showLoginForm() {

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
    }
}
