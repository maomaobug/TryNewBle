package me.zhanghailin.trynewble;

import android.bluetooth.BluetoothGatt;
import android.util.Log;

import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.device.DevicePool;
import timber.log.Timber;

/**
 * Created by zhanghailin on 9/17/15.
 */
public class IHereDevicePool extends DevicePool<IHereDevice> {

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
            Timber.i("duplicate not building");
            return;
        }

        IHereDevice device = new IHereDevice(gatt, connectionManager, address);
        deviceMap.put(address, device);

        Log.d("IHereDevicePool", "device map size: " + deviceMap.size());
    }

}
