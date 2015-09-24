package me.zhanghailin.trynewble.protocol;

import java.util.UUID;

import me.zhanghailin.bluetooth.StandardBleProfile;
import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;

/**
 * Created by zhanghailin on 9/18/15.
 */
public class ImmediateAlertProtocol implements BluetoothProtocol{
    public static final byte[] MIDDLE_LEVEL_ALERT = new byte[]{0x02};

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
        return UUID.fromString(StandardBleProfile.SERVICE_IMMEDIATE_ALERT);
    }

    @Override
    public UUID getCharacteristicUuid() {
        return UUID.fromString(StandardBleProfile.CHAR_ALERT_LEVEL);
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
        return true;
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
