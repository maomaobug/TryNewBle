package me.zhanghailin.bluetooth;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.connection.DemoConnectionManager;
import me.zhanghailin.bluetooth.device.DevicePool;
import me.zhanghailin.bluetooth.process.BleDataHandler;
import me.zhanghailin.bluetooth.process.BleResponseProcessor;
import me.zhanghailin.bluetooth.response.BleDataDelivery;
import me.zhanghailin.bluetooth.response.HandlerDataDelivery;
import me.zhanghailin.bluetooth.task.ITaskManager;
import me.zhanghailin.bluetooth.task.TaskManager;
import timber.log.Timber;

public abstract class BleService extends Service {
    private final IBinder binder = new LocalBinder();

    private ConnectionManager connectionManager;
    private BleCallback bleCallback;

    private DevicePool devicePool;

    public BleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.d("onCreate");

        ITaskManager taskManager = new TaskManager.Builder().build();

        BleResponseProcessor processor = new BleResponseProcessor();
        BleDataDelivery delivery = new HandlerDataDelivery(new BleDataHandler(getMainLooper(), processor));
        bleCallback = new BleCallback(delivery);
        connectionManager = new DemoConnectionManager(bleCallback, taskManager, this);


        devicePool = createDevicePool(connectionManager);
        connectionManager.setDevicePool(devicePool);

        processor.setConnectionManager(connectionManager);
        processor.setDevicePool(devicePool);

        connectAllRecorded();

    }

    protected void connectAllRecorded() {
        connect(DemoConstants.ADDR);
        connect(DemoConstants.ADDR_1);
        connect(DemoConstants.ADDR_3);
        connect(DemoConstants.ADDR_4);
    }

    abstract protected DevicePool createDevicePool(ConnectionManager connectionManager);

    public void connect(String address) {
        try {
            connectionManager.connect(address);
        } catch (Exception e) {
            // TODO: Handle if bluetooth is not on
        }
    }

    public void disconnect(String address) {
        try {
            connectionManager.disconnect(address);
        } catch (Exception e) {
            // TODO: Handle if bluetooth is not on
        }
    }

    public DevicePool getDevicePool() {
        return devicePool;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public BleService getService() {
            return BleService.this;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        connectionManager.clearAll();
    }
}
