package me.zhanghailin.trynewble;

import android.content.Context;
import android.content.Intent;

import me.zhanghailin.bluetooth.BleService;
import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.device.DevicePool;

/**
 * fixme bluetooth library 中需要通过 command 与 service 沟通
 * fixme 这里最好不用继承的方式实现 device pool 的创建
 */
public class ApplicationBleService extends BleService {

    private static final String KEY_WHAT = "KEY_WHAT";
    private static final int WHAT_CONNECT_WAITING_DEVICES = 0x2233;
    private static final int WHAT_SAVE_WAITING_DEVICES = 0x2234;

    public static void startReconnectWaitingDevices(Context context) {
        startCommandByWhat(context, WHAT_CONNECT_WAITING_DEVICES);
    }

    public static void startSaveDevicesToWaiting(Context context) {
        startCommandByWhat(context, WHAT_SAVE_WAITING_DEVICES);
    }

    private static void startCommandByWhat(Context context, int what) {
        Intent intent = new Intent(context, ApplicationBleService.class);
        intent.putExtra(KEY_WHAT, what);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int what = intent.getIntExtra(KEY_WHAT, -1);
        switch (what) {
            case WHAT_CONNECT_WAITING_DEVICES:
                getConnectionManager().connectWaitingDevices();
                break;
            case WHAT_SAVE_WAITING_DEVICES:
                getConnectionManager().saveWaitingDevices();
                break;
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    protected DevicePool createDevicePool(ConnectionManager connectionManager) {
        return new IHereDevicePool(connectionManager);
    }


}
