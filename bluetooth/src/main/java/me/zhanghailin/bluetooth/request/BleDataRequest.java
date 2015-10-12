package me.zhanghailin.bluetooth.request;

import android.bluetooth.BluetoothGatt;
import android.util.Log;

/**
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/14/15.
 */
public abstract class BleDataRequest implements ITaskRequest {

    private static final String TAG = "BleTaskRequest";

    private final BluetoothGatt gatt;

    public BleDataRequest(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    /**
     * 用于 RequestFilter 判断是否需要取消这个 request
     *
     * @return tag 内容 eg: BleDevice address
     */
    @Override
    public String tag() {
        if (gatt == null) {
            return null;
        }
        return gatt.getDevice().getAddress();
    }

    /**
     * 执行成功 或 失败
     *
     * @return 成功 或 失败
     */
    public boolean execute() {
        if (interceptExecute()) {
            return false;
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
