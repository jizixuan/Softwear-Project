package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.adapter.InOrOutDetailAdapter;
import com.example.myapplication.entity.IoDetail;
import com.example.myapplication.util.ConfigUtil;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InOrOutDetailActivity extends AppCompatActivity {
    private ImageView re;
    private TextView date;
    private TextView txt1;
    private TextView num;
    private TextView txt2;
    private ListView lv;
    private List<IoDetail> ioDetails=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_or_out_detail);
        getView();
        initData();
        setListener();
        Explode explode = new Explode();
        explode.setDuration(500L);
        getWindow().setEnterTransition(explode);
    }

    private void setListener() {
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
            info="-";
        }else {
            txt1.setText("本月总收入");
            txt2.setText("单笔收入排行");
            info="+";
        }
        String dateValue=year+"年"+month+"月";
        date.setText(dateValue);
        getData(year,month,info);
    }

    /**
     * 从数据库根据年月获取信息
     * @param year
     * @param month
     */
    private void getData(String year, String month, final String info) {
        IoDetail io1=new IoDetail(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"食品",new Date(),99.00,19.6,(float) 100);
        IoDetail io2=new IoDetail(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"饮料",new Date(),99.00,16.2,(float) 88.6);
        IoDetail io3=new IoDetail(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"文具",new Date(),99.00,13.1,(float) 66.4);
        ioDetails.add(io1);
        ioDetails.add(io2);
        ioDetails.add(io3);
        InOrOutDetailAdapter adapter=new InOrOutDetailAdapter(ioDetails,getApplicationContext(),R.layout.io_detail);
        lv.setAdapter(adapter);
        //设置点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                IoDetail ioDetail=ioDetails.get(i);
                intent.putExtra("numType",info);
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
        setSumNum();
    }

    private void setSumNum() {
        double numValue=0;
        for (IoDetail item:ioDetails) {
            numValue+=item.getNum();
        }
        num.setText(numValue+"");
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
}
