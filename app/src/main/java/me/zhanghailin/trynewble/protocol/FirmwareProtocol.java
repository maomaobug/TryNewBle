package me.zhanghailin.trynewble.protocol;

import java.util.UUID;

/**
 * TryNewBle
 * package me.zhanghailin.trynewble.protocol
 * author shenwenjun
 * Date 9/24/15.
 */
public class FirmwareProtocol implements BleReadProtocol {

    private OnBleReadCompleteListener onBleReadCompleteListener;

    public void setOnBleReadCompleteListener(OnBleReadCompleteListener onBleReadCompleteListener) {
        this.onBleReadCompleteListener = onBleReadCompleteListener;
    }

    @Override
    public void onValueRead(byte[] value) {
        if (onBleReadCompleteListener != null) {
            onBleReadCompleteListener.onBleReadComplete(new String(value));
        }
    }

    @Override
    public void setValue(byte[] value) {
        onValueRead(value);
    }

    @Override
    public byte[] getValue() {
        return null;
    }

    @Override
    public UUID getServiceUuid() {
        return IHereProfile.SERVICE_DEVICE_INFORMATION;
    }

    @Override
    public UUID getCharacteristicUuid() {
        return IHereProfile.CHARACTERISTIC_FIRMWARE;
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
