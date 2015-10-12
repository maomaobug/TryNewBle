package me.zhanghailin.bluetooth.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.zhanghailin.bluetooth.request.ITaskRequest;
import me.zhanghailin.bluetooth.request.filter.RequestFilter;
import me.zhanghailin.bluetooth.task.executor.BleTaskExecutor;
import me.zhanghailin.bluetooth.task.executor.TaskExecutor;
import me.zhanghailin.bluetooth.task.policy.SimpleFinishPolicy;
import me.zhanghailin.bluetooth.task.policy.TimeoutPolicy;
import me.zhanghailin.bluetooth.task.queue.SimpleTaskQueue;
import me.zhanghailin.bluetooth.task.queue.TaskQueue;
import me.zhanghailin.bluetooth.task.timer.EmptyTimer;
import me.zhanghailin.bluetooth.task.timer.ITimeoutTimer;
import timber.log.Timber;

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

    private ITimeoutTimer timeoutTimer;

    private ITaskRequest currentTask;

    private TaskManager() {
        /* use Builder pattern*/
    }

    @Override
    public void submitTask(ITaskRequest request) {
        Timber.d("submit exect  %s %s", request.getClass().getSimpleName(), request.tag());

        taskQueue.addTask(request);

        nextTask();
    }

    @Override
    public void finishTask() {
        if (currentTask == null) {
            Timber.d("finish exef 飞来横祸一记");
        } else {
            Timber.d("finish exef  %s %s", currentTask.getClass().getSimpleName(), currentTask.tag());
        }

        currentTask = null;

        timeoutTimer.stopTiming();
    }

    @Override
    public void finishTaskWithRunNext() {
        finishTask();

        nextTask();
    }

    @Override
    public void cancelTask(RequestFilter filter) {

        TaskQueue localQueue = taskQueue;

        Iterator<ITaskRequest> iterator = localQueue.iterator();
        ITaskRequest dataRequest;
        while (iterator.hasNext()) {
            dataRequest = iterator.next();
            if (filter.apply(dataRequest)) {
                localQueue.removeTask(dataRequest);
            }
        }

        ITaskRequest currentTask = currentTask();
        if (currentTask != null && filter.apply(currentTask)) {
            // 蓝牙没有办法停止当前任务, 这里直接 finishTaskWithRunNext
            finishTask();
        }
    }

    @Override
    public void cancelAllTask() {
        taskQueue.clearTask();
    }

    @Override
    public ITaskRequest currentTask() {
        return currentTask;
    }

    @Override
    public List<ITaskRequest> listTask() {
        Iterator<ITaskRequest> iterator = taskQueue.iterator();
        List<ITaskRequest> list = new ArrayList<>();

        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        return list;
    }

    private void nextTask() {

        if (taskQueue.isEmpty()) {
            Timber.v(" taskQueue is empty");
            return;
        }

        if (currentTask != null) {
            Timber.v(" currentTask [%s] is running, waiting...", currentTask.tag());
            return;
        }

        currentTask = taskQueue.pollTask();

        if (currentTask == null) {
            Timber.w(" currentTask is null");
            return;
        }

        performExecute();
    }

    private void performExecute() {
        timeoutTimer.startTiming();
        boolean success = taskExecutor.executeTask(currentTask);
        if (success) {
            taskExecutor.onExecuteSuccess(currentTask);
        } else {
            taskExecutor.onExecuteFailed(currentTask);
            finishTaskWithRunNext();
        }
    }

    public static class Builder {

        private TaskManager mTaskManager;

        public Builder() {
            mTaskManager = new TaskManager();

            mTaskManager.taskQueue = new SimpleTaskQueue();
            mTaskManager.taskExecutor = new BleTaskExecutor();

            mTaskManager.timeoutTimer = new EmptyTimer();

            TimeoutPolicy timeoutPolicy = new SimpleFinishPolicy(mTaskManager);

            mTaskManager.timeoutTimer.setupTimeoutPolicy(timeoutPolicy);
            mTaskManager.timeoutTimer.setupTimeout(4 * 1000);
        }

        public Builder setTaskQueue(TaskQueue taskQueue) {
            mTaskManager.taskQueue = taskQueue;
            return this;
        }

        public Builder setTaskExecutor(TaskExecutor taskExecutor) {
            mTaskManager.taskExecutor = taskExecutor;
            return this;
        }

        public Builder setTimeoutTimer(ITimeoutTimer timeoutTimer) {
            mTaskManager.timeoutTimer = timeoutTimer;
            return this;
        }

        public Builder setTimeoutTimer(ITimeoutTimer timeoutTimer, TimeoutPolicy timeoutPolicy, long timeout) {
            mTaskManager.timeoutTimer = timeoutTimer;
            setTimeoutPolicy(timeoutPolicy, timeout);
            return this;
        }

        public Builder setTimeoutPolicy(TimeoutPolicy timeoutPolicy) {
            mTaskManager.timeoutTimer.setupTimeoutPolicy(timeoutPolicy);
            mTaskManager.timeoutTimer.setupTimeout(4 * 1000);
            return this;
        }

        public Builder setTimeoutPolicy(TimeoutPolicy timeoutPolicy, long timeout) {
            mTaskManager.timeoutTimer.setupTimeoutPolicy(timeoutPolicy);
            mTaskManager.timeoutTimer.setupTimeout(timeout);
            return this;
        }

        public TaskManager build() {
            return mTaskManager;
        }

    }

}
