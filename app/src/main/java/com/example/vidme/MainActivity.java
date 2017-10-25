package com.example.vidme;

import android.app.ActionBar;
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
            fragments.add(FeaturedFragment.class.getName());
            fragments.add(NewFragment.class.getName());
            fragments.add(FeedFragment.class.getName());
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(getBaseContext(), fragments.get(position));
        }

        @Override
        public int getCount() {
            return NUM_TABS;
        }
    }
}
