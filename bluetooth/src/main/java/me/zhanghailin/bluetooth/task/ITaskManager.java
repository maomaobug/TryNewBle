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

    /**
     * 需要保证即使重复调用该函数， 也不能影响程序逻辑正常执行
     * 例如 断开连接时， 请求队列中的数据没有增加， 但是能够触发 finishTask()
     */
    void finishTask();

    /**
     * 使用 filter 批量取消 task
     * </br>
     * 一般是根据 request 的 tag 来判断是否是要取消任务的
     * @param filter
     */
    void cancelTask(RequestFilter filter);

    void cancelAllTask();

    ITaskRequest currentTask();

}
