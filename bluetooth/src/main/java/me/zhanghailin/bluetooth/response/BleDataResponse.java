package me.zhanghailin.bluetooth.response;

import java.util.UUID;

/**
 * 从蓝牙模块接收到的数据
 * Created by zhanghailin on 9/10/15.
 */
public class BleDataResponse {
    public final Type type;
    public final String address;
    public final int status;
    public final int newState;
    public final UUID characteristicUuid;
    public final UUID descriptorUuid;
    public final byte[] value;
    public final int rssi;

    private BleDataResponse(
            Type type, String address, int status, int newState, UUID characteristicUuid,
            UUID descriptorUuid, byte[] value, int rssi) {
        this.type = type;
        this.address = address;
        this.status = status;
        this.newState = newState;
        this.characteristicUuid = characteristicUuid;
        this.descriptorUuid = descriptorUuid;
        this.value = value;
        this.rssi = rssi;
    }

    public enum Type {
        CONNECTION_STATE,
        SERVICE_DISCOVERED,
        VALUE_READ,
        VALUE_WRITE,
        VALUE_NOTIFIED,
        DESCRIPTOR_WRITE,
        RSSI,
        ;
    }

    public static BleDataResponse buildConnectionState(String address, int status, int newState) {
        return new Builder()
                .setAddress(address)
                .setNewState(newState)
                .setStatus(status)
                .build(Type.CONNECTION_STATE);
    }

    public static BleDataResponse buildServiceDiscovered(String address) {
        return new Builder()
                .setAddress(address)
                .build(Type.SERVICE_DISCOVERED);
    }

    public static BleDataResponse buildValueRead(String address, UUID characteristicUuid, byte[] value) {
        return new Builder()
                .setAddress(address)
                .setCharacteristicUuid(characteristicUuid)
                .setValue(value)
                .build(Type.VALUE_READ);
    }

    public static BleDataResponse buildValueWrite(String address, UUID characteristicUuid) {
        return new Builder()
                .setAddress(address)
                .setCharacteristicUuid(characteristicUuid)
                .build(Type.VALUE_WRITE);
    }

    public static BleDataResponse buildValueNotified(String address, UUID characteristicUuid, byte[] value) {
        return new Builder()
                .setAddress(address)
                .setCharacteristicUuid(characteristicUuid)
                .setValue(value)
                .build(Type.VALUE_NOTIFIED);
    }

    public static BleDataResponse buildDescriptorWrite(String address, UUID characteristicUuid,
                                                       UUID descriptorUuid) {
        return new Builder()
                .setAddress(address)
                .setCharacteristicUuid(characteristicUuid)
                .setDescriptorUuid(descriptorUuid)
                .build(Type.DESCRIPTOR_WRITE);
    }

    public static BleDataResponse buildRssi(String address, int rssi) {
        return new Builder()
                .setAddress(address)
                .setRssi(rssi)
                .build(Type.RSSI);
    }

    private static class Builder {
        private String buildingAddress = null;
        private int buildingStatus = Integer.MIN_VALUE;
        private int buildingNewState = Integer.MIN_VALUE;
        private UUID buildingCharacteristicUuid = null;
        private UUID buildingDescriptorUuid = null;
        private byte[] buildingValue = null;
        private int buildingRssi = Integer.MIN_VALUE;

        public BleDataResponse build(Type type) {
            return new BleDataResponse(type,
                    buildingAddress, buildingStatus,buildingNewState, buildingCharacteristicUuid,
                    buildingDescriptorUuid, buildingValue, buildingRssi);
        }

        public Builder setAddress(String address) {
            buildingAddress = address;
            return this;
        }

        public Builder setStatus(int status) {
            buildingStatus = status;
            return this;
        }

        public Builder setNewState(int newState) {
            buildingNewState = newState;
            return this;
        }

        public Builder setCharacteristicUuid(UUID characteristicUuid) {
            buildingCharacteristicUuid = characteristicUuid;
            return this;
        }

        public Builder setDescriptorUuid(UUID descriptorUuid) {
            buildingDescriptorUuid = descriptorUuid;
            return this;
        }

        public Builder setValue(byte[] value) {
            buildingValue = value;
            return this;
        }

        public Builder setRssi(int rssi) {
            buildingRssi = rssi;
            return this;
        }
    }
}
