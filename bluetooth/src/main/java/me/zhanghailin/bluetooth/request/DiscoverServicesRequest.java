package me.zhanghailin.bluetooth.request;

import android.bluetooth.BluetoothGatt;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/14/15.
 */
public class DiscoverServicesRequest extends BleDataRequest {

    public DiscoverServicesRequest(BluetoothGatt gatt) {
        super(gatt);
    }

    @Override
    protected boolean performExecute(BluetoothGatt gatt) {
        return gatt.discoverServices();
    }
}
