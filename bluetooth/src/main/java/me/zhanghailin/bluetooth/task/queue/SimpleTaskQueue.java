package me.zhanghailin.bluetooth.task.queue;

import android.util.Log;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.zhanghailin.bluetooth.request.BleDataRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.queue
 * author shenwenjun
 * Date 9/16/15.
 */
public class SimpleTaskQueue implements TaskQueue {

    private static final String TAG = "SimpleTaskQueue";

    private Queue<BleDataRequest> mQueue;

    public SimpleTaskQueue() {
        mQueue = new ConcurrentLinkedQueue<>();
    }

    public boolean addTask(BleDataRequest request) {
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
    public BleDataRequest peekTask() {
        return mQueue.peek();
    }

    @Override
    public BleDataRequest pollTask() {
        return mQueue.poll();
    }

    @Override
    public boolean containTask(BleDataRequest request) {
        return mQueue.contains(request);
    }

    @Override
    public boolean removeTask(BleDataRequest request) {
        return mQueue.remove(request);
    }

    @Override
    public boolean removeAllTask() {
        mQueue.clear();
        return true;
    }

    @Override
    public boolean isEmpty() {
        return mQueue.isEmpty();
    }

    @Override
    public Iterator<BleDataRequest> iterator() {
        return mQueue.iterator();
    }

}
