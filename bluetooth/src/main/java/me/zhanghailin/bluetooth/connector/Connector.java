package me.zhanghailin.bluetooth.connector;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.connector
 * author shenwenjun
 * Date 9/27/15.
 */
public interface Connector {

    int STATE_NO = 0x0000;
    int STATE_CONNECTED = 0x1234;
    int STATE_ERROR = 0x1236;

    boolean isConnected();

    boolean isError();

    void setState(int state);

}
