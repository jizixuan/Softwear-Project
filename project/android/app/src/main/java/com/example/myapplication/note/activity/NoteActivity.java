package com.example.myapplication.note.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.AlertDialog;
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
import android.view.MenuItem;
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
import android.widget.TextView;
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
import com.example.myapplication.util.SpacesItemDecoration;
import com.example.myapplication.util.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class NoteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener{

    private ImageView re;
    //提醒
    private TextView remind;
    //扫描
    private TextView scan;
    //听写
    private TextView listen;
    private FloatingActionButton table;
    private ScrollView sv;
    RecyclerView recyclerView;
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
        NoteItem bill1 = new NoteItem(1,"11111",str,"11","sss");
        NoteItem bill2 = new NoteItem(2,"欢迎欢迎",str,"","");
        NoteItem bill3 = new NoteItem(3,"不觉得哪里是否",str,"","sss");
        NoteItem bill4 = new NoteItem(4,"11111",str,"2222","sss");
        NoteItem bill5 = new NoteItem(5,"欢迎欢迎",str,"","sss");
        NoteItem bill6 = new NoteItem(6,"不觉得哪里是否不觉得哪里是否不觉得哪里是否v不觉得哪里是否",str,"","sss");
        NoteItem bill7 = new NoteItem(7,"11111",str,"","sss");
        NoteItem bill8 = new NoteItem(8,"欢迎欢迎",str,"","sss");
        bills.add(bill1);
        bills.add(bill2);
        bills.add(bill3);
        bills.add(bill4);
        bills.add(bill5);
        bills.add(bill6);
        bills.add(bill7);
        bills.add(bill8);
        dateAdapter=new NoteAdapter(bills,NoteActivity.this);
        dateAdapter.setOnItemClickListener(new NoteAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, NoteItem note) {
                Intent intent = new Intent(NoteActivity.this,NoteItemActivity.class);
                intent.putExtra("date",note.getCreateTime());
                intent.putExtra("title",note.getTitle());
                intent.putExtra("note",note.getContent());
                intent.putExtra("id",note.getId());
                intent.putExtra("type","item");
                startActivityForResult(intent,6);
            }
        });
        dateAdapter.setOnItemLongClickListener(new NoteAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final NoteItem note) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                android.app.AlertDialog.Builder builder=new AlertDialog.Builder(NoteActivity.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateAdapter.delete(note);
                        if(!bills.contains(note)){
                            System.out.println("success");
                            //删除列表操作数据库
                        }else {
                            System.out.println("failed");
                        }
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
            }
        });
        recyclerView.addItemDecoration(new SpacesItemDecoration(3));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);  //两列，纵向排列
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(dateAdapter);

        //listView长按事件
//        dateAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                           final int position, long id) {
//                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
//                android.app.AlertDialog.Builder builder=new AlertDialog.Builder(NoteActivity.this);
//                builder.setMessage("确定删除?");
//                builder.setTitle("提示");
//
//                //添加AlertDialog.Builder对象的setPositiveButton()方法
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if(bills.remove(position)!=null){
//                            System.out.println("success");
//                            //删除列表操作数据库
//                        }else {
//                            System.out.println("failed");
//                        }
//                        dateAdapter.notifyDataSetChanged();
//                        Toast.makeText(getBaseContext(), "删除列表项", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                //添加AlertDialog.Builder对象的setNegativeButton()方法
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//                builder.create().show();
//                return true;
//            }
//        });

    }

    private void initViews() {
        recyclerView = findViewById(R.id.lv_note);
        re = findViewById(R.id.income_detail_re_note);
        remind = findViewById(R.id.ti);
        scan = findViewById(R.id.ti1);
        listen = findViewById(R.id.ti2);
        table = findViewById(R.id.ti_bottom);
        sv = findViewById(R.id.sv_note);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
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
                    intent.putExtra("id","");
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
                    intent1.putExtra("id","");
                    intent1.putExtra("title","");
                    intent1.putExtra("note","");
                    intent1.putExtra("type","create");
                    intent1.setClass(NoteActivity.this,NoteItemActivity.class);
                    startActivityForResult(intent1,5);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 5:
                if(data!=null) {
                    Bundle bundle=data.getBundleExtra("note");
                    NoteItem noteItem= (NoteItem) bundle.getSerializable("note");
                    noteItem.setId(bills.size());
                    dateAdapter.add(noteItem);
                }
                break;
            case 6:
                if(data!=null) {
                    if(data.getStringExtra("operation")==null) {
                        Bundle bundle = data.getBundleExtra("note");
                        NoteItem noteItem = (NoteItem) bundle.getSerializable("note");
                        int id = noteItem.getId();
                        for (int i = 0; i < bills.size(); i++) {
                            if (id == bills.get(i).getId()) {
                                dateAdapter.update(noteItem, i);
                                break;
                            }
                        }
                    }else{
                        int id=data.getIntExtra("id",0);
                        for (int i = 0; i < bills.size(); i++) {
                            if (id == bills.get(i).getId()) {
                                dateAdapter.delete(i);
                                break;
                            }
                        }
                    }
                }
                break;
        }
    }
}
