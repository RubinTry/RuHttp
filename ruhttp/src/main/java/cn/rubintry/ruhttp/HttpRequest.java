package cn.rubintry.ruhttp;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author logcat
 * 请求对象的实现类
 */
public class HttpRequest implements IHttpRequest {

    private String url;
    private String params;
    private IRuHttpResponseListener responseListener;
    private HttpURLConnection httpURLConnection;
    private int connectTimeout = 0;
    private int readTimeout = 0;
    private String method;
    private String TAG = this.getClass().getSimpleName();
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private String line;
    private StringBuilder stringBuilder;
    private String response;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setParams(String params) {
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
            //设置请求方式
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-Length", String
                    .valueOf(params.length()));
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

            //建立连接
            httpURLConnection.connect();

            printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            printWriter.print(params);
            printWriter.flush();
            //获取响应结果
            stringBuilder = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            response = stringBuilder.toString().trim();
            //如果响应码为200代表请求成功
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                Log.d(TAG, "RuHttpResponse: " + response);
                //回调响应接口
                if (responseListener != null) {
                    responseListener.onSuccess(response);
                }
            } else {
                if (responseListener != null) {
                    responseListener.onFail(new Exception("请求失败,错误码：" + httpURLConnection.getResponseCode()));
                }
                throw new RuntimeException("请求失败,错误码：" + httpURLConnection.getResponseCode() + "  ,错误信息：" + httpURLConnection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("请求失败，信息：" + e.getMessage());
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                //关闭httpUrlConnection连接对象
                httpURLConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
