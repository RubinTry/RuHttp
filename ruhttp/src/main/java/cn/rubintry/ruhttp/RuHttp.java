package cn.rubintry.ruhttp;


/**
 * @author logcat
 *
 */
public class RuHttp {


    /**
     * 默认超时时间 10秒
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private static final int DEFAULT_READ_TIMEOUT = 10000;
    private static final int DEFAULT_RETRY_TIMES = 3;
    private static int retryMaxTimes = 0;

    public static <T , K> void sendRequest(String url , T data , Class <K> cls, String method , IRuHttpRequestListener listener){
        //构建一个请求对象 里氏替换原则
        IHttpRequest iHttpRequest = new HttpRequest();
        //构建一个返回时的回调函数
        IRuHttpResponseListener iRuHttpResponseListener = new RuHttpResponseListener<>(cls , listener);

        //构建一个请求执行线程
        HttpTask httpTask = new HttpTask(iHttpRequest , url , data , method , DEFAULT_CONNECT_TIMEOUT , DEFAULT_READ_TIMEOUT , iRuHttpResponseListener);
        httpTask.setRetryMaxTimes(retryMaxTimes == 0 ? DEFAULT_RETRY_TIMES : retryMaxTimes);
        //构建一个线程池进行管理
        ThreadManager.getInstance().addTask(httpTask);
    }

    public static void setRetryMaxTimes(int retryMaxTimes) {
        RuHttp.retryMaxTimes = retryMaxTimes;
    }
}
