package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.entity.BillItemMessage;
import com.example.myapplication.util.ServerConfig;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BillDetailsActivity extends AppCompatActivity {
    private TextView type;
    private TextView numType;
    private TextView num;
    private TextView date;
    private TextView note;
    private ImageView re;
    private ImageView img;
    private Button edit;
    private Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        Explode explode = new Explode();
        explode.setDuration(450);
        getWindow().setEnterTransition(explode);
        getView();
        initData();
        setListener();
    }

    private void setListener() {
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        //修改跳转界面
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent request=getIntent();
                Intent intent=new Intent();
                intent.setClass(BillDetailsActivity.this,AddBillActivity.class);
                intent.putExtra("type",request.getStringExtra("type"));
                intent.putExtra("num",request.getDoubleExtra("num",0));
                intent.putExtra("note",request.getStringExtra("note"));
                String date0=request.getStringExtra("date").split(" ")[0];
                String date1=date0.split("-")[0]+"/"+date0.split("-")[1]+"/"+date0.split("-")[2];
                intent.putExtra("date",date1);
                intent.putExtra("id",request.getIntExtra("id",0));
                intent.putExtra("numType",request.getStringExtra("numType"));
                startActivity(intent);
            }
        });
        //删除跳转界面
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent requestIntent=getIntent();
                OkHttpClient okHttpClient=new OkHttpClient();
                //删除该账单
                FormBody formBody =
                        new FormBody.Builder()
                                .add("id",requestIntent.getIntExtra("id",0)+"")
                                .add("userId", ServerConfig.USER_ID+"")
                                .build();
                //创建请求对象
                Request request = new Request.Builder()
                        .url(ServerConfig.SERVER_HOME + "DeleteBillItemServlet")
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
                        if(result.equals("成功")){
                            BillItemMessage message=new BillItemMessage();
                            message.setId(requestIntent.getIntExtra("id",0));
                            message.setFlag(1);
                            EventBus.getDefault().post(message);
                            Intent intent=new Intent();
                            intent.putExtra("operation","delete");
                            String date0=requestIntent.getStringExtra("date").split(" ")[0];
                            intent.putExtra("year",date0.split("-")[0]);
                            intent.putExtra("month",date0.split("-")[1]);
                            intent.setClass(BillDetailsActivity.this,FragmentTabHost.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_bottom,
                                    R.anim.slide_out_top);
                        }
                    }
                });
            }
        });
    }

    private void initData() {
        Intent request=getIntent();
        type.setText(request.getStringExtra("type"));
        if(request.getStringExtra("numType").equals("+")){
            numType.setText("收入");
        }else{
            numType.setText("支出");
        }
        num.setText(request.getDoubleExtra("num",0)+"");
        date.setText(request.getStringExtra("date"));
        note.setText(request.getStringExtra("note"));
        byte[] data=request.getByteArrayExtra("bitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        img.setImageBitmap(bitmap);
    }

    private void getView() {
        type=findViewById(R.id.bill_detail_type);
        numType=findViewById(R.id.bill_detail_numType);
        num=findViewById(R.id.bill_detail_num);
        date=findViewById(R.id.bill_detail_date);
        note=findViewById(R.id.bill_detail_note);
        re=findViewById(R.id.bill_detail_re);
        img=findViewById(R.id.bill_detail_img);
        edit=findViewById(R.id.bill_detail_edit);
        delete=findViewById(R.id.bill_detail_delete);
    }

}
