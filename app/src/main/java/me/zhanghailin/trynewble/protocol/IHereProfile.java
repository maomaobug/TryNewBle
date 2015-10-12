package me.zhanghailin.trynewble.protocol;

import java.util.UUID;

/**
 * Created by zhanghailin on 9/18/15.
 */
public interface IHereProfile {
    String SERVICE_CLICK = "00001c00-d102-11e1-9b23-000efb0000a5";
    String CHARACTERISTIC_CLICK = "00001c00-d102-11e1-9b23-000efb000011";

    // device information: firmware, etc
    UUID SERVICE_DEVICE_INFORMATION
            = UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb");

    UUID CHARACTERISTIC_FIRMWARE =
            UUID.fromString("00002A28-0000-1000-8000-00805f9b34fb");
}
