package com.example.myapplication.note.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myapplication.R;
import com.example.myapplication.note.entity.NoteItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteItemActivity extends AppCompatActivity {

    private String date,title,note;
    private EditText etTitle;
    private TextView countNum;
    private EditText etNote;
    private TextView tvDate;
    private TimePickerView pvTime;
    //提醒
    private TextView remind;
    //扫描
    private TextView scan;
    private TextView protect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_item);
        findViews();
        showDatePicker();
        initdata();
        setListener();
    }

    private void setListener() {
        etNote.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                countNum.setText(temp.length()+"字");
            }
        });
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });
        protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //存入数据库
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteItemActivity.this,SimpleTextActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViews() {
        etTitle = findViewById(R.id.et_title);
        etNote = findViewById(R.id.edit_test);
        countNum = findViewById(R.id.tv_num);
        remind = findViewById(R.id.ti_note);
        scan = findViewById(R.id.ti1_note);
        tvDate = findViewById(R.id.tv_date);
        protect = findViewById(R.id.ti2_note);
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
    private void initdata() {
        Intent request=getIntent();
        date = request.getStringExtra("date");
        title = request.getStringExtra("title");
        note = request.getStringExtra("note");
        String type = request.getStringExtra("type");
        tvDate.setText(date);
        if(title!=null){
            etTitle.setText(title);
        }
        if(type.equals("item")){
            etNote.setText(note);
            countNum.setText(note.length()+"字");
        }else if(type.equals("create")){
            countNum.setText(0+"字");
        }else if(type.equals("remind")){
            pvTime.show();
            countNum.setText(0+"字");
        }
    }
    /**
     * 配置日期选择控件
     */
    private void showDatePicker() {
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //点击后的回调事件
                String[] str = getTime(date);
                String yearValue=str[0].split("-")[0];
                String momthValue=str[0].split("-")[1];
                String dateValue=str[0].split("-")[2];
                String hourValue=str[1].split(":")[0];
                String minentValue=str[1].split(":")[1];
                Intent intent=new Intent(NoteItemActivity.this,alarmActivity.class);
                PendingIntent pend=PendingIntent.getActivity(NoteItemActivity.this,0,intent,0); //显示闹钟，alarmActivity
                AlarmManager alarm= (AlarmManager) getSystemService(Context.ALARM_SERVICE);       // 通过Context.ALARM_SERVICE获取AlarmManager对象
                Calendar calendar =Calendar.getInstance();                     //获取日历对象
                calendar.set(Calendar.DAY_OF_YEAR, Integer.parseInt(yearValue));
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(momthValue));
                calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(dateValue));
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourValue));       //利用时间拾取组件timePicker得到要设定的时间
                calendar.set(Calendar.MINUTE, Integer.parseInt(minentValue));
                calendar.set(Calendar.SECOND,0);
                alarm.set(AlarmManager.RTC,calendar.getTimeInMillis(),pend);     //设定闹钟
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
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
    private String[] getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        String[] str = {format.format(date),format1.format(date)};
        return str;
    }
}