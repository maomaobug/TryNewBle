package me.zhanghailin.bluetooth.task;

import android.util.Log;

import java.util.Iterator;

import me.zhanghailin.bluetooth.request.BleDataRequest;
import me.zhanghailin.bluetooth.request.filter.RequestFilter;
import me.zhanghailin.bluetooth.task.executor.SimpleTaskExecutor;
import me.zhanghailin.bluetooth.task.executor.TaskExecutor;
import me.zhanghailin.bluetooth.task.policy.DefaultTimeoutPolicy;
import me.zhanghailin.bluetooth.task.policy.TimeoutPolicy;
import me.zhanghailin.bluetooth.task.queue.SimpleTaskQueue;
import me.zhanghailin.bluetooth.task.queue.TaskQueue;

/**
 * 管理 task 的调度 和 执行
 * package me.zhanghailin.bluetooth.task
 * author shenwenjun
 * Date 9/14/15.
 */
public class TaskManager implements ITaskManager {

    private static final String TAG = "TaskManager";

    private TaskQueue taskQueue;

    private TaskExecutor taskExecutor;

    private Timeout timeout;

    private BleDataRequest currentTask;

    private TaskManager() {
        /* use Builder pattern*/
    }

    @Override
    public void submitTask(BleDataRequest request) {
        taskQueue.addTask(request);

        nextTask();
    }

    @Override
    public void finishTask() {
        timeout.stopTiming();

        currentTask.setRunning(false);

        nextTask();
    }

    @Override
    public void retryCurrentTask() {
        cancelTask(currentTask);

        retryTask(currentTask);
    }

    @Override
    public void cancelTask(RequestFilter filter) {

        TaskQueue localQueue = taskQueue;

        Iterator<BleDataRequest> iterator = localQueue.iterator();
        BleDataRequest dataRequest;
        while (iterator.hasNext()) {
            dataRequest = iterator.next();
            if (filter.apply(dataRequest)) {
                localQueue.removeTask(dataRequest);
            }
        }
    }

    @Override
    public void cancelAllTask() {
        taskQueue.removeAllTask();
    }

    private void nextTask() {

        if (taskQueue.isEmpty()) {
            Log.i(TAG, " taskQueue is empty");
            return;
        }

        if (currentTask != null && currentTask.isRunning()) {
            Log.i(TAG, " currentTask is running, wait...");
            return;
        }

        currentTask = taskQueue.pollTask();

        if (currentTask == null || currentTask.isRunning()) {
            Log.w(TAG, " currentTask is running, wait...");
            return;
        }

        performExecute();
    }

    private void cancelTask(BleDataRequest request) {
        if (request != null && request.isRunning()) {
            request.setRunning(false);

        } else {
            if (request == null) {
                Log.e(TAG, " retry failed because request is null ");
            } else {
                Log.e(TAG, " retry failed because request not running");
            }
        }
    }

    private void retryTask(BleDataRequest request) {
        if (request != null && !request.isRunning()) {
            performExecute();

        } else {
            if (request == null) {
                Log.e(TAG, " retry failed because request is null ");
            } else {
                Log.e(TAG, " retry failed because request running");
            }
        }
    }

    private void performExecute() {
        timeout.startTiming();
        currentTask.setRunning(true);
        taskExecutor.executeTask(currentTask);
    }

    public static class Builder {

        private TaskManager mTaskManager;

        public Builder() {
            mTaskManager = new TaskManager();

            mTaskManager.taskQueue = new SimpleTaskQueue();
            mTaskManager.taskExecutor = new SimpleTaskExecutor();

            mTaskManager.timeout = new Timeout();

            TimeoutPolicy timeoutPolicy = new DefaultTimeoutPolicy(mTaskManager);

            mTaskManager.timeout.setTimeoutPolicy(timeoutPolicy);
        }

        public Builder setTaskQueue(TaskQueue taskQueue) {
            mTaskManager.taskQueue = taskQueue;
            return this;
        }

        public Builder setTaskExecutor(TaskExecutor taskExecutor) {
            mTaskManager.taskExecutor = taskExecutor;
            return this;
        }

        public Builder setTimeoutPolicy(TimeoutPolicy timeoutPolicy) {
            mTaskManager.timeout.setTimeoutPolicy(timeoutPolicy);
            return this;
        }

        public TaskManager build() {
            return mTaskManager;
        }

    }

}
