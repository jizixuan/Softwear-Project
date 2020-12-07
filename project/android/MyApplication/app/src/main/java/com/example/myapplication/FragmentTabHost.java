package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.fragment.PersonalFragment;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class FragmentTabHost extends AppCompatActivity {
    SpaceNavigationView spaceNavigationView;
    private Fragment currentFragment = new Fragment();
    private MainActivity mainActivity;
    private IconActivity iconActivity;
    private PersonalFragment personalFragment;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tab_host);

        setFragment(0);
        initSpace(savedInstanceState);
    }

    private void initSpace(Bundle savedInstanceState) {
        spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("明细", R.drawable.account));
        spaceNavigationView.addSpaceItem(new SpaceItem("图表", R.drawable.chart));
        spaceNavigationView.addSpaceItem(new SpaceItem("社区", R.drawable.compass));
        spaceNavigationView.addSpaceItem(new SpaceItem("我的", R.drawable.personal));
        spaceNavigationView.shouldShowFullBadgeText(false);
        spaceNavigationView.setCentreButtonIcon(R.drawable.add2);
        spaceNavigationView.showIconOnly();
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Log.d("onCentreButtonClick ", "onCentreButtonClick");
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),AddBillActivity.class);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.slide_in_bottom,
                        R.anim.slide_out_top);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Log.e("lww",itemName);
                setFragment(itemIndex);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Log.d("onItemReselected ", "" + itemIndex + " " + itemName);
            }
        });
    }
    public void setFragment(int index) {
        //获取Fragment管理器
        FragmentManager mFragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        //隐藏所有Fragment
        hideFragments(mTransaction);
        switch (index){
            default:
                break;
            case 0:
                //显示对应Fragment
                if(mainActivity == null){
                    mainActivity = new MainActivity();
                    mTransaction.add(R.id.frame_layout, mainActivity,
                            "main");
                }else {
                    mTransaction.show(mainActivity);
                }
                currentFragment=mainActivity;
                break;

            case 1:
                //显示对应Fragment
                if(iconActivity == null){
                    iconActivity = new IconActivity();
                    mTransaction.add(R.id.frame_layout, iconActivity,
                            "icon");
                }else {
                    mTransaction.show(iconActivity);
                }
                currentFragment=iconActivity;
                break;
            case 3:
                //显示对应Fragment
                if(personalFragment == null){
                     personalFragment= new PersonalFragment();
                    mTransaction.add(R.id.frame_layout, personalFragment,
                            "personal");
                }else {
                    mTransaction.show(personalFragment);
                }
                currentFragment=iconActivity;
                break;
        }
        //提交事务
        mTransaction.commitAllowingStateLoss();
    }
    //隐藏Fragment
    private void hideFragments(FragmentTransaction transaction) {
        if(mainActivity != null){
            transaction.hide(mainActivity);
        }
        if(iconActivity != null){
            transaction.hide(iconActivity);
        }
        if(personalFragment != null){
            transaction.hide(personalFragment);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                if(data!=null) {
                    mainActivity.addItem(data.getDoubleExtra("num", 0.0), data.getStringExtra("date"),
                            data.getStringExtra("note"), data.getStringExtra("typeName"),data.getIntExtra("id",0));
                }
                break;
        }
    }
}
