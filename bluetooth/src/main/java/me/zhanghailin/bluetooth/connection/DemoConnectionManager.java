package me.zhanghailin.bluetooth.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.util.HashSet;
import java.util.Set;

import me.zhanghailin.bluetooth.device.BleDevice;
import me.zhanghailin.bluetooth.device.DevicePool;
import me.zhanghailin.bluetooth.request.BleDataRequest;
import me.zhanghailin.bluetooth.request.ConnectRequest;
import me.zhanghailin.bluetooth.request.ITaskRequest;
import me.zhanghailin.bluetooth.request.ReconnectRequest;
import me.zhanghailin.bluetooth.task.ITaskManager;
import me.zhanghailin.bluetooth.task.TaskManager;
import me.zhanghailin.bluetooth.task.queue.HashTaskQueue;
import me.zhanghailin.bluetooth.task.timer.EmptyTimer;
import timber.log.Timber;

/**
 * Created by zhanghailin on 9/17/15.
 */
@SuppressWarnings("deprecation")
public class DemoConnectionManager implements ConnectionManager {

    private Context applicationContext;

    private DevicePool devicePool;

    private final BluetoothGattCallback callback;

    private BluetoothAdapter bluetoothAdapter;

    private ITaskManager dataManager;

    private ITaskManager connectionManager;

    private Set<String> failedAddress = new HashSet<>();

    public DemoConnectionManager(BluetoothGattCallback callback, ITaskManager dataManager, Context context) {
        this.callback = callback;

        applicationContext = context.getApplicationContext();

        BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        this.dataManager = dataManager;

        connectionManager = new TaskManager.Builder()
                .setTaskQueue(new HashTaskQueue())
                .setTimeoutTimer(new EmptyTimer())
                .build();
    }

    @Override
    public void nextConnectionOperation() {
        connectionManager.finishTask();
    }

    @Override
    public void nextOperation() {
        dataManager.finishTask();
    }

    @Override
    public void enQueueRequest(BleDataRequest request) {
        dataManager.submitTask(request);
    }

    @Override
    public void addNewDevice(String address) {
        // 直接连接某个设备
        BleDevice bleDevice = devicePool.buildNewDevice(bluetoothAdapter, address);
        ITaskRequest connect = new ConnectRequest(applicationContext, bleDevice, callback);
        connectionManager.submitTask(connect);
    }

    @Override
    public void connectWaitingDevices() {
        if (failedAddress.size() == 0) {
            Timber.d("no waiting devices");
            return;
        }
        String[] waitingDeviceAddress = failedAddress.toArray(new String[1]);
        failedAddress.clear();
        for (String address : waitingDeviceAddress) {
            Timber.d("connect waiting device [%s]", address);
            addNewDevice(address);
        }
    }

    @Override
    public void addWaitingDevice(String address) {
        failedAddress.add(address);
    }

    @Override
    public void removeWaitingDevice(String address) {
        failedAddress.remove(address);
    }

    @Override
    public void clearAll() {
        failedAddress.clear();

        connectionManager.cancelAllTask();

        dataManager.cancelAllTask();

        devicePool.closeAll();
    }

    @Override
    public void close(String address) {
        BleDevice bleDevice = devicePool.buildNewDevice(bluetoothAdapter, address);
        bleDevice.getGatt().close();
    }

    @Override
    public void reconnect(String address) {
        BleDevice bleDevice = devicePool.buildNewDevice(bluetoothAdapter, address);
        ITaskRequest connect = new ReconnectRequest(applicationContext, bleDevice, callback);
        connectionManager.submitTask(connect);
    }

    @Override
    public void setDevicePool(DevicePool devicePool) {
        this.devicePool = devicePool;
    }

    @Override
    public ITaskRequest currentTask() {
        return connectionManager.currentTask();
    }

}
