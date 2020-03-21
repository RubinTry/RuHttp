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

    private String url = "https://www.wanandroid.com/user/register";
    private String[] permissionArray = new String[]{
            Manifest.permission.INTERNET
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
            if(ContextCompat.checkSelfPermission(this , permission) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this , permissionArray , REQUEST_CODE);
                break;
            }
        }
    }

    public void test(View view) {
        startTime = System.currentTimeMillis();
//        for (int i = 0; i < 1000; i++) {
//            final int finalI = i;
            Map<String , Object> params = new TreeMap<>();
            params.put("username" , "Rubintry");
            params.put("password" , "abc913430");
            params.put("repassword" , "abc913430");
            RuHttp.sendRequest(url, params, ResultModel.class , MethodType.POST, new IRuHttpRequestListener<ResultModel>() {
                @Override
                public void onSuccess(ResultModel resultModel) {
                    startTime = System.currentTimeMillis();
                    Log.d(TAG, "onSuccess: " + new Gson().toJson(resultModel));
                }

                @Override
                public void onFail(Throwable e) {
                }

            });
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
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
