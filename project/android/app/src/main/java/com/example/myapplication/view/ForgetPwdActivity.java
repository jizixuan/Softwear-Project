package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPwdActivity extends AppCompatActivity {
    String APPKEY = "317d3a41cc848";
    String APPSECRETE = "98f114c147e2560ca62e58e6619b6993";
    int i = 60;
    private EditText phone1;
    private EditText confirm;
    private Button btnConfirm;
    private Button btnUpdate;
    private ImageView imgBack;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        initViews();
        initMobSdk();
        setMyListener();

    }

    private void setMyListener() {
        MyListener myListener = new MyListener();
        btnUpdate.setOnClickListener(myListener);
        btnConfirm.setOnClickListener(myListener);
        imgBack.setOnClickListener(myListener);
    }

    private void initViews() {
        btnConfirm = findViewById(R.id.btn_confirm1);
        btnUpdate = findViewById(R.id.btn_confirm2);
        phone1 = findViewById(R.id.username2);
        confirm =findViewById(R.id.confirm1);
        imgBack =findViewById(R.id.img_back1);

    }

    /**
     * /启动短信验证sdk
     */
    private void initMobSdk() {
        MobSDK.init(this, APPKEY, APPSECRETE);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                btnConfirm.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                btnConfirm.setText("获取验证码");
                btnConfirm.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("lr", "event=" + event);
                Log.e("lr", "event=" + result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgetPwdActivity.this,NewPwdActivity.class);
                        intent.putExtra("phone",phone);
                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };
    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            phone = phone1.getText().toString();
            switch (view.getId()){
                case R.id.img_back1:
                    finish();
                    break;
                case R.id.btn_confirm1:
                    //1、确认获得的电话号码是否正确
                    if(!judgePhoneNums(phone)){
                        return;
                    } // 2. 通过sdk发送短信验证
                    SMSSDK.getVerificationCode("86", phone);
                    // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                    btnConfirm.setClickable(false);
                    btnConfirm.setText("重新发送(" + i + ")");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (; i > 0; i--) {
                                handler.sendEmptyMessage(-9);
                                if (i <= 0) {
                                    break;
                                }
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            handler.sendEmptyMessage(-8);
                        }
                    }).start();
                    break;
                case R.id.btn_confirm2:
                    //将收到的验证码和手机号提交再次核对
                    SMSSDK.submitVerificationCode("86", phone, confirm
                            .getText().toString());
                    createProgressBar();
                    //获取信息
                    break;
            }
        }
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
    private void createProgressBar() {
        FrameLayout layout = findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }
    /**
     * 销毁SMSSDK
     */
    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
