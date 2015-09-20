package me.zhanghailin.bluetooth.request;

import android.bluetooth.BluetoothGatt;
import android.util.Log;

/**
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/14/15.
 */
public abstract class BleTaskRequest {

    private static final String TAG = "BleTaskRequest";

    private final BluetoothGatt gatt;

    private volatile boolean running;

    public BleTaskRequest(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * 执行成功 或 失败
     *
     * @return 成功 或 失败
     */
    public boolean execute() {
        if (gatt == null) {
            Log.w(TAG, "gatt == null");
//            return false; // FIXME: 9/18/15
        }
        return performExecute(gatt);
    }

    /**
     * 执行实际的 request 操作， 交给子类实现
     *
     * @param gatt 实际操作蓝牙设备的句柄
     * @return 操作成功 / 失败
     */
    public abstract boolean performExecute(BluetoothGatt gatt);

}
