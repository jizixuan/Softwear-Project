package com.example.myapplication.calendar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.baidu.ocr.ui.util.ImageUtil;
import com.blankj.utilcode.util.ImageUtils;
import com.example.myapplication.R;
import com.example.myapplication.adapter.DateAdapter;
import com.example.myapplication.calendar.base.activity.BaseActivity;
import com.example.myapplication.calendar.group.GroupItemDecoration;
import com.example.myapplication.calendar.group.GroupRecyclerView;
import com.example.myapplication.entity.BillType;
import com.example.myapplication.entity.DateBill;
import com.example.myapplication.util.ConfigUtil;
import com.example.myapplication.util.ServerConfig;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.TrunkBranchAnnals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main1Activity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnWeekChangeListener,
        CalendarView.OnViewChangeListener,
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnYearViewChangeListener,
        View.OnClickListener {
    GroupRecyclerView mRecyclerView;

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private AlertDialog mFuncDialog;
    private int mYear;
    CalendarLayout mCalendarLayout;
    private List<DateBill> dateBills;
    private Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Log.i("lr", "handleMessage: aaaaa");
                    String str= (String) msg.obj;
                    Log.i("lr", "handleMessage: "+str);
                    if (str.equals("[]")) {
                        Log.i("null", "handleMessage: 空值");
                        mRecyclerView = findViewById(R.id.recyclerView);
                        com.example.myapplication.entity.BillItem billItem=new com.example.myapplication.entity.BillItem();
                        billItem.setType("今日无记账");
                        billItem.setNum(0);
                        billItem.setNumType("+");
                        billItem.setImg(ImageUtils.getBitmap(R.drawable.empty_data));
                        dateBills=new ArrayList<>();

                        List<com.example.myapplication.entity.BillItem>billItems = new ArrayList<>();
                        DateBill dateBill=new DateBill();
                        billItems.add(billItem);
                        dateBill.setBills(billItems);
                        dateBills.add(dateBill);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(Main1Activity.this));
                        //mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
                        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, BillItem>());
                        BillItemAdapter adapter=new BillItemAdapter(Main1Activity.this,dateBills.get(0).getBills());
                        mRecyclerView.setAdapter(adapter);
                        break;
                    }
                    com.example.myapplication.entity.DateBill dateBill=new com.example.myapplication.entity.DateBill();;
                    Double dateIncomeValue=0.0;
                    Double dateExpenditureValue=0.0;
                    dateBills=new ArrayList<>();
                    List<com.example.myapplication.entity.BillItem> billItems =new ArrayList<>();
                    try {
                        JSONArray jsonArray=new JSONArray(str);
                        if(jsonArray.length()>0) {
                            JSONObject jsonObject0 = (JSONObject) jsonArray.get(0);
                            int day = jsonObject0.getInt("day");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                int dayValue = jsonObject.getInt("day");
                                if (day != dayValue) {
                                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i - 1);
                                    Date date = stringToDate(jsonObject1.getString("date"), "yyyy-MM-dd");
                                    dateBill.setDate(date);
                                    dateBill.setExpenditure(dateExpenditureValue);
                                    dateBill.setIncome(dateIncomeValue);
                                    dateBill.setBills(billItems);
                                    dateBills.add(dateBill);

                                    day = dayValue;
                                    dateBill = new com.example.myapplication.entity.DateBill();
                                    dateIncomeValue = 0.0;
                                    dateExpenditureValue = 0.0;
                                    billItems = new ArrayList<>();

                                }
                                com.example.myapplication.entity.BillItem billItem = new com.example.myapplication.entity.BillItem();
                                billItem.setNum(jsonObject.getDouble("num"));
                                int id = jsonObject.getInt("typeId");
                                Log.i("type", "handleMessage: "+id);
                                BillType billType = com.example.myapplication.util.ServerConfig.BILL_TYPES.get(id - 1);
                                billItem.setType(billType.getName());
                                Log.i("type", "handleMessage: "+billType.getName());
                                billItem.setId(jsonObject.getInt("id"));
                                billItem.setNote(jsonObject.getString("note"));
                                billItem.setNumType(billType.getNumType());
                                if (billItem.getNumType().equals("+")) {
                                    dateIncomeValue += billItem.getNum();
                                } else {
                                    dateExpenditureValue += billItem.getNum();
                                }
                                billItem.setImg(billType.getImg());
                                billItems.add(billItem);
                                if (i == jsonArray.length() - 1) {
                                    if (day != dayValue) {
                                        com.example.myapplication.entity.DateBill dateBill1 = null;
                                        dateBill1 = new com.example.myapplication.entity.DateBill();
                                        List<com.example.myapplication.entity.BillItem> billItems1 = new ArrayList<>();
                                        billItems1.add(billItem);
                                        dateBill1.setBills(billItems1);
                                        Date date = stringToDate(jsonObject.getString("date"), "yyyy-MM-dd");
                                        dateBill1.setDate(date);
                                        dateBill1.setExpenditure(dateExpenditureValue);
                                        dateBill1.setIncome(dateIncomeValue);
                                        dateBills.add(dateBill1);
                                    } else {
                                        dateBill.setExpenditure(dateExpenditureValue);
                                        dateBill.setIncome(dateIncomeValue);
                                        dateBill.setBills(billItems);
                                        Date date = stringToDate(jsonObject.getString("date"), "yyyy-MM-dd");
                                        dateBill.setDate(date);
                                        dateBills.add(dateBill);
                                    }
                                    break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mRecyclerView = findViewById(R.id.recyclerView);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(Main1Activity.this));
                    //mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
                    mRecyclerView.addItemDecoration(new GroupItemDecoration<String, BillItem>());
                    mRecyclerView.setAdapter(new BillItemAdapter(Main1Activity.this,dateBills.get(0).getBills()));
                    mRecyclerView.notifyDataSetChanged();

                    break;


                case 2:
                    //将数据库传来日期信息，解析，保存到map中
                    Map<String, Calendar> map = new HashMap<>();
                    String str1 = (String) msg.obj;

                    try {
                        Log.i("date", "1");
                        JSONArray jsonArray=new JSONArray(str1);
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                int day = (int) jsonObject.get("day");
                                int month = (int) jsonObject.get("month");
                                int year = (int) jsonObject.get("year");
                                map.put(getSchemeCalendar(year, month, day, 0xFF40db25, "记账").toString(),
                                        getSchemeCalendar(year, month, day, 0xFF40db25, "记账"));
                                Log.i("date", "handleMessage: 日期数值"+year+"年"+month+"月"+day+"日");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //28560 数据量增长不会影响UI响应速度，请使用这个API替换
                    mRecyclerView = findViewById(R.id.recyclerView);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(Main1Activity.this));
                    //mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
                    mRecyclerView.addItemDecoration(new GroupItemDecoration<String, BillItem>());
                    mCalendarView.setSchemeDate(map);
                    mRecyclerView.notifyDataSetChanged();
                    Log.i("date", "handleMessage: 日期显示");

                    break;
            }
        }
    };
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main1;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        Log.i("lr", "initView: 日历");

        setStatusBarDarkMode();
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);

        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mTextCurrentDay = findViewById(R.id.tv_current_day);

        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        final DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mCalendarLayout.expand();
                                break;
                            case 1:
                                boolean result = mCalendarLayout.shrink();
                                Log.e("shrink", " --  " + result);
                                break;
                            case 2:
                                mCalendarView.scrollToPre(false);
                                break;
                            case 3:
                                mCalendarView.scrollToNext(false);
                                break;
                        }
                    }
                };
        findViewById(R.id.iv_func).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.iv_func).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFuncDialog == null) {
                            mFuncDialog = new AlertDialog.Builder(Main1Activity.this)
                                    .setTitle(R.string.func_dialog_title)
                                    .setItems(R.array.func_dialog_items, listener)
                                    .create();
                        }
                        mFuncDialog.show();
                    }
                });

            }
        });
        //返回当日
        findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = mCalendarView.getCurDay();
                int month = mCalendarView.getCurMonth();
                int year = mCalendarView.getCurYear();
                mCalendarView.scrollToCalendar(year,month,day);
                Log.i("lr", "onClick: "+year+"年"+month+"月"+day+"日");
            }
        });

        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);

        mCalendarView.setOnWeekChangeListener(this);
        mCalendarView.setOnYearViewChangeListener(this);

        //设置日期拦截事件，仅适用单选模式，当前无效
        mCalendarView.setOnCalendarInterceptListener(this);

        mCalendarView.setOnViewChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));


    }

    @SuppressWarnings("unused")
    @Override
    /**
     * 初始化数据，传入信息
     */
    protected void initData() {

        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        getDays(year,month);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, BillItem>());
        //mRecyclerView.setAdapter(new ArticleAdapter(this));
