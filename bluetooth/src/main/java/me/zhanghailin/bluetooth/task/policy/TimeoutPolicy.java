package me.zhanghailin.bluetooth.task.policy;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task
 * author shenwenjun
 * Date 9/16/15.
 */
public interface TimeoutPolicy {

    void onTaskTimeout();

    void onResetTimeout();

}
