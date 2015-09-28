package me.zhanghailin.bluetooth.process;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;

import java.util.UUID;

import me.zhanghailin.bluetooth.HexUtil;
import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.connector.Connector;
import me.zhanghailin.bluetooth.device.BleDevice;
import me.zhanghailin.bluetooth.device.DevicePool;
import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;
import me.zhanghailin.bluetooth.response.BleDataResponse;
import timber.log.Timber;

/**
 * 所有操作都在MainThread
 * Created by zhanghailin on 9/14/15.
 */
public class BleResponseProcessor {

    DevicePool devicePool;
    ConnectionManager connectionManager;

    public DevicePool getDevicePool() {
        return devicePool;
    }

    public void setDevicePool(DevicePool devicePool) {
        this.devicePool = devicePool;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void processResponse(BleDataResponse response) {
        if (response == null) {
            return;
        }

        switch (response.type) {
            case CONNECTION_STATE:
                onConnectionChange(response.address, response.status, response.newState);
                break;
            case SERVICE_DISCOVERED:
                connectionManager.nextOperation();

                onServiceDiscovered(response.address);
                break;
            case VALUE_READ:
                connectionManager.nextOperation();

                onNewValue(response.address, response.characteristicUuid, response.value);
                break;
            case VALUE_WRITE:
                connectionManager.nextOperation();

                onValueWrite(response.address, response.characteristicUuid);
                break;
            case VALUE_NOTIFIED:
                onValueNotify(response.address, response.characteristicUuid, response.value);
                break;
            case DESCRIPTOR_WRITE:
                connectionManager.nextOperation();

                onDescriptorWrite(response.address, response.characteristicUuid, response.descriptorUuid);
                break;
            case RSSI:
                connectionManager.nextOperation();

                onRssi(response.address, response.rssi);
                break;
            default:
                throw new TypeNotPresentException(
                        "response type not recognised: " + response.type.name(), null);
        }
    }

    private void onConnectionChange(String address, int status, int newState) {
        BleDevice device = devicePool.get(address);
        device.newState(newState);

        if (status != BluetoothGatt.GATT_SUCCESS) {
//            device.clearAndReconnect();
            // 先断开连接， 下次回调进入时 close 设备
            device.getGatt().close();
            connectionManager.enQueueConnect(address);
            return;
        }

        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            // 正常断开连接， 尝试重连
            connectionManager.enQueueConnect(address);

        } else if (newState == BluetoothProfile.STATE_CONNECTED) {
            if (!device.getConnector().isConnected()) {
                device.getConnector().setState(Connector.STATE_CONNECTED);
                device.discoverServices();
            }
        }
    }

    private void onServiceDiscovered(String address) {
        BleDevice device = devicePool.get(address);
        device.registerNotificationProtocols();
    }

    private void onValueNotify(String address, UUID characteristicUuid, byte[] value) {
        Timber.i("value notify address[%s], char[%s], value[%s]",
                address, characteristicUuid, HexUtil.bytesToHex(value));

        // TODO: 9/23/15  区分 notify value 和 read value
        BleDevice device = devicePool.get(address);
        BluetoothProtocol protocol = device.getProtocol(characteristicUuid);
        protocol.setValue(value);
    }

    private void onNewValue(String address, UUID characteristicUuid, byte[] value) {
        Timber.i("new value address[%s], char[%s], value[%s]",
                address, characteristicUuid, HexUtil.bytesToHex(value));
        BleDevice device = devicePool.get(address);
        BluetoothProtocol protocol = device.getProtocol(characteristicUuid);
        protocol.setValue(value);
    }

    private void onValueWrite(String address, UUID characteristicUuid) {
        Timber.i("write address[%s] char[%s]", address, characteristicUuid);
    }

    /**
     * 暂时我们应用到的descriptor只有开启通知，不需要额外处理。
     */
    private void onDescriptorWrite(String address, UUID characteristicUuid, UUID descriptorUuid) {
        BleDevice device = devicePool.get(address);
        device.onDescriptorWrite(characteristicUuid, descriptorUuid);
    }

    private void onRssi(String address, int rssi) {
        BleDevice device = devicePool.get(address);
        device.setRssi(rssi);
    }

}
