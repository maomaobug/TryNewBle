package me.zhanghailin.bluetooth.task;

import me.zhanghailin.bluetooth.request.BleDataRequest;
import me.zhanghailin.bluetooth.request.filter.RequestFilter;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task
 * author shenwenjun
 * Date 9/16/15.
 */
public interface ITaskManager {

    void submitTask(BleDataRequest request);

    void finishTask();

    void retryCurrentTask();

    /**
     *
     * @param filter
     */
    void cancelTask(RequestFilter filter);

    void cancelAllTask();

}
