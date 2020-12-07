package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.myapplication.adapter.BillYearAdapter;
import com.example.myapplication.entity.DateBill;

public class BillActivity extends AppCompatActivity {
    private TextView tvYear;//年份
    private TimePickerView pvTime;//时间选择器
    private LinearLayout changeYear;//年份选择
    private ListView list_Bill;//账单list
    private BillYearAdapter adapter;
    private List<DateBill> dateBillList;
    private ScrollView sv;
    private double sumIncome,sumOutcome,sumBalance;
    private TextView tvIncome,tvOutcome,tvBalancce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initViews();
        setTime();
        showDatePicker();
        //获得假数据
        initvalues();
        changeYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });
        adapter = new BillYearAdapter(this,R.layout.date_bill_item,dateBillList);
        list_Bill.setAdapter(adapter);
        setScrollerHeight();
    }

    private void initvalues() {
        dateBillList = new ArrayList<>();
        Date a = new Date(2020,11,11);
        Date b = new Date(2020,10,11);
        DateBill dateBill11 = new DateBill(a,600,500);
        DateBill dateBill12 = new DateBill(b,600,500);
        dateBillList.add(dateBill11);
        dateBillList.add(dateBill12);
        for (int i=0;i<dateBillList.size();i++){
            sumIncome = dateBillList.get(i).getIncome()+sumIncome;
            sumOutcome = dateBillList.get(i).getExpenditure()+sumOutcome;
        }
        sumBalance = sumIncome-sumOutcome;
        tvOutcome.setText(sumOutcome+"");
        tvIncome.setText(sumIncome+"");
        tvBalancce.setText(sumBalance+"");

    }

    private void initViews() {
        tvIncome = findViewById(R.id.tv_billIncome);
        tvBalancce = findViewById(R.id.tv_billBalance);
        tvOutcome = findViewById(R.id.tv_billOutcome);
        tvYear = findViewById(R.id.tv_year);
        changeYear = findViewById(R.id.changeYear);
        sv = findViewById(R.id.bill_ScrollView);
        list_Bill = findViewById(R.id.bill_list);
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int yearValue = calendar.get(Calendar.YEAR);
        tvYear.setText(yearValue+"年");
    }
    /**
     * 设置scrollerview高度
     */
    private void setScrollerHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;
        //动态设置高度
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) sv.getLayoutParams();
        linearParams.height=height-770;
        sv.setLayoutParams(linearParams);
    }
    /**
     * 配置日期选择控件
     */
    private void showDatePicker() {
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //点击后的回调事件
                String yearValue=getTime(date).split("-")[0];
                tvYear.setText(yearValue+"年");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, false, false, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.BOTTOM);

            params.leftMargin =0;
            params.rightMargin =0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            if (dialogWindow != null) {
                dialogWindow.setAttributes(layoutParams);
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    /**
     * 解析日期选择控件获得数据
     * @param date
     * @return
     */
    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }
}
