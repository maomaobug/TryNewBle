package me.zhanghailin.bluetooth.task.policy;

import me.zhanghailin.bluetooth.task.ITaskManager;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.policy
 * author shenwenjun
 * Date 9/16/15.
 */
public class SimpleFinishPolicy implements TimeoutPolicy {

    private final static int RETRY_COUNT = 2;

    private int mCount = 0;

    private ITaskManager taskManager;

    public SimpleFinishPolicy(ITaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void onTaskTimeout() {
        taskManager.finishTaskWithRunNext();
    }

    @Override
    public void onResetTimeout() {
        mCount = 0;
    }

}
