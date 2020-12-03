package com.example.myapplication.fragment;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.BillTypeAdapter;
import com.example.myapplication.entity.BillItem;
import com.example.myapplication.entity.BillType;
import com.example.myapplication.util.ServerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddIncomeFragment extends Fragment {
    private View view;
    private TimePickerView pvTime;
    private OkHttpClient okHttpClient;
    private RecyclerView recyclerView;
    private LinearLayout input;
    private ScrollView sv;
    private EditText note;
    private TextView num;
    private TextView num1,num2,num3,num4,num5,num6,num7,num8,num9,num0,numDot,numSum,numDim,numEq;
    private RelativeLayout numDelete,numDate;
    private ImageView calendarImg;
    private TextView calendarTv;
    private String text="";
    private Double result = 0.0;
    private int position0;
    private BillItem item;
    private List<BillType> billTypes=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    String str= (String) msg.obj;
                    try {
                        JSONArray jsonArray=new JSONArray(str);
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            BillType billType=new BillType();
                            billType.setName(jsonObject.getString("name"));
                            String files = view.getContext().getFilesDir().getAbsolutePath();
                            String imgs = files + "/"+ "typeImgs";
                            String imgPath = imgs + "/" + jsonObject.getString("img");
                            FileInputStream fis = new FileInputStream(imgPath);
                            Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                            billType.setImg(bitmap);
                            billType.setNumType(jsonObject.getString("numType"));
                            billTypes.add(billType);
                        }
                    } catch (JSONException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final BillTypeAdapter adapter=new BillTypeAdapter(billTypes);
                    recyclerView.setAdapter(adapter);
                    adapter.setItemListener(new BillTypeAdapter.onRecyclerItemClickerListener() {
                        @Override
                        public void onRecyclerItemClick(View view1, Object data, int position) {
                            adapter.setmPosition(position);
                            adapter.notifyDataSetChanged();
                            input.setVisibility(View.VISIBLE);
                            setScrollerHeight();
                            position0=position;
                        }
                    });
                    break;
            }
        }
    };
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_income,container,false);
        //初始化示图
        findView();
        setUpdateData();
        initViews();
        initData();
        setListener();
        showDatePicker();
        return view;
    }

    /**
     * 主界面点击修改后
     */
    private void setUpdateData() {
        item= getBillItem();
        if(item!=null){
            input.setVisibility(View.VISIBLE);
            num.setText(item.getNum()+"");
            text=item.getNum()+"";
            String dateValue=getDateValue();
            calendarImg.setVisibility(View.GONE);
            calendarTv.setText(dateValue);
            calendarTv.setGravity(Gravity.CENTER);
            note.setText(item.getNote());
        }
    }

    private void setListener() {
        MyListener listener=new MyListener();
        num1.setOnClickListener(listener);
        num2.setOnClickListener(listener);
        num3.setOnClickListener(listener);
        num4.setOnClickListener(listener);
        num5.setOnClickListener(listener);
        num6.setOnClickListener(listener);
        num7.setOnClickListener(listener);
        num8.setOnClickListener(listener);
        num9.setOnClickListener(listener);
        num0.setOnClickListener(listener);
        numDot.setOnClickListener(listener);
        numEq.setOnClickListener(listener);
        numSum.setOnClickListener(listener);
        numDim.setOnClickListener(listener);
        numDate.setOnClickListener(listener);
        numDelete.setOnClickListener(listener);
    }

    private void findView() {
        note=view.findViewById(R.id.add_bill_note);
        sv=view.findViewById(R.id.add_bill_sv);
        input=view.findViewById(R.id.add_bill_input);
        note=view.findViewById(R.id.add_bill_note);
        num=view.findViewById(R.id.add_bill_num);
        num1=view.findViewById(R.id.num1);
        num2=view.findViewById(R.id.num2);
        num3=view.findViewById(R.id.num3);
        num4=view.findViewById(R.id.num4);
        num5=view.findViewById(R.id.num5);
        num6=view.findViewById(R.id.num6);
        num7=view.findViewById(R.id.num7);
        num8=view.findViewById(R.id.num8);
        num9=view.findViewById(R.id.num9);
        num0=view.findViewById(R.id.num0);
        numDot=view.findViewById(R.id.num_dot);
        numSum=view.findViewById(R.id.num_sum);
        numDim=view.findViewById(R.id.num_dim);
        numEq=view.findViewById(R.id.num_eq);
        numDelete=view.findViewById(R.id.num_delete);
        numDate=view.findViewById(R.id.num_date);
        calendarImg=view.findViewById(R.id.calendar_img);
        calendarTv=view.findViewById(R.id.calendar_tv);
        input.setVisibility(View.GONE);
    }
    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.num_date:
                    pvTime.show();
                    break;
                case R.id.num1:
                    num(1);
                    break;
                case R.id.num2:
                    num(2);
                    break;
                case R.id.num3:
                    num(3);
                    break;
                case R.id.num4:
                    num(4);
                    break;
                case R.id.num5:
                    num(5);
                    break;
                case R.id.num6:
                    num(6);
                    break;
                case R.id.num7:
                    num(7);
                    break;
                case R.id.num8:
                    num(8);
                    break;
                case R.id.num9:
                    num(9);
                    break;
                case R.id.num0:
                    num(0);
                    break;
                case R.id.num_dot:
                    dot();
                    break;
                case R.id.num_sum:
                    add();
                    break;
                case R.id.num_dim:
                    sub();
                    break;
                case R.id.num_delete:
                    back();
                    break;
                case R.id.num_eq:
                    result();
                    text=result+"";
                    if(numEq.getText().equals("完成")){
                        //传输数据
                        getData();
                    }else{
                        numEq.setText("完成");
                    }

                    break;
            }
            num.setText(text);
        }
    }

    /**
     * 点击完成后获取填写的数据
     */
    private void getData() {
        if(isDouble(text)) {
            Log.e("lww","isd");
            Double numValue = Double.parseDouble(text);
            String dateValue0;
            if (calendarTv.getText().equals("今天")) {
                Calendar calendar = Calendar.getInstance();
                int yearValue = calendar.get(Calendar.YEAR);
                int monthValue = calendar.get(Calendar.MONTH) + 1;
                int dayValue = calendar.get(Calendar.DATE);
                dateValue0 = yearValue + "/" + monthValue + "/" + dayValue;
            } else {
                dateValue0 = calendarTv.getText().toString();
            }
            String noteValue = note.getText().toString();
            String typeName = billTypes.get(position0).getName();

            Intent intent = new Intent();
            if (billItem != null) {
                billItem = null;
                dateValue = null;
                //更新账单
            } else {
                //添加新账单
                insertData(numValue, dateValue0, noteValue, typeName);
                //把返回数据存入Intent
                intent.putExtra("num", numValue);
                intent.putExtra("date", dateValue0);
                intent.putExtra("note", noteValue);
                intent.putExtra("typeName", typeName);
                getActivity().setResult(1, intent);
            }
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.slide_in_top,
                    R.anim.slide_out_bottom);
        }else{
            Toast.makeText(view.getContext(),"输入数字有误，请重新输入",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 向服务器上传数据
     */
    private void insertData(Double numValue,String dateValue0,String noteValue,String typeName) {
        FormBody formBody =
                new FormBody.Builder()
                        .add("num", numValue+"")
                        .add("date",dateValue0)
                        .add("note",noteValue)
                        .add("typeName",typeName)
                        .add("userId",ServerConfig.USER_ID+"")
                        .build();
        //创建请求对象
        Request request = new Request.Builder()
                .url(ServerConfig.SERVER_HOME + "InsertBillItemServlet")
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
            }
        });
    }

    private void num(int i) {
        if(text.equals("0")){
            text=String.valueOf(i);
        }else {
            text = text + String.valueOf(i);
        }
    }
    private void dot() {
        text = text + ".";
    }
    private void back() {
        if(!text.equals("")) {
            if (text.substring(text.length() - 1).equals("-") || text.substring(text.length() - 1).equals("+")) {
                numEq.setText("完成");
            }
            String str = text.substring(0, text.length()-1);
            if(str.equals("")||str.equals("0")){
                text=0+"";
            }else {
                text = str;
            }
        }
    }
    private void add() {
        text += "+";
        numEq.setText("=");
    }
    private void sub() {
        text += "-";
        numEq.setText("=");
    }
    //计算输出结果
    private void result() {
        result = testOperation(text);
    }
    public Double testOperation(String s){
        //分割字符然后放进数组
        String s1 =s.replace("+","-");
        String[] str = s1.split("-");
        double total1=0;
        //先遍历数组，把里面的乘除结果算出来
        for(String str1:str){
            if(str1.contains("*")||str1.contains("/")){
                double total = 0;
                for(int i =0;i<str1.length();){
                    int count =1;
                    a:for(int j =i+1;j<str1.length();j++){
                        char c =str1.charAt(j);
                        if(c=='*'||c=='/'){
                            break a;
                        }else{
                            count++;
                        }
                    }

                    //将数字截取出来
                    String s2 =str1.substring(i,i+count);
                    double d = Double.parseDouble(s2);
                    if(i==0){
                        total = d;
                    }else{
                        char c1 = str1.charAt(i-1);
                        if(c1=='*'){
                            total*=d;
                        }else if(c1=='/'){
                            //如果除数为0，直接返回null;
                            if(d == 0)
                                return null;
                            total/=d;
                        }
                    }
                    i+=count+1;
                }
                s= s.replace(str1, total+"");
            }
        }
        //进行加减运算
        for(int i =0;i<s.length();i++){
            int count =1;
            a:for(int j=i+1;j<s.length();j++){
                char c = s.charAt(j);
                if(c=='+'||c=='-'){
                    break a;
                }else{
                    count++;
                }
            }
            String s3= s.substring(i,i+count);
            double d2 = Double.parseDouble(s3);
            if(i==0){
                total1 = d2;
            }else{
                char c = s.charAt(i-1);
                if(c=='+'){
                    total1+=d2;
                }else if(c=='-'){
                    total1-=d2;
                }
            }
            i+=count;
        }
        if(Double.parseDouble(String.valueOf(total1))<0){
            return 0.0;
        }else {
            return total1;
        }
    }

    /**
     * 加载服务器传来的数据
     */
    private void initData() {

        FormBody formBody =
                new FormBody.Builder()
                        .add("numType", "+")
                        .build();
        //创建请求对象
        Request request = new Request.Builder()
                .url(ServerConfig.SERVER_HOME + "GetBillTypeListByNumType")
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

    private void initViews() {
        recyclerView=view.findViewById(R.id.add_expenditure_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity (),4,GridLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator (new DefaultItemAnimator());
        okHttpClient=new OkHttpClient();
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

        linearParams.height=height-1000;
        sv.setLayoutParams(linearParams);
    }

    /**
     * 配置日期选择控件
     */
    private void showDatePicker() {
        pvTime = new TimePickerBuilder(view.getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //点击后的回调事件
                calendarImg.setVisibility(View.GONE);
                calendarTv.setText(getTime(date));
                calendarTv.setGravity(Gravity.CENTER);

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(date);
    }
    public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    /**

     * 判断 String 是否是 double<br>通过正则表达式判断

     * @param input

     * @return

     */
    public boolean isDouble(String input){
        Matcher mer = Pattern.compile("^[+-]?[0-9.]+$").matcher(input);

        return mer.find();

    }
    private BillItem billItem;
    private String dateValue;

    public String getDateValue() {
        return dateValue;
    }

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    public BillItem getBillItem() {
        return billItem;
    }

    public void setBillItem(BillItem billItem) {
        this.billItem = billItem;
    }
}
