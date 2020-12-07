package com.example.myapplication.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.fragment.MoneyInFragment;
import com.example.myapplication.fragment.MoneyOutFragment;

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private String[] title = new String[]{"支出","收入"};
    public MyFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new MoneyOutFragment();
        }
        return new MoneyInFragment();
    }

    @Override
    public int getCount() {
        return title.length;
    }
    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
