package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.entity.User;
import com.example.myapplication.util.ServerConfig;
import com.example.myapplication.view.ForgetPwdActivity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    private EditText edtUser;//用户名
    private EditText edtPwd;//密码
    private Button btnLogin;//登录按钮
    private Button btnRegist;//注册按钮
    private ImageView imgHidePwd;//隐藏密码
    private boolean isPwdVisible = false;//密码是否可见
    private String phone,pwd;
    private TextView forgetPwd;
    private CustomVideoView videoview;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    String a = (String) msg.obj;
                    if (a.equals("1")) {
                        Toast.makeText(getApplicationContext(),"请先去注册",Toast.LENGTH_LONG).show();
                    }else if(a.equals("3")){
                        Toast.makeText(getApplicationContext(),"该号码已经被注册，忘记密码，请找回",Toast.LENGTH_LONG).show();
                    }else {
                        Gson gson = new Gson();
                        ServerConfig.USER_INFO = gson.fromJson(a,User.class);
                        ServerConfig.USER_ID = ServerConfig.USER_INFO.getId();
                        SharedPreferences sd = getSharedPreferences("Login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sd.edit();
                        editor.putString("name",phone);
                        editor.putString("pwd",pwd);
                        editor.putString("info",a);
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), FragmentTabHost.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setListener();
        setVideo();
    }

    /**
     * 设置背景视频
     */
    private void setVideo() {
        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.sport));

        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });

    }

    /**
     * 设置监听器
     */
    private void setListener() {
        Mylistener mylistener = new Mylistener();
        btnRegist.setOnClickListener(mylistener);
        btnLogin.setOnClickListener(mylistener);
        imgHidePwd.setOnClickListener(mylistener);
        forgetPwd.setOnClickListener(mylistener);
    }
    /**
     * 获取控件布局
     */
    private void initViews() {
        videoview = findViewById(R.id.videoview);
        edtPwd = findViewById(R.id.password);
        edtUser = findViewById(R.id.username);
        btnLogin = findViewById(R.id.login);
        btnRegist = findViewById(R.id.regist);
        imgHidePwd = findViewById(R.id.hidepwd);
        forgetPwd = findViewById(R.id.forgetPwd);
    }
    class Mylistener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.login:
                    phone = edtUser.getText().toString();
                    pwd = edtPwd.getText().toString();
                    if(judgePhoneNums(phone)){
                        getValues();
                    }
                    break;
                case R.id.regist:
                    Intent intent1 = new Intent();
                    intent1.setClass(getApplicationContext(), RegistActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.hidepwd:
                    //设置密码是否可见的方法
                    setPasswordVisible();
                    break;
                case R.id.forgetPwd:
                    Intent intent2 = new Intent();
                    intent2.setClass(getApplicationContext(), ForgetPwdActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    }

    /**
     * 获得用户登录信息,并判断是否正确
     */
    private void getValues() {
        //数据库判断账户是否正确
        Log.i("lr","发送信息");
        OkHttpClient client = new OkHttpClient();
        User user = new User();
        user.setPhone(phone);
        user.setPwd(pwd);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        Log.i("lr","发送的信息+json");
                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(ServerConfig.SERVER_HOME+"FindUserServlet")
                        .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String str = response.body().string();
                    Log.i("lr","返回的信息"+ str +"龙瑞");
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = str;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 设置密码是否可见
     */
    private void setPasswordVisible() {
        isPwdVisible = !isPwdVisible;
        //设置密码是否可见
        if (isPwdVisible) {
            //设置密码为明文，并更改眼睛图标
            edtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgHidePwd.setImageResource(R.mipmap.openeye);
        } else {
            //设置密码为暗文，并更改眼睛图标
            edtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgHidePwd.setImageResource(R.mipmap.closeye);
        }
        //设置光标位置的代码需放在设置明暗文的代码后面
        edtPwd.setSelection(edtPwd.getText().toString().length());
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        super.onRestart();
        setVideo();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        super.onStop();
        videoview.stopPlayback();
    }
    /**
     * 判断电话号码是否正确
     * @param phone
     * @return
     */
    private boolean judgePhoneNums(String phone) {
        if (isMatchLength(phone, 11)
                && isMobileNO(phone)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断字符串长度
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 判断电话号码前三位是否符合标准
     * @param mobileNums
     * @return
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

}
