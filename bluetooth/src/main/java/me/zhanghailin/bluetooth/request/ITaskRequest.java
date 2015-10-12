package me.zhanghailin.bluetooth.request;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/28/15.
 */
public interface ITaskRequest {

    /**
     * 标识一个 request 的tag ， 一般用发起这个 request 的 device 的 mac 地址
     *
     * @return device 的 mac 地址
     */
    String tag();

    boolean execute();

}
