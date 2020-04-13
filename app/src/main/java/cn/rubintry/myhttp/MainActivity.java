package cn.rubintry.myhttp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Map;
import java.util.TreeMap;

import cn.rubintry.ruhttp.IRuHttpRequestListener;
import cn.rubintry.ruhttp.MethodType;
import cn.rubintry.ruhttp.RuHttp;

/**
 * @author logcat
 */
public class MainActivity extends AppCompatActivity implements ApiPresenter.ViewInterface {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1111;

//    private String url = "https://www.baidu.com";
    private String url = "http://101.37.71.102/vc/biz/ride/create";

    private TextView tvTest;
    private String[] permissionArray = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private long startTime;
    private ApiPresenter apiPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_main);
        tvTest = findViewById(R.id.tvTest);
    }

    private void checkPermission() {
        for (String permission : permissionArray) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissionArray, REQUEST_CODE);
                break;
            }
        }
    }

    public void test(View view) {
        startTime = System.currentTimeMillis();

        RuHttp ruHttp = new RuHttp.Builder<>()
                .setUrl("https://www.baidu.com")
                .setType(String.class)
                .setMethod(MethodType.GET)
                .setHttpRequestListener(new IRuHttpRequestListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d(TAG, "onSuccess: " + s);
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                }).build();
        ruHttp.execute();

        apiPresenter = new ApiPresenter(this);
        apiPresenter.subscribe(RequestCodeConfig.SUBSCRIBE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                // 如果请求被取消，则结果数组为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限请求成功
                    Log.d(TAG, "权限请求成功: ");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSucceed(Object data, int requestCode) {
        switch (requestCode){
            case RequestCodeConfig.SUBSCRIBE:
                Toast.makeText(this, "订阅成功", Toast.LENGTH_SHORT).show();
                apiPresenter.getMessage(RequestCodeConfig.GET_MESSAGE);
                break;
            case RequestCodeConfig.GET_MESSAGE:
                Toast.makeText(this, "获取消息成功", Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }
    }

    @Override
    public void onFail(Throwable t, int requestCode) {
        switch (requestCode){
            case RequestCodeConfig.SUBSCRIBE:
                Toast.makeText(this, "订阅失败", Toast.LENGTH_SHORT).show();

                break;
            case RequestCodeConfig.GET_MESSAGE:
                Toast.makeText(this, "获取消息失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
