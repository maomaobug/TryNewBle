package me.zhanghailin.bluetooth.device;

import android.bluetooth.BluetoothAdapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.zhanghailin.bluetooth.connection.ConnectionManager;

/**
 * Created by zhanghailin on 9/14/15.
 */
public abstract class DevicePool<T extends BleDevice> {

    protected final Map<String, T> deviceMap = new HashMap<>();

    public DevicePool(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    protected ConnectionManager connectionManager;

    // TODO: 9/17/15
    public abstract T get(String address);

    public abstract T buildNewDevice(BluetoothAdapter adapter, String address);

    public void closeAll() {
        Iterator<Map.Entry<String, T>> iterator = deviceMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, T> entry = iterator.next();

            entry.getValue().closeSafely();

            iterator.remove();
        }
    }
}
