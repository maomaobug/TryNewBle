package me.zhanghailin.bluetooth.device;

import java.util.List;
import java.util.UUID;

import me.zhanghailin.bluetooth.protocol.BluetoothProtocol;

/**
 * 代表一个设备包含的所有Protocol，发送蓝牙命令，存储所有接收到的数据，实现中可以由这里向外部发出model改变的事件。
 * Created by zhanghailin on 9/14/15.
 */
public interface BleDevice {

    // 状态相关
    void setRssi(int rssi);

    BluetoothProtocol getProtocol(UUID characteristicUuid);

    List<BluetoothProtocol> getAllProtocols();

    void newState(int newState);

    // 命令相关
    void initProtocols();

    void clearAndReconnect();

    void discoverServices();
}
