package me.zhanghailin.bluetooth.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import me.zhanghailin.bluetooth.device.DevicePool;
import me.zhanghailin.bluetooth.request.BleDataRequest;
import me.zhanghailin.bluetooth.task.ITaskManager;

/**
 * Created by zhanghailin on 9/17/15.
 */
public class DemoConnectionManager implements ConnectionManager {
    private DevicePool devicePool;
    private final BluetoothGattCallback callback;

    private BluetoothAdapter bluetoothAdapter;
    private Context applicationContext;

    private ITaskManager taskManager;

    public DemoConnectionManager(BluetoothGattCallback callback, ITaskManager taskManager, Context context) {
        this.callback = callback;

        applicationContext = context.getApplicationContext();

        BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        this.taskManager = taskManager;
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
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        BluetoothGatt gatt = device.connectGatt(applicationContext, true, callback);
        devicePool.buildNewDevice(gatt);
    }

    @Override
    public void clearAll() {
        taskManager.cancelAllTask();
    }

    @Override
    public void enQueueDisconnect(String address) {

    }

    @Override
    public void enQueueConnect(String address) {
        //xxx 为什么提供 入队连接 的接口， 与上面的 connect 有什么区别
        //xxx 另外当时讨论的结果是 连接 和 断连 不入队的吧？
    }

    @Override
    public void setDevicePool(DevicePool devicePool) {
        this.devicePool = devicePool;
    }
}
