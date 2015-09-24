package me.zhanghailin.trynewble.protocol;

import java.util.UUID;

import me.zhanghailin.bluetooth.StandardBleProfile;
import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;

/**
 * TryNewBle
 * package me.zhanghailin.trynewble.protocol
 * author shenwenjun
 * Date 9/22/15.
 */
public class BatteryProtocol implements BluetoothProtocol {

    public interface OnReadBatteryCompleteListener {
        void onReadComplete(int batteryLevel);
    }

    private OnReadBatteryCompleteListener onReadBatteryCompleteListener;

    public void setOnReadBatteryCompleteListener(OnReadBatteryCompleteListener onReadBatteryCompleteListener) {
        this.onReadBatteryCompleteListener = onReadBatteryCompleteListener;
    }

    @Override
    public void setValue(byte[] value) {
        int batteryLevel = value[0];

        if (onReadBatteryCompleteListener != null) {
            onReadBatteryCompleteListener.onReadComplete(batteryLevel);
        }
    }

    @Override
    public byte[] getValue() {
        return new byte[0];
    }

    @Override
    public UUID getServiceUuid() {
        return UUID.fromString(StandardBleProfile.SERVICE_BATTERY_SERVICE);
    }

    @Override
    public UUID getCharacteristicUuid() {
        return UUID.fromString(StandardBleProfile.CHAR_BATTERY_LEVEL);
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
