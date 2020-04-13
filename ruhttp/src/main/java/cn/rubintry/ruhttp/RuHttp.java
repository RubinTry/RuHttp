package cn.rubintry.ruhttp;


import android.os.Build;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author logcat
 */
public class RuHttp<T, K> {


    /**
     * 默认超时时间 10秒
     */
    private int DEFAULT_CONNECT_TIMEOUT = 10000;
    private int DEFAULT_READ_TIMEOUT = 10000;
    private int DEFAULT_RETRY_TIMES = 3;
    private static int retryMaxTimes = 0;
    private Class<K> cls;
    private Map<String, T> params;
    private IRuHttpRequestListener listener;
    private String method;
    private String url;
    private int connectTimeOut;

    public RuHttp(Builder builder) {
        this.cls = builder.cls;
        this.params = builder.params;
        this.listener = builder.listener;
        this.method = builder.method;
        this.url = builder.url;
        this.connectTimeOut = builder.connectTimeOut;
    }

    public void execute() {
        //构建一个请求对象 里氏替换原则
        IHttpRequest iHttpRequest = new HttpRequest();
        //构建一个返回时的回调函数
        IRuHttpResponseListener iRuHttpResponseListener = new RuHttpResponseListener<>(cls, listener);

        //构建一个请求执行线程
        HttpTask httpTask = new HttpTask(iHttpRequest, url, params, method, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, iRuHttpResponseListener);

        httpTask.setRetryMaxTimes(retryMaxTimes == 0 ? DEFAULT_RETRY_TIMES : retryMaxTimes);

        httpTask.setConnectTimeout(connectTimeOut);
        //将线程添加进线程管理器
        ThreadManager.getInstance().addTask(httpTask);
    }


    /**
     * 设置最大重试次数
     *
     * @param retryMaxTimes
     */
    public static void setRetryMaxTimes(int retryMaxTimes) {
        RuHttp.retryMaxTimes = retryMaxTimes;
    }


    /**
     * http构建器
     *
     * @param <T>
     * @param <K>
     */
    public static class Builder<T, K> {
        private String url;
        private Map<String, T> params;
        private Class<K> cls;
        private String method;
        private int connectTimeOut;
        private IRuHttpRequestListener listener;

        public Builder() {
            params = new LinkedHashMap<>();
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder<T, K> setConnectTimeOut(int connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        public Builder addParam(String key, T value) {
            params.put(key, value);
            return this;
        }

        public Builder setType(Class<K> cls) {
            this.cls = cls;
            return this;
        }

        public Builder<T, K> setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder<T, K> setHttpRequestListener(IRuHttpRequestListener listener) {
            this.listener = listener;
            return this;
        }

        public RuHttp build() {
            return new RuHttp(this);
        }
    }
}
