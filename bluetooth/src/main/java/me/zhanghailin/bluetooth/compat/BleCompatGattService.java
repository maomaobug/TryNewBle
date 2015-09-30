package me.zhanghailin.bluetooth.compat;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.compat
 * author shenwenjun
 * Date 9/29/15.
 */
public class BleCompatGattService {

    private BleCompatSDK mBleSDK;
    private com.samsung.android.sdk.bt.gatt.BluetoothGattService mGattServiceS;
    private com.broadcom.bt.gatt.BluetoothGattService mGattServiceB;
    private android.bluetooth.BluetoothGattService mGattServiceA;
    private String mName;

    public BleCompatGattService(com.samsung.android.sdk.bt.gatt.BluetoothGattService s) {
        mBleSDK = BleCompatSDK.SAMSUNG;
        mGattServiceS = s;
        initInfo();
    }

    public BleCompatGattService(com.broadcom.bt.gatt.BluetoothGattService s) {
        mBleSDK = BleCompatSDK.BROADCOM;
        mGattServiceB = s;
        initInfo();
    }

    public BleCompatGattService(android.bluetooth.BluetoothGattService s) {
        mBleSDK = BleCompatSDK.ANDROID;
        mGattServiceA = s;
        initInfo();
    }

    private void initInfo() {
        mName = "Unknown Service";
    }

    public UUID getUuid() {
        if (mBleSDK == BleCompatSDK.BROADCOM) {
            return mGattServiceB.getUuid();
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            return mGattServiceS.getUuid();
        } else if (mBleSDK == BleCompatSDK.ANDROID) {
            return mGattServiceA.getUuid();
        }

        return null;
    }

    public List<BleCompatGattCharacteristic> getCharacteristics() {
        ArrayList<BleCompatGattCharacteristic> list = new ArrayList<BleCompatGattCharacteristic>();
        if (mBleSDK == BleCompatSDK.BROADCOM) {
            for (com.broadcom.bt.gatt.BluetoothGattCharacteristic c : mGattServiceB
                    .getCharacteristics()) {
                list.add(new BleCompatGattCharacteristic(c));
            }
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            for (Object o : mGattServiceS.getCharacteristics()) {
                com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic c = (com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic) o;
                list.add(new BleCompatGattCharacteristic(c));
            }
        } else if (mBleSDK == BleCompatSDK.ANDROID) {
            for (android.bluetooth.BluetoothGattCharacteristic c : mGattServiceA
                    .getCharacteristics()) {
                list.add(new BleCompatGattCharacteristic(c));
            }
        }

        return list;
    }

    public BleCompatGattCharacteristic getCharacteristic(UUID uuid) {
        if (mBleSDK == BleCompatSDK.ANDROID) {
            BluetoothGattCharacteristic c = mGattServiceA
                    .getCharacteristic(uuid);
            if (c != null) {
                return new BleCompatGattCharacteristic(c);
            }
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic c = mGattServiceS
                    .getCharacteristic(uuid);
            if (c != null) {
                return new BleCompatGattCharacteristic(c);
            }
        } else if (mBleSDK == BleCompatSDK.BROADCOM) {
            com.broadcom.bt.gatt.BluetoothGattCharacteristic c = mGattServiceB
                    .getCharacteristic(uuid);
            if (c != null) {
                return new BleCompatGattCharacteristic(c);
            }
        }

        return null;
    }

//    public void setInfo(JSONObject info) {
//        if (info == null) {
//            return;
//        }
//
//        try {
//            setName(info.getString("name"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String getName() {
//        return mName;
//    }
//
//    public void setName(String mName) {
//        this.mName = mName;
//    }

}
