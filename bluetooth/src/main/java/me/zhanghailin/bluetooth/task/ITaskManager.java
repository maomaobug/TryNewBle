package me.zhanghailin.bluetooth.task;

import me.zhanghailin.bluetooth.request.BleTaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task
 * author shenwenjun
 * Date 9/16/15.
 */
public interface ITaskManager {

    void submitTask(BleTaskRequest request);

    void finishTask();

    void retryCurrentTask();

}
