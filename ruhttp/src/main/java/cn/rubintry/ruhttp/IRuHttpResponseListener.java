package cn.rubintry.ruhttp;

import java.io.InputStream;

/**
 * @author logcat
 *请求成功后，服务器返回数据的回调接口
 */
public interface IRuHttpResponseListener {

    /**
     * 返回成功
     */
    void onSuccess(String json);


    /**
     * 返回失败
     */
    void onFail(Throwable e);
}
