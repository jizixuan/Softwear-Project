package com.example.myapplication.note.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.transition.Explode;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.IconAdapter;
import com.example.myapplication.entity.IconItem;
import com.example.myapplication.entity.IconList;
import com.example.myapplication.note.adapter.NoteAdapter;
import com.example.myapplication.note.entity.NoteItem;
import com.example.myapplication.util.ConfigUtil;
import com.example.myapplication.util.NumSort;
import com.example.myapplication.util.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private ImageView re;
    //提醒
    private ImageView remind;
    //扫描
    private ImageView scan;
    //听写
    private ImageView listen;
    private ImageView table;
    private ListView list;
    private ScrollView sv;
    private NoteAdapter dateAdapter;
    private List<NoteItem> bills = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initViews();
        setListener();
        setScrollerHeight();
        initFlash();
        test();
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
        RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) sv.getLayoutParams();
        linearParams.height=height-750;
        sv.setLayoutParams(linearParams);
    }
    /**
     * 设置跳转动画
     */
    private void initFlash() {
        Explode explode = new Explode();
        explode.setDuration(450);
        getWindow().setEnterTransition(explode);
    }

    private void setListener() {
        MyListener listener = new MyListener();
        re.setOnClickListener(listener);
        remind.setOnClickListener(listener);
        scan.setOnClickListener(listener);
        listen.setOnClickListener(listener);
        table.setOnClickListener(listener);

    }
    /**
     * 放数据
     */
    private void test() {
        //设置星期
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String str = format.format(date);
        NoteItem bill1 = new NoteItem("11111",str,"11");
        NoteItem bill2 = new NoteItem("欢迎欢迎",str,"");
        NoteItem bill3 = new NoteItem("不觉得哪里是否",str,"");
        NoteItem bill4 = new NoteItem("11111",str,"2222");
        NoteItem bill5 = new NoteItem("欢迎欢迎",str,"");
        NoteItem bill6 = new NoteItem("不觉得哪里是否不觉得哪里是否不觉得哪里是否v不觉得哪里是否",str,"");
        NoteItem bill7 = new NoteItem("11111",str,"");
        NoteItem bill8 = new NoteItem("欢迎欢迎",str,"");
        NoteItem bill9 = new NoteItem("不觉得哪里是否",str,"");
        NoteItem bill10 = new NoteItem("11111",str,"");
        NoteItem bill11 = new NoteItem("欢迎欢迎",str,"");
        NoteItem bill12 = new NoteItem("不觉得哪里是否",str,"");
        bills.add(bill1);
        bills.add(bill2);
        bills.add(bill3);
        bills.add(bill4);
        bills.add(bill5);
        bills.add(bill6);
        bills.add(bill7);
        bills.add(bill8);
        bills.add(bill9);
        bills.add(bill10);
        bills.add(bill11);
        bills.add(bill12);
        dateAdapter=new NoteAdapter(bills,this, R.layout.adapter_note_list);
        list.setAdapter(dateAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                NoteItem item=bills.get(i);
                intent.putExtra("title",item.getTitle());
                intent.putExtra("note",item.getNote());
                intent.putExtra("type","item");
                intent.putExtra("date",getDate());
                intent.setClass(NoteActivity.this,NoteItemActivity.class);
                startActivity(intent);
            }
        });
        //stView长按事件
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder=new AlertDialog.Builder(NoteActivity.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(bills.remove(position)!=null){
                            System.out.println("success");
                            //删除列表操作数据库
                        }else {
                            System.out.println("failed");
                        }
                        dateAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "删除列表项", Toast.LENGTH_SHORT).show();
                    }
                });

                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                return true;
            }
        });
        ConfigUtil.setListViewHeightBasedOnChildren(list);
    }

    private void initViews() {
        re = findViewById(R.id.income_detail_re_note);
        remind = findViewById(R.id.ti);
        scan = findViewById(R.id.ti1);
        listen = findViewById(R.id.ti2);
        table = findViewById(R.id.ti_bottom);
        list = findViewById(R.id.lv_note);
        sv = findViewById(R.id.sv_note);
    }
    public class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.income_detail_re_note:
                    onBackPressed();
                    break;
                case R.id.ti://提醒
                    //设置选择日期
                    Intent intent = new Intent();
                    intent.putExtra("date",getDate());
                    intent.putExtra("title","");
                    intent.putExtra("note","");
                    intent.putExtra("type","remind");
                    intent.setClass(NoteActivity.this,NoteItemActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ti1://扫描
                    Intent intent2 = new Intent(NoteActivity.this,SimpleTextActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.ti2://听写
                    break;
                case R.id.ti_bottom://创建便签
                    Intent intent1 = new Intent();
                    intent1.putExtra("date",getDate());
                    intent1.putExtra("title","");
                    intent1.putExtra("note","");
                    intent1.putExtra("type","create");
                    intent1.setClass(NoteActivity.this,NoteItemActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    }
    private String getDate(){
        //设置星期
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String str = format.format(date);
        //设置日期
        Date date1 = new Date();
        DateFormat format1 = new SimpleDateFormat("MM月dd日");
        final String str1 = format1.format(date1);
        //设置时间
        Date date2 = new Date();
        DateFormat format2 = new SimpleDateFormat("HH:mm:ss");
        final String str2 = format2.format(date2);
        return str+"   "+str2+"  "+Week(str1);
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
    /**
     * 解析日期选择控件获得数据
     * @param date
     * @return
     */
    private String[] getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        String[] str = {format.format(date),format1.format(date)};
        return str;
    }
}
