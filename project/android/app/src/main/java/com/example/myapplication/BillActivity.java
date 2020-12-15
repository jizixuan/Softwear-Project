package com.example.myapplication;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myapplication.adapter.BillYearAdapter;
import com.example.myapplication.entity.BillMonth;
import com.example.myapplication.entity.DateBill;
import com.example.myapplication.util.ServerConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BillActivity extends AppCompatActivity {
    private TextView tvYear;//年份
    private TimePickerView pvTime;//时间选择器
    private LinearLayout changeYear;//年份选择
    private ListView list_Bill;//账单list
    private BillYearAdapter adapter;
    private List<BillMonth> billMonths;
    private List<DateBill> dateBillList;
    private int yearValue;//年分
    private ScrollView sv;
    private double sumIncome,sumOutcome,sumBalance;
    private TextView tvIncome,tvOutcome,tvBalancce;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    String str = (String) msg.obj;
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<BillMonth>>(){}.getType();
                    Log.i("lr",type+"");
                    List<BillMonth> billMonths = gson.fromJson(str,type);
                    dateBillList = getDateBillList(billMonths);
                    adapter = new BillYearAdapter(BillActivity.this, R.layout.date_bill_item,dateBillList);
                    list_Bill.setAdapter(adapter);
                    break;
            }
        }
    };

    private List<DateBill> getDateBillList(List<BillMonth> billMonths) {
        dateBillList = new ArrayList<>();
        DateBill dateBill = new DateBill();
        for(int i =0; i<billMonths.size();i++){
            double income = 0;
            double out =0;
            int n = -1;
            if (i == billMonths.size() - 1) {
                n = -1;
            }else{
                n = billMonths.get(i+1).getMonth();
            }
            if(n == billMonths.get(i).getMonth()){
                if(billMonths.get(i).getType().equals("+")){
                    income = billMonths.get(i).getBill();
                    out = billMonths.get(i+1).getBill();
                    sumIncome +=income;
                    sumOutcome +=out;
                }else{
                    income = billMonths.get(i+1).getBill();
                    out = billMonths.get(i).getBill();
                    sumIncome +=income;
                    sumOutcome +=out;
                }
                Date date = new Date(yearValue,billMonths.get(i).getMonth()-1,12);
                dateBill = new DateBill(date,income,out);
                dateBillList.add(dateBill);
                i+=1;
            }else{
                if(billMonths.get(i).getType().equals("+")){
                    income = billMonths.get(i).getBill();
                    sumIncome +=income;
                    sumOutcome +=out;
                }else{
                    out = billMonths.get(i).getBill();
                    sumIncome +=income;
                    sumOutcome +=out;
                }
                Date date = new Date(yearValue,billMonths.get(i).getMonth()-1,12);
                dateBill = new DateBill(date,income,out);
                dateBillList.add(dateBill);
            }
        }
        sumBalance = sumIncome-sumOutcome;
        DecimalFormat myformat = new DecimalFormat("0.00");
        String str1 = myformat.format(sumIncome);
        String str2 = myformat.format(sumOutcome);
        tvIncome.setText(str1);
        tvOutcome.setText(str2);
        String str3 = myformat.format(sumBalance);
        if(sumBalance>0){
            tvBalancce.setText(str3);
        }else{
            tvBalancce.setText("-"+str3);
        }
        return dateBillList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initViews();
        setTime();
        showDatePicker();
        changeYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });

        setScrollerHeight();
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
        yearValue = calendar.get(Calendar.YEAR);
        getValue(yearValue);
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
                yearValue= Integer.parseInt(getTime(date).split("-")[0]);
                tvYear.setText(yearValue+"年");
                getValue(yearValue);

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
    public void getValue(int yearValue){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("year",yearValue+"");
        formBody.add("id", ServerConfig.USER_INFO.getId()+"");
        Request request = new Request.Builder()
                .url(ServerConfig.SERVER_HOME+"GetBillMonthListByYearServlet")
                .post(formBody.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

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
