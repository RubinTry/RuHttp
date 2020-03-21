package cn.rubintry.ruhttp;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @author logcat
 */
public class RuHttpResponseListener<T> implements IRuHttpResponseListener {

    private Class<T> cls;
    private IRuHttpRequestListener listener;
    private Handler handler = new Handler(Looper.getMainLooper());

    public RuHttpResponseListener(Class<T> cls , IRuHttpRequestListener listener) {
        this.cls = cls;
        this.listener = listener;
    }

    @Override
    public void onSuccess(InputStream is) {
        final String jsonStr = byteArrayToString(is , null);
        //如果期望的返回值为string则不进行对象转换
        if(cls != String.class){
            final T t = new Gson().fromJson(jsonStr , cls);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(listener != null){
                        listener.onSuccess(t);
                    }
                }
            });
        }else{
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(listener != null){
                        listener.onSuccess(jsonStr);
                    }
                }
            });
        }
    }

    @Override
    public void onFail(final Throwable e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(listener != null){
                    listener.onFail(e);
                }
            }
        });
    }

    private String byteArrayToString(InputStream in, String encode)
    {

        String str = "";
        try
        {
            if (encode == null || encode.equals(""))
            {
                // 默认以utf-8形式
                encode = "utf-8";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
            StringBuffer sb = new StringBuffer();

            while ((str = reader.readLine()) != null)
            {
                sb.append(str).append("\n");
            }
            return sb.toString();
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return str;
    }
}
