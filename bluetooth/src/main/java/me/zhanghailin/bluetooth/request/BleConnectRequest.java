package me.zhanghailin.bluetooth.request;

/**
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/28/15.
 */
public class BleConnectRequest implements ITaskRequest {

    @Override
    public void setRunning(boolean running) {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public String tag() {
        return null;
    }

    @Override
    public boolean execute() {
        return false;
    }

}
