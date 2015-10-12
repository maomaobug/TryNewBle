package me.zhanghailin.trynewble.protocol;

import java.util.UUID;

/**
 * Created by zhanghailin on 9/18/15.
 */
public class ClickProtocol implements BleNotifyProtocol {

    private final UUID serviceUuid = UUID.fromString(IHereProfile.SERVICE_CLICK);
    private final UUID characteristicUuid = UUID.fromString(IHereProfile.CHARACTERISTIC_CLICK);

    private OnBleNotifyListener onBleNotifyListener;

    @Override
    public void setOnBleNotifyListener(OnBleNotifyListener onBleNotifyListener) {
        this.onBleNotifyListener = onBleNotifyListener;
    }

    @Override
    public void onValueNotify(byte[] value) {
        if (onBleNotifyListener != null) {
            onBleNotifyListener.onBleNotify(value);
        }
    }

    @Override
    public void setValue(byte[] value) {
        onValueNotify(value);
    }

    @Override
    public byte[] getValue() {
        return null;
    }

    @Override
    public UUID getServiceUuid() {
        return serviceUuid;
    }

    @Override
    public UUID getCharacteristicUuid() {
        return characteristicUuid;
    }

    @Override
    public boolean canNotify() {
        return true;
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
