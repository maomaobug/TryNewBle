package me.zhanghailin.bluetooth.device;

import android.bluetooth.BluetoothProfile;

/**
 * Created by zhanghailin on 9/14/15.
 */
public interface DevicePool {
    /**
     * New bluetooth connection callback received.
     * @param address the mac of the remote device.
     * @param stateConnected {@link BluetoothProfile#STATE_CONNECTED} and other STATE_* etc.
     */
    void newConnectionState(String address, int stateConnected);

    BleDevice get(String address);
}
