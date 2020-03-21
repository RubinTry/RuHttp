package cn.rubintry.ruhttp;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author logcat
 * 线程池管理器
 */
public class ThreadManager {
    private  final String TAG = this.getClass().getSimpleName();
    /**
     * 建立一个线程池，用来管理队列
     */
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 请求队列
     */
    private LinkedBlockingDeque<Runnable> requestQueue = new LinkedBlockingDeque();


    /**
     * 请求失败时的队列
     */
    private DelayQueue<Delayed> failQueue = new DelayQueue();

    private static volatile ThreadManager INSTANCE;

    public static ThreadManager getInstance(){
        if(INSTANCE == null){
            synchronized (ThreadManager.class){
                if(INSTANCE == null){
                    INSTANCE = new ThreadManager();
                }
            }
        }
        return INSTANCE;
    }

    private ThreadManager() {
        threadPoolExecutor = new ThreadPoolExecutor(5, 10, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        addTask(r);
                    }
                });

        threadPoolExecutor.execute(coreRunnable);
        threadPoolExecutor.execute(failCoreRunnable);
    }









    /**
     * 添加请求线程
     */
    public void addTask(Runnable runnable){
        if(runnable == null){
            return;
        }

        try {
            requestQueue.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加请求失败的线程
     */
    public void addFailTask(HttpTask httpTask){
        if(httpTask == null){
            return;
        }
        httpTask.setDelayTime(3000);
        //放进阻塞队列中，用不阻塞的方法添加
        failQueue.offer(httpTask);
    }



    /**
     * 请求核心线程
     */
    public Runnable coreRunnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                try {
                    Runnable coreRunnable = requestQueue.take();
                    threadPoolExecutor.execute(coreRunnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * 失败核心线程
     */
    public Runnable failCoreRunnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                try {

                    HttpTask httpTask = (HttpTask) failQueue.take();

                    if(httpTask.getRetryTimes() < httpTask.getRetryMaxTimes()){
                        httpTask.setRetryTimes(httpTask.getRetryTimes() + 1);
                        threadPoolExecutor.execute(httpTask);
                        Log.e(TAG, "请求失败，正在重试===============请求次数: " + httpTask.getRetryTimes());
                    }else{
                        IRuHttpResponseListener listener = httpTask.getListener();
                        if(listener != null){
                            httpTask.getListener().onFail(new Exception("请求失败"));
                        }
                        Log.e(TAG, "请求失败");
                        httpTask.setRetryTimes(0);
                        break;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
