package me.zhanghailin.bluetooth.task.timer;

import me.zhanghailin.bluetooth.task.policy.TimeoutPolicy;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.timer
 * author shenwenjun
 * Date 9/28/15.
 */
public interface ITimeoutTimer {

    void setupTimeout(long milliseconds);

    void setupTimeoutPolicy(TimeoutPolicy timeoutPolicy);

    void startTiming();

    void stopTiming();

}
