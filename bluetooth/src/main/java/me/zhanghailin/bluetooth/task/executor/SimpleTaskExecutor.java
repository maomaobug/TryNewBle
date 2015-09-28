package me.zhanghailin.bluetooth.task.executor;

import me.zhanghailin.bluetooth.request.ITaskRequest;
import timber.log.Timber;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.executor
 * author shenwenjun
 * Date 9/14/15.
 */
public class SimpleTaskExecutor implements TaskExecutor {

    @Override
    public boolean executeTask(ITaskRequest request) {
        Timber.d("request executing %s", request);
        return request.execute();
    }

}
