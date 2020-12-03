package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.entity.BillType;
import com.example.myapplication.util.ServerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private int recLen = 5;//跳过倒计时提示5秒
    private TextView tv;
    Timer timer = new Timer();
    private OkHttpClient okHttpClient;
    private Runnable runnable;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    String str= (String) msg.obj;
                    JSONArray jsonArray=null;
                    try {
                        jsonArray=new JSONArray(str);
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            BillType billType=new BillType();
//                            billType.setImg(bitmap);
                            billType.setName(jsonObject.getString("name"));
                            billType.setNumType(jsonObject.getString("numType"));
                            billType.setId(jsonObject.getInt("id"));
                            billType.setImgName(jsonObject.getString("img"));
                            ServerConfig.BILL_TYPES.add(billType);
                            downloadImg(jsonObject.getString("img"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_welcome);
        initView();
        InitBillTypes();
        timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒
        /**
         * 正常情况下不点击跳过
         */
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                String name = getSharedPreferences("Login",MODE_PRIVATE).getString("name","");
                String pwd = getSharedPreferences("Login",MODE_PRIVATE).getString("pwd","");
                Log.d("lr","龙瑞"+name+pwd+"龙瑞");
                if(name!=null && !name.equals("") && pwd!=null && !pwd.equals("")){
                    //从闪屏界面跳转到首界面
                    Intent intent = new Intent(WelcomeActivity.this, FragmentTabHost.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                //从闪屏界面跳转到首界面

                finish();
            }
        }, 5000);//延迟5S后发送handler信息

    }

    private void initView() {
        tv = findViewById(R.id.tv);//跳过
        tv.setOnClickListener(this);//跳过监听
        okHttpClient=new OkHttpClient();
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen--;
                    tv.setText("跳过 " + recLen);
                    if (recLen < 0) {
                        timer.cancel();
                        tv.setVisibility(View.GONE);//倒计时到0隐藏字体
                    }
                }
            });
        }
    };

    /**
     * 点击跳过
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv:
                String name = getSharedPreferences("Login",MODE_PRIVATE).getString("name","");
                String pwd = getSharedPreferences("Login",MODE_PRIVATE).getString("pwd","");
                Log.d("lr","龙瑞"+name+pwd+"龙瑞");
                if(name!=null && !name.equals("") && pwd!=null && !pwd.equals("")){
                    //从闪屏界面跳转到首界面
                    Intent intent = new Intent(WelcomeActivity.this, FragmentTabHost.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                break;
            default:
                break;
        }
    }
    /**
     * 加载所有账单类型
     */
    private void InitBillTypes(){
        FormBody formBody =
                new FormBody.Builder()
                        .build();
        //创建请求对象
        Request request = new Request.Builder()
                .url(ServerConfig.SERVER_HOME + "GetBillTypeList")
//                .method("POST", formBody)
                .post(formBody)
                .build();
        //3. 创建CALL对象
        okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        //3. 异步方式提交请求并获取响应
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lww", "请求失败");
                e.printStackTrace();
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
    /**
     * 下载图片到本地
     */
    private void downloadImg( final String name) {
        new Thread() {
            @Override
            public void run() {
                String imgPath = ServerConfig.SERVER_HOME + "typeImgs\\" + name;
                URL imgUrl = null;
                String files = getApplicationContext().getFilesDir().getAbsolutePath();
                String imgs = files + "/"+ "typeImgs";
                String imgPath1 = imgs + "/" + name;
                File img0 = new File(imgPath1);
                try {
                    if (!img0.exists()) {
                        imgUrl = new URL(imgPath);
                        //解析成bitmap对象
                        InputStream in = imgUrl.openStream();
                        File dirImgs = new File(imgs);
                        if (!dirImgs.exists()) {
                            dirImgs.mkdir();
                        }
                        OutputStream os = new FileOutputStream(imgPath1);
                        int n = -1;
                        while ((n = in.read()) != -1) {
                            os.write(n);
                            os.flush();
                        }
                        os.close();
                        in.close();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
