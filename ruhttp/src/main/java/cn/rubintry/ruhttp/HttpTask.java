package cn.rubintry.ruhttp;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author logcat
 * 执行线程
 */
public class HttpTask<T> implements Runnable, Delayed {

    private IHttpRequest request;

    /**
     * 设置失败次数上限
     */
    private int retryTimes;

    private int retryMaxTimes;

    /**
     * 等待时间
     */
    private long delayTime;


    /**
     * 返回时的回调函数
     */
    private IRuHttpResponseListener listener;


    public HttpTask(IHttpRequest request, String url, T params, String method, int connectTimeout, int readTimeout, IRuHttpResponseListener listener) {
        this.request = request;
        this.request.setUrl(url);
        this.request.setRequestMethod(method);
        this.request.setConnectTimeout(connectTimeout);
        this.request.setReadTimeout(readTimeout);
        this.request.setHttpResponseListener(listener);
        this.listener = listener;
        if (params != null) {
            String jsonStr = new Gson().toJson(params);
            this.request.setParams(jsonStr.getBytes());
        }
    }

    public int getRetryMaxTimes() {
        return retryMaxTimes;
    }

    public void setRetryMaxTimes(int retryMaxTimes) {
        this.retryMaxTimes = retryMaxTimes;
    }

    public IRuHttpResponseListener getListener() {
        return listener;
    }


    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public long getDelayTime() {
        return delayTime;
    }


    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime + System.currentTimeMillis();
    }

    @Override
    public void run() {
        try {
            request.execute();
        } catch (Exception e) {
            ThreadManager.getInstance().addFailTask(this);
        }
    }




    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(getDelayTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
