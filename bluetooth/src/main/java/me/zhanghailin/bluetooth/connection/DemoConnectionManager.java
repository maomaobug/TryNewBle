package me.zhanghailin.bluetooth.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import me.zhanghailin.bluetooth.connector.Connector;
import me.zhanghailin.bluetooth.device.BleDevice;
import me.zhanghailin.bluetooth.device.DevicePool;
import me.zhanghailin.bluetooth.request.BleDataRequest;
import me.zhanghailin.bluetooth.task.ITaskManager;
import me.zhanghailin.bluetooth.task.TaskManager;
import me.zhanghailin.bluetooth.task.timer.EmptyTimer;
import timber.log.Timber;

/**
 * Created by zhanghailin on 9/17/15.
 */
@SuppressWarnings("deprecation")
public class DemoConnectionManager implements ConnectionManager {
    private DevicePool devicePool;
    private final BluetoothGattCallback callback;

    private BluetoothAdapter bluetoothAdapter;
    private Context applicationContext;

    private ITaskManager taskManager;

    private ITaskManager connectTaskManager;

    public DemoConnectionManager(BluetoothGattCallback callback, ITaskManager taskManager, Context context) {
        this.callback = callback;

        applicationContext = context.getApplicationContext();

        BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        this.taskManager = taskManager;

        connectTaskManager = new TaskManager.Builder()
                .setTimeoutTimer(new EmptyTimer())
                .build();
    }

    @Override
    public void nextOperation() {
        taskManager.finishTask();
    }

    @Override
    public void enQueueRequest(BleDataRequest request) {
        taskManager.submitTask(request);
    }

    @Override
    public void connect(String address) {
        // 直接连接某个设备
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        BluetoothGatt gatt = device.connectGatt(applicationContext, false, callback);
        devicePool.buildNewDevice(gatt);
    }

    @Override
    public void disconnect(String address) {
//        BleDevice bleDevice = devicePool.get(address);
    }

    @Override
    public void clearAll() {
        taskManager.cancelAllTask();

        devicePool.closeAll();
    }

    @Override
    public void enQueueDisconnect(String address) {
        // TODO: 9/28/15
    }

    @Override
    public void enQueueConnect(String address) {
        // 向手机注册连接请求
        BleDevice bleDevice = devicePool.get(address);
        if (!bleDevice.getConnector().isConnecting()) {
            bleDevice.getConnector().setState(Connector.STATE_CONNECTING);
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            BluetoothGatt gatt = device.connectGatt(applicationContext, true, callback);
            bleDevice.setGatt(gatt);

            Timber.d("enQueueConnect address : %s", address);
        }

        // TODO: 9/28/15
//        添加连接队列， 同一时间只发起一个 连接请求，
//        收到回调后， 发起下一个 连接请求。
//        若 回调连接成功，则不做其他附加操作。
//        若 回调连接失败，则将当前连接请求重新加入队列尾， 等待下次连接
    }

    @Override
    public void setDevicePool(DevicePool devicePool) {
        this.devicePool = devicePool;
    }
}
