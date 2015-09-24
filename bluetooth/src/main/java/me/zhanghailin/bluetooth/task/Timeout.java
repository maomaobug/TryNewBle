package me.zhanghailin.bluetooth.task;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import me.zhanghailin.bluetooth.task.policy.TimeoutPolicy;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task
 * author shenwenjun
 * Date 9/18/15.
 */
public class Timeout {

    private static final String TAG = "Timeout";

    private final static int TIMEOUT = 4 * 1000;

    private Handler handler;

    private TimeoutPolicy timeoutPolicy;

    public Timeout() {

        // 优先使用当前线程环境, 如果如果当前线程没有初始化过 Looper ， 则使用主线程处理超时回调函数
        Looper looper = Looper.myLooper();
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        handler = new Handler(looper);
    }

    public void setTimeoutPolicy(TimeoutPolicy timeoutPolicy) {
        this.timeoutPolicy = timeoutPolicy;
    }

    public void startTiming() {
        handler.removeCallbacksAndMessages(null);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timeoutPolicy == null) {
                    Log.w(TAG, "timeoutPolicy is null");
                    return;
                }
                timeoutPolicy.onTaskTimeout();
            }
        }, TIMEOUT);
    }

    public void stopTiming() {
        handler.removeCallbacksAndMessages(null);

        timeoutPolicy.onResetTimeout();
    }

}
