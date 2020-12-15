package com.example.myapplication;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.util.ServerConfig;
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

public class ChangePwdActivity extends AppCompatActivity {
    private boolean isPwdVisible1 = false;
    private boolean isPwdVisible2 = false;
    private boolean isPwdVisible3 = false;
    private String oldPwd,newPwd,newPwd1;
    private EditText edtOldPwd,edtNewPwd,edtNewPwd1;
    private ImageView imghide,imghide1,imghide2,imgBack;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cahnge_info);
        initViews();
        setListener();

    }

    private void setListener() {
        MyListener myListener = new MyListener();
        imghide.setOnClickListener(myListener);
        imghide2.setOnClickListener(myListener);
        imghide1.setOnClickListener(myListener);
        imgBack.setOnClickListener(myListener);
        btnConfirm.setOnClickListener(myListener);

    }

    private void initViews() {
        imgBack = findViewById(R.id.img_back);
        edtNewPwd = findViewById(R.id.edt_new_pwd);
        edtOldPwd = findViewById(R.id.edt_old_pwd);
        edtNewPwd1 = findViewById(R.id.edt_new_pwd1);
        imghide = findViewById(R.id.hidepwd);
        imghide1 = findViewById(R.id.hidepwd1);
        imghide2 = findViewById(R.id.hidepwd2);
        btnConfirm = findViewById(R.id.btn_confirm_new_pwd);
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.hidepwd:
                    setPasswordVisible1();
                    break;
                case R.id.hidepwd1:
                    setPasswordVisible2();
                    break;
                case R.id.hidepwd2:
                    setPasswordVisible3();
                    break;
                case R.id.img_back:
                    finish();
                    break;
                case R.id.btn_confirm_new_pwd:
                    updatePwd();
                    break;
            }
        }
    }

    private void updatePwd() {
        oldPwd = edtOldPwd.getText().toString();
        newPwd = edtNewPwd.getText().toString();
        newPwd1 = edtNewPwd1.getText().toString();
        //判断旧密码是否一致
        if(ServerConfig.USER_INFO.getPwd().equals(oldPwd)){
            //判断两次密码是否一致
            if (newPwd1.equals(newPwd)){
                //一致的话提交到数据库
                ServerConfig.USER_INFO.setPwd(newPwd);
                updateInfo();
            }else{
                edtNewPwd.setText("");
                edtNewPwd1.setText("");
                Toast.makeText(ChangePwdActivity.this,"两次密码不一致，请重新输入",Toast.LENGTH_LONG).show();
            }
        }

    }
    /**
     * 更新用户信息
     */
    private void updateInfo(){
        Gson gson = new Gson();
        String json = gson.toJson(ServerConfig.USER_INFO);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        Log.i("lr","发送的信息密码"+json);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(ServerConfig.SERVER_HOME+"UpdateUserServlet")
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
                    if(str.equals("1")){
                        finish();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 判断是否可见
     */
    private void setPasswordVisible1() {
        isPwdVisible1 = !isPwdVisible1;
        //设置密码是否可见
        if (isPwdVisible1) {
            //设置密码为明文，并更改眼睛图标
            edtOldPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imghide.setImageResource(R.mipmap.openeye);
        } else {
            //设置密码为暗文，并更改眼睛图标
            edtOldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imghide.setImageResource(R.mipmap.closeye);
        }
        //设置光标位置的代码需放在设置明暗文的代码后面
        edtOldPwd.setSelection(edtOldPwd.getText().toString().length());
    }
    private void setPasswordVisible2() {
        isPwdVisible2 = !isPwdVisible2;
        //设置密码是否可见
        if (isPwdVisible2) {
            //设置密码为明文，并更改眼睛图标
            edtNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imghide1.setImageResource(R.mipmap.openeye);
        } else {
            //设置密码为暗文，并更改眼睛图标
            edtNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imghide1.setImageResource(R.mipmap.closeye);
        }
        //设置光标位置的代码需放在设置明暗文的代码后面
        edtNewPwd.setSelection(edtNewPwd.getText().toString().length());
    }
    private void setPasswordVisible3() {
        isPwdVisible3 = !isPwdVisible3;
        //设置密码是否可见
        if (isPwdVisible3) {
            //设置密码为明文，并更改眼睛图标
            edtNewPwd1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imghide2.setImageResource(R.mipmap.openeye);
        } else {
            //设置密码为暗文，并更改眼睛图标
            edtNewPwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imghide2.setImageResource(R.mipmap.closeye);
        }
        //设置光标位置的代码需放在设置明暗文的代码后面
        edtNewPwd1.setSelection(edtNewPwd1.getText().toString().length());
    }
}
