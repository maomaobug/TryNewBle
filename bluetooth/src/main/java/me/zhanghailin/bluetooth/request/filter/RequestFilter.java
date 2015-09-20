package me.zhanghailin.bluetooth.request.filter;

import me.zhanghailin.bluetooth.request.BleTaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/14/15.
 */
public interface RequestFilter {

    boolean through(BleTaskRequest request);

}
