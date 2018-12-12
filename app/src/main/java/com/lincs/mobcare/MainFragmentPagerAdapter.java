package com.lincs.mobcare;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.lincs.mobcare.evolution.EvolutionFragment;
import com.lincs.mobcare.notice.NoticeFragment;
import com.lincs.mobcare.profile.ProfileFragment;
import com.lincs.mobcare.scheduler.SchedulerFragment;
import com.lincs.mobcare.daily.AtividadeCorFragment;
import java.util.ArrayList;
import java.util.List;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private List<Fragment> fragments;

    MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new ProfileFragment());
        fragments.add(new SchedulerFragment());
        fragments.add(new EvolutionFragment());
        fragments.add(new NoticeFragment());
        fragments.add(new BTFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}

    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Perfil";
            case 1:
                return "Consultas";
            case 2:
                return "-";
            case 3:
                return "Rotina";
            case 4:
                return "Atividades";

        }
        return super.getPageTitle(position);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}
}
