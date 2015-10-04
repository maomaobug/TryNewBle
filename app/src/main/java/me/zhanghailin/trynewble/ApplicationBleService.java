package me.zhanghailin.trynewble;

import android.content.Context;
import android.content.Intent;

import me.zhanghailin.bluetooth.BleService;
import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.device.DevicePool;

public class ApplicationBleService extends BleService {


    private static final String KEY_CONNECT_WAITING_DEVICES = "KEY_CONNECT_WAITING_DEVICES";
    private static final int VALUE_CONNECT_WAITING_DEVICES = 0x2233;

    public static void startReconnectWaitingDevices(Context context) {
        Intent intent = new Intent(context, ApplicationBleService.class);
        intent.putExtra(KEY_CONNECT_WAITING_DEVICES, VALUE_CONNECT_WAITING_DEVICES);
        context.startService(intent);
    }

    public ApplicationBleService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int what = intent.getIntExtra(KEY_CONNECT_WAITING_DEVICES, -1);
        switch (what) {
            case VALUE_CONNECT_WAITING_DEVICES:
                getConnectionManager().connectWaitingDevices();
                break;
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    protected DevicePool createDevicePool(ConnectionManager connectionManager) {
        return new IHereDevicePool(connectionManager);
    }


}
