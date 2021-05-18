package com.example.myapplication.calendar;

import android.content.Context;
import android.content.Intent;

import com.example.myapplication.R;
import com.example.myapplication.calendar.base.activity.BaseActivity;


/**
 * Only calendar
 * Created by haibin on 2019/6/12.
 */

public class CalendarActivity extends BaseActivity {

    public static void show(Context context) {
        context.startActivity(new Intent(context, CalendarActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
    }

    @Override
    protected void initData() {

    }
}
