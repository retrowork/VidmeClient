package com.example.vidme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    public static int NUM_TABS = 3;

    public static int FEATURED = 0;
    public static int NEW = 1;
    public static int FEED = 2;

    public static String LIST_TYPE = "LIST_TYPE";

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        PagerAdapter adapter = new PagerAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);


    }


    class PagerAdapter extends FragmentPagerAdapter {

        List<String> fragments = new ArrayList<>();

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
            fragments.add(LoginFragment.class.getName());
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
            return Fragment.instantiate(getBaseContext(), fragments.get(position), args);
        }

        @Override
        public int getCount() {
            return NUM_TABS;
        }
    }
}
