package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.example.myapplication.view.ForgetPwdActivity;


public class LoginActivity extends AppCompatActivity {
    private EditText edtUser;//用户名
    private EditText edtPwd;//密码
    private Button btnLogin;//登录按钮
    private Button btnRegist;//注册按钮
    private ImageView imgHidePwd;//隐藏密码
    private boolean isPwdVisible = false;//密码是否可见
    private String user,pwd;
    private TextView forgetPwd;
    private CustomVideoView videoview;

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
        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sport));

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
                    getValues();
                    SharedPreferences sd = getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sd.edit();
                    editor.putString("name",user);
                    editor.putString("pwd",pwd);
                    editor.commit();
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),FragmentTabHost.class);
                    startActivity(intent);
                    break;
                case R.id.regist:
                    Intent intent1 = new Intent();
                    intent1.setClass(getApplicationContext(),RegistActivity.class);
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
        user = edtUser.getText().toString();
        pwd = edtPwd.getText().toString();
        //数据库判断账户是否正确
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


}
