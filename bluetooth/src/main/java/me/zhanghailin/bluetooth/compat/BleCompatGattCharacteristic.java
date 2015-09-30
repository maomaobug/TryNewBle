package me.zhanghailin.bluetooth.compat;

import java.util.UUID;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.compat
 * author shenwenjun
 * Date 9/29/15.
 */
public class BleCompatGattCharacteristic {

    public static final int PROPERTY_READ = 2;
    public static final int PROPERTY_WRITE = 8;
    public static final int PROPERTY_NOTIFY = 16;
    public static final int PROPERTY_INDICATE = 32;

    /**
     * Characteristic value format type uint8
     */
    public static final int FORMAT_UINT8 = 0x11;

    /**
     * Characteristic value format type uint16
     */
    public static final int FORMAT_UINT16 = 0x12;

    /**
     * Characteristic value format type uint24 Note: this is not a standard data
     * type!
     */
    public static final int FORMAT_UINT24 = 0x13;

    /**
     * Characteristic value format type uint32
     */
    public static final int FORMAT_UINT32 = 0x14;

    /**
     * Characteristic value format type sint8
     */
    public static final int FORMAT_SINT8 = 0x21;

    /**
     * Characteristic value format type sint16
     */
    public static final int FORMAT_SINT16 = 0x22;

    /**
     * Characteristic value format type sint32
     */
    public static final int FORMAT_SINT32 = 0x24;

    /**
     * Characteristic value format type sfloat (16-bit float)
     */
    public static final int FORMAT_SFLOAT = 0x32;

    /**
     * Characteristic value format type float (32-bit float)
     */
    public static final int FORMAT_FLOAT = 0x34;

    private android.bluetooth.BluetoothGattCharacteristic mGattCharacteristicA;
    private com.broadcom.bt.gatt.BluetoothGattCharacteristic mGattCharacteristicB;
    private com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic mGattCharacteristicS;
    private BleCompatSDK mBleSDK;
    private String name;


    protected android.bluetooth.BluetoothGattCharacteristic getGattCharacteristicA() {
        return mGattCharacteristicA;
    }

    protected com.broadcom.bt.gatt.BluetoothGattCharacteristic getGattCharacteristicB() {
        return mGattCharacteristicB;
    }

    protected com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic getGattCharacteristicS() {
        return mGattCharacteristicS;
    }

    protected void setGattCharacteristicA(
            android.bluetooth.BluetoothGattCharacteristic mGattCharacteristicA) {
        this.mGattCharacteristicA = mGattCharacteristicA;
    }

    protected void setGattCharacteristicB(
            com.broadcom.bt.gatt.BluetoothGattCharacteristic mBCGattCharacteristic) {
        this.mGattCharacteristicB = mBCGattCharacteristic;
    }

