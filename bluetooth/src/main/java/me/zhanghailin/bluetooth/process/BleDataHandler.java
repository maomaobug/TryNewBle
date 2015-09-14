package me.zhanghailin.bluetooth.process;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import me.zhanghailin.bluetooth.response.BleDataResponse;

/**
 * Created by zhanghailin on 9/10/15.
 */
public class BleDataHandler extends Handler {
    public static final int CONNECTION_STATE = 0;
    public static final int SERVICE_DISCOVERED = 1;
    public static final int VALUE_READ = 2;
    public static final int VALUE_WITE = 3;
    public static final int VALUE_NOTIFIED = 4;
    public static final int RSSI = 5;

    public BleDataHandler(Looper looper, BleResponseProcessor processor) {
        super(looper);
        this.processor = processor;
    }

    private BleResponseProcessor processor;

    @Override
    public void handleMessage(Message msg) {
        try {
            BleDataResponse response = (BleDataResponse) msg.obj;
            processor.processConnectionState(response);

        } catch (ClassCastException castException) {
            super.handleMessage(msg);
        }
    }

}
