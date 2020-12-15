package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.myapplication.fragment.WeekDetialFragment;
import com.example.myapplication.view.MoneyFragmentAdapter;
import com.example.myapplication.view.MyFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IconItemActivity extends AppCompatActivity {

    //返回箭头
    private ImageView re;
    //名称
    private TextView type;
    //收入或支出
    private String numType;
    //年，金额，备注
    private double num;
    private String date,typeName;
    private String note;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MoneyFragmentAdapter moneyFragmentAdapter;
    private View view;
    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_item);
        initViews();
        getData();
        setListener();
    }


    private void initViews() {
        //使用适配器将viewpage和fragment绑定
        mViewPager = findViewById(R.id.viewPager_icon1);
        moneyFragmentAdapter = new MoneyFragmentAdapter(getSupportFragmentManager(),3);
        mViewPager.setAdapter(moneyFragmentAdapter);
        //将tablelayout与pageview绑定
        mTabLayout = findViewById(R.id.tabLayout_icon);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        //指定tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
        re = findViewById(R.id.income_detail_re_icon);
        type = findViewById(R.id.tv_type);
    }
    private void getData() {
        Intent request=getIntent();
        type.setText(request.getStringExtra("type"));
        typeName = request.getStringExtra("type");
        if(request.getStringExtra("numType").equals("+")){
            numType="收入";
        }else{
            numType="支出";
        }
        num=request.getDoubleExtra("num",0);
        date=request.getStringExtra("date");
        note=request.getStringExtra("note");

        byte[] data=request.getByteArrayExtra("bitmap");
    }
    private void setListener() {
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }
    /**
     * 计算星期
     * @param dateTime
     * @return
     */
    private int getDayofWeek(String dateTime) {

        Calendar cal = Calendar.getInstance();
        if (dateTime.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date;
            try {
                date = sdf.parse(dateTime);
            } catch (ParseException e) {
                date = null;
                e.printStackTrace();
            }
            if (date != null) {
                cal.setTime(new Date(date.getTime()));
            }
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    private String Week(String dateTime) {
        String week = "";
        switch (getDayofWeek(dateTime)) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }
}
