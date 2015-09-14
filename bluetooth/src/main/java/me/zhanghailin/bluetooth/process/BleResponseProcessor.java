package me.zhanghailin.bluetooth.process;

import me.zhanghailin.bluetooth.response.BleDataResponse;

/**
 * Created by zhanghailin on 9/10/15.
 */
public interface BleResponseProcessor {
    void processResponse(BleDataResponse response);
}
