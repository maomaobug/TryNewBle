package me.zhanghailin.bluetooth;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by zhanghailin on 9/9/15.
 */
public class LooperThread extends Thread {
    private Handler handler;

    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler();
    }

    public Handler getHandler() {
        return handler;
    }
}
