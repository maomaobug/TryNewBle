package me.zhanghailin.trynewble;

import me.zhanghailin.bluetooth.BleService;
import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.device.DevicePool;

public class ApplicationBleService extends BleService {
    public ApplicationBleService() {
    }

    @Override
    protected DevicePool createDevicePool(ConnectionManager connectionManager) {
        return new IHereDevicePool(connectionManager);
    }


}
