package com.example.myapplication.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.FragmentTabHost;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.entity.User;
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

public class NewPwdActivity extends AppCompatActivity {
    private Button btnUpdate;
    private TextView tvPwd;
    private String phone;
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
                        User user = gson.fromJson(a,User.class);
                        tvPwd.setText(user.getPwd());
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pwd);
        Intent request = getIntent();
        phone = request.getStringExtra("phone");
        btnUpdate = findViewById(R.id.btn_update);
        tvPwd =findViewById(R.id.edt_new_pwd2);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewPwdActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
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

}
