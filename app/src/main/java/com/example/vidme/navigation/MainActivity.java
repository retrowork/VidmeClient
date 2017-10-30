package com.example.vidme.navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.vidme.login.ContainerFragment;
import com.example.vidme.login.LoginFragment;
import com.example.vidme.R;
import com.example.vidme.videolist.VideoListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements LoginFragment.OnLogin {

    public static final int NUM_TABS = 3;

    public static final int FEATURED = 0;
    public static final int NEW = 1;
    public static final int FEED = 2;

    public static String LIST_TYPE = "LIST_TYPE";

    private ImageButton mPopupMenuButton;

    private ViewPager mViewPager;

    private PagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPopupMenuButton = (ImageButton) findViewById(R.id.popupMenuButton);
        mAdapter = new PagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3); // do not destroy inactive fragments
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (sp.getString("ACCESS_TOKEN", "").equals("")) {
            mPopupMenuButton.setVisibility(View.INVISIBLE);
        } else {
            mPopupMenuButton.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onLoginListener() {
        mPopupMenuButton.setVisibility(View.VISIBLE);
    }


    class PagerAdapter extends FragmentPagerAdapter {

        List<String> fragments = new ArrayList<>();

        ContainerFragment containerFragment;

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.featured_tab_title);
                case 1: return getString(R.string.new_tab_title);
                case 2: return getString(R.string.feed_tab_title);
            }
            return null;
        }


        public PagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(VideoListFragment.class.getName());
            fragments.add(VideoListFragment.class.getName());
            fragments.add(ContainerFragment.class.getName());

            containerFragment = new ContainerFragment();
        }

        private int getListType(int position){
            switch (position) {
                case 0: return FEATURED;
                case 1: return NEW;
                case 2: return FEED;
            }
            return -1;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putInt(LIST_TYPE, getListType(position));
            switch (position) {
                case 0: return Fragment.instantiate(MainActivity.this, fragments.get(0), args);
                case 1: return Fragment.instantiate(MainActivity.this, fragments.get(1), args);
                case 2: return containerFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_TABS;
        }
    }


    public void showPopupMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                logout();
                ((ContainerFragment) mAdapter.getItem(2)).showLoginForm();
                return true;
            }
        });
        menu.show();
    }

    private void logout() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("ACCESS_TOKEN");
        editor.apply();
    }
}
