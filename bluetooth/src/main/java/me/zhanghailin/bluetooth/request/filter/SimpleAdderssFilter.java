package me.zhanghailin.bluetooth.request.filter;

import android.text.TextUtils;

import me.zhanghailin.bluetooth.device.BleDevice;
import me.zhanghailin.bluetooth.request.ITaskRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.request.filter
 * author shenwenjun
 * Date 9/16/15.
 */
public class SimpleAdderssFilter implements RequestFilter {

    private BleDevice bleDevice;

    public SimpleAdderssFilter(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    @Override
    public boolean apply(ITaskRequest request) {
        if (request == null || bleDevice == null) {
            return false;
        }
        String tag = request.tag();
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        return tag.equals(bleDevice.getAddress());
    }

}
