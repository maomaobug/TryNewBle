package me.zhanghailin.bluetooth.task.executor;

import me.zhanghailin.bluetooth.request.BleTaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.executor
 * author shenwenjun
 * Date 9/14/15.
 */
public interface TaskExecutor {

    boolean executeTask(BleTaskRequest request);

}
