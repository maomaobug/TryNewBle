package me.zhanghailin.bluetooth.request;

import android.bluetooth.BluetoothGatt;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/14/15.
 */
public class RSSIRequest extends BleDataRequest {

    public RSSIRequest(BluetoothGatt gatt) {
        super(gatt);
    }

    @Override
    protected boolean performExecute(BluetoothGatt gatt) {
        return gatt.readRemoteRssi();
    }
}
