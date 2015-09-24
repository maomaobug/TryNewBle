package me.zhanghailin.bluetooth.connection;

import me.zhanghailin.bluetooth.device.DevicePool;
import me.zhanghailin.bluetooth.request.BleDataRequest;

/**
 * Created by zhanghailin on 9/17/15.
 */
public interface ConnectionManager {
    void nextOperation();
    void enQueueRequest(BleDataRequest request);
    void connect(String address);
    void disconnect(String address);
    void clearAll();
    void enQueueDisconnect(String address);
    void enQueueConnect(String address);
    void setDevicePool(DevicePool devicePool);
}
