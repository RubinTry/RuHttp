package cn.rubintry.ruhttp;

/**
 * @author logcat
 * 请求时用的回调接口
 */
public interface IRuHttpRequestListener<T> {

    /**
     * 请求成功
     */
    void onSuccess(T t);


    /**
     * 请求失败
     */
    void onFail(Throwable e);
}
