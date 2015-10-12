package me.zhanghailin.bluetooth.task.queue;

import java.util.Iterator;

import me.zhanghailin.bluetooth.request.ITaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.queue
 * author shenwenjun
 * Date 9/14/15.
 */
public interface TaskQueue {

    boolean addTask(ITaskRequest request);

    /**
     * 获取队头的 task
     */
    ITaskRequest peekTask();

    /**
     * 获取一个 task ， 并从队列中移除该 task
     */
    ITaskRequest pollTask();

    boolean containTask(ITaskRequest request);

    boolean removeTask(ITaskRequest request);

    boolean clearTask();

    boolean isEmpty();

    Iterator<ITaskRequest> iterator();

}
