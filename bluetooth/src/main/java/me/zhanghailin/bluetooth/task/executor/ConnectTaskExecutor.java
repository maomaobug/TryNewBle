package me.zhanghailin.bluetooth.task.executor;

import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.request.ITaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.task.executor
 * author shenwenjun
 * Date 10/5/15.
 */
public class ConnectTaskExecutor extends BleTaskExecutor {

    private ConnectionManager connectionManager;

    public ConnectTaskExecutor(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void onExecuteSuccess(ITaskRequest request) {
        super.onExecuteSuccess(request);
    }

    @Override
    public void onExecuteFailed(ITaskRequest request) {
        super.onExecuteFailed(request);
        connectionManager.addWaitingDevice(request.tag());
    }
}
