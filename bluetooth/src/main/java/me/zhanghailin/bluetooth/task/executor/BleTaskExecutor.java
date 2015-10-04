package me.zhanghailin.bluetooth.task.executor;

import android.bluetooth.BluetoothAdapter;

import me.zhanghailin.bluetooth.request.ITaskRequest;
import timber.log.Timber;

/**
 * 蓝牙状态为 ON 时才会正常执行
 * package me.zhanghailin.bluetooth.task.executor
 * author shenwenjun
 * Date 9/14/15.
 */
public class BleTaskExecutor implements TaskExecutor {

    @Override
    public boolean executeTask(ITaskRequest request) {
        Timber.d("executing %s %s", request.getClass().getSimpleName(), request.tag());
        boolean result = false;
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            int state = adapter.getState();
            if (state == BluetoothAdapter.STATE_ON) {
                request.execute();
                result = true;
            }
        } catch (Exception e) {
            Timber.e(e, "in executeTask");
        }
        return result;
    }

}
