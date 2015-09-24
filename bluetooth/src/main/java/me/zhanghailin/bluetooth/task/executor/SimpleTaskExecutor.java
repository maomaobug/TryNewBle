package me.zhanghailin.bluetooth.task.executor;

import me.zhanghailin.bluetooth.request.BleDataRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.executor
 * author shenwenjun
 * Date 9/14/15.
 */
public class SimpleTaskExecutor implements TaskExecutor {

    @Override
    public boolean executeTask(BleDataRequest request) {
        return request.execute();
    }

}
