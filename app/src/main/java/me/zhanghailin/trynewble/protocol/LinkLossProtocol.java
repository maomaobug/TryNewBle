package me.zhanghailin.trynewble.protocol;

import java.util.UUID;

import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;

/**
 * TryNewBle
 * package me.zhanghailin.trynewble.protocol
 * author shenwenjun
 * Date 9/23/15.
 */
public class LinkLossProtocol implements BluetoothProtocol {


    @Override
    public void setValue(byte[] value) {
        /* no-op */
    }

    @Override
    public byte[] getValue() {
        return null;
    }

    @Override
    public UUID getServiceUuid() {
        return UUID.fromString(StandardBleProfile.SERVICE_LINK_LOSS);
    }

    @Override
    public UUID getCharacteristicUuid() {
        return UUID.fromString(StandardBleProfile.CHAR_ALERT_LEVEL_LINK_LOSS);
    }

    @Override
    public boolean canNotify() {
        return false;
    }

    @Override
    public boolean canRead() {
        return false;
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public boolean canReliableWrite() {
        return false;
    }

    @Override
    public long getLastUpdateTime() {
        return 0;
    }
}
