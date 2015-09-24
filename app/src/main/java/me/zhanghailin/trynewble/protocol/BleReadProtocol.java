package me.zhanghailin.trynewble.protocol;

import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;

/**
 * TryNewBle
 * package me.zhanghailin.trynewble.protocol
 * author shenwenjun
 * Date 9/24/15.
 */
public interface BleReadProtocol extends BluetoothProtocol {

    interface OnBleReadCompleteListener {
        void onBleReadComplete(Object value);
    }

    void setOnBleReadCompleteListener(OnBleReadCompleteListener onBleReadCompleteListener);

    void onValueRead(byte[] value);

}
