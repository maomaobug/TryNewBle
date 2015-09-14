package me.zhanghailin.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

import me.zhanghailin.bluetooth.process.BleDataHandler;
import me.zhanghailin.bluetooth.process.BleResponseProcessor;
import me.zhanghailin.bluetooth.response.BleDataDelivery;
import me.zhanghailin.bluetooth.response.HandlerDataDelivery;

public class BleService extends Service {
    private BluetoothAdapter bluetoothAdapter;

    private final IBinder binder = new LocalBinder();

    private final Map<String, StateWrappedGatt> gattPool = new HashMap<>();
    private BleCallback bleCallback;

    public BleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        BleResponseProcessor processor = null; // TODO: 9/10/15  implementation
        BleDataDelivery delivery = new HandlerDataDelivery(new BleDataHandler(getMainLooper(), processor));
        bleCallback = new BleCallback(delivery);
    }

    public void connect(String address) {
        if (gattPool.containsKey(address)) {
            StateWrappedGatt existingGatt = gattPool.get(address);
            existingGatt.gatt.connect();
            return;
        }

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        BluetoothGatt gatt = device.connectGatt(this, true, bleCallback);

        StateWrappedGatt wrappedGatt = new StateWrappedGatt(gatt);
        gattPool.put(address, wrappedGatt);
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
}
