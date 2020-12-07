package com.example.myapplication.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.transition.Explode;
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
import com.example.myapplication.adapter.IconAdapter;
import com.example.myapplication.entity.BillItem;
import com.example.myapplication.entity.DateBill;
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

public class YearFragment extends Fragment {
    private View view;
    private ListView list;
    private ScrollView sv3;
    private List<IconList> dateBills=new ArrayList<>();
    private LineChart lineChart;// 声明图表控件
    private TextView all;//总的钱数
    private TextView average;//平均值
    List<IconItem> bills=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_year,container,false);
        initViews();
        test();
        setAllAndAverage();
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
    /**
     * 计算平均值和总钱数
     */
    private void setAllAndAverage() {
        double m = 0;
        for(int i=0;i<bills.size();i++){
            m+=bills.get(i).getNum();
        }
        all.setText(m+"");
        double c = m/12;
        java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.00");
        String str = myformat.format(c);
        average.setText(str);
    }

    /**
     * 获取本周的所有日期
     * @return 日期集合
     */
    private static List<String> getWeekDay() {
        Calendar calendar = Calendar.getInstance();
        // 获取本周的第一天
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
            // 获取星期的显示名称，例如：周一、星期一、Monday等等
            String day = new SimpleDateFormat("MM-dd").format(calendar.getTime());

            list.add(day);
        }

        return list;
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
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) sv3.getLayoutParams();
        linearParams.height=height-810;
        sv3.setLayoutParams(linearParams);
    }
    /**
     * 设置跳转动画
     */
    private void initFlash() {
        Explode explode = new Explode();
        explode.setDuration(450);
        getActivity().getWindow().setEnterTransition(explode);
    }

    /**
     * 放数据
     */
    private void test() {
        //设置星期
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String str = format.format(date);
        IconItem bill1=new IconItem(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"购物",16.00,"+","",str);
        IconItem bill2=new IconItem(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"ww",88.00,"+","",str);
        IconItem bill3=new IconItem(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"ss",79.00,"+","",str);
        bills.add(bill1);
        bills.add(bill2);
        bills.add(bill3);
        Collections.sort(bills,new NumSort());
        IconList dateBill=new IconList(new Date(),bills);
        dateBills.add(dateBill);
        IconAdapter dateAdapter=new IconAdapter(dateBills, getContext(),R.layout.icon_fragment_item);
        list.setAdapter(dateAdapter);
        Utility.setListViewHeightBasedOnChildren(list);
        ConfigUtil.setListViewHeightBasedOnChildren(list);
    }
    private void initViews() {
        sv3 = view.findViewById(R.id.sv3);
        list = view.findViewById(R.id.lv_icon_fragment2);
        lineChart = view.findViewById(R.id.demo_linechart2);//绑定控件
        all = view.findViewById(R.id.tv_icon_out_year);
        average = view.findViewById(R.id.tv_icon_out_year_average);
    }
}
