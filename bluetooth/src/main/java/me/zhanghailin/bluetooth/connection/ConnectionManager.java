package me.zhanghailin.bluetooth.connection;

import me.zhanghailin.bluetooth.device.DevicePool;
import me.zhanghailin.bluetooth.request.BleDataRequest;
import me.zhanghailin.bluetooth.request.ITaskRequest;

/**
 * Created by zhanghailin on 9/17/15.
 */
public interface ConnectionManager {
    void nextConnectionOperation();
    void nextOperation();
    void enQueueRequest(BleDataRequest request);
    void addNewDevice(String address);
    void connectWaitingDevices();
    void addWaitingDevice(String address);
    void removeWaitingDevice(String address);
    void close(String address);
    void reconnect(String address);
    void setDevicePool(DevicePool devicePool);
    ITaskRequest currentTask();
    void clearAll();
}
