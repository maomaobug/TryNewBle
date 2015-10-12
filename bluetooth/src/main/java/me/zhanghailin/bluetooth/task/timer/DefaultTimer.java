package me.zhanghailin.bluetooth.task.timer;

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
public class DefaultTimer implements ITimeoutTimer {

    private static final String TAG = "Timeout";

    private long timeout = 10 * 1000;

    private Handler handler;

    private TimeoutPolicy timeoutPolicy;

    public DefaultTimer() {

        // 优先使用当前线程环境, 如果如果当前线程没有初始化过 Looper ， 则使用主线程处理超时回调函数
        Looper looper = Looper.myLooper();
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        handler = new Handler(looper);
    }

    @Override
    public void setupTimeoutPolicy(TimeoutPolicy timeoutPolicy) {
        this.timeoutPolicy = timeoutPolicy;
    }

    @Override
    public void setupTimeout(long milliseconds) {
        timeout = milliseconds;
    }

    @Override
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
        }, timeout);
    }

    @Override
    public void stopTiming() {
        handler.removeCallbacksAndMessages(null);

        timeoutPolicy.onResetTimeout();
    }

}
