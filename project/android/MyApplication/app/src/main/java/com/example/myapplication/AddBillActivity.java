package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.entity.BillItem;
import com.example.myapplication.fragment.AddExpenditureFragment;
import com.example.myapplication.fragment.AddIncomeFragment;
import com.example.myapplication.util.ViewFindUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Date;

public class AddBillActivity extends AppCompatActivity implements OnTabSelectListener {
    private Context mContext = this;
    private TextView tv;
    private ViewPager vp;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "支出", "收入"
    };
    private MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_bottom,
                R.anim.slide_out_top);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        getView();
        setLinstener();
        mFragments.add(new AddExpenditureFragment());
        mFragments.add(new AddIncomeFragment());


        View decorView = getWindow().getDecorView();
        vp = ViewFindUtils.find(decorView, R.id.add_vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);
        SlidingTabLayout tabLayout = ViewFindUtils.find(decorView, R.id.add_tl);
        tabLayout.setViewPager(vp);
        getIntentData();
    }

    private void getIntentData() {
        //接收修改跳转
        Intent request=getIntent();
        String numType = request.getStringExtra("numType");
        Double num = request.getDoubleExtra("num", 0);
        String type = request.getStringExtra("type");
        String note = request.getStringExtra("note");
        String date=request.getStringExtra("date");
        int id=request.getIntExtra("id",0);
        BillItem item = new BillItem();
        item.setNote(note);
        item.setNum(num);
        item.setNumType(numType);
        item.setType(type);
        item.setId(id);
        if(numType!=null) {
            AddIncomeFragment fragment2= (AddIncomeFragment) mFragments.get(1);
            fragment2.setDateValue(date);
            fragment2.setBillItem(item);
            AddExpenditureFragment fragment1= (AddExpenditureFragment) mFragments.get(0);
            fragment1.setDateValue(date);
            fragment1.setBillItem(item);
            if (numType.equals("+")) {
                vp.setCurrentItem(1);

                fragment2.setTypeFalg(1);
            } else {
                vp.setCurrentItem(0);

                fragment1.setTypeFalg(1);
            }
            mFragments.set(1,fragment2);
            mFragments.set(0,fragment1);
        }else{
            vp.setCurrentItem(0);
        }
    }

    private void setLinstener() {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_top,
                        R.anim.slide_out_bottom);
            }
        });

    }

    private void getView() {
        tv=findViewById(R.id.add_bill_tv);
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
