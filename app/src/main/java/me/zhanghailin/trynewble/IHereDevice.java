package me.zhanghailin.trynewble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import me.zhanghailin.bluetooth.connection.ConnectionManager;
import me.zhanghailin.bluetooth.device.BleDevice;
import me.zhanghailin.bluetooth.request.BleDataRequest;
import me.zhanghailin.bluetooth.request.ReadWriteRequest;
import me.zhanghailin.trynewble.protocol.BatteryProtocol;
import me.zhanghailin.trynewble.protocol.ClickProtocol;
import me.zhanghailin.trynewble.protocol.ImmediateAlertProtocol;
import me.zhanghailin.trynewble.protocol.LinkLossProtocol;

/**
 * Created by zhanghailin on 9/17/15.
 */
public class IHereDevice extends BleDevice {
    private int rssi;
    private final ImmediateAlertProtocol immediateAlertProtocol = new ImmediateAlertProtocol();
    private final ClickProtocol clickProtocol = new ClickProtocol();
    private final BatteryProtocol batteryProtocol = new BatteryProtocol();
    private final LinkLossProtocol linkLossProtocol = new LinkLossProtocol();

    public IHereDevice(BluetoothGatt gatt, ConnectionManager connectionManager, String address) {
        super(gatt, connectionManager, address);
    }

    {
        protocols.add(immediateAlertProtocol);
        protocols.add(clickProtocol);
        protocols.add(batteryProtocol);
        protocols.add(linkLossProtocol);
    }

    @Override
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void middleAlert() {
        BluetoothGattService service = gatt.getService(immediateAlertProtocol.getServiceUuid());
        BluetoothGattCharacteristic characteristic =
                service.getCharacteristic(immediateAlertProtocol.getCharacteristicUuid());
        characteristic.setValue(ImmediateAlertProtocol.MIDDLE_LEVEL_ALERT);

        BleDataRequest request = new ReadWriteRequest(gatt, characteristic, ReadWriteRequest.WRITE);
        connectionManager.enQueueRequest(request);
    }

    public void battery(BatteryProtocol.OnReadBatteryCompleteListener onReadBatteryCompleteListener) {
        BluetoothGattService service = gatt.getService(batteryProtocol.getServiceUuid());
        BluetoothGattCharacteristic characteristic =
                service.getCharacteristic(batteryProtocol.getCharacteristicUuid());

        BleDataRequest request = new ReadWriteRequest(gatt, characteristic, ReadWriteRequest.READ);
        connectionManager.enQueueRequest(request);

        batteryProtocol.setOnReadBatteryCompleteListener(onReadBatteryCompleteListener);
    }

    public void setOnBleClickListener(ClickProtocol.OnBleClickListener onBleClickListener) {
        clickProtocol.setOnBleClickListener(onBleClickListener);
    }

}
