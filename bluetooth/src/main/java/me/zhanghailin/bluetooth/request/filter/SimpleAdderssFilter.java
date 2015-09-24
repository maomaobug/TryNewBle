package me.zhanghailin.bluetooth.request.filter;

import android.text.TextUtils;

import me.zhanghailin.bluetooth.device.BleDevice;
import me.zhanghailin.bluetooth.request.BleDataRequest;

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
    public boolean apply(BleDataRequest request) {
        if (request == null || bleDevice == null) {
            return false;
        }
        String tag = request.getTag();
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        return tag.equals(bleDevice.getAddress());
    }

}
