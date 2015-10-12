package me.zhanghailin.bluetooth.task.queue;

import android.util.Log;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.zhanghailin.bluetooth.request.ITaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.queue
 * author shenwenjun
 * Date 9/16/15.
 */
public class SimpleTaskQueue implements TaskQueue {

    private static final String TAG = "SimpleTaskQueue";

    private Queue<ITaskRequest> mQueue;

    public SimpleTaskQueue() {
        mQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean addTask(ITaskRequest request) {
        boolean result;
        try {
            result = mQueue.offer(request);
        } catch (RuntimeException e) {
            Log.e(TAG, "addTask has exception", e);
            result = false;
        }
        return result;
    }

    @Override
    public ITaskRequest peekTask() {
        return mQueue.peek();
    }

    @Override
    public ITaskRequest pollTask() {
        return mQueue.poll();
    }

    @Override
    public boolean containTask(ITaskRequest request) {
        return mQueue.contains(request);
    }

    @Override
    public boolean removeTask(ITaskRequest request) {
        return mQueue.remove(request);
    }

    @Override
    public boolean clearTask() {
        mQueue.clear();
        return true;
    }

    @Override
    public boolean isEmpty() {
        return mQueue.isEmpty();
    }

    @Override
    public Iterator<ITaskRequest> iterator() {
        return mQueue.iterator();
    }

}