//        if (dateBills.get(0).getBills() == null){
//            List<BillItem> billItems = new ArrayList<>();
//            mRecyclerView.setAdapter(new BillItemAdapter(this);
//        }
        mRecyclerView.setAdapter(new BillItemAdapter(this,dateBills.get(0).getBills()));
        mRecyclerView.notifyDataSetChanged();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
        Toast.makeText(this, String.format("%s : OutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        //Log.e("onDateSelected", "  -- " + calendar.getYear() + "  --  " + calendar.getMonth() + "  -- " + calendar.getDay());
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        if (isClick) {//点击日期，跳到当前日期
        }
//        Log.e("lunar "," --  " + calendar.getLunarCalendar().toString() + "\n" +
//        "  --  " + calendar.getLunarCalendar().getYear());
        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
        Log.e("onDateSelected", "  " + mCalendarView.getSelectedCalendar().getScheme() +
                "  --  " + mCalendarView.getSelectedCalendar().isCurrentDay());
        Log.e("干支年纪 ： ", " -- " + TrunkBranchAnnals.getTrunkBranchYear(calendar.getLunarCalendar().getYear()));
        int year1 = calendar.getYear();
        int month1 = calendar.getMonth();
        getBills(calendar);

        Log.i("lr", "onMonthChange:选择日期 "+year1+"年"+month1+"月");
        getDays(year1,month1);
    }




    @SuppressLint("SetTextI18n")
    @Override
    public void onMonthChange(int year, int month) {
            Log.e("onMonthChange", "  -- " + year + "  --  " + month+111111111);
            Calendar calendar = mCalendarView.getSelectedCalendar();
            mTextLunar.setVisibility(View.VISIBLE);
            mTextYear.setVisibility(View.VISIBLE);
            mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mTextYear.setText(String.valueOf(calendar.getYear()));
            mTextLunar.setText(calendar.getLunar());
            mYear = calendar.getYear();
            int year1 = calendar.getYear();
            int month1 = calendar.getMonth()+1;

            getDays(year1,month1);
        Log.i("lr", "onMonthChange:改变月份代码 "+year1+"年"+month1+"月");
    }

    @Override
    public void onViewChange(boolean isMonthView) {
        Log.e("onViewChange", "  ---  " + (isMonthView ? "月视图" : "周视图"));
    }


    @Override
    public void onWeekChange(List<Calendar> weekCalendars) {
        for (Calendar calendar : weekCalendars) {
            Log.e("onWeekChange", calendar.toString());
        }
    }

    @Override
    public void onYearViewChange(boolean isClose) {
        Log.e("onYearViewChange", "年视图 -- " + (isClose ? "关闭" : "打开"));
    }

    /**
     * 屏蔽某些不可点击的日期，可根据自己的业务自行修改
     *
     * @param calendar calendar
     * @return 是否屏蔽某些不可点击的日期，MonthView和WeekView有类似的API可调用
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        Log.e("onCalendarIntercept", calendar.toString());
        int day = calendar.getDay();
        return day == 1 || day == 3 || day == 6 || day == 11 || day == 12 || day == 15 || day == 20 || day == 26;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this, calendar.toString() + "拦截不可点击", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
        Log.e("onYearChange", " 年份变化 " + year);
    }

    /**
     * 根据月份获得当前月，有那几天进行了记账操作
     * @param year,month
     */
    public void getDays(int year,int month){

        OkHttpClient client = new OkHttpClient();
        FormBody formBody =
                new FormBody.Builder()
                        .add("year",year+"")
                        .add("month",month+"")
                        .add("userId", com.example.myapplication.util.ServerConfig.USER_ID+"")
                        .build();
        Request request = new Request.Builder()
                .url(com.example.myapplication.util.ServerConfig.SERVER_HOME +"GetBillItemListMark")
                .method("POST", formBody)
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lr","month返回信息失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = 2;
                msg.obj = str;
                handler.sendMessage(msg);
                Log.i("lr", "日期返回信息成功");
            }
        });

    }

    /**
     * 根据点击日期，获得当前日期的账单，并返回到客户端
     * @param calendar
     */
    public void getBills(Calendar calendar){
        Log.i("lr", "getBills: ddddd");
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        Log.i("lr",year+"年:"+month+"月"+day+"日");
        OkHttpClient client = new OkHttpClient();
        FormBody formBody =
                new FormBody.Builder()
                        .add("year",year+"")
                        .add("month",month+"")
                        .add("day",day+"")
                        .add("userId", com.example.myapplication.util.ServerConfig.USER_ID+"")
                        .build();
        //创建请求对象
        Request request = new Request.Builder()
                .url(com.example.myapplication.util.ServerConfig.SERVER_HOME + "GetBillItemListByDateServlet1")
                .method("POST", formBody)
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lr","返回信息失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = str;

                handler.sendMessage(msg);
                Log.i("lr", "返回信息成功");
            }
        });
    }
    public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }



}