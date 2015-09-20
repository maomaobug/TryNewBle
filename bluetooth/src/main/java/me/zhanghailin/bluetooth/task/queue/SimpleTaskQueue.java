package me.zhanghailin.bluetooth.task.queue;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.zhanghailin.bluetooth.request.BleTaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.queue
 * author shenwenjun
 * Date 9/16/15.
 */
public class SimpleTaskQueue implements TaskQueue {

    private static final String TAG = "SimpleTaskQueue";

    private Queue<BleTaskRequest> mQueue;

    public SimpleTaskQueue() {
        mQueue = new ConcurrentLinkedQueue<>();
    }

    public boolean addTask(BleTaskRequest request) {
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
    public BleTaskRequest peekTask() {
        return mQueue.peek();
    }

    @Override
    public BleTaskRequest pollTask() {
        return mQueue.poll();
    }

    @Override
    public boolean containTask(BleTaskRequest request) {
        return mQueue.contains(request);
    }

    @Override
    public boolean isEmpty() {
        return mQueue.isEmpty();
    }


}
