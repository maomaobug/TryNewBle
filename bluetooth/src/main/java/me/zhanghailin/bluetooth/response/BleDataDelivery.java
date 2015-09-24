package me.zhanghailin.bluetooth.response;

import java.util.UUID;

/**
 * 负责分发从系统蓝牙模块接收到的回调信息
 * Created by zhanghailin on 9/10/15.
 */
public interface BleDataDelivery {

    void onNewConnectionState(String address, int status, int newState);

    void onServiceDiscovered(String address);

    void onValueRead(String address, UUID characteristicUuid, byte[] value);

    void onValueWrite(String address, UUID characteristicUuid);

    void onValueNotified(String address, UUID characteristicUuid, byte[] value);

    void onRssi(String address, int rssi);

    void onDescriptorWrite(String address, UUID characteristicUuid, UUID descriptorUuid);
}
