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
import android.os.Handler;
import android.os.Message;
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
import com.example.myapplication.util.ServerConfig;
import com.example.myapplication.util.SpacesItemDecoration;
import com.example.myapplication.util.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String str= (String) msg.obj;
                    try {
                        JSONArray jsonArray=new JSONArray(str);
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            NoteItem noteItem=new NoteItem();
                            noteItem.setId(jsonObject.getInt("id"));
                            noteItem.setCreateTime(jsonObject.getString("createTime"));
                            noteItem.setSubContent(jsonObject.getString("subContent"));
                            noteItem.setTitle(jsonObject.getString("title"));
                            noteItem.setContent(jsonObject.getString("content"));
                            bills.add(noteItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                            overridePendingTransition(R.anim.slide_in_right,
                                    R.anim.slide_out_left);
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
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initViews();
        setListener();
        setScrollerHeight();
        initFlash();
        initData();
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
    private void initData() {
        OkHttpClient okHttpClient=new OkHttpClient();
        FormBody formBody =
                new FormBody.Builder()
                        .add("userId", ServerConfig.USER_ID+"")
                        .build();
        //创建请求对象
        Request request = new Request.Builder()
                .url(ServerConfig.SERVER_HOME + "GetNoteListServlet")
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
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_right);
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
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    break;
                case R.id.ti1://扫描
                    Intent intent2 = new Intent(NoteActivity.this,SimpleTextActivity.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
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
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
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
