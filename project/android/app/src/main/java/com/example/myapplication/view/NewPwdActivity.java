package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;

public class NewPwdActivity extends AppCompatActivity {
    private EditText edtPwd,edtPwd1;
    private Button btnUpdate;
    private String pwd,pwd1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pwd);
        edtPwd =findViewById(R.id.edt_new_pwd2);
        edtPwd1 =findViewById(R.id.edt_new_pwd3);
        btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(judgePwd()){
                    //传入数据库
                    Intent intent = new Intent(NewPwdActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean judgePwd() {
        pwd1 = edtPwd1.getText().toString();
        pwd = edtPwd.getText().toString();
        if (pwd1.equals(pwd)){
            return true;
        }else {
            edtPwd1.setText("");
            edtPwd.setText("");
            return false;
        }
    }
}
