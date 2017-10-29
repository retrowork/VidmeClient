package com.example.vidme;

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
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements LoginFragment.OnLogin {

    public static int NUM_TABS = 3;

    public static int FEATURED = 0;
    public static int NEW = 1;
    public static int FEED = 2;

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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sp.getString("ACCESS_TOKEN", "") == "") {
            mPopupMenuButton.setVisibility(View.INVISIBLE);
        } else {
            mPopupMenuButton.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void showVideosList() {
        mPopupMenuButton.setVisibility(View.VISIBLE);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        List<String> fragmentTitles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            fragments.add(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            fragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentTitles.add(title);
            fragments.add(fragment);
            notifyDataSetChanged();
        }

        public void removeFragment(int position) {
            fragments.remove(position);
            fragmentTitles.remove(position);
            notifyDataSetChanged();
        }

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
            fragmentTitles.add(VideoListFragment.class.getName());
            fragmentTitles.add(VideoListFragment.class.getName());
            fragmentTitles.add(ContainerFragment.class.getName());
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            int listType = -1;
            switch (position) {
                case 0: listType = FEATURED; break;
                case 1: listType = NEW; break;
                case 2: listType = FEED; break;
            }
            args.putInt(LIST_TYPE, listType);
            return Fragment.instantiate(getBaseContext(), fragmentTitles.get(position), args);
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
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("ACCESS_TOKEN");
                editor.apply();
                return true;
            }
        });
        menu.show();
    }
}
