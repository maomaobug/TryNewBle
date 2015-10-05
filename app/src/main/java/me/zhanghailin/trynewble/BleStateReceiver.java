package me.zhanghailin.trynewble;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * fixme 应该移入 bluetooth 模块， 但是由于 依赖了 BleService 的实现， 不能移入 library
 * fixme 这里先做 demo， 后续修改
 * package me.zhanghailin.bluetooth
 * author shenwenjun
 * Date 10/3/15.
 */
public class BleStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            if (state == BluetoothAdapter.STATE_ON) {
                performBleOn(context);
            } else if (state == BluetoothAdapter.STATE_OFF) {
                performBleOff(context);
            }
        }
    }

    /**
     * 蓝牙打开时，尝试重连断开的设备
     */
    private void performBleOn(Context context) {
        ApplicationBleService.startReconnectWaitingDevices(context);
    }

    /**
     * 蓝牙关闭时， 记录所有请求队列中的设备， 以备打开蓝牙时尝试重连
     */
    private void performBleOff(Context context) {
        ApplicationBleService.startSaveDevicesToWaiting(context);
    }

}
