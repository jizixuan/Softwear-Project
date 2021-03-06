package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.view.MoneyFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class MoneyOutFragment extends Fragment {
    private View view;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MoneyFragmentAdapter myFragmentPagerAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab there;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_money_out,container,false);
        //初始化示图
        initViews();
        return view;
    }

    private void initViews() {
        //使用适配器将viewpage和fragment绑定
        mViewPager = view.findViewById(R.id.viewPager2);
        myFragmentPagerAdapter = new MoneyFragmentAdapter(getFragmentManager(),1);
        mViewPager.setAdapter(myFragmentPagerAdapter);
        //将tablelayout与pageview绑定
        mTabLayout = view.findViewById(R.id.tabLayout2);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        //指定tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        there = mTabLayout.getTabAt(2);
    }

}
