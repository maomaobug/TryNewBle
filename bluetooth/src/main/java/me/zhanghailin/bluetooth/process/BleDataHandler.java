package me.zhanghailin.bluetooth.process;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import me.zhanghailin.bluetooth.response.BleDataResponse;

/**
 * 将{@link BleDataResponse}交给一个{@link BleResponseProcessor}处理
 * Created by zhanghailin on 9/10/15.
 */
public class BleDataHandler extends Handler {
    public BleDataHandler(Looper looper, BleResponseProcessor processor) {
        super(looper);
        this.processor = processor;
    }

    private BleResponseProcessor processor;

    @Override
    public void handleMessage(@NonNull Message msg) {
        try {
            BleDataResponse response = (BleDataResponse) msg.obj;
            processor.processResponse(response);

        } catch (ClassCastException castException) {
            super.handleMessage(msg);
        }
    }

}
