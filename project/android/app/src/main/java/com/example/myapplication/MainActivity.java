package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.myapplication.entity.BillType;
import com.example.myapplication.entity.DateBill;
import com.example.myapplication.util.ConfigUtil;
import com.example.myapplication.util.ServerConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
    private SmartRefreshLayout refreshLayout;
    private DateAdapter dateAdapter;

    List<DateBill> dateBills;
    private OkHttpClient okHttpClient=new OkHttpClient();
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String str= (String) msg.obj;
                    DateBill dateBill=new DateBill();;
                    Double dateIncomeValue=0.0;
                    Double dateExpenditureValue=0.0;
                    Double incomeValue=0.0;
                    Double expenditureValue=0.0;
                    dateBills=new ArrayList<>();
                    List<BillItem> billItems =new ArrayList<>();
                    try {
                        JSONArray jsonArray=new JSONArray(str);
                        JSONObject jsonObject0 = (JSONObject) jsonArray.get(0);
                        int day=jsonObject0.getInt("day");
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            int dayValue=jsonObject.getInt("day");
                            if(day!=dayValue){

                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i-1);
                                Date date = stringToDate(jsonObject1.getString("date"), "yyyy-MM-dd");
                                dateBill.setDate(date);
                                dateBill.setExpenditure(dateExpenditureValue);
                                dateBill.setIncome(dateIncomeValue);
                                dateBill.setBills(billItems);
                                dateBills.add(dateBill);

                                day = dayValue;
                                dateBill = new DateBill();
                                dateIncomeValue = 0.0;
                                dateExpenditureValue = 0.0;
                                billItems = new ArrayList<>();

                            }
                            BillItem billItem=new BillItem();
                            billItem.setNum(jsonObject.getDouble("num"));
                            int id=jsonObject.getInt("typeId");
                            BillType billType=ServerConfig.BILL_TYPES.get(id-1);
                            billItem.setType(billType.getName());
                            billItem.setNote(jsonObject.getString("note"));
                            billItem.setNumType(billType.getNumType());
                            if(billItem.getNumType().equals("+")){
                                dateIncomeValue+=billItem.getNum();
                            }else {
                                dateExpenditureValue+=billItem.getNum();
                            }
                            billItem.setImg(billType.getImg());
                            billItems.add(billItem);
                            if(i==jsonArray.length()-1){
                                if(day!=dayValue){
                                    DateBill dateBill1=null;
                                    dateBill1=new DateBill();
                                    List<BillItem> billItems1 =new ArrayList<>();
                                    billItems1.add(billItem);
                                    dateBill1.setBills(billItems1);
                                    Date date = stringToDate(jsonObject.getString("date"), "yyyy-MM-dd");
                                    dateBill1.setDate(date);
                                    dateBill1.setExpenditure(dateExpenditureValue);
                                    dateBill1.setIncome(dateIncomeValue);
                                    dateBills.add(dateBill1);
                                }else{
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dateAdapter=new DateAdapter(dateBills,root.getContext(), R.layout.date_bill);
                    list.setAdapter(dateAdapter);
                    ConfigUtil.setListViewHeightBasedOnChildren(list);
                    //设置当月总收入和支出
                    for(DateBill bill:dateBills){
                        for(BillItem item:bill.getBills()){
                            if(item.getNumType().equals("+")){
                                incomeValue+=item.getNum();
                            }else {
                                expenditureValue+=item.getNum();
                            }
                        }
                    }
                    //保留两位小数
                    income.setText(String.format("%.2f", incomeValue));
                    expenditure.setText(String.format("%.2f", expenditureValue));
                    break;
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.activity_main, container, false);
        findView();
        //显示当前年份
        setTime();
        initTypeImg();
        initData();
        showDatePicker();
        setLinstener();
        setRefreshLayout();
        return root;
    }

    /**
     * 加载类型图片
     */
    private void initTypeImg() {
        for(int i=0;i<ServerConfig.BILL_TYPES.size();i++){
            BillType billType=ServerConfig.BILL_TYPES.get(i);
            String files = root.getContext().getFilesDir().getAbsolutePath();
            String imgs = files + "/"+ "typeImgs";
            String imgPath = imgs + "/" + billType.getImgName();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imgPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap  = BitmapFactory.decodeStream(fis);
            billType.setImg(bitmap);
            ServerConfig.BILL_TYPES.set(i,billType);
        }
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
                int year=Integer.parseInt(tvYear.getText().toString());
                int month=Integer.parseInt(tvMonth.getText().toString());
                if(month==12){
                    year+=1;
                    month=1;
                }else{
                    month+=1;
                }
                tvMonth.setText(month+"");
                tvYear.setText(year+"");
                initData();
                refreshLayout.finishRefresh();
            }
        });
        //给智能刷新控件注册上拉加载更多事件监听器
        //查看上月数据
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                int year=Integer.parseInt(tvYear.getText().toString());
                int month=Integer.parseInt(tvMonth.getText().toString());
                if(month==1){
                    year-=1;
                    month=12;
                }else{
                    month-=1;
                }
                tvMonth.setText(month+"");
                tvYear.setText(year+"");
                initData();
                refreshLayout.finishLoadMore();
            }
        });
    }


    /**
     * 从服务器获取数据
     */
    public void initData() {
        dateAdapter=null;
        FormBody formBody =
                new FormBody.Builder()
                        .add("year",tvYear.getText().toString())
                        .add("month",tvMonth.getText().toString())
                        .add("userId", ServerConfig.USER_ID+"")
                        .build();
        Log.e("lww",ServerConfig.USER_ID+"");
        //创建请求对象
        Request request = new Request.Builder()
                .url(ServerConfig.SERVER_HOME + "GetBillItemListByDateServlet")
                .method("POST", formBody)
                .post(formBody)
                .build();
        //3. 创建CALL对象
        Call call = okHttpClient.newCall(request);
        //3. 异步方式提交请求并获取响应
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lww", "请求失败");
            }

            @Override
            public void onResponse( Call call,  Response response) throws IOException {
                //获取服务端返回的数据
                String result = response.body().string();
                //使用handler将数据封装在Message中，并发布出去，以备显示在UI控件中
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    private void setLinstener() {
        MyListener listener=new MyListener();
        changeTime.setOnClickListener(listener);
        income.setOnClickListener(listener);
        expenditure.setOnClickListener(listener);
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
                //重新刷新界面

                initData();


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
    public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * fragmenttabhost界面回调方法调用此方法
     * 修改
     * @param num
     * @param dateValue
     * @param noteValue
     * @param typeName
     */
    public void addItem(Double num,String dateValue,String noteValue,String typeName){
        int year=Integer.parseInt(dateValue.split("/")[0]);
        int month=Integer.parseInt(dateValue.split("/")[1]);
        int day=Integer.parseInt(dateValue.split("/")[2]);

        if(month==Integer.parseInt(tvMonth.getText().toString())&&year==Integer.parseInt(tvYear.getText().toString())) {
            BillItem billItem=new BillItem();
            billItem.setNum(num);
            billItem.setNote(noteValue);
            for(BillType type:ServerConfig.BILL_TYPES){
                if(type.getName().equals(typeName)){
                    billItem.setImg(type.getImg());
                    billItem.setNumType(type.getNumType());
                    billItem.setType(type.getName());
                }
            }
            int flag=-5;
            int flag1=-5;
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            for (int i = 0; i < dateBills.size(); i++) {
                int day1=Integer.parseInt(format.format(dateBills.get(i).getDate()).split("/")[2]);
                if(day<day1){
                    flag = i ;
                    break;
                }else if(day==day1){
                    flag1=i;
                    break;
                }
            }
            Double expenditure=0.0;
            Double income=0.0;
            if(flag1!=-5){//如果日期已存在
                dateBills.get(flag1).getBills().add(billItem);
                if(billItem.getNumType().equals("+")){
                    income=dateBills.get(flag1).getIncome()+billItem.getNum();
                    dateBills.get(flag1).setIncome(income);
                }else{
                    expenditure=dateBills.get(flag1).getExpenditure()+billItem.getNum();
                    dateBills.get(flag1).setExpenditure(expenditure);
                }
            } else if(flag==-5){//如果日期位置在最后
                DateBill dateBill1=new DateBill();
                dateBill1.setDate(stringToDate(dateValue, "yyyy/MM/dd"));
                List<BillItem> billItems=new ArrayList<>();
                billItems.add(billItem);
                dateBill1.setBills(billItems);
                if(billItem.getNumType().equals("+")) {
                    dateBill1.setIncome(billItem.getNum());
                }else{
                    dateBill1.setExpenditure(billItem.getNum());
                }
                dateBills.add(dateBill1);
            }else{//不满足前两个条件则插入
                List<DateBill> dateBills1=new ArrayList<>();
                if(flag>0) {
                    for (int i = 0; i < flag; i++) {
                        dateBills1.add(dateBills.get(i));
                    }
                }
                DateBill dateBill1=new DateBill();
                dateBill1.setDate(stringToDate(dateValue, "yyyy/MM/dd"));
                List<BillItem> billItems=new ArrayList<>();
                billItems.add(billItem);
                dateBill1.setBills(billItems);
                if(billItem.getNumType().equals("+")) {
                    dateBill1.setIncome(billItem.getNum());
                }else{
                    dateBill1.setExpenditure(billItem.getNum());
                }
                dateBills1.add(dateBill1);
                for (int i=flag;i<dateBills.size();i++){
                    dateBills1.add(dateBills.get(i));
                }
                dateBills.clear();
                dateBills=dateBills1;
            }
            dateAdapter=new DateAdapter(dateBills,root.getContext(), R.layout.date_bill);
            list.setAdapter(dateAdapter);
            ConfigUtil.setListViewHeightBasedOnChildren(list);
        }
    }
}
