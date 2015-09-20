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
        handler = new Handler(Looper.getMainLooper());
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
