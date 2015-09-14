package me.zhanghailin.bluetooth.protocol;

import java.util.UUID;

/**
 * 代表一个Characteristic的应用
 * Created by zhanghailin on 9/14/15.
 */
public interface BluetoothProtocol {
    void setValue(byte[] value);
    byte[] getValue();
    UUID getServiceUuid();
    UUID getCharacteristicUuid();
    boolean canNotify();
    boolean canRead();
    boolean canWrite();
    boolean canReliableWrite();
}
