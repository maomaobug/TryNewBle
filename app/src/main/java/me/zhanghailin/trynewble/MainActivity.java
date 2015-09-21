package me.zhanghailin.trynewble;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.zhanghailin.bluetooth.BleService;

public class MainActivity extends AppCompatActivity {
//    private static final String ADDR = "57:A3:05:13:C8:E2";
    private static final String ADDR = "0A:35:CD:01:32:3D";

    private BleService bleService;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = ((BleService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bleService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            bleService.connect(ADDR);
        }
    }

    public void beep(View view) {
        Log.d("main", "beep clicked");

        IHereDevicePool devicePool = (IHereDevicePool) bleService.getDevicePool();
        IHereDevice device = devicePool.get(ADDR);
        device.middleAlert();
    }

    public void disconnect(View view) {
        Log.d("main", "disconnect clicked");
    }
}
