package me.zhanghailin.bluetooth.task;

import me.zhanghailin.bluetooth.request.ITaskRequest;
import me.zhanghailin.bluetooth.request.filter.RequestFilter;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task
 * author shenwenjun
 * Date 9/16/15.
 */
public interface ITaskManager {

    void submitTask(ITaskRequest request);

    void finishTask();

    void retryCurrentTask();

    /**
     * 使用 filter 批量取消 task
     * </br>
     * 一般是根据 request 的 tag 来判断是否是要取消任务的
     * @param filter
     */
    void cancelTask(RequestFilter filter);

    void cancelAllTask();

}
