package me.zhanghailin.bluetooth.connector;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.connector
 * author shenwenjun
 * Date 9/27/15.
 */
public interface Connector {

    int STATE_NO = 0x0000;
    int STATE_READY_TO_CLOSE = 0x1231;
    int STATE_CONNECTED = 0x1234;
    int STATE_CONNECTING = 0x1235;

    boolean isConnecting();

    boolean isConnected();

    boolean isReadyToClosed();

    void setState(int state);

}
