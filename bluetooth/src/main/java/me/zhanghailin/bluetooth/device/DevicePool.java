package me.zhanghailin.bluetooth.device;

import android.bluetooth.BluetoothGatt;

import me.zhanghailin.bluetooth.connection.ConnectionManager;

/**
 * Created by zhanghailin on 9/14/15.
 */
public abstract class DevicePool<T extends BleDevice> {

    public DevicePool(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    protected ConnectionManager connectionManager;

    // TODO: 9/17/15
    public abstract T get(String address);

    public abstract void buildNewDevice(BluetoothGatt gatt);
}
