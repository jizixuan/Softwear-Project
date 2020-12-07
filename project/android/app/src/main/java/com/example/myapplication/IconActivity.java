package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.util.ConfigUtil;
import com.example.myapplication.view.MyFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.luseen.spacenavigation.SpaceNavigationView;


public class IconActivity extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentAdapter myFragmentPagerAdapter;

    private View view;
    private TabLayout.Tab one;
    private TabLayout.Tab two;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_icon,container,false);
        initViews();
        return view;
    }

    private void initViews() {
        //使用适配器将viewpage和fragment绑定
        mViewPager = view.findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentAdapter(getFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);
        //将tablelayout与pageview绑定
        mTabLayout = view.findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        //指定tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
    }
}
