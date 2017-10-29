package com.example.vidme;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.vidme.MainActivity.FEED;
import static com.example.vidme.MainActivity.LIST_TYPE;

public class ContainerFragment extends Fragment implements LoginFragment.OnLogin {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String token = sp.getString("ACCESS_TOKEN", "");
        if (token != "") {
            VideoListFragment fragment = new VideoListFragment();
            Bundle args = new Bundle();
            args.putInt(LIST_TYPE, FEED);
            fragment.setArguments(args);
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        } else {
            LoginFragment fragment = new LoginFragment();
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        return view;
    }

    @Override
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
}
