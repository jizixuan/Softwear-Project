package com.example.myapplication.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.fragment.MouthDetialFragment;
import com.example.myapplication.fragment.MouthFragment;
import com.example.myapplication.fragment.MouthOutFragment;
import com.example.myapplication.fragment.WeekDetialFragment;
import com.example.myapplication.fragment.WeekFragment;
import com.example.myapplication.fragment.WeekOutFragment;
import com.example.myapplication.fragment.YearDetialFragment;
import com.example.myapplication.fragment.YearFragment;
import com.example.myapplication.fragment.YearOutFragment;

public class MoneyFragmentAdapter extends FragmentPagerAdapter {
    private String[] title = new String[]{"日","月","年"};
    private int behavior;

    public MoneyFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.behavior = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (behavior == 1) {
            if (position == 1) {
                return new MouthOutFragment();
            } else if (position == 2) {
                return new YearOutFragment();
            }
            return new WeekOutFragment();
        } else if (behavior == 2) {
            if (position == 1) {
                return new MouthFragment();
            } else if (position == 2) {
                return new YearFragment();
            }
            return new WeekFragment();
        }else if(behavior == 3){
            if(position == 1){
                return new MouthDetialFragment();
            }else if(position == 2){
                return new YearDetialFragment();
            }
            return new WeekDetialFragment();
        }
        return null;
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
