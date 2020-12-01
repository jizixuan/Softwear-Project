package com.example.myapplication.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.adapter.IconDetialAdapter;
import com.example.myapplication.entity.IconItem;
import com.example.myapplication.entity.IconList;
import com.example.myapplication.util.ChartUtil;
import com.example.myapplication.util.ConfigUtil;
import com.example.myapplication.util.NumSort;
import com.example.myapplication.util.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class YearDetialFragment extends Fragment {
    private View view;
    private ListView list;
    private ScrollView sv1;
    private TextView textName;
    //名称
    private String type;
    //收入或支出
    private String numType;
    //年，金额，备注
    private double num;
    private String date;
    private String note;
    private Bitmap bitmap;
    private List<IconList> dateBills=new ArrayList<>();
    private LineChart lineChart;// 声明图表控件
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_week_detial_,container,false);
        initViews();
        getData();
        test();
        setScrollerHeight();
        initFlash();
        List<String> xDataList = new ArrayList<>();// x轴数据源
        List<Entry> yDataList = new ArrayList<>();// y轴数据数据源
        //给上面的X、Y轴数据源做假数据测试
        for (int i = 0; i < 12; i++) {
            // x轴显示的数据
            xDataList.add((i+1)+"月");
            //y轴生成float类型的随机数
            float value = (float) (Math.random() * 15) + 3;
            yDataList.add(new Entry(value, i));
        }
        //显示图表,参数（ 上下文，图表对象， X轴数据，Y轴数据，图表标题，曲线图例名称，坐标点击弹出提示框中数字单位）
        ChartUtil.showChart(getContext(), lineChart, xDataList, yDataList, "星期/支出", "钱/星期","元");
        return view;
    }

    private void getData() {
        Intent request=getActivity().getIntent();
        type =request.getStringExtra("type");
        if(request.getStringExtra("numType").equals("+")){
            numType="+";
            textName.setText("收入排行榜");
        }else{
            numType="-";
            textName.setText("支出排行榜");
        }
        num=request.getDoubleExtra("num",0);
        date=request.getStringExtra("date");
        note=request.getStringExtra("note");
        byte[] data=request.getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private void initViews() {
        sv1 = view.findViewById(R.id.sv_week_detial);
        list = view.findViewById(R.id.lv_icon_fragment_week);
        lineChart = view.findViewById(R.id.demo_linechart_week);//绑定控件
        textName = view.findViewById(R.id.tv_icon_or_out_);
    }
    /**
     * 放数据
     */
    private void test() {
        //设置星期
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String str = format.format(date);
        List<IconItem> bills=new ArrayList<>();
        IconItem bill1=new IconItem(bitmap,type,num,numType,"",str);
        Log.e("YearDetialFragment",numType);
        bills.add(bill1);
        Collections.sort(bills,new NumSort());
        IconList dateBill=new IconList(new Date(),bills);
        dateBills.add(dateBill);
        IconDetialAdapter dateAdapter=new IconDetialAdapter(dateBills, getContext(),R.layout.icon_fragment_item);
        list.setAdapter(dateAdapter);
        Utility.setListViewHeightBasedOnChildren(list);
        ConfigUtil.setListViewHeightBasedOnChildren(list);
    }
//    /**
//     * 获取本月的所有日期
//     * @return 日期集合
//     */
//    private static List<String> getAllTheDateOftheMonth(Date date) {
//        List<String> list = new ArrayList<String>();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.set(Calendar.DATE, 1);
//
//        int month = cal.get(Calendar.MONTH);
//        while(cal.get(Calendar.MONTH) == month){
//            String day = new SimpleDateFormat("MM-dd").format(cal.getTime());
//
//            list.add(day);
//            cal.add(Calendar.DATE, 1);
//        }
//        return list;
//    }
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
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) sv1.getLayoutParams();
        linearParams.height=height-810;
        sv1.setLayoutParams(linearParams);
    }
    /**
     * 设置跳转动画
     */
    private void initFlash() {
        Explode explode = new Explode();
        explode.setDuration(450);
        getActivity().getWindow().setEnterTransition(explode);
    }
}
