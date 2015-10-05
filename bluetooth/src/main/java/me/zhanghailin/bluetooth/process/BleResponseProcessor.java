package me.zhanghailin.bluetooth.process;

import android.bluetooth.BluetoothAdapter;
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

    private BluetoothAdapter bluetoothAdapter;

    public BleResponseProcessor() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void setDevicePool(DevicePool devicePool) {
        this.devicePool = devicePool;
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

                onServiceDiscovered(response.address, response.status);
                break;
            case VALUE_READ:
                connectionManager.nextOperation();

                onNewValue(response.address, response.characteristicUuid, response.value, response.status);
                break;
            case VALUE_WRITE:
                connectionManager.nextOperation();

                onValueWrite(response.address, response.characteristicUuid, response.status);
                break;
            case VALUE_NOTIFIED:
                onValueNotify(response.address, response.characteristicUuid, response.value);
                break;
            case DESCRIPTOR_READ:
                connectionManager.nextOperation();

                onDescriptorRead(response.address, response.characteristicUuid, response.descriptorUuid, response.status);
                break;
            case DESCRIPTOR_WRITE:
                connectionManager.nextOperation();

                onDescriptorWrite(response.address, response.characteristicUuid, response.descriptorUuid, response.status);
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

        // 1. 首先判断蓝牙状态， 蓝牙不处于正常打开且可用的情况下时，把所有设备都 close 掉
        if (bluetoothAdapter.getState() != BluetoothAdapter.STATE_ON) {
            connectionManager.nextConnectionOperation();
//            蓝牙不处于打开状态的情况下，直接把设备添加到等待连接集合， 以备后续需要时发起连接
            device.getGatt().close();
            device.setGatt(null);
            connectionManager.addWaitingDevice(address);
            return;
        }

        // 2. 然后判断 Gatt 操作状态是否为成功， 连接不在范围内的设备时会进入这里
        if (status != BluetoothGatt.GATT_SUCCESS) {
            device.getGatt().close();
            device.setGatt(null);
            switch (status) {
                case 133: // 尝试重连， 能救回来
                    connectionManager.reconnect(address);
                    connectionManager.addWaitingDevice(address);
                    break;
                case 257: // 这个状态的就没见过能活过来的， 加入等待连接队列吧
                    connectionManager.addWaitingDevice(address);
                default: //其他错误情况 忽略， 直接进行下一个任务
                    connectionManager.nextConnectionOperation();
                    break;
            }
            return;
        }

        // 3. 当操作状态正常成功时, 判断是连接上了设备， 还是设备被断开
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {

            device.getGatt().close();
            device.setGatt(null);
            if (device.getConnector().isConnected()) {
                //设备之前连上了， 现在发现断开，则需要尝试重连
                connectionManager.reconnect(address);
            } else {
                // 设备从来没有连上过，现在发生了断开 =>
                // 这种情况很难发生，因为 Gatt 连接不上设备的时候， 返回的 status 不会是 success 也就不会走到这个分支
                // 既然这样，准备报错吧 !
                device.getConnector().setState(Connector.STATE_ERROR);
                Timber.d("executing connect error");
                connectionManager.nextConnectionOperation();
            }
        } else if (newState == BluetoothProfile.STATE_CONNECTED) {
            connectionManager.nextConnectionOperation();

            device.getConnector().setState(Connector.STATE_CONNECTED);
            connectionManager.removeWaitingDevice(address);
            device.discoverServices();
        }
    }

    private void onServiceDiscovered(String address, int status) {
        Timber.i("service discovered address[%s] status[%s]", address, status);

        BleDevice device = devicePool.get(address);
        device.registerNotificationProtocols();
    }

    private void onValueNotify(String address, UUID characteristicUuid, byte[] value) {
        Timber.i("value notify address[%s], char[%s], value[%s]",
                address, characteristicUuid, HexUtil.bytesToHex(value));

        BleDevice device = devicePool.get(address);
        BluetoothProtocol protocol = device.getProtocol(characteristicUuid);
        protocol.setValue(value);
    }

    private void onNewValue(String address, UUID characteristicUuid, byte[] value, int status) {
        Timber.i("new value address[%s], char[%s], value[%s]",
                address, characteristicUuid, HexUtil.bytesToHex(value));

        BleDevice device = devicePool.get(address);
        BluetoothProtocol protocol = device.getProtocol(characteristicUuid);
        protocol.setValue(value);
    }

    private void onValueWrite(String address, UUID characteristicUuid, int status) {
        Timber.i("write address[%s] char[%s] status[%s]", address, characteristicUuid, status);
    }

    private void onDescriptorRead(String address, UUID characteristicUuid, UUID descriptorUuid, int status) {
        Timber.i("read address[%s] char[%s] desc[%s] status[%s]", address, characteristicUuid, descriptorUuid, status);
    }

    /**
     * 暂时我们应用到的descriptor只有开启通知，不需要额外处理。
     */
    private void onDescriptorWrite(String address, UUID characteristicUuid, UUID descriptorUuid, int status) {
        Timber.i("write address[%s] char[%s] desc[%s] status[%s]", address, characteristicUuid, descriptorUuid, status);

        BleDevice device = devicePool.get(address);
        device.onDescriptorWrite(characteristicUuid, descriptorUuid);
    }

    private void onRssi(String address, int rssi) {
        Timber.i("rssi address[%s] rssi[%s]", address, rssi);

        BleDevice device = devicePool.get(address);
        device.setRssi(rssi);
    }

}
