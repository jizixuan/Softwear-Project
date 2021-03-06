package com.example.myapplication.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Explode;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.adapter.IconAdapter;
import com.example.myapplication.entity.BillType;
import com.example.myapplication.entity.IconItem;
import com.example.myapplication.entity.IconList;
import com.example.myapplication.util.ChartUtil;
import com.example.myapplication.util.ConfigUtil;
import com.example.myapplication.util.NumSort;
import com.example.myapplication.util.ServerConfig;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YearFragment extends Fragment {
    private View view;
    private ListView list;
    private ScrollView sv3;
    private List<IconList> dateBills=new ArrayList<>();
    private LineChart lineChart;// 声明图表控件
    private TextView all;//总的钱数
    private TextView average;//平均值
    private List<IconItem> bills=new ArrayList<>();
    private List<String> xDataList = getX();// x轴数据源
    private List<Entry> yDataList = new ArrayList<>();// y轴数据数据源
    private SmartRefreshLayout refreshLayout;
    private IconAdapter dateAdapter;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String str = (String) msg.obj;
                    IconList iconList = new IconList();
                    try {
                        JSONArray jsonArray = new JSONArray(str);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            if (iconList.getDate() == null) {
                                Date date = stringToDate(jsonObject.getString("date"), "yyyy-MM-dd");
                                iconList.setDate(date);
                            }
                            for (int j = 0; j < xDataList.size(); j++) {
                                if (jsonObject.getString("month").equals(getXx().get(j))) {
                                    int id = jsonObject.getInt("typeId");
                                    BillType billType = ServerConfig.BILL_TYPES.get(id - 1);
                                    if (billType.getNumType().equals("-")) {
                                        IconItem iconItem = new IconItem();
                                        iconItem.setDate(jsonObject.getString("date"));
                                        iconItem.setImg(billType.getImg());
                                        iconItem.setNote(jsonObject.getString("note"));
                                        iconItem.setNum(jsonObject.getDouble("num"));
                                        iconItem.setType(billType.getName());
                                        iconItem.setNumType(billType.getNumType());
                                        bills.add(iconItem);
                                    }
                                }
                            }
                            Collections.sort(bills, new NumSort());
                            iconList.setBills(bills);
                        }
                        dateBills.add(iconList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dateAdapter = new IconAdapter(dateBills, getContext(), R.layout.icon_fragment_item);
                    list.setAdapter(dateAdapter);
                    setAllAndAverage();
                    ConfigUtil.setListViewHeightBasedOnChildren(list);
                    break;
                case 2:
                    String str1 = (String) msg.obj;
                    try {
                        Entry e = null;
                        JSONArray jsonArray = new JSONArray(str1);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            e = new Entry((float) jsonObject.getDouble("num"), i);
                            yDataList.add(e);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //显示图表,参数（ 上下文，图表对象， X轴数据，Y轴数据，图表标题，曲线图例名称，坐标点击弹出提示框中数字单位）
                    ChartUtil.showChart(getContext(), lineChart, xDataList, yDataList, "星期/支出", "钱/星期", "元");
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_year,container,false);
        initViews();
//        test();
        initData();
        setRefreshLayout();
        setScrollerHeight();
        initFlash();
        yDataList = new ArrayList<>();// y轴数据数据源
        getY();
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

    public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
    public List<String> getX(){
        xDataList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            // x轴显示的数据
            xDataList.add((i+1)+"月");
        }
        return xDataList;
    }
    public List<String> getXx(){
        xDataList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            // x轴显示的数据
            xDataList.add((i+1)+"");
            //y轴生成float类型的随机数
        }
        return xDataList;
    }
    /**
     * 获取本年的第一天
     * @return String
     * **/
    public static String getYearStart(){
        return new SimpleDateFormat("yyyy").format(new Date())+"-01-01";
    }

    /**
     * 获取本年的最后一天
     * @return String
     * **/
    public static String getYearEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date currYearLast = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(currYearLast);
    }
    private void setRefreshLayout() {
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setEnableLoadMoreWhenContentNotFull(true);

        //给智能刷新控件注册下拉刷新事件监听器
        //查看下月数据
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
                yDataList.clear();
                getY();
                refreshLayout.finishRefresh();
            }
        });
    }
    /**
     * 从服务器获取数据
     */
    public void getY() {
        dateBills.clear();
        bills.clear();
        FormBody formBody =
                new FormBody.Builder()
                        .add("first", getYearStart())
                        .add("last", getYearEnd())
                        .add("userId", ServerConfig.USER_ID + "")
                        .build();
        //创建请求对象
        Request request = new Request.Builder()
                .url(ServerConfig.SERVER_HOME + "IconItemGetAllNumByYear")
                .method("POST", formBody)
                .post(formBody)
                .build();
        //3. 创建CALL对象
        Call call = okHttpClient.newCall(request);
        //3. 异步方式提交请求并获取响应
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lww", "请求失败" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取服务端返回的数据
                String result = response.body().string();
                Log.e("result", result);
//                setyDataList(result);
                Message msg = handler.obtainMessage();
                msg.what = 2;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
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
        SmartRefreshLayout.LayoutParams linearParams =(SmartRefreshLayout.LayoutParams) sv3.getLayoutParams();
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
     * 从服务器获取数据
     */
    public void initData() {
        dateBills.clear();
        bills.clear();
        dateAdapter = null;
        FormBody formBody =
                new FormBody.Builder()
                        .add("first", getYearStart())
                        .add("last", getYearEnd())
                        .add("userId", ServerConfig.USER_ID + "")
                        .build();
        //创建请求对象
        Request request = new Request.Builder()
                .url(ServerConfig.SERVER_HOME + "IconItemGetByDateServlet")
                .method("POST", formBody)
                .post(formBody)
                .build();
        //3. 创建CALL对象
        Call call = okHttpClient.newCall(request);
        //3. 异步方式提交请求并获取响应
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lww", "请求失败" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取服务端返回的数据
                String result = response.body().string();
                //使用handler将数据封装在Message中，并发布出去，以备显示在UI控件中
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = result;
                Log.e("jzx", result);
                handler.sendMessage(msg);
            }
        });
    }
    private void initViews() {
        sv3 = view.findViewById(R.id.sv3);
        list = view.findViewById(R.id.lv_icon_fragment2);
        lineChart = view.findViewById(R.id.demo_linechart2);//绑定控件
        all = view.findViewById(R.id.tv_icon_out_year);
        average = view.findViewById(R.id.tv_icon_out_year_average);
        refreshLayout = view.findViewById(R.id.srll_year);
    }
}
