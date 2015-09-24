package me.zhanghailin.trynewble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.zhanghailin.bluetooth.StandardBleProfile;
import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.device.BleDevice;
import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;
import me.zhanghailin.bluetooth.request.BleDataRequest;
import me.zhanghailin.bluetooth.request.ReadWriteRequest;
import me.zhanghailin.trynewble.protocol.BatteryProtocol;
import me.zhanghailin.trynewble.protocol.ClickProtocol;
import me.zhanghailin.trynewble.protocol.ImmediateAlertProtocol;
import me.zhanghailin.trynewble.protocol.LinkLossProtocol;
import timber.log.Timber;

/**
 * Created by zhanghailin on 9/17/15.
 */
public class IHereDevice implements BleDevice {
    private int rssi;
    private final List<BluetoothProtocol> protocols = new ArrayList<>();
    private final ImmediateAlertProtocol immediateAlertProtocol = new ImmediateAlertProtocol();
    private final ClickProtocol clickProtocol = new ClickProtocol();
    private final BatteryProtocol batteryProtocol = new BatteryProtocol();
    private final LinkLossProtocol linkLossProtocol = new LinkLossProtocol();

    private int connectionState;

    private final ConnectionManager connectionManager;
    private BluetoothGatt gatt;
    private String address;

    public IHereDevice(BluetoothGatt gatt, ConnectionManager connectionManager, String address) {
        this.gatt = gatt;
        this.connectionManager = connectionManager;
        this.address = address;
    }

    {
        protocols.add(immediateAlertProtocol);
        protocols.add(clickProtocol);
        protocols.add(batteryProtocol);
        protocols.add(linkLossProtocol);
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
        final UUID notificationUuid =
                UUID.fromString(StandardBleProfile.DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION);

        for (BluetoothGattService service : gatt.getServices()) {
            for (BluetoothGattCharacteristic c : service.getCharacteristics()) {
                Timber.i("service[%s], characteristic[%s]", service.getUuid(), c.getUuid());
            }
        }

        for (final BluetoothProtocol protocol : protocols) {
            if (protocol.canNotify()) {

                connectionManager.enQueueRequest(new BleDataRequest(gatt) {
                    @Override
                    protected boolean performExecute(BluetoothGatt gatt) {
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

                        Timber.i("Descriptor write [%s] for characteristic[%s]",
                                writeSuccess , protocol.getCharacteristicUuid());

                        return writeSuccess;
                    }
                });

            }
        }
    }

    @Override
    public void onDescriptorWrite(UUID characteristicUuid, UUID descriptorUuid) {
        if (descriptorUuid.toString().equalsIgnoreCase(StandardBleProfile.DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION)) {
            BluetoothProtocol protocol = getProtocol(characteristicUuid);
            BluetoothGattService service = gatt.getService(protocol.getServiceUuid());
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
            boolean setNotifySuccess = gatt.setCharacteristicNotification(characteristic, true);

            Timber.i("setNotification [%s] [%s]", characteristicUuid, setNotifySuccess);
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
        characteristic.setValue(ImmediateAlertProtocol.MIDDLE_LEVEL_ALERT);

        BleDataRequest request = new ReadWriteRequest(gatt, characteristic, ReadWriteRequest.WRITE);
        connectionManager.enQueueRequest(request);
    }

    public void battery(BatteryProtocol.OnReadBatteryCompleteListener onReadBatteryCompleteListener) {
        BluetoothGattService service = gatt.getService(batteryProtocol.getServiceUuid());
        BluetoothGattCharacteristic characteristic =
                service.getCharacteristic(batteryProtocol.getCharacteristicUuid());

        BleDataRequest request = new ReadWriteRequest(gatt, characteristic, ReadWriteRequest.READ);
        connectionManager.enQueueRequest(request);

        batteryProtocol.setOnReadBatteryCompleteListener(onReadBatteryCompleteListener);
    }

    public void setOnBleClickListener(ClickProtocol.OnBleClickListener onBleClickListener) {
        clickProtocol.setOnBleClickListener(onBleClickListener);
    }

}
