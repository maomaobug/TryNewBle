package me.zhanghailin.bluetooth.device;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.zhanghailin.bluetooth.StandardBleProfile;
import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.connector.Connector;
import me.zhanghailin.bluetooth.connector.DefaultConnector;
import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;
import me.zhanghailin.bluetooth.request.BleDataRequest;
import me.zhanghailin.bluetooth.request.DiscoverServicesRequest;
import timber.log.Timber;

/**
 * 代表一个设备包含的所有Protocol，发送蓝牙命令，存储所有接收到的数据，实现中可以由这里向外部发出model改变的事件。
 * Created by zhanghailin on 9/14/15.
 */
public abstract class BleDevice {

    protected final List<BluetoothProtocol> protocols = new ArrayList<>();
    protected final ConnectionManager connectionManager;
    private final BluetoothDevice device;
    protected int connectionState;
    protected BluetoothGatt gatt;

    private Connector connector;

    private boolean serviceDiscovered;

    public BleDevice(ConnectionManager connectionManager, BluetoothDevice device) {
        this(connectionManager, device, new DefaultConnector());
    }

    public BleDevice(ConnectionManager connectionManager, BluetoothDevice device, Connector connector) {
        this.connectionManager = connectionManager;
        this.device = device;
        this.connector = connector;
    }

    //---- 状态相关 ----

    public boolean isServiceDiscovered() {
        return serviceDiscovered;
    }

    public void setServiceDiscovered(boolean serviceDiscovered) {
        this.serviceDiscovered = serviceDiscovered;
    }

    public String getAddress() {
        return device.getAddress();
    }

    public abstract void setRssi(int rssi);

    public void newState(int newState) {
        connectionState = newState;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setGatt(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    /**
     * @throws IllegalArgumentException If no protocol found for the UUID
     */
    public BluetoothProtocol getProtocol(UUID characteristicUuid) {
        for (BluetoothProtocol protocol : protocols) {
            if (protocol.getCharacteristicUuid().equals(characteristicUuid)) {
                return protocol;
            }
        }

        throw new IllegalArgumentException(
                "No protocol found for this uuid: " + characteristicUuid);
    }

    public List<BluetoothProtocol> getAllProtocols() {
        return protocols;
    }

    //---- 操作相关 ----

    public void closeSafely() {
        if (gatt != null) {
            gatt.close();
            gatt = null;
        } else {
            Timber.w("device has closed address[%s]", getAddress());
        }
    }

    //---- 命令相关 ----

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
                                writeSuccess, protocol.getCharacteristicUuid());

                        return writeSuccess;
                    }
                });

            }
        }
    }

    public void onDescriptorWrite(UUID characteristicUuid, UUID descriptorUuid) {
        if (descriptorUuid.toString().equalsIgnoreCase(StandardBleProfile.DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION)) {
            BluetoothProtocol protocol = getProtocol(characteristicUuid);
            BluetoothGattService service = gatt.getService(protocol.getServiceUuid());
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
            boolean setNotifySuccess = gatt.setCharacteristicNotification(characteristic, true);

            Timber.i("setNotification [%s] [%s]", characteristicUuid, setNotifySuccess);
        }
    }

    public void discoverServices() {
        DiscoverServicesRequest request = new DiscoverServicesRequest(gatt);
        connectionManager.enQueueRequest(request);
    }

}
