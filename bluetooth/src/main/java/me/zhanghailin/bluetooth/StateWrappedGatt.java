package me.zhanghailin.bluetooth;

import android.bluetooth.BluetoothGatt;

/**
 * Created by zhanghailin on 9/10/15.
 */
public class StateWrappedGatt {
    public final BluetoothGatt gatt;
    private boolean connected;

    public StateWrappedGatt(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
