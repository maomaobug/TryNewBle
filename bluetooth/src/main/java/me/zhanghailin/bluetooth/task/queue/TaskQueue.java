package me.zhanghailin.bluetooth.task.queue;

import java.util.Iterator;

import me.zhanghailin.bluetooth.request.BleDataRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.queue
 * author shenwenjun
 * Date 9/14/15.
 */
public interface TaskQueue {

    boolean addTask(BleDataRequest request);

    /**
     * 获取队头的 task
     */
    BleDataRequest peekTask();

    /**
     * 获取一个 task ， 并从队列中移除该 task
     */
    BleDataRequest pollTask();

    boolean containTask(BleDataRequest request);

    boolean removeTask(BleDataRequest request);

    boolean removeAllTask();

    boolean isEmpty();

    Iterator<BleDataRequest> iterator();

}
