package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.entity.BillItem;
import com.example.myapplication.entity.BillType;
import com.example.myapplication.entity.Budget;
import com.example.myapplication.entity.DateBill;
import com.example.myapplication.util.ServerConfig;
import com.example.myapplication.util.TakeBudgetPopWin;
import com.google.gson.Gson;
import com.king.view.circleprogressview.CircleProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BudgetActivity extends AppCompatActivity {
    private EditText edtBudget;//输入的预算
    private Button btnConfirm;//确认预算
    private Button btnCancel;//取消预算
    private String str;//输入预算字符串
    private RelativeLayout budget;//预算
    private PopupWindow mPop;
    private int radio;
    private TextView tvMonth;//月份
    private TextView tvBudget;//剩余预算
    private TextView tvBugget1;//预算
    private double value;//是否已经有预算
    private TextView tvOut;//支出
    private List<DateBill> dateBills;
    private CircleProgressView mPieChart;
    private Double expenditureValue;//这个月支出
    private TakeBudgetPopWin takePhotoPopWin;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String str= (String) msg.obj;
                    com.example.myapplication.entity.DateBill dateBill=new com.example.myapplication.entity.DateBill();;
                    Double dateIncomeValue=0.0;
                    Double dateExpenditureValue=0.0;
                    Double incomeValue=0.0;
                    expenditureValue=0.0;
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
                                dateBill = new com.example.myapplication.entity.DateBill();
                                dateIncomeValue = 0.0;
                                dateExpenditureValue = 0.0;
                                billItems = new ArrayList<>();

                            }
                            BillItem billItem=new BillItem();
                            billItem.setNum(jsonObject.getDouble("num"));
                            int id=jsonObject.getInt("typeId");
                            BillType billType= ServerConfig.BILL_TYPES.get(id-1);
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
                                    com.example.myapplication.entity.DateBill dateBill1=null;
                                    dateBill1=new com.example.myapplication.entity.DateBill();
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
                    tvOut.setText(String.format("%.2f", expenditureValue));
                    tvBudget.setText(String.format("%.2f",value-expenditureValue));
                    radio = (int) ((value-expenditureValue)/value*100);
                    if(radio>0){
                        mPieChart.setLabelText("剩余"+radio+"%");
                        mPieChart.showAnimation(radio,1400);
                    }else {
                        mPieChart.setLabelText("超出预算");
                        mPieChart.showAnimation(0,1400);
                    }

                    mPieChart.setShowTick(false);
                    mPieChart.setMax(100);
                    break;
                case 2:
                    String s2 = (String) msg.obj;
                    if (s2.equals("1")){
                        value=0;
                        radio = 0;
                        tvBugget1.setText(value+"");
                        tvBudget.setText(String.format("%.2f",value-expenditureValue));
                        mPieChart.setLabelText("剩余"+radio+"%");
                        mPieChart.setShowTick(false);
                        mPieChart.setMax(100);
                        mPieChart.showAnimation(radio,1400);
                    }
                    break;
                case 3:

                    break;
                case 4:
                    String s4 = (String) msg.obj;
                    Gson gson = new Gson();
                    Budget budget  = gson.fromJson(s4, Budget.class);
                    if(budget.getBudget() != 0){
                        value = budget.getBudget();
                        Log.i("lr","获取预算"+value);
                    }else{
                        value = 0.0;
                    }
                    tvBugget1.setText(value+"");
                    tvBudget.setText(String.format("%.2f",value-expenditureValue));
                    radio = (int) ((value-expenditureValue)/value*100);
                    if(radio>0){
                        mPieChart.setLabelText("剩余"+radio+"%");
                        mPieChart.showAnimation(radio,1400);
                    }else {
                        mPieChart.setLabelText("超出预算");
                        mPieChart.showAnimation(0,1400);
                    }

                    mPieChart.setShowTick(false);
                    mPieChart.setMax(100);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        Intent intent = getIntent();
        String s = intent.getStringExtra("budget");
        value = Double.parseDouble(s);
        initView();
        getBills();
        if(value == 0){
            showAlertDialog();
            Log.i("lr","弹窗预算"+value);
        }else{
            tvBugget1.setText(value+"");
        }

        budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopFormBottom(view);
            }
        });
        Log.i("lr",value+"比例图"+expenditureValue);

    }

    private void initView() {
        mPieChart = findViewById(R.id.Circle1);
        budget = findViewById(R.id.budget);
        tvMonth = findViewById(R.id.tv_budgetMonth);
        tvBudget = findViewById(R.id.tv_budget);
        tvBugget1 = findViewById(R.id.tv_budget1);
        tvOut = findViewById(R.id.tv_Out);
    }

    /**
     * 显示弹窗
     */
    private void showAlertDialog() {
        //创建Builder对象
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框属性
        final View view = getLayoutInflater().inflate(R.layout.budget_message,null);
        builder.setView(view);
        edtBudget = view.findViewById(R.id.edt_dialogBudget);

        btnConfirm = view.findViewById(R.id.btn_dialogConfirm);
        btnCancel = view.findViewById(R.id.btn_dialogCancel);
        btnConfirm.setEnabled(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();//显示弹窗
        edtBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //boolean tag1 = false;
                boolean tag = edtBudget.getText().length()>0;
                if(tag){
                    btnConfirm.setEnabled(true);
                    btnConfirm.setTextColor(getResources().getColor(R.color.bule));
                }else{
                    btnConfirm.setEnabled(false);
                    btnConfirm.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(value == 0){
                    Log.i("lr","取消判断条件value"+value);
                    finish();
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(value == 0){
                    //添加预算
                    str = edtBudget.getText().toString();
                    AddBudget(Double.parseDouble(str));
                    alertDialog.dismiss();
                }else{
                    str = edtBudget.getText().toString();
                    //更改预算
                    UpdateBudget(Double.parseDouble(str));
                    alertDialog.dismiss();
                }

            }
        });
    }
    public void showPopFormBottom(View view) {
        takePhotoPopWin = new TakeBudgetPopWin(this, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        takePhotoPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.CENTER, 0, 0);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_changeBudget:
                    //打开弹窗
                    showAlertDialog();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.btn_deleteBudget:
                    //删除预算
                    DeleteBudget();
                    Intent intent = new Intent(BudgetActivity.this, MainActivity.class);
                    setResult(1,intent);
                    takePhotoPopWin.dismiss();
                    finish();
                    break;
            }
        }
    };
    public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 从数据库里获取账单数据
     */
    private void getBills() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        tvMonth.setText(month+"月总预算");
        Log.i("lr",year+"年:"+month+"月");
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("year",year+"");
        formBody.add("month",month+"");
        formBody.add("userId", ServerConfig.USER_ID+"");
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
    /**
     * 更改预算信息
     * @param str
     */
    private void UpdateBudget(double str) {
        Budget budget = new Budget();
        budget.setId(ServerConfig.USER_ID);
        budget.setBudget(str);
        ServerConfig.BUDGET = str;
        value = str;
        Gson gson = new Gson();
        String json = gson.toJson(budget);
        OkHttpClient client  = new OkHttpClient();
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        Request request = new Request.Builder()
                .post(requestBody)
                .url(ServerConfig.SERVER_HOME+"UpdateBudgetServlet")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.e("budget",str);
                Message msg = handler.obtainMessage();
                msg.what = 4;
                msg.obj = str;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 添加预算信息
     * @param str
     */
    private void AddBudget(double str) {
        Budget budget = new Budget();
        budget.setId(ServerConfig.USER_ID);
        budget.setBudget(str);
        ServerConfig.BUDGET = str;
        value = str;
        Gson gson = new Gson();
        String json = gson.toJson(budget);
        OkHttpClient client  = new OkHttpClient();
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        Request request = new Request.Builder()
                .post(requestBody)
                .url(ServerConfig.SERVER_HOME+"AddBudgetServlet")
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
                msg.what = 4;
                msg.obj = str;
                handler.sendMessage(msg);
            }
        });
    }
    /**
     * 删除预算
     */
    private void DeleteBudget() {
        ServerConfig.BUDGET = 0.0;
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("id", ServerConfig.USER_ID+"");
        Request request = new Request.Builder()
                .post(formBody.build())
                .url(ServerConfig.SERVER_HOME+"DeleteBudgetServlet")
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
}
