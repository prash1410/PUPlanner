package com.puchd.puplanner;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class DayFragmentAdapter extends FragmentPagerAdapter{
    final int PAGE_COUNT = 7;
    private String tabTitles[] = new String[] {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private Context context;

    public DayFragmentAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return DayFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
