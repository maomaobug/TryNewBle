package me.zhanghailin.bluetooth.request;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/14/15.
 */
public class ReadWriteRequest extends BleDataRequest {

    public static final String READ = "READ";
    public static final String WRITE = "WRITE";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({READ, WRITE})
    public @interface ReadOrWrite {
    }

    private static final int MODE_READ_CHAR = 0x1231;
    private static final int MODE_READ_DESC = 0x1232;
    private static final int MODE_WRITE_CHAR = 0x1233;
    private static final int MODE_WRITE_DESC = 0x1234;

    private final BluetoothGattCharacteristic characteristic;

    private final BluetoothGattDescriptor descriptor;

    private int mMode;

    public ReadWriteRequest(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, @ReadOrWrite String readOrWrite) {
        super(gatt);
        this.characteristic = characteristic;
        this.descriptor = null;
        mMode = READ.equals(readOrWrite) ? MODE_READ_CHAR : MODE_WRITE_CHAR;
    }

    public ReadWriteRequest(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, @ReadOrWrite String readOrWrite) {
        super(gatt);
        this.characteristic = null;
        this.descriptor = descriptor;
        mMode = READ.equals(readOrWrite) ? MODE_READ_DESC : MODE_WRITE_DESC;
    }

    @Override
    protected boolean performExecute(BluetoothGatt gatt) {
        boolean result;

        switch (mMode) {
            case MODE_READ_CHAR:
                result = gatt.readCharacteristic(characteristic);
                break;
            case MODE_READ_DESC:
                result = gatt.readDescriptor(descriptor);
                break;
            case MODE_WRITE_CHAR:
                result = gatt.writeCharacteristic(characteristic);
                break;
            case MODE_WRITE_DESC:
                result = gatt.writeDescriptor(descriptor);
                break;
            default:
                result = false;
                break;
        }

        return result;
    }

}
