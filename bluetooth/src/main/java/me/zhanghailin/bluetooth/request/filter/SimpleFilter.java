package me.zhanghailin.bluetooth.request.filter;

import me.zhanghailin.bluetooth.request.BleTaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.request.filter
 * author shenwenjun
 * Date 9/16/15.
 */
public class SimpleFilter implements RequestFilter {

    @Override
    public boolean through(BleTaskRequest request) {
        return false;
    }

}
