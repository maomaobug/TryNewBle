package me.zhanghailin.bluetooth.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.zhanghailin.bluetooth.device.BleDevice;
import me.zhanghailin.bluetooth.device.DevicePool;
import me.zhanghailin.bluetooth.request.BleDataRequest;
import me.zhanghailin.bluetooth.request.ConnectRequest;
import me.zhanghailin.bluetooth.request.ITaskRequest;
import me.zhanghailin.bluetooth.request.ReconnectRequest;
import me.zhanghailin.bluetooth.request.filter.SimpleAdderssFilter;
import me.zhanghailin.bluetooth.task.ITaskManager;
import me.zhanghailin.bluetooth.task.TaskManager;
import me.zhanghailin.bluetooth.task.executor.ConnectTaskExecutor;
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

    private Set<String> waitingDeviceAddress = new HashSet<>();

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
                .setTaskExecutor(new ConnectTaskExecutor(this))
                .build();
    }

    @Override
    public void nextConnectionOperation() {
        connectionManager.finishTaskWithRunNext();
    }

    @Override
    public void nextOperation() {
        dataManager.finishTaskWithRunNext();
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
        if (waitingDeviceAddress.size() == 0) {
            Timber.d("no waiting devices");
            return;
        }
        String[] waitingDeviceAddress = this.waitingDeviceAddress.toArray(new String[1]);
        this.waitingDeviceAddress.clear();
        for (String address : waitingDeviceAddress) {
            Timber.d("connect waiting device [%s]", address);
            addNewDevice(address);
        }
    }

    @Override
    public void saveWaitingDevices() {

        List<ITaskRequest> requests = connectionManager.listTask();

        for (ITaskRequest request : requests) {
            if (request == null || request.tag() == null) {
                continue;
            }
            addWaitingDevice(request.tag());
        }
    }

    @Override
    public void addWaitingDevice(String address) {
        Timber.d("exec add device to waiting list address[%s]", address);

        waitingDeviceAddress.add(address);
    }

    @Override
    public void removeWaitingDevice(String address) {
        waitingDeviceAddress.remove(address);
    }

    @Override
    public void clearAll() {
        waitingDeviceAddress.clear();

        connectionManager.cancelAllTask();

        dataManager.cancelAllTask();

        devicePool.closeAll();
    }

    @Override
    public void close(String address) {
        BleDevice bleDevice = devicePool.buildNewDevice(bluetoothAdapter, address);

        connectionManager.cancelTask(new SimpleAdderssFilter(bleDevice));
        removeWaitingDevice(address);

        bleDevice.closeSafely();
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
