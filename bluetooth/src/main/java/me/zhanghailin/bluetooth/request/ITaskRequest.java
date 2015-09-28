package me.zhanghailin.bluetooth.request;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/28/15.
 */
public interface ITaskRequest {

    void setRunning(boolean running);

    boolean isRunning();

    String tag();

    boolean execute();

}
