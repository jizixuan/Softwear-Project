package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class ChangePwdActivity extends AppCompatActivity {
    private boolean isPwdVisible1 = false;
    private boolean isPwdVisible2 = false;
    private boolean isPwdVisible3 = false;
    private String oldPwd,newPwd,newPwd1;
    private EditText edtOldPwd,edtNewPwd,edtNewPwd1;
    private ImageView imghide,imghide1,imghide2,imgBack;

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
    }

    private void initViews() {
        imgBack = findViewById(R.id.img_back);
        edtNewPwd = findViewById(R.id.edt_new_pwd);
        edtOldPwd = findViewById(R.id.edt_old_pwd);
        edtNewPwd1 = findViewById(R.id.edt_new_pwd1);
        imghide = findViewById(R.id.hidepwd);
        imghide1 = findViewById(R.id.hidepwd1);
        imghide2 = findViewById(R.id.hidepwd2);
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

        //判断两次密码是否一致
        if (newPwd1.equals(newPwd)){
            //一致的话提交到数据库
        }else{
            edtNewPwd.setText("");
            edtNewPwd1.setText("");
            Toast.makeText(getApplicationContext(),"两次密码不一致，请重新输入",Toast.LENGTH_LONG).show();
        }
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
