package me.zhanghailin.trynewble;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import me.zhanghailin.bluetooth.BleService;
import me.zhanghailin.bluetooth.DemoConstants;
import me.zhanghailin.trynewble.protocol.BleNotifyProtocol;
import me.zhanghailin.trynewble.protocol.BleReadProtocol;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private StringBuilder stringBuilder;
    private BleService bleService;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = ((BleService.LocalBinder) service).getService();
            textView.setText("service connected!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bleService = null;
            textView.setText("service disconnected~~~~~");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());

        stringBuilder = new StringBuilder();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent serviceIntent = new Intent(this, ApplicationBleService.class);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unbindService(serviceConnection);
        textView.setText("disconnected~~~~~");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void connect(View view) {
        Log.d("main", "connect clicked");
        if (bleService != null) {
            bleService.connect(DemoConstants.ADDR);
        }
    }

    public void beep(View view) {
        Log.d("main", "beep clicked");

        IHereDevicePool devicePool = (IHereDevicePool) bleService.getDevicePool();
        IHereDevice device = devicePool.get(DemoConstants.ADDR);
        device.middleAlert();
    }

    public void bindBleClick(View v) {
        IHereDevicePool devicePool = (IHereDevicePool) bleService.getDevicePool();
        IHereDevice device = devicePool.get(DemoConstants.ADDR);
        device.setOnBleClickListener(new BleNotifyProtocol.OnBleNotifyListener() {
            @Override
            public void onBleNotify(Object value) {
                updateText("Ble Device Clicked");
            }
        });
    }

    public void onBatteryClick(View v) {
        IHereDevicePool devicePool = (IHereDevicePool) bleService.getDevicePool();
        IHereDevice device = devicePool.get(DemoConstants.ADDR);
        device.battery(new BleReadProtocol.OnBleReadCompleteListener() {
            @Override
            public void onBleReadComplete(Object value) {
                updateText("电池电量：" + (int) value);
            }
        });
    }

    public void onSomeTaskClick(View v) {
        IHereDevicePool devicePool = (IHereDevicePool) bleService.getDevicePool();
        IHereDevice device = devicePool.get(DemoConstants.ADDR);

        BleReadProtocol.OnBleReadCompleteListener firmware = new BleReadProtocol.OnBleReadCompleteListener() {
            @Override
            public void onBleReadComplete(Object value) {
                updateText("firmware version：" + value);
            }
        };
        BleReadProtocol.OnBleReadCompleteListener battery = new BleReadProtocol.OnBleReadCompleteListener() {
            @Override
            public void onBleReadComplete(Object value) {
                updateText("电池电量：" + (int) value);
            }
        };

        for (int i = 0; i < 50; i++) {
            device.readFirmware(firmware);
            device.battery(battery);
            device.middleAlert();
        }

    }

    public void disconnect(View view) {
        Log.d("main", "disconnect clicked");

        if (bleService != null) {
            bleService.disconnect(DemoConstants.ADDR);
        }
    }

    private int lineNum = 1;

    private void updateText(String text) {
        stringBuilder.append(lineNum++).append(". ").append(text).append("\n");
        textView.setText(stringBuilder.toString());
    }

}
