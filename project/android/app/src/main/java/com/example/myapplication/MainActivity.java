package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myapplication.adapter.DateAdapter;
import com.example.myapplication.entity.BillItem;
import com.example.myapplication.entity.DateBill;
import com.example.myapplication.note.activity.NoteActivity;
import com.example.myapplication.util.ConfigUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Fragment {
    private TextView tvYear;
    private TextView tvMonth;
    private LinearLayout changeTime;
    private ListView list;
    private TimePickerView pvTime;
    private ScrollView sv;
    private View root;
    private TextView income;
    private TextView expenditure;
    //便签
    private TextView note;
    private TextView financing;
    private SmartRefreshLayout refreshLayout;
    private List<DateBill> dateBills=new ArrayList<>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.activity_main, container, false);
        findView();
        //显示当前年份
        setTime();
        showDatePicker();
        setLinstener();
        test();
        setScrollerHeight();
        setRefreshLayout();
        return root;
    }

    private void setRefreshLayout() {
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        refreshLayout.setEnableAutoLoadMore(false);
        //给智能刷新控件注册下拉刷新事件监听器
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });
        //给智能刷新控件注册上拉加载更多事件监听器
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }


    /**
     * 设置scrollerview高度
     */
    private void setScrollerHeight() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;
        //动态设置高度
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) sv.getLayoutParams();
        linearParams.height=height-820;
        sv.setLayoutParams(linearParams);
    }

    private void test() {
        List<BillItem> bills=new ArrayList<>();
        BillItem bill1=new BillItem(BitmapFactory.decodeResource(getResources(), R.mipmap.type_shopping),"购物",16.00,"+","");
        BillItem bill2=new BillItem(BitmapFactory.decodeResource(getResources(), R.mipmap.type_shopping),"ww",88.00,"+","");
        BillItem bill3=new BillItem(BitmapFactory.decodeResource(getResources(), R.mipmap.type_shopping),"ss",76.00,"+","");
        bills.add(bill1);
        bills.add(bill2);
        bills.add(bill3);
        DateBill dateBill=new DateBill(new Date(),18.00,88.00,bills);
        DateBill dateBil2=new DateBill(new Date(),28.00,88.00,bills);
        DateBill dateBil3=new DateBill(new Date(),69.00,88.00,bills);
        DateBill dateBil4=new DateBill(new Date(),69.00,88.00,bills);
        dateBills.add(dateBill);
        dateBills.add(dateBil2);
        dateBills.add(dateBil3);
        dateBills.add(dateBil4);
        DateAdapter dateAdapter=new DateAdapter(dateBills,root.getContext(), R.layout.date_bill);
        list.setAdapter(dateAdapter);
        ConfigUtil.setListViewHeightBasedOnChildren(list);
    }

    private void setLinstener() {
        MyListener listener=new MyListener();
        changeTime.setOnClickListener(listener);
        income.setOnClickListener(listener);
        expenditure.setOnClickListener(listener);
        note.setOnClickListener(listener);
        financing.setOnClickListener(listener);
    }
    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int yearValue = calendar.get(Calendar.YEAR);
        tvYear.setText(yearValue+"");
        //月
        int monthValue = calendar.get(Calendar.MONTH)+1;
        tvMonth.setText(monthValue+"");
    }

    private void findView() {
        tvYear=root.findViewById(R.id.year);
        tvMonth=root.findViewById(R.id.month);
        changeTime=root.findViewById(R.id.changeTime);
        list=root.findViewById(R.id.lv);
        sv=root.findViewById(R.id.sv);
        income=root.findViewById(R.id.income);
        expenditure=root.findViewById(R.id.expenditure);
        refreshLayout=root.findViewById(R.id.srl);
        note = root.findViewById(R.id.note);
        financing = root.findViewById(R.id.financing);
    }
    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.changeTime:
                    //设置选择日期
                    pvTime.show();
                    break;
                case R.id.income:
                    Intent intent=new Intent();
                    intent.setClass(root.getContext(), InOrOutDetailActivity.class);
                    intent.putExtra("year",tvYear.getText());
                    intent.putExtra("month",tvMonth.getText());
                    intent.putExtra("info","income");
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) root.getContext()).toBundle());
                    break;
                case R.id.expenditure:
                    Intent intent1=new Intent();
                    intent1.setClass(root.getContext(), InOrOutDetailActivity.class);
                    intent1.putExtra("year",tvYear.getText());
                    intent1.putExtra("month",tvMonth.getText());
                    intent1.putExtra("info","expenditure");
                    startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation((Activity) root.getContext()).toBundle());
                    break;
                case R.id.financing:
                    Intent intent3 = new Intent();
                    intent3.setClass(root.getContext(), FinacingActivity.class);
                    startActivity(intent3, ActivityOptions.makeSceneTransitionAnimation((Activity) root.getContext()).toBundle());
                    break;
                case R.id.note:
                    Intent intent2 = new Intent();
                    intent2.setClass(root.getContext(), NoteActivity.class);
                    startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation((Activity) root.getContext()).toBundle());
                    break;
            }
        }
    }


    /**
     * 配置日期选择控件
     */
    private void showDatePicker() {
        pvTime = new TimePickerBuilder(root.getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //点击后的回调事件
                String yearValue=getTime(date).split("-")[0];
                String momthValue=getTime(date).split("-")[1];
                tvYear.setText(yearValue);
                tvMonth.setText(momthValue);

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, false, false, false, false})
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
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }
}
