package com.example.myapplication.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ImageUtils;
import com.example.myapplication.BillActivity;
import com.example.myapplication.BudgetActivity;
import com.example.myapplication.ChangeInfoActvity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.entity.BillMonth;
import com.example.myapplication.entity.DateBill;
import com.example.myapplication.util.ServerConfig;
import com.example.myapplication.view.ObservableScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.king.view.circleprogressview.CircleProgressView;

import java.io.IOException;
import java.lang.reflect.Type;
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

public class PersonalFragment extends Fragment {
    private ImageView imgPhoto;//头像
    private TextView tvName;//账户名
    private TextView tvNum;//记账笔数

    private RelativeLayout relativeVip;//vip部分relativeLayout

    private RelativeLayout relativeBill;
    private TextView tvMonth1;//当前账单月份
    private TextView tvIncome;//本月收入
    private TextView tvOutcome;//本月支出
    private TextView tvBalance;//本月结余

    private RelativeLayout relativeBudget;//本月预算
    private TextView tvMonth;//当前预算月份
    private int month;
    private TextView tvBudget1;//本月总预算
    private TextView tvBudget;//本月剩余预算
    private TextView tvOut;//本月支出

    private ObservableScrollView scrollView;//滑动控件
    private RelativeLayout relative;//头像部分relativeLayout
    private TextView textView;//标题
    private int relativeHeight;//头像部分高度
    private View view;
    private Button btnSettings;
    private Button btnChange;
    private Button btnExit;
    private CircleProgressView mPieChart;
    private int radio;
    private List<DateBill> dateBillList;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Log.i("lr","龙瑞");
                    String str = (String) msg.obj;
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<BillMonth>>(){}.getType();
                    Log.i("lr",type+"");
                    List<BillMonth> billMonths = gson.fromJson(str,type);
                    dateBillList = getDateBillList(billMonths);
                    for (DateBill dateBill : dateBillList){
                        SimpleDateFormat format = new SimpleDateFormat("MM");
                        String s = format.format(dateBill.getDate());
                        if(s.equals(month+"")){
                            tvMonth1.setText(month+"");
                            tvMonth.setText(month+"月总预算");
                            tvIncome.setText(dateBill.getIncome()+"");
                            tvOut.setText(dateBill.getExpenditure()+"");
                            tvOutcome.setText(dateBill.getExpenditure()+"");
                            tvBudget1.setText(ServerConfig.BUDGET+"");
                            double b = ServerConfig.BUDGET-dateBill.getExpenditure();
                            if(b>0){
                                tvBudget.setText(b+"");
                            }else {
                                tvBudget.setText("0.0");
                            }
                            double a = dateBill.getIncome()-dateBill.getExpenditure();
                            if(a>0){
                                tvBalance.setText(""+a);
                            }else {
                                tvBalance.setText("-"+a);
                            }
                            radio = (int) (b/ ServerConfig.BUDGET*100);
                            if(radio>0){
                                mPieChart.setLabelText("剩余"+radio+"%");
                                mPieChart.showAnimation(radio,1400);
                            }else {
                                mPieChart.setLabelText("超出预算");
                                mPieChart.showAnimation(0,1400);
                            }

                            mPieChart.setShowTick(false);
                            mPieChart.setMax(100);

                        }
                    }
                    break;
                case 2:
                    String str1 = (String) msg.obj;
                    Log.i("lr","记账总数"+str1);
                    tvNum.setText(str1);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal,container,false);
        //获取控件
        initViews();
        //设置预算比
        //设置滑动监听
        initListeners();
        setListener();
        downImg();
        getBillNum();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        getValue(year);
        tvName.setText(ServerConfig.USER_INFO.getName());
        return view;
    }
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

                }else{
                    income = billMonths.get(i+1).getBill();
                    out = billMonths.get(i).getBill();

                }
                Date date = new Date(1,billMonths.get(i).getMonth()-1,12);
                dateBill = new DateBill(date,income,out);
                dateBillList.add(dateBill);
                i+=1;
            }else{
                if(billMonths.get(i).getType().equals("+")){
                    income = billMonths.get(i).getBill();

                }else{
                    out = billMonths.get(i).getBill();

                }
                Date date = new Date(1,billMonths.get(i).getMonth()-1,12);
                dateBill = new DateBill(date,income,out);
                dateBillList.add(dateBill);
            }
        }

        return dateBillList;
    }
    private void downImg() {
        String files = getContext().getFilesDir().getAbsolutePath();
        String imgs = files+"/imgs";
        String imgPath = imgs + "/" + ServerConfig.USER_INFO.getPhone()+".jpg";
        Bitmap header1 = BitmapFactory.decodeFile(imgPath);
        header1 = ImageUtils.toRound(header1);
        imgPhoto.setImageBitmap(header1);
    }

    /**
     * 获取当月的记账数
     */
    private void getBillNum() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)+1;
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("month",month+"");
        formBody.add("id", ServerConfig.USER_INFO.getId()+"");
        final Request request = new Request.Builder()
                .post(formBody.build())
                .url(ServerConfig.SERVER_HOME+"GetBillNumServlet")
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
                msg.what = 2;
                msg.obj = str;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        MyListener myListener = new MyListener();
        relativeBudget.setOnClickListener(myListener);
        btnChange.setOnClickListener(myListener);
        relativeBill.setOnClickListener(myListener);
        btnExit.setOnClickListener(myListener);
    }


    private void initViews() {
        imgPhoto = view.findViewById(R.id.personalImg);
        tvName = view.findViewById(R.id.tv_personalName);
        tvNum = view.findViewById(R.id.tv_personalNum);

        relativeVip = view.findViewById(R.id.personalVip);

        relativeBill = view.findViewById(R.id.personalBill);
        tvMonth1 = view.findViewById(R.id.tv_personalMonth1);
        tvIncome = view.findViewById(R.id.tv_personalIncome);
        tvOutcome = view.findViewById(R.id.tv_personalOutcome);
        tvBalance = view.findViewById(R.id.tv_personalBalance);

        relativeBudget = view.findViewById(R.id.personalBudget);
        tvMonth = view.findViewById(R.id.tv_personalMonth);
        tvBudget = view.findViewById(R.id.tv_personalBudget);
        tvBudget1 = view.findViewById(R.id.tv_personalBudget1);
        tvOut = view.findViewById(R.id.tv_personalOut);

        btnChange = view.findViewById(R.id.btn_personalChange);
        btnExit = view.findViewById(R.id.btn_personalExit);
        btnSettings = view.findViewById(R.id.btn_personalSettings);

        textView = view.findViewById(R.id.textview);
        scrollView = view.findViewById(R.id.scrollview);
        relative = view.findViewById(R.id.personal);
        mPieChart = view.findViewById(R.id.Circle);
    }

    private void initListeners() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = relative.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relative.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                relativeHeight = relative.getHeight();
                scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        // TODO Auto-generated method stub
                        // Log.i("TAG", "y--->" + y + "    height-->" + height);
                        if (y <= 0) {
//                          设置文字背景颜色，白色
                            textView.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
//                          设置文字颜色，黑色
                            textView.setTextColor(Color.argb((int) 0, 255, 255, 255));
                            Log.e("111","y <= 0");
                        } else if (y > 0 && y <= relativeHeight) {
                            float scale = (float) y / relativeHeight;
                            float alpha = (255 * scale);
                            // 只是layout背景透明(仿知乎滑动效果)白色透明
                            textView.setBackgroundColor(Color.argb((int) alpha, 0, 145, 252));
                            //                          设置文字颜色，黑色，加透明度
                            textView.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                            Log.e("111","y > 0 && y <= imageHeight");
                        } else {
//							白色不透明
                            textView.setBackgroundColor(Color.argb((int) 255, 0, 145, 252));
                            //                          设置文字颜色
                            //黑色
                            textView.setTextColor(Color.argb((int) 255, 0, 0, 0));
                            Log.e("111","else");
                        }
                    }
                });

            }
        });
    }
    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.personalBudget:
                    Intent intent5 = new Intent(view.getContext(), BudgetActivity.class);
                    intent5.putExtra("budget", ServerConfig.BUDGET+"");
                    startActivityForResult(intent5,1);
                    break;
                case R.id.btn_personalChange:
                    Intent i1 = new Intent();
                    i1.setClass(view.getContext(), ChangeInfoActvity.class);
                    startActivityForResult(i1,2);
                    break;
                case R.id.personalBill:
                    Intent i2 = new Intent();
                    i2.setClass(view.getContext(), BillActivity.class);
                    startActivity(i2);
                    break;

                case R.id.btn_personalExit:
                    deleteData();
                    break;
            }
        }
    }

    private void deleteData() {
        SharedPreferences sd = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sd.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent();
        intent.setClass(getContext(), LoginActivity.class);
        startActivity(intent);
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
                Log.i("lr","龙瑞1");
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = str;
                handler.sendMessage(msg);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            downImg();
        }else if(requestCode == 1){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            getValue(year);
        }
    }
}