    protected void setGattCharacteristicS(
            com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic mSSGattCharacteristic) {
        this.mGattCharacteristicS = mSSGattCharacteristic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public BleCompatGattCharacteristic(android.bluetooth.BluetoothGattCharacteristic c) {
        mBleSDK = BleCompatSDK.ANDROID;
        setGattCharacteristicA(c);
        initInfo();
    }

    public BleCompatGattCharacteristic(
            com.broadcom.bt.gatt.BluetoothGattCharacteristic c) {
        mBleSDK = BleCompatSDK.BROADCOM;
        setGattCharacteristicB(c);
    }

    public BleCompatGattCharacteristic(
            com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic c) {
        mBleSDK = BleCompatSDK.SAMSUNG;
        setGattCharacteristicS(c);
    }

    private void initInfo() {
        name = "Unknown characteristic";
    }

    public UUID getUuid() {
        if (mBleSDK == BleCompatSDK.ANDROID) {
            return getGattCharacteristicA().getUuid();
        } else if (mBleSDK == BleCompatSDK.BROADCOM) {
            return getGattCharacteristicB().getUuid();
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            return getGattCharacteristicS().getUuid();
        }

        return null;
    }

    public int getProperties() {
        if (mBleSDK == BleCompatSDK.ANDROID) {
            return getGattCharacteristicA().getProperties();
        } else if (mBleSDK == BleCompatSDK.BROADCOM) {
            return getGattCharacteristicB().getProperties();
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            return getGattCharacteristicS().getProperties();
        }

        return 0;
    }

    public boolean setValue(byte[] val) {
        if (mBleSDK == BleCompatSDK.ANDROID) {
            return getGattCharacteristicA().setValue(val);
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            return mGattCharacteristicS.setValue(val);
        } else if (mBleSDK == BleCompatSDK.BROADCOM) {
            return mGattCharacteristicB.setValue(val);
        }

        return false;
    }

    public byte[] getValue() {
        if (mBleSDK == BleCompatSDK.ANDROID) {
            return getGattCharacteristicA().getValue();
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            return mGattCharacteristicS.getValue();
        } else if (mBleSDK == BleCompatSDK.BROADCOM) {
            return mGattCharacteristicB.getValue();
        }

        return null;
    }

    public boolean setValue(int value, int formatType, int offset) {
        if (mBleSDK == BleCompatSDK.ANDROID) {
            return getGattCharacteristicA().setValue(value, formatType, offset);
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            return mGattCharacteristicS.setValue(value, formatType, offset);
        } else if (mBleSDK == BleCompatSDK.BROADCOM) {
            return mGattCharacteristicB.setValue(value, formatType, offset);
        }

        return false;
    }

    public boolean setValue(int mantissa, int exponent, int formatType,
                            int offset) {
        if (mBleSDK == BleCompatSDK.ANDROID) {
            return getGattCharacteristicA().setValue(mantissa, exponent,
                    formatType, offset);
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            return mGattCharacteristicS.setValue(mantissa, exponent,
                    formatType, offset);
        } else if (mBleSDK == BleCompatSDK.BROADCOM) {
            return mGattCharacteristicB.setValue(mantissa, exponent,
                    formatType, offset);
        }

        return false;
    }

    public boolean setValue(String value) {
        return setValue(value.getBytes());
    }

    public String getStringValue(int offset) {
        if (mBleSDK == BleCompatSDK.ANDROID) {
            return getGattCharacteristicA().getStringValue(offset);
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            return mGattCharacteristicS.getStringValue(offset);
        } else if (mBleSDK == BleCompatSDK.BROADCOM) {
            return mGattCharacteristicB.getStringValue(offset);
        }

        return null;
    }

    public Float getFloatValue(int formatType, int offset) {
        if (mBleSDK == BleCompatSDK.ANDROID) {
            return getGattCharacteristicA().getFloatValue(formatType, offset);
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            return mGattCharacteristicS.getFloatValue(formatType, offset);
        } else if (mBleSDK == BleCompatSDK.BROADCOM) {
            return mGattCharacteristicB.getFloatValue(formatType, offset);
        }

        return null;
    }

    public Integer getIntValue(int formatType, int offset) {
        if (mBleSDK == BleCompatSDK.ANDROID) {
            if (formatType == FORMAT_UINT24) {
                byte[] value = getGattCharacteristicA().getValue();
                return byte2uint24(offset, value);
            } else {
                return getGattCharacteristicA().getIntValue(formatType, offset);
            }
        } else if (mBleSDK == BleCompatSDK.SAMSUNG) {
            if (formatType == FORMAT_UINT24) {
                byte[] value = mGattCharacteristicS.getValue();
                return byte2uint24(offset, value);
            } else {
                return mGattCharacteristicS.getIntValue(formatType, offset);
            }
        } else if (mBleSDK == BleCompatSDK.BROADCOM) {
            if (formatType == FORMAT_UINT24) {
                byte[] value = mGattCharacteristicB.getValue();
                return byte2uint24(offset, value);
            } else {
                return mGattCharacteristicB.getIntValue(formatType, offset);
            }
        }

        return null;
    }

    private Integer byte2uint24(int offset, byte[] value) {
        if ((offset + 3) > value.length)
            return null;
        return (value[offset] & 0xFF)
                | (value[offset + 1] & 0xFF) << 8
                | (value[offset + 2] & 0xFF) << 16;
    }

}
