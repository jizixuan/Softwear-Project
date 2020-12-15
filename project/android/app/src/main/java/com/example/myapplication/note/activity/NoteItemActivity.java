package com.example.myapplication.note.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myapplication.R;
import com.example.myapplication.note.entity.NoteItem;
import com.example.myapplication.util.ContentToSpannableString;
import com.example.myapplication.util.GlideImageEngine;
import com.example.myapplication.util.ServerConfig;
import com.example.myapplication.util.UriToPathUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class NoteItemActivity extends AppCompatActivity {
    private GlideImageEngine glideImageEngine;
    private String date,title,note;
    private String type;
    private EditText etTitle;
    private int REQUEST_CODE_CHOOSE = 23;
    private List<Uri> mSelected;
    private TextView countNum;
    private EditText etNote;
    private TextView tvDate;
    private TimePickerView pvTime;
    private int REQUEST_PERMISSION_CODE;
    //提醒
    private TextView remind;
    //扫描
    private TextView scan;
    private TextView protect;
    private TextView delete;
    private FloatingActionButton actionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_item);
        ActivityCompat.requestPermissions(NoteItemActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                REQUEST_PERMISSION_CODE);
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
                Log.i("lr","longrui");
                String mContent = etNote.getText().toString();
                String title=etTitle.getText().toString();
                String subContent;
                int i=title.length();
                if(i < mContent.length()){
                    int j = 0;
                    for(j = 0;j < mContent.length();j++){
                        if(mContent.charAt(j) == '\n'){
                            break;
                        }
                    }
                    subContent = mContent.substring(0,j);
                }else{
                    subContent = "";
                }
                final NoteItem noteItem=new NoteItem();
                noteItem.setContent(mContent);
                noteItem.setTitle(title);
                noteItem.setSubContent(subContent);

                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(date);
                noteItem.setCreateTime(str);
                final Intent intent = new Intent();
                if(type.equals("item")){
                    //更新
                    final int id=getIntent().getIntExtra("id",0);
                    OkHttpClient okHttpClient=new OkHttpClient();
                    FormBody formBody =
                            new FormBody.Builder()
                                    .add("userId", ServerConfig.USER_ID+"")
                                    .add("content",mContent)
                                    .add("createTime",str)
                                    .add("title",title)
                                    .add("id",id+"")
                                    .add("subContent",subContent)
                                    .build();
                    //创建请求对象
                    Request request = new Request.Builder()
                            .url(ServerConfig.SERVER_HOME + "UpdateNoteServlet")
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
                    noteItem.setId(id);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("note", noteItem);
                    intent.putExtra("note", bundle);
                    setResult(6, intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_right);
                }else if (type.equals("create")){
                    //创建
                    OkHttpClient okHttpClient=new OkHttpClient();
                    FormBody formBody =
                            new FormBody.Builder()
                                    .add("userId", ServerConfig.USER_ID+"")
                                    .add("content",mContent)
                                    .add("createTime",str)
                                    .add("title",title)
                                    .add("subContent",subContent)
                                    .build();
                    //创建请求对象
                    Request request = new Request.Builder()
                            .url(ServerConfig.SERVER_HOME + "InsertNoteServlet")
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
                            noteItem.setId(Integer.parseInt(result));
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("note",noteItem);
                            intent.putExtra("note",bundle);
                            setResult(5,intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_left,
                                    R.anim.slide_out_right);
                        }
                    });

                }else if (type.equals("create") || type.equals("scan")){
                    //创建
                    OkHttpClient okHttpClient=new OkHttpClient();
                    FormBody formBody =
                            new FormBody.Builder()
                                    .add("userId", ServerConfig.USER_ID+"")
                                    .add("content",mContent)
                                    .add("createTime",str)
                                    .add("title",title)
                                    .add("subContent",subContent)
                                    .build();
                    //创建请求对象
                    Request request = new Request.Builder()
                            .url(ServerConfig.SERVER_HOME + "InsertNoteServlet")
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
                            noteItem.setId(Integer.parseInt(result));
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("note",noteItem);
                            intent.putExtra("note",bundle);
                            setResult(5,intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_left,
                                    R.anim.slide_out_right);
                        }
                    });

                }

            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteItemActivity.this,SimpleTextActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
            }
        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callGallery();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            //删除
            final int id=getIntent().getIntExtra("id",0);
            @Override
            public void onClick(View view) {
                if(type.equals("item")){
                    OkHttpClient okHttpClient=new OkHttpClient();
                    FormBody formBody =
                            new FormBody.Builder()
                                    .add("id",id+"")
                                    .build();
                    //创建请求对象
                    Request request = new Request.Builder()
                            .url(ServerConfig.SERVER_HOME + "DeleteNoteServlet")
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
                    int id = getIntent().getIntExtra("id", 0);
                    Intent intent = new Intent();
                    intent.putExtra("id", id);
                    intent.putExtra("operation", "delete");
                    setResult(6, intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_right);
                }
            }
        });
    }

    private void findViews() {
        delete=findViewById(R.id.ti2_delete);
        actionButton=findViewById(R.id.button_note_new_picture);
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
        type = request.getStringExtra("type");
        tvDate.setText(date);
        if(title!=null){
            etTitle.setText(title);
        }
        if(type.equals("item")){
            //不能识别换行？？/n   replace 因为  Html.fromHtml 无法识别\n
            SpannableString spannableString = ContentToSpannableString.Content2SpanStr(NoteItemActivity.this, note);

            //不加下面这句点击没反应  可点击 字 的实现要求 注意：要位于textView.setText()的前面
            etNote.setMovementMethod(LinkMovementMethod.getInstance());
            etNote.setText(spannableString);
            countNum.setText(note.length()+"字");
        }else if(type.equals("create")){
            countNum.setText(0+"字");
        }else if(type.equals("remind")){
            pvTime.show();
            countNum.setText(0+"字");
        }else if(type.equals("scan")){
            etNote.setMovementMethod(LinkMovementMethod.getInstance());
            etNote.setText(note);
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
    private void callGallery(){
        glideImageEngine = new GlideImageEngine();

        Matisse.from(NoteItemActivity.this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(glideImageEngine)
                .forResult(REQUEST_CODE_CHOOSE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            if(data != null){
                if(requestCode == 1){

                }else if(requestCode == REQUEST_CODE_CHOOSE){
                    mSelected = Matisse.obtainResult(data);
                    Uri nSelected = mSelected.get(0);

                    //用Uri的string来构造spanStr，不知道能不能获得图片
                    //  ## +  string +  ##  来标识图片  <img src=''>

                    //SpannableString spanStr = new SpannableString(nSelected.toString());
                    SpannableString spanStr = new SpannableString("<img src='" + nSelected.toString() + "'/>");
                    Log.d("图片Uri",nSelected.toString());
                    String path = UriToPathUtil.getRealFilePath(this,nSelected);
                    Log.d("图片Path",path);

                    try{

                        //根据Uri 获得 drawable资源
                        Drawable drawable = Drawable.createFromStream(this.getContentResolver().openInputStream(nSelected),null);
                        drawable.setBounds(0,0,2 * drawable.getIntrinsicWidth(),2 * drawable.getIntrinsicHeight());
                        //BitmapDrawable bd = (BitmapDrawable) drawable;
                        //Bitmap bp = bd.getBitmap();
                        //bp.setDensity(160);
                        ImageSpan span = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
                        spanStr.setSpan(span,0,spanStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Log.d("spanString：",spanStr.toString());
                        int cursor = etNote.getSelectionStart();
                        etNote.getText().insert(cursor, spanStr);
                    }catch (Exception FileNotFoundException){
                        Log.d("异常","无法根据Uri找到图片资源");
                    }
                    //Drawable drawable = NoteNewActivity.this.getResources().getDrawable(nSelected);
                }
            }
        }
    }
}