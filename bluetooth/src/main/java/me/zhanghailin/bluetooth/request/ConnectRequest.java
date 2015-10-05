package me.zhanghailin.bluetooth.request;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;
import android.util.Log;

import me.zhanghailin.bluetooth.device.BleDevice;

/**
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/28/15.
 */
public class ConnectRequest implements ITaskRequest {

    private final Context context;

    private final BleDevice bleDevice;

    private final BluetoothGattCallback bleCallback;

    public ConnectRequest(Context context, BleDevice bleDevice, BluetoothGattCallback bleCallback) {
        this.context = context.getApplicationContext();
        this.bleDevice = bleDevice;
        this.bleCallback = bleCallback;
    }

    @Override
    public String tag() {
        return bleDevice.getAddress();
    }

    @Override
    public boolean execute() {
        boolean result = true;
        try {
            if (bleDevice.getGatt() != null) {
                bleDevice.getGatt().close();
                bleDevice.setGatt(null);
            }
            BluetoothDevice device = bleDevice.getDevice();
            BluetoothGatt gatt = device.connectGatt(context, false, bleCallback);
            bleDevice.setGatt(gatt);
        } catch (Exception e) {
            Log.e("BleConnectRequest", "address :" + tag(), e);
            result = false;
        }
        return result;
    }

}
