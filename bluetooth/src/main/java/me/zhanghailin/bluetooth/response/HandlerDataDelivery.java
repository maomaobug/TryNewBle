package me.zhanghailin.bluetooth.response;

import android.os.Handler;
import android.os.Message;

import java.util.UUID;

/**
 * 将所有Response 发送到Handler上
 * Created by zhanghailin on 9/10/15.
 */
public class HandlerDataDelivery implements BleDataDelivery {

    private final Handler handler;

    public HandlerDataDelivery(Handler handler) {
        this.handler = handler;
    }

    private void deliverResponse(BleDataResponse response) {
        Message message = handler.obtainMessage();
        message.obj = response;
        message.sendToTarget();
    }

    @Override
    public void onNewConnectionState(String address, int status, int newState) {
        BleDataResponse response = BleDataResponse.buildConnectionState(address, status, newState);
        deliverResponse(response);
    }

    @Override
    public void onServiceDiscovered(String address) {
        BleDataResponse response = BleDataResponse.buildServiceDiscovered(address);
        deliverResponse(response);
    }

    @Override
    public void onValueRead(String address, UUID characteristicUuid, byte[] value) {
        BleDataResponse response =
                BleDataResponse.buildValueRead(address, characteristicUuid, value);
        deliverResponse(response);
    }

    @Override
    public void onValueWrite(String address, UUID characteristicUuid) {
        BleDataResponse response = BleDataResponse.buildValueWrite(address, characteristicUuid);
        deliverResponse(response);
    }

    @Override
    public void onValueNotified(String address, UUID characteristicUuid, byte[] value) {
        BleDataResponse response =
                BleDataResponse.buildValueNotified(address, characteristicUuid, value);
        deliverResponse(response);
    }

    @Override
    public void onRssi(String address, int rssi) {
        BleDataResponse response = BleDataResponse.buildRssi(address, rssi);
        deliverResponse(response);
    }
}
