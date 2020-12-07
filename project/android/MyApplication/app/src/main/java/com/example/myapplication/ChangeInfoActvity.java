package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.example.myapplication.util.TakeBudgetPopWin;
import com.example.myapplication.util.TakePhotoPopWin;
import com.example.myapplication.util.TakeSexPopWin;

import java.io.File;
import java.io.IOException;

public class ChangeInfoActvity extends AppCompatActivity {

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA };
    private File tempFile = null;   //新建一个 File 文件（用于相机拿数据）
    private ImageView imgBack;//返回我的主页
    private ImageView imgAvatar;//头像
    private TextView tvName;//昵称
    private TextView tvSex;//性别

    private Button btnChangePwd;//修改密码
    private LinearLayout linearAvatar;//修改头像
    private LinearLayout linearName;//修改姓名
    private LinearLayout linearSex;//修改性别
    private LinearLayout linearPhone;

    private TakeSexPopWin takeSexPopWin;//性别弹出框
    private TakePhotoPopWin takePhotoPopWin;//头像弹出框
    private Button btnCancel,btnConfirm;
    private EditText edtName;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //跳转相机动态权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        setContentView(R.layout.activity_change_info_actvity);
        initViews();
        setListener();
    }
    /**
     * 检查是否有对应权限
     *
     * @param activity 上下文
     * @param permission 要检查的权限
     * @return  结果标识
     */
    public int verifyPermissions(Activity activity, java.lang.String permission) {
        int Permission = ActivityCompat.checkSelfPermission(activity,permission);
        if (Permission == PackageManager.PERMISSION_GRANTED) {
            Log.e("lr","已经同意权限");
            return 1;
        }else{
            Log.e("lr","没有同意权限");
            return 0;
        }
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        MyListener myListener = new MyListener();
        imgBack.setOnClickListener(myListener);
        btnChangePwd.setOnClickListener(myListener);
        linearAvatar.setOnClickListener(myListener);
        linearName.setOnClickListener(myListener);
        linearSex.setOnClickListener(myListener);
        linearPhone.setOnClickListener(myListener);
    }

    private void initViews() {
        imgBack = findViewById(R.id.img_back);
        imgAvatar = findViewById(R.id.img_avatar);
        tvName = findViewById(R.id.tv_name);
        tvSex = findViewById(R.id.tv_sex);
        btnChangePwd = findViewById(R.id.btn_changePwd);
        linearAvatar = findViewById(R.id.avatar);
        linearName = findViewById(R.id.name);
        linearSex = findViewById(R.id.sex);
        linearPhone = findViewById(R.id.changePhone);
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_back:
                    //返回我的界面
                    finish();
                    break;
                case R.id.btn_changePwd:
                    //跳转到修改密码界面
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(),ChangePwdActivity.class);
                    startActivity(i);
                    break;
                case R.id.avatar:
                    showPopFormBottomPhoto(view);
                    //修改头像
                    break;
                case R.id.name:
                    //修改姓名
                    showAlertDialog();
                    break;
                case R.id.sex:
                    //修改性别
                    showPopFormBottomSex(view);
                    break;
                case R.id.changePhone:
                    //修改电话号码
                    Intent intent = new Intent(ChangeInfoActvity.this,ChangePhoneActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * 设置姓名弹窗
     */
    private void showAlertDialog() {
        //创建Builder对象
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框属性
        final View view = getLayoutInflater().inflate(R.layout.dialog_info_name,null);
        builder.setView(view);
        edtName = view.findViewById(R.id.edt_dialogname);

        btnConfirm = view.findViewById(R.id.btn_nameConfirm);
        btnCancel = view.findViewById(R.id.btn_nameCancel);
        btnConfirm.setEnabled(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();//显示弹窗
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //boolean tag1 = false;
                boolean tag = edtName.getText().length()>0;
                name = edtName.getText().toString();
//                if (str.equals("") && str.equals("0")){
//                    tag1 = true;
//                }
                if(tag){
                    btnConfirm.setEnabled(true);
                    btnConfirm.setTextColor(getResources().getColor(R.color.bule));
                }else{
                    btnConfirm.setEnabled(false);
                    btnConfirm.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvName.setText(name);
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 设置性别
     * @param view
     */
    public void showPopFormBottomSex(View view) {
        takeSexPopWin = new TakeSexPopWin(this, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        takeSexPopWin.showAtLocation(findViewById(R.id.Info_view), Gravity.CENTER, 0, 0);
    }

    /**
     * 设置照片
     * @param view
     */
    public void showPopFormBottomPhoto(View view) {
        takePhotoPopWin = new TakePhotoPopWin(this, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        takePhotoPopWin.showAtLocation(findViewById(R.id.Info_view), Gravity.CENTER, 0, 0);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_sex_man:
                    tvSex.setText("男");
                    takeSexPopWin.dismiss();
                    break;
                case R.id.btn_sex_women:
                    tvSex.setText("女");
                    takeSexPopWin.dismiss();
                    break;
                case R.id.btn_photo_take:
                    //检查是否已经获得相机的权限
                    if(verifyPermissions(ChangeInfoActvity.this,PERMISSIONS_STORAGE[2]) == 0){
                        Log.e("lr","提示是否要授权");
                        ActivityCompat.requestPermissions(ChangeInfoActvity.this, PERMISSIONS_STORAGE, 3);
                    }else{
                        //已经有权限
                        toCamera();  //打开相机
                    }
                    takePhotoPopWin.dismiss();
                    toCamera();
                    break;
                case R.id.btn_photo_pick:
                    toPicture();
                    takePhotoPopWin.dismiss();
                    break;
            }
        }
    };
    //获取 相机 或者 图库 返回的图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //判断返回码不等于0
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != RESULT_CANCELED) {    //RESULT_CANCELED = 0(也可以直接写“if (requestCode != 0 )”)
            //读取返回码
            switch (requestCode) {
                case 100:   //相册返回的数据（相册的返回码）
                    Log.e("lr", "相册");
                    Uri uri01 = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri01));
                        bitmap = ImageUtils.toRound(bitmap);
                        imgAvatar.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case 101:  //相机返回的数据（相机的返回码）
                    Log.e("lr", "相机");
                    try {
                        tempFile = new File(Environment.getExternalStorageDirectory(), "fileImg.jpg");  //相机取图片数据文件
                        Uri uri02 = Uri.fromFile(tempFile);   //图片文件
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri02));
                        bitmap = ImageUtils.toRound(bitmap);
                        imgAvatar.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    /**
     * 调用相册
     */
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
        intent.setType("image/*");
        startActivityForResult(intent,100);
    }

    /**
     *   调用相机
     */
    private void toCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //跳转到 ACTION_IMAGE_CAPTURE
        //判断内存卡是否可用，可用的话就进行存储
        //putExtra：取值，Uri.fromFile：传一个拍照所得到的文件，fileImg.jpg：文件名
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fileImg.jpg")));
        startActivityForResult(intent,101); // 101: 相机的返回码参数（随便一个值就行，只要不冲突就好）
        Log.e("lr","跳转相机成功");
    }
}
