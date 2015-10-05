package me.zhanghailin.bluetooth.task.queue;

import java.util.HashSet;
import java.util.Set;

import me.zhanghailin.bluetooth.request.ITaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.queue
 * author shenwenjun
 * Date 10/2/15.
 */
public class HashTaskQueue extends SimpleTaskQueue {

    private Set<String> tags;

    public HashTaskQueue() {
        tags = new HashSet<>();
    }

    @Override
    public boolean addTask(ITaskRequest request) {

        if (tags.contains(request.tag())) {
            //如果一个 request 已经在队列里， 不要重复添加
            return false;
        }

        tags.add(request.tag());

        return super.addTask(request);
    }

    @Override
    public ITaskRequest pollTask() {

        ITaskRequest request = super.pollTask();

        tags.remove(request.tag());

        return request;
    }

    @Override
    public boolean removeTask(ITaskRequest request) {

        tags.remove(request.tag());

        return super.removeTask(request);
    }

    @Override
    public boolean clearTask() {
        tags.clear();
        return super.clearTask();
    }
}
