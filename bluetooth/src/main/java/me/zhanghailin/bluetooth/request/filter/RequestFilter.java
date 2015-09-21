package me.zhanghailin.bluetooth.request.filter;

import me.zhanghailin.bluetooth.request.BleDataRequest;

/**
 * TryNewBle
 * package me.zhanghailin.bluetooth.request
 * author shenwenjun
 * Date 9/14/15.
 */
public interface RequestFilter {

    /**
     * 判断传入的 request 是否符合 过滤的条件， 并返回结果
     *
     * @param request 要过滤的 request
     * @return 是否符合过滤条件， 即是否要处理该 request
     */
    boolean apply(BleDataRequest request);

}
