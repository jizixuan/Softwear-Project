package com.example.myapplication.calendar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

import com.example.myapplication.R;
import com.example.myapplication.calendar.base.activity.BaseActivity;
import com.example.myapplication.calendar.group.GroupItemDecoration;
import com.example.myapplication.calendar.group.GroupRecyclerView;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.TrunkBranchAnnals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main1Activity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnCalendarLongClickListener,
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
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case '1':
                    break;
                case '2':
                    //将数据库传来日期信息，解析，保存到map中
                    Map<String, Calendar> map = new HashMap<>();
                    for(int i = 0;i<20;++i){
                        int year = 2021;//传来的数据年份
                        int month = 1;//传来的数据月份
                        int day = 2;//传来的数据日子

                        map.put(getSchemeCalendar(year, month, day, 0xFF40db25, "记账").toString(),
                                getSchemeCalendar(year, month, day, 0xFF40db25, "记账"));
                    }
                    //28560 数据量增长不会影响UI响应速度，请使用这个API替换
                    mCalendarView.setSchemeDate(map);
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
        mCalendarView.setOnCalendarLongClickListener(this, true);
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
        mRecyclerView.setAdapter(new BillItemAdapter(this));
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
            Toast.makeText(this, getCalendarText(calendar), Toast.LENGTH_SHORT).show();
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
        Log.i("lr", "onMonthChange:选择日期 "+year1+"年"+month1+"月");
        //getDays(year1,month1);
    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {
        Toast.makeText(this, String.format("%s : LongClickOutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalendarLongClick(Calendar calendar) {
        Toast.makeText(this, "长按不选择日期\n" + getCalendarText(calendar), Toast.LENGTH_SHORT).show();
    }

    private static String getCalendarText(Calendar calendar) {
        return String.format("新历%s \n 农历%s \n 公历节日：%s \n 农历节日：%s \n 节气：%s \n 是否闰月：%s",
                calendar.getMonth() + "月" + calendar.getDay() + "日",
                calendar.getLunarCalendar().getMonth() + "月" + calendar.getLunarCalendar().getDay() + "日",
                TextUtils.isEmpty(calendar.getGregorianFestival()) ? "无" : calendar.getGregorianFestival(),
                TextUtils.isEmpty(calendar.getTraditionFestival()) ? "无" : calendar.getTraditionFestival(),
                TextUtils.isEmpty(calendar.getSolarTerm()) ? "无" : calendar.getSolarTerm(),
                calendar.getLeapMonth() == 0 ? "否" : String.format("闰%s月", calendar.getLeapMonth()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMonthChange(int year, int month) {
            Log.e("onMonthChange", "  -- " + year + "  --  " + month);
            Calendar calendar = mCalendarView.getSelectedCalendar();
            mTextLunar.setVisibility(View.VISIBLE);
            mTextYear.setVisibility(View.VISIBLE);
            mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mTextYear.setText(String.valueOf(calendar.getYear()));
            mTextLunar.setText(calendar.getLunar());
            mYear = calendar.getYear();
            int year1 = calendar.getYear();
            int month1 = calendar.getMonth()+1;
            Log.i("lr", "onMonthChange:改变月份代码 "+year1+"年"+month1+"月");
            getDays(year1,month1);
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
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("year",year+"");
        formBody.add("month",month+"");
        //formBody.add("userId", ServerConfig.USER_ID+"");
        formBody.add("userId", 1+"");
        Request request = new Request.Builder()
                .post(formBody.build())
                .url(ServerConfig.SERVER_HOME+"GetBillItemListByDateServlet")
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
                msg.what = 2;
                msg.obj = str;
                handler.sendMessage(msg);
            }
        });

    }

    /**
     * 根据点击日期，获得当前日期的账单，并返回到客户端
     * @param calendar
     */
    public void getBills(Calendar calendar){
        int year = calendar.getYear();
        int month = calendar.getMonth()+1;
        int day = calendar.getDay();
        Log.i("lr",year+"年:"+month+"月"+day+"日");
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("year",year+"");
        formBody.add("month",month+"");
        formBody.add("day",day+"");
        //formBody.add("userId", ServerConfig.USER_ID+"");
        formBody.add("userId", 1+"");
        Request request = new Request.Builder()
                .post(formBody.build())
                .url(ServerConfig.SERVER_HOME+"GetBillItemListByDateServlet")
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
            }
        });
    }


}