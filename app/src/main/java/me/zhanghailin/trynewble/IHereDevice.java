package me.zhanghailin.trynewble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.device.BleDevice;
import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;
import me.zhanghailin.trynewble.protocol.ClickProtocol;
import me.zhanghailin.trynewble.protocol.ImmediateAlertProtocol;
import me.zhanghailin.trynewble.protocol.StandardBleProfile;
import timber.log.Timber;

/**
 * Created by zhanghailin on 9/17/15.
 */
public class IHereDevice implements BleDevice {
    private int rssi;
    private final List<BluetoothProtocol> protocols = new ArrayList<>();
    private final ImmediateAlertProtocol immediateAlertProtocol = new ImmediateAlertProtocol();
    private final ClickProtocol clickProtocol = new ClickProtocol();
    private int connectionState;

    private final ConnectionManager connectionManager;
    private String address;
    private BluetoothGatt gatt;

    public IHereDevice(BluetoothGatt gatt, ConnectionManager connectionManager) {
        this.gatt = gatt;
        this.connectionManager = connectionManager;
    }

    {
        protocols.add(immediateAlertProtocol);
        protocols.add(clickProtocol);
    }

    @Override
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public BluetoothProtocol getProtocol(UUID characteristicUuid) {
        for (BluetoothProtocol protocol :
                protocols) {
            if (protocol.getCharacteristicUuid().equals(characteristicUuid)) {
                return protocol;
            }
        }

        throw new IllegalArgumentException(
                "No protocol found for this uuid: " + characteristicUuid);
    }

    @Override
    public List<BluetoothProtocol> getAllProtocols() {
        return protocols;
    }

    @Override
    public void newState(int newState) {
        connectionState = newState;
    }

    @Override
    public void registerNotificationProtocols() {
        UUID notificationUuid =
                UUID.fromString(StandardBleProfile.DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION);

        for (BluetoothGattService service : gatt.getServices()) {
            for (BluetoothGattCharacteristic c : service.getCharacteristics()) {
                Timber.i("service[%s], characteristic[%s]", service.getUuid(), c.getUuid());
            }
        }

        for (BluetoothProtocol protocol : protocols) {
            if (protocol.canNotify()) {
                BluetoothGattService service = gatt.getService(protocol.getServiceUuid());
                if (service == null) {
                    throw new RuntimeException(
                            "No service found for: " + protocol.getCharacteristicUuid());
                }

                BluetoothGattCharacteristic characteristic =
                        service.getCharacteristic(protocol.getCharacteristicUuid());
                if (characteristic == null) {
                    throw new RuntimeException(
                            "No characteristic found for: " + protocol.getCharacteristicUuid());
                }

                BluetoothGattDescriptor descriptor =
                        characteristic.getDescriptor(notificationUuid);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                boolean writeSuccess = gatt.writeDescriptor(descriptor);

                boolean setSuccess = gatt.setCharacteristicNotification(characteristic, true);

                Timber.i("Notification enabled[%s] for characteristic[%s]",
                        writeSuccess && setSuccess, protocol.getCharacteristicUuid());
            }
        }
    }

    @Override
    public void clearAndReconnect() {
        connectionManager.enQueueDisconnect(address);
        connectionManager.enQueueConnect(address);
    }

    @Override
    public void discoverServices() {
        gatt.discoverServices();
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void middleAlert() {
        BluetoothGattService service = gatt.getService(immediateAlertProtocol.getServiceUuid());
        BluetoothGattCharacteristic characteristic =
                service.getCharacteristic(immediateAlertProtocol.getCharacteristicUuid());
        characteristic.setValue(ImmediateAlertProtocol.middleLevelAlert);
        gatt.writeCharacteristic(characteristic);
    }
}