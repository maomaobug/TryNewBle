package me.zhanghailin.bluetooth.process;

import android.bluetooth.BluetoothProfile;

import java.util.UUID;

import me.zhanghailin.bluetooth.device.BleDevice;
import me.zhanghailin.bluetooth.device.DevicePool;
import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;
import me.zhanghailin.bluetooth.response.BleDataResponse;

/**
 * 所有操作都在MainThread
 * Created by zhanghailin on 9/14/15.
 */
public class BleResponseProcessorImp implements BleResponseProcessor {

    DevicePool devicePool;

    @Override
    public void processResponse(BleDataResponse response) {
        if (response == null) {
            return;
        }

        switch (response.type) {
            case CONNECTION_STATE:
                onConnectionChange(response.address, response.status, response.newState);
                break;
            case SERVICE_DISCOVERED:
                onServiceDiscovered(response.address);
                break;
            case VALUE_READ:
                onNewValue(response.address, response.characteristicUuid, response.value);
                break;
            case VALUE_WRITE:
                onValueWrite(response.address, response.characteristicUuid);
                break;
            case VALUE_NOTIFIED:
                onNewValue(response.address, response.characteristicUuid, response.value);
                break;
            case RSSI:
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

        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            switch (status) {
                case 133:
                    device.clearAndReconnect();
                    break;
                default:
                    /* no-op */
            }
        } else if (newState == BluetoothProfile.STATE_CONNECTED) {
            device.discoverServices();
        }
    }

    private void onServiceDiscovered(String address) {
        BleDevice device = devicePool.get(address);
        device.initProtocols();
    }

    private void onNewValue(String address, UUID characteristicUuid, byte[] value) {
        BleDevice device = devicePool.get(address);
        BluetoothProtocol protocol = device.getProtocol(characteristicUuid);
        protocol.setValue(value);
    }

    private void onValueWrite(String address, UUID characteristicUuid) {

    }

    private void onRssi(String address, int rssi) {
        BleDevice device = devicePool.get(address);
        device.setRssi(rssi);
    }

}
