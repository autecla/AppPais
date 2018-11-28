package com.lincs.mobcare.daily;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<String> tabTitles;
    private List<Integer> tabTitleIndexes;

    DailyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        tabTitles = new ArrayList<>();
        tabTitleIndexes = new ArrayList<>();
        Calendar rightNow = Calendar.getInstance();

        for(int i = 4; i >= 0; i--) {
            int tmp = rightNow.get(Calendar.MONTH)-i;
            if(tmp < 0) tmp += 12;
            tabTitles.add(getMonth(tmp));
            tabTitleIndexes.add(tmp);
        }
    }

    private String getMonth(int i) {
        switch (i) {
            case 0:
                return "JAN";
            case 1:
                return "FEV";
            case 2:
                return "MAR";
            case 3:
                return "ABR";
            case 4:
                return "MAI";
            case 5:
                return "JUN";
            case 6:
                return "JUL";
            case 7:
                return "AGO";
            case 8:
                return "SET";
            case 9:
                return "OUT";
            case 10:
                return "NOV";
            case 11:
                return "DEZ";
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public Fragment getItem(int position) {
        return DailyFragment.newInstance(tabTitleIndexes.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles.get(position);
    }
}
