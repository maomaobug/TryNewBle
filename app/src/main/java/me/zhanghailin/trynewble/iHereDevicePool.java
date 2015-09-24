package me.zhanghailin.trynewble;

import android.bluetooth.BluetoothGatt;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.device.DevicePool;

/**
 * Created by zhanghailin on 9/17/15.
 */
public class IHereDevicePool extends DevicePool<IHereDevice> {

    private final Map<String, IHereDevice> deviceMap = new HashMap<>();

    public IHereDevicePool(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public IHereDevice get(String address) {
        return deviceMap.get(address);
    }

    @Override
    public void buildNewDevice(BluetoothGatt gatt) {
        Log.i("iHereDevicePool", "build new device");

        String address = gatt.getDevice().getAddress();
        if (deviceMap.containsKey(address)) {
            return;
        }

        IHereDevice device = new IHereDevice(gatt, connectionManager, address);
        deviceMap.put(address, device);

        Log.d("IHereDevicePool", "device map size: " + deviceMap.size());
    }
}
