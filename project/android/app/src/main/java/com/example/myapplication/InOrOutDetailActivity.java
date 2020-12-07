package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myapplication.adapter.InOrOutDetailAdapter;
import com.example.myapplication.entity.BillType;
import com.example.myapplication.entity.IoDetail;
import com.example.myapplication.util.ConfigUtil;
import com.example.myapplication.util.ServerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InOrOutDetailActivity extends AppCompatActivity {
    private ImageView re;
    private TextView date;
    private TextView txt1;
    private TextView num;
    private TextView txt2;
    private ListView lv;
    private List<IoDetail> ioDetails=new ArrayList<>();
    private String numType;
    private Double sum;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String str= (String) msg.obj;
                    try {
                        float progress=-1;
                        JSONArray jsonArray=new JSONArray(str);
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            int typeId=jsonObject.getInt("typeId");
                            BillType billType=ServerConfig.BILL_TYPES.get(typeId-1);
                            if(billType.getNumType().equals(numType)){
                                IoDetail ioDetail=new IoDetail();
                                ioDetail.setImg(billType.getImg());
                                Date date = stringToDate(jsonObject.getString("date"), "yyyy-MM-dd");
                                ioDetail.setDate(date);
                                ioDetail.setNote(jsonObject.getString("note"));
                                double num=jsonObject.getDouble("num");
                                ioDetail.setNum(num);
                                ioDetail.setType(billType.getName());
                                if(progress==-1) {
                                    progress = (float) num;
                                }
                                ioDetail.setPercent(num/sum*100);
                                ioDetail.setProgress((float) (num/progress)*100);
                                ioDetails.add(ioDetail);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    InOrOutDetailAdapter adapter=new InOrOutDetailAdapter(ioDetails,getApplicationContext(),R.layout.io_detail);
                    lv.setAdapter(adapter);
                    //设置点击事件
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent=new Intent();
                            IoDetail ioDetail=ioDetails.get(i);
                            intent.putExtra("numType",numType);
                            intent.putExtra("type",ioDetail.getType());
                            intent.putExtra("num",ioDetail.getNum());
                            //设置星期
                            Date date = ioDetail.getDate();
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String str0 = format.format(date);
                            Date date1 = ioDetail.getDate();
                            DateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
                            String str1 = format1.format(date);
                            String str=str1+" "+Week(str0);
                            intent.putExtra("date",str);
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            ioDetail.getImg().compress(Bitmap.CompressFormat.PNG, 100, out);
                            byte[] array= out.toByteArray();
                            intent.putExtra("bitmap",array);
                            intent.putExtra("note",ioDetail.getNote());
                            intent.setClass(InOrOutDetailActivity.this,BillDetailsActivity.class);
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(InOrOutDetailActivity.this).toBundle());
                        }
                    });
                    ConfigUtil.setListViewHeightBasedOnChildren(lv);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_or_out_detail);
        getView();
        initData();
        setListener();
    }

    private void setListener() {
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
            }
        });
    }

    private void initData() {
        Intent request=getIntent();
        String info=request.getStringExtra("info");
        String year=request.getStringExtra("year");
        String month=request.getStringExtra("month");
        if(info.equals("expenditure")){
            txt1.setText("本月总支出");
            txt2.setText("单笔支出排行");
            sum=request.getDoubleExtra("expenditureValue",0.0);
            info="-";
        }else {
            txt1.setText("本月总收入");
            txt2.setText("单笔收入排行");
            sum=request.getDoubleExtra("incomeValue",0.0);
            info="+";
        }
        numType=info;
        num.setText(sum+"");
        String dateValue=year+"年"+month+"月";
        date.setText(dateValue);
        getData(year,month);
    }

    /**
     * 从数据库根据年月获取信息
     * @param year
     * @param month
     */
    private void getData(String year, String month) {
        OkHttpClient okHttpClient=new OkHttpClient();
        FormBody formBody =
                new FormBody.Builder()
                        .add("year",year)
                        .add("month",month)
                        .add("userId", ServerConfig.USER_ID+"")
                        .build();
        //创建请求对象
        Request request = new Request.Builder()
                .url(ServerConfig.SERVER_HOME1 + "GetBillItemListOrderByNum")
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


    private void getView() {
        re=findViewById(R.id.io_detail_re);
        date=findViewById(R.id.io_detail_date);
        txt1=findViewById(R.id.io_detail_txt1);
        txt2=findViewById(R.id.io_detail_txt2);
        num=findViewById(R.id.io_detail_num);
        lv=findViewById(R.id.io_detail_lv);
    }
    /**
     * 计算星期
     * @param dateTime
     * @return
     */
    private int getDayofWeek(String dateTime) {

        Calendar cal = Calendar.getInstance();
        if (dateTime.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date;
            try {
                date = sdf.parse(dateTime);
            } catch (ParseException e) {
                date = null;
                e.printStackTrace();
            }
            if (date != null) {
                cal.setTime(new Date(date.getTime()));
            }
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    private String Week(String dateTime) {
        String week = "";
        switch (getDayofWeek(dateTime)) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
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
