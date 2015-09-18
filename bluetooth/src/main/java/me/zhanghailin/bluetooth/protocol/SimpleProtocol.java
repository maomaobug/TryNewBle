package me.zhanghailin.bluetooth.protocol;

/**
 * Created by zhanghailin on 9/18/15.
 */
public abstract class SimpleProtocol implements BluetoothProtocol{
    @Override
    public void setValue(byte[] value) {
        /* no-op */
    }

    @Override
    public byte[] getValue() {
        return null;
    }

    @Override
    public boolean canNotify() {
        return false;
    }

    @Override
    public boolean canRead() {
        return false;
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public boolean canReliableWrite() {
        return false;
    }

    @Override
    public long getLastUpdateTime() {
        return 0;
    }
}
