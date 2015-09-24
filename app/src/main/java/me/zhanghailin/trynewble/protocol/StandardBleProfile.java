package me.zhanghailin.trynewble.protocol;

/**
 * Created by zhanghailin on 7/29/15.
 */
@SuppressWarnings("unused")
public interface StandardBleProfile {

    /**
     * 2902 Configuration descriptor
     *
     * This descriptor shall be persistent across connections for bonded devices.
     * The Client Characteristic Configuration descriptor is unique for each client.
     * A client may read and write this descriptor to determine and set the configuration for that client.
     * Authentication and authorization may be required by the server to write this descriptor.
     * The default value for the Client Characteristic Configuration descriptor is 0x00.
     * Upon connection of non-binded clients, this descriptor is set to the default value.
     */
    String DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION = "00002902-0000-1000-8000-00805f9b34fb";

    /**
     * 180A Device Information
     *
     * @see <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.device_information.xml">spec page</a>
     */

    // All "*_STRING"s are utf8s
    String SERVICE_DEVICE_INFORMATION = "0000180a-0000-1000-8000-00805f9b34fb";
    String CHAR_MANUFACTURER_NAME_STRING = "00002a29-0000-1000-8000-00805f9b34fb";
    String CHAR_MODEL_NUMBER_STRING = "00002a24-0000-1000-8000-00805f9b34fb";
    String CHAR_SERIAL_NUMBER_STRING = "00002a25-0000-1000-8000-00805f9b34fb";
    String CHAR_HARDWARE_REVISION_STRING = "00002a27-0000-1000-8000-00805f9b34fb";
    String CHAR_FIRMWARE_REVISION_STRING = "00002a26-0000-1000-8000-00805f9b34fb";
    String CHAR_SOFTWARE_REVISION_STRING = "00002a28-0000-1000-8000-00805f9b34fb";
    // Format uint64(uint40 instance id + uint24 OUI)
    String CHAR_SYSTEM_ID = "00002a23-0000-1000-8000-00805f9b34fb";

    /**
     * 1802  Immediate Alert Service
     *
     * @see <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.immediate_alert.xml">spec page</a>
     */
    String SERVICE_IMMEDIATE_ALERT = "00001802-0000-1000-8000-00805f9b34fb";
    // WriteWithoutResponse
    // 0x00 No Alert
    // 0x01 Mild Alert
    // 0x02 High Alert
    String CHAR_ALERT_LEVEL = "00002a06-0000-1000-8000-00805f9b34fb";

    /**
     * 180F Battery
     * @see <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.battery_service.xml">spec page</a>
     */
    String SERVICE_BATTERY_SERVICE = "0000180F-0000-1000-8000-00805f9b34fb";
    // Format       uint8
    // Min Value    0
    // Max value    100
    String CHAR_BATTERY_LEVEL = "00002a19-0000-1000-8000-00805f9b34fb";


    /**
     * 1805 current time
     * @see <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.current_time.xml">spec page</a>
     */
    String SERVICE_CURRENT_TIME = "00001805-0000-1000-8000-00805f9b34fb";
    // TODO: 7/30/15

    /**
     * 1803 Link Loss
     * @see <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.link_loss.xml">spec page</a>
     */
    String SERVICE_LINK_LOSS = "00001803-0000-1000-8000-00805f9b34fb";
    // Write Characteristic Value
    // 0x00 No Alert
    // 0x01 Mild Alert
    // 0x02 High Alert
    String CHAR_ALERT_LEVEL_LINK_LOSS = "00002a06-0000-1000-8000-00805f9b34fb";
}
