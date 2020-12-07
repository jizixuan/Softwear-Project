package com.example.myapplication.note.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.example.myapplication.R;

import java.io.File;
import java.net.URI;
import java.util.List;
public class SimpleTextActivity extends AppCompatActivity {
    private boolean hasGotToken = false;

    private File tempFile;

    private static final int REQUEST_CODE_CAMERA = 102;
    private int REQUEST_CODE_ALBUM = 2;
    private int REQUEST_ENHANCED_CODE_ALBUM = 3;

    private TextView infoTextView;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_text);
        initAccessTokenWithAkSk();
        alertDialog = new AlertDialog.Builder(this);
        infoTextView = findViewById(R.id.info_text_view);
        findViewById(R.id.img_back1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        //infoTextView.setText("本地质量控制初始化错误，错误原因： " + msg);
                    }
                });
        findViewById(R.id.take_a_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkGalleryPermission()) {
                    if (!checkTokenStatus()){
                        return;
                    }
                    tempFile = new File(Environment.getExternalStorageDirectory(), "fileImg.jpg");
                    Intent intent = new Intent(SimpleTextActivity.this, CameraActivity.class);
                    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, tempFile.getAbsolutePath());
                    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }
            }
        });
        findViewById(R.id.select_a_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkTokenStatus()){
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_CODE_ALBUM);
            }
        });
        findViewById(R.id.enhanced_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkTokenStatus()){
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_ENHANCED_CODE_ALBUM);
            }
        });

    }

    /**
     * 文字普通识别
     * @param filePath
     */
    private void recSimpleText(String filePath) {
        // 通用文字识别参数设置
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        // 调用通用文字识别服务
        OCR.getInstance(this).recognizeGeneralBasic(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult generalResult) {
                if (generalResult != null) {
                    //alertText("", parseResultBean(generalResult));
                    setInfoTextView(parseResultBean(generalResult));
                }
            }

            @Override public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                alertText("调用失败", error.getMessage());
            } });
    }

    /**
     * 文字高精度识别
     * @param filePath
     */
    private void recEnhancedText(String filePath) {
        GeneralBasicParams params = new GeneralBasicParams();
        params.setDetectDirection(true);
        params.setImageFile(new File(filePath));

        OCR.getInstance(this).recognizeAccurateBasic(params, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult generalResult) {
                if (generalResult != null) {
                    //alertText("", parseResultBean(generalResult));
                    setInfoTextView(parseResultBean(generalResult));
                }
            }

            @Override
            public void onError(OCRError ocrError) {
                // 调用失败，返回OCRError对象
                alertText("调用失败", ocrError.getMessage());
            }
        });
    }

    /**
     * 解析返回结果
     * @param generalResult
     * @return
     */
    private String parseResultBean(GeneralResult generalResult) {
        StringBuilder stringBuilder = new StringBuilder();
        List<?> list = generalResult.getWordList();
        for (Object o : list) {
            if (o instanceof WordSimple) {
                stringBuilder.append(((WordSimple) o).getWords());
                stringBuilder.append("\n\n");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 检查是否有权限
     * @return
     */
    private boolean checkGalleryPermission() {
        int ret = ActivityCompat.checkSelfPermission(SimpleTextActivity.this, Manifest.permission
                .READ_EXTERNAL_STORAGE);
        if (ret != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SimpleTextActivity.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1000);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = getRealPathFromURI(Uri.fromFile(tempFile));
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_GENERAL.equals(contentType)) {
                        recEnhancedText(filePath);
                    }
                }
            }
        }else if (requestCode == REQUEST_CODE_ALBUM) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                recSimpleText(getRealPathFromURI(uri));
            }
        }
        else if (requestCode == REQUEST_ENHANCED_CODE_ALBUM) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                recEnhancedText(getRealPathFromURI(uri));
            }
        }
    }

    /**
     * 错误弹窗
     * @param title
     * @param message
     */
    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    /**
     * 设置显示结果
     * @param text
     */
    private void setInfoTextView(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoTextView.setText(text);
            }
        });
    }

    /**
     * 将URI转化为string格式
     * @param contentURI
     * @return
     */
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        // 释放本地质量控制模型
        CameraNativeHelper.release();
        super.onDestroy();
    }
    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token未获取到", Toast.LENGTH_SHORT).show();
        }
        return hasGotToken;
    }
    /**
     * 使用明文方式初始化token
     */
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Looper.prepare();
                Toast.makeText(SimpleTextActivity.this, "token获取失败.........", Toast.LENGTH_SHORT).show();
            }
        }, getApplicationContext(), "grpYE0miIwwNVDGhD6D2BpM8", "7qnNI1F7LNLLR2GfQkuwIwrSwqMt1401");
    }
}
