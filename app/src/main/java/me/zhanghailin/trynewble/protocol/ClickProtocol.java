package me.zhanghailin.trynewble.protocol;

import java.util.UUID;

/**
 * Created by zhanghailin on 9/18/15.
 */
public class ClickProtocol implements BleNotifyProtocol {

    private final UUID serviceUuid = UUID.fromString(IHereProfile.SERVICE_CLICK);
    private final UUID characteristicUuid = UUID.fromString(IHereProfile.CHARACTERISTIC_CLICK);

    public interface OnBleClickListener {
        void onBleClick();
    }

    private OnBleClickListener onBleClickListener;

    public void setOnBleClickListener(OnBleClickListener onBleClickListener) {
        this.onBleClickListener = onBleClickListener;
    }

    @Override
    public void onValueNotify(byte[] value) {
        if (onBleClickListener != null) {
            onBleClickListener.onBleClick();
        }
    }

    @Override
    public void onValueRead(byte[] value) {

    }

    @Override
    public void setValue(byte[] value) {
//        if (canNotify()) {
        onValueNotify(value);
//        } else if (canRead()) {
//            onValueRead(value);
//        }
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
