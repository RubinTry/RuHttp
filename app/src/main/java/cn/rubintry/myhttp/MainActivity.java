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

import com.google.gson.Gson;

import java.util.Map;
import java.util.TreeMap;

import cn.rubintry.ruhttp.IRuHttpRequestListener;
import cn.rubintry.ruhttp.MethodType;
import cn.rubintry.ruhttp.RuHttp;

/**
 * @author logcat
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Http请求";
    private static final int REQUEST_CODE = 1111;

    private String url = "http://101.37.71.102/vc/inf/route/list";
//    private String url = "https://github.com/";
    private String[] permissionArray = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_main);

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
                .setMethod(MethodType.GET)
                .setUrl(url)
                .setType(ResultModel.class)
                .setHttpRequestListener(new IRuHttpRequestListener<ResultModel>() {

                    @Override
                    public void onSuccess(ResultModel resultModel) {
                        Log.d(TAG, "结果: " + new Gson().toJson(resultModel));
                        Log.d(TAG, "成功，耗时: " + (System.currentTimeMillis() - startTime) + "毫秒");
                        startTime = System.currentTimeMillis();
                    }

                    @Override
                    public void onFail(Throwable e) {

                        Log.e(TAG, "请求失败，耗时: " + (System.currentTimeMillis() - startTime) + "毫秒");
                    }
                }).build();
        for (int i = 0; i < 1000; i++) {
            ruHttp.execute();
        }
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
}
