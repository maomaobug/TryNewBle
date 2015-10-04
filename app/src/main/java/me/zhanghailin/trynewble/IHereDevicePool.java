package me.zhanghailin.trynewble;

import android.bluetooth.BluetoothAdapter;
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
    public IHereDevice buildNewDevice(BluetoothAdapter adapter, String address) {

        IHereDevice iHereDevice;
        if (deviceMap.containsKey(address)) {
            iHereDevice = deviceMap.get(address);
            Timber.i("duplicate not building");
        } else {
            Timber.i("build new device");
            iHereDevice = new IHereDevice(connectionManager, adapter.getRemoteDevice(address));
            deviceMap.put(address, iHereDevice);
        }

        Log.d("IHereDevicePool", "device map size: " + deviceMap.size());
        return iHereDevice;
    }

}
