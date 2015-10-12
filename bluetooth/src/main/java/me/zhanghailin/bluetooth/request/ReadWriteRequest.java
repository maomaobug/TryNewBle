package me.zhanghailin.bluetooth.request;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/14/15.
 */
public class ReadWriteRequest extends BleDataRequest {

    public static final int READ_CHARACTERISTIC = 0x1234;
    public static final int READ_DESCRIPTOR = 0x1235;
    public static final int WRITE_CHARACTERISTIC = 0x1236;
    public static final int WRITE_DESCRIPTOR = 0x1237;

    private int mMode;

    private BluetoothProtocol protocol;

    private byte[] characteristicValue;

    private UUID descriptorUUID;

    private byte[] descriptorValue;

    /**
     * Read Characteristic Value
     */
    public ReadWriteRequest(BluetoothGatt gatt, BluetoothProtocol protocol) {
        super(gatt);
        this.protocol = protocol;
        mMode = READ_CHARACTERISTIC;
    }

    /**
     * Read descriptor Value
     */
    public ReadWriteRequest(BluetoothGatt gatt, BluetoothProtocol protocol, UUID descriptorUUID) {
        super(gatt);
        this.protocol = protocol;
        this.descriptorUUID = descriptorUUID;
        mMode = READ_DESCRIPTOR;
    }

    /**
     * Write Characteristic
     */
    public ReadWriteRequest(BluetoothGatt gatt, BluetoothProtocol protocol, byte[] characteristicValue) {
        super(gatt);
        this.protocol = protocol;
        this.characteristicValue = characteristicValue;
        this.mMode = WRITE_CHARACTERISTIC;
    }

    /**
     * Write descriptor
     */
    public ReadWriteRequest(BluetoothGatt gatt, BluetoothProtocol protocol, UUID descriptorUUID, byte[] descriptorValue) {
        super(gatt);
        this.protocol = protocol;
        this.descriptorUUID = descriptorUUID;
        this.descriptorValue = descriptorValue;
        this.mMode = WRITE_DESCRIPTOR;
    }

    @Override
    protected boolean performExecute(BluetoothGatt gatt) {
        boolean result;

        BluetoothGattService service = gatt.getService(protocol.getServiceUuid());
        if (service == null) {
            return false;
        }
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(protocol.getCharacteristicUuid());
        if (characteristic == null) {
            return false;
        }

        switch (mMode) {
            case READ_CHARACTERISTIC:
                result = gatt.readCharacteristic(characteristic);
                break;
            case READ_DESCRIPTOR:
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descriptorUUID);
                if (descriptor == null) {
                    return false;
                }
                result = gatt.readDescriptor(descriptor);
                break;
            case WRITE_CHARACTERISTIC:
                characteristic.setValue(characteristicValue);
                result = gatt.writeCharacteristic(characteristic);
                break;
            case WRITE_DESCRIPTOR:
                BluetoothGattDescriptor descriptorToWrite = characteristic.getDescriptor(descriptorUUID);
                if (descriptorToWrite == null) {
                    return false;
                }
                descriptorToWrite.setValue(descriptorValue);
                result = gatt.writeDescriptor(descriptorToWrite);
                break;
            default:
                result = false;
                break;
        }

        return result;
    }

}
