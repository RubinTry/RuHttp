package cn.rubintry.ruhttp;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author logcat
 * 请求对象的实现类
 */
public class HttpRequest implements IHttpRequest {

    private String url;
    private byte[] params;
    private IRuHttpResponseListener responseListener;
    private HttpURLConnection httpURLConnection;
    private int connectTimeout = 0;
    private int readTimeout = 0;
    private String method;
    private String TAG = this.getClass().getSimpleName();

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setParams(byte[] params) {
        this.params = params;
    }

    @Override
    public void setConnectTimeout(int time) {
        this.connectTimeout = time;
    }

    @Override
    public void setRequestMethod(String method) {
        this.method = method;
    }

    @Override
    public void setReadTimeout(int time) {
        this.readTimeout = time;
    }

    @Override
    public void setHttpResponseListener(IRuHttpResponseListener listener) {
        this.responseListener = listener;
    }

    @Override
    public void execute() {
        URL url = null;
        try {

            url = new URL(this.url);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(connectTimeout);
            httpURLConnection.setReadTimeout(readTimeout);
            //不使用缓存
            httpURLConnection.setUseCaches(false);
            //自动处理重定向
            httpURLConnection.setInstanceFollowRedirects(true);


            //设置这个连接是否可以输出数据
            httpURLConnection.setDoOutput(true);
            //设置这个连接是否可以写入数据
            httpURLConnection.setDoInput(true);

            httpURLConnection.setRequestProperty("Content-Type", ("application/json;charset=utf-8").replaceAll("\\s", ""));

            //设置请求方式
            httpURLConnection.setRequestMethod(method);
            //建立连接
            httpURLConnection.connect();

            //-----------------------  使用字节流发送数据  -------------------------
            OutputStream out = httpURLConnection.getOutputStream();
            //缓冲字节流  包装字节流
            BufferedOutputStream bos  = new BufferedOutputStream(out);
            //把字节流写入到缓存区中
            if(params != null){
                bos.write(params);
            }


            //刷新缓存区，发送数据
            bos.flush();
            out.close();
            bos.close();
            //如果响应码为200代表请求成功
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = httpURLConnection.getInputStream();
                Log.d(TAG, "Method: " + httpURLConnection.getRequestMethod());
                //回调响应接口
                if(responseListener != null){
                    responseListener.onSuccess(in);
                }
            }else{
                throw new RuntimeException("请求失败,错误码：" + httpURLConnection.getResponseCode() + "  ,错误信息：" + httpURLConnection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("请求失败，信息：" + e.getMessage());
        }finally {
            //关闭httpUrlConnection连接对象
            httpURLConnection.disconnect();
        }
    }
}
