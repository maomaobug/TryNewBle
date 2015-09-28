package me.zhanghailin.bluetooth.connector;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.connector
 * author shenwenjun
 * Date 9/27/15.
 */
public class DefaultConnector implements Connector {

    private int mState = STATE_NO;

    @Override
    public boolean isConnecting() {
        return mState == STATE_CONNECTING;
    }

    @Override
    public boolean isConnected() {
        return mState == STATE_CONNECTED;
    }

    @Override
    public boolean isReadyToClosed() {
        return mState == STATE_READY_TO_CLOSE;
    }

    @Override
    public void setState(int state) {
        mState = state;
    }

}
