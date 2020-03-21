package cn.rubintry.ruhttp;

/**
 * @author logcat
 * 请求对象的底层接口
 */
public interface IHttpRequest {


    /**
     * 设置请求连接
     * @param url
     */
    void setUrl(String url);


    /**
     * 设置请求参数
     */
    void setParams(byte[] params);


    /**
     * 设置连接超时时间
     * @param time
     */
    void setConnectTimeout(int time);


    /**
     * 设置请求方式
     */
    void setRequestMethod(String method);


    /**
     * 设置读取超时时间
     * @param time
     */
    void setReadTimeout(int time);


    /**
     * 设置回调接口
     * @param listener
     */
    void setHttpResponseListener(IRuHttpResponseListener listener);


    /**
     * 执行
     */
    void execute();
}
