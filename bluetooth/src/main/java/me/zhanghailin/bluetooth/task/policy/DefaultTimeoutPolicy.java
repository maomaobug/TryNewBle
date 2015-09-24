package me.zhanghailin.bluetooth.task.policy;

import me.zhanghailin.bluetooth.task.ITaskManager;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.policy
 * author shenwenjun
 * Date 9/16/15.
 */
public class DefaultTimeoutPolicy implements TimeoutPolicy {

    private final static int RETRY_COUNT = 2;

    private int mCount = 0;

    private ITaskManager taskManager;

    public DefaultTimeoutPolicy(ITaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void onTaskTimeout() {
        if (mCount <= RETRY_COUNT) {
            taskManager.retryCurrentTask();
            mCount++;
        } else {
            taskManager.finishTask();
            mCount = 0;
        }
    }

    @Override
    public void onResetTimeout() {
        mCount = 0;
    }

}
