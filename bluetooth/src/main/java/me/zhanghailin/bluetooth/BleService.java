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
        BleCallback bleCallback = new BleCallback(delivery);
        connectionManager = new DemoConnectionManager(bleCallback, taskManager, this);

        devicePool = createDevicePool(connectionManager);
        connectionManager.setDevicePool(devicePool);

        processor.setConnectionManager(connectionManager);
        processor.setDevicePool(devicePool);
    }

    abstract protected DevicePool createDevicePool(ConnectionManager connectionManager);

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void addDevice(String address) {
        connectionManager.addNewDevice(address);
    }

    public void autoReconnect(String address) {
        connectionManager.reconnect(address);
    }

    public void enqueueDisconnect(String address) {
        connectionManager.close(address);
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
