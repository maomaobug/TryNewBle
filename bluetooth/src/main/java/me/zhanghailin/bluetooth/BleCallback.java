package me.zhanghailin.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

import java.util.UUID;

import me.zhanghailin.bluetooth.response.BleDataDelivery;

/**
 * 将所有系统给来的状态变更和数据通过{@link BleDataDelivery} 发送出去，比如发送到统一的数据处理线程上
 * Created by zhanghailin on 9/9/15.
 */
public class BleCallback extends BluetoothGattCallback {
    private static final String TAG = "BleCallback";
    private BleDataDelivery dataDeliver;

    public BleCallback(BleDataDelivery bleDataDelivery) {
        this.dataDeliver = bleDataDelivery;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        String address = gatt.getDevice().getAddress();

        Log.d(TAG, "new state address " + address +" newState:" + newState + " status:" + status);

        dataDeliver.onNewConnectionState(address, status, newState);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        String address = gatt.getDevice().getAddress();
        Log.d(TAG, "service discovered address " + address);
        dataDeliver.onServiceDiscovered(address);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        String address = gatt.getDevice().getAddress();
        UUID characteristicUuid = characteristic.getUuid();
        Log.d(TAG, "read uuid " + characteristicUuid.toString() + " address" + address);
        byte[] value = characteristic.getValue();
        dataDeliver.onValueRead(address, characteristicUuid, value);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        String address = gatt.getDevice().getAddress();
        UUID characteristicUuid = characteristic.getUuid();
        dataDeliver.onValueWrite(address, characteristicUuid);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        String address = gatt.getDevice().getAddress();
        UUID characteristicUuid = characteristic.getUuid();
        byte[] value = characteristic.getValue();
        dataDeliver.onValueNotified(address, characteristicUuid, value);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        String address = gatt.getDevice().getAddress();
        UUID characteristicUuid = descriptor.getCharacteristic().getUuid();
        UUID descriptorUuid = descriptor.getUuid();
        dataDeliver.onDescriptorWrite(address, characteristicUuid, descriptorUuid);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        String address = gatt.getDevice().getAddress();
        dataDeliver.onRssi(address, rssi);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        super.onMtuChanged(gatt, mtu, status);
    }
}
