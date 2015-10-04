package me.zhanghailin.trynewble;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * TryNewBle
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
            }
        }
    }

    /**
     * 蓝牙打开时，尝试重连断开的设备
     */
    private void performBleOn(Context context) {
        ApplicationBleService.startReconnectWaitingDevices(context);
    }

}
