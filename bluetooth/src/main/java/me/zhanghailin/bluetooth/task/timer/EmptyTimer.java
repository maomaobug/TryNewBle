package me.zhanghailin.bluetooth.task.timer;

import me.zhanghailin.bluetooth.task.policy.TimeoutPolicy;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.timer
 * author shenwenjun
 * Date 9/28/15.
 */
public class EmptyTimer implements ITimeoutTimer {
    @Override
    public void setupTimeout(long milliseconds) {

    }

    @Override
    public void setupTimeoutPolicy(TimeoutPolicy timeoutPolicy) {

    }

    @Override
    public void startTiming() {

    }

    @Override
    public void stopTiming() {

    }
}
