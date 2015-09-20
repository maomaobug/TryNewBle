package me.zhanghailin.bluetooth.task.queue;

import me.zhanghailin.bluetooth.request.BleTaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.queue
 * author shenwenjun
 * Date 9/14/15.
 */
public interface TaskQueue {

    boolean addTask(BleTaskRequest request);

    /**
     * 获取队头的 task
     */
    BleTaskRequest peekTask();

    /**
     * 获取一个 task ， 并从队列中移除该 task
     */
    BleTaskRequest pollTask();

    boolean containTask(BleTaskRequest request);

    boolean isEmpty();

}
