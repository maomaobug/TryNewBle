package me.zhanghailin.bluetooth.request;

import android.bluetooth.BluetoothGatt;
import android.util.Log;

/**
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/14/15.
 */
public abstract class BleDataRequest {

    private static final String TAG = "BleTaskRequest";

    private final BluetoothGatt gatt;

    private volatile boolean running;

    public BleDataRequest(BluetoothGatt gatt) {
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
        if (interceptExecute()) {
            return true;
        }
        return performExecute(gatt);
    }

    /**
     * 是否中断执行 实际的蓝牙操作
     * <p/>
     * 必要时子类可以重写该函数，eg : test class
     *
     * @return 是否中断
     */
    protected boolean interceptExecute() {
        if (gatt == null) {
            Log.w(TAG, "gatt == null");
            return true;
        }
        return false;
    }

    /**
     * 执行实际的 request 操作， 交给子类实现
     *
     * @param gatt 实际操作蓝牙设备的句柄
     * @return 操作成功 / 失败
     */
    protected abstract boolean performExecute(BluetoothGatt gatt);

}
