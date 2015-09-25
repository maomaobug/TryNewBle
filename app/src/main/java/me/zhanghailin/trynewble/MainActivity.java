package me.zhanghailin.trynewble;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import me.zhanghailin.bluetooth.BleService;
import me.zhanghailin.bluetooth.DemoConstants;
import me.zhanghailin.trynewble.protocol.BatteryProtocol;
import me.zhanghailin.trynewble.protocol.ClickProtocol;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

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
            textView.setText("device connected");
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
        device.setOnBleClickListener(new ClickProtocol.OnBleClickListener() {
            @Override
            public void onBleClick() {
                textView.setText("Ble Device Clicked");
            }
        });
    }

    public void onBatteryClick(View v) {
        IHereDevicePool devicePool = (IHereDevicePool) bleService.getDevicePool();
        IHereDevice device = devicePool.get(DemoConstants.ADDR);
        device.battery(new BatteryProtocol.OnReadBatteryCompleteListener() {
            @Override
            public void onReadComplete(int batteryLevel) {
                textView.setText("电池电量：" + batteryLevel);
            }
        });
    }

    public void disconnect(View view) {
        Log.d("main", "disconnect clicked");
    }
}
