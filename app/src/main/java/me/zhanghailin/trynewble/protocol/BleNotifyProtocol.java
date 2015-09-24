package me.zhanghailin.trynewble.protocol;

import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;

/**
 * TryNewBle
 * package me.zhanghailin.trynewble.protocol
 * author shenwenjun
 * Date 9/23/15.
 */
public interface BleNotifyProtocol extends BluetoothProtocol {

    void onValueNotify(byte[] value);

    void onValueRead(byte[] value);

}
