package me.zhanghailin.trynewble;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.zhanghailin.bluetooth.BleService;
import me.zhanghailin.bluetooth.DemoConstants;
import me.zhanghailin.trynewble.protocol.BleNotifyProtocol;
import me.zhanghailin.trynewble.protocol.BleReadProtocol;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private StringBuilder stringBuilder;
    private BleService bleService;

    private String currentAddress;

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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(this));
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

    public void setCurrent(String address) {
        currentAddress = address;
    }

    public void reconnect(View v) {
        if (bleService != null) {
            for (String address : DemoConstants.ADDRS) {
                bleService.autoReconnect(address);
            }
        }
    }

    public void close(View v) {
        if (bleService != null) {
            for (String address : DemoConstants.ADDRS) {
                bleService.enqueueDisconnect(address);
            }
        }
    }

    public void addDevices(View v) {
        if (bleService != null) {
            for (String address : DemoConstants.ADDRS) {
                bleService.addDevice(address);
            }
        }
    }

    public void connectWaiting(View v) {
        if (bleService != null) {
            bleService.getConnectionManager().connectWaitingDevices();
        }
    }

    public void bindBleClick(View v) {
        IHereDevicePool devicePool = (IHereDevicePool) bleService.getDevicePool();
        IHereDevice device = devicePool.get(currentAddress);
        device.setOnBleClickListener(new BleNotifyProtocol.OnBleNotifyListener() {
            @Override
            public void onBleNotify(Object value) {
                updateText("Ble Device Clicked");
            }
        });
    }

    public void beep(View view) {
        IHereDevicePool devicePool = (IHereDevicePool) bleService.getDevicePool();
        IHereDevice device = devicePool.get(currentAddress);
        device.middleAlert();
    }

    public void onBatteryClick(View v) {
        IHereDevicePool devicePool = (IHereDevicePool) bleService.getDevicePool();
        IHereDevice device = devicePool.get(currentAddress);
        device.battery(new BleReadProtocol.OnBleReadCompleteListener() {
            @Override
            public void onBleReadComplete(Object value) {
                updateText("电池电量：" + (int) value);
            }
        });
    }

    public void onSomeTaskClick(View v) {
        IHereDevicePool devicePool = (IHereDevicePool) bleService.getDevicePool();
        IHereDevice device = devicePool.get(currentAddress);

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

        for (int i = 0; i < 10; i++) {
            device.readFirmware(firmware);
            device.battery(battery);
            device.middleAlert();
        }

    }

    private int lineNum = 1;

    private void updateText(String text) {
        stringBuilder.append(lineNum++).append(". ").append(text).append("\n");
        textView.setText(stringBuilder.toString());
    }

}


class Adapter extends RecyclerView.Adapter<Holder> implements View.OnClickListener {

    private final static String[] source = DemoConstants.ADDRS;

    private MainActivity mainActivity;

    private float height;

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;

    public Adapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, mainActivity.getResources().getDisplayMetrics());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        this.layoutManager = recyclerView.getLayoutManager();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
        this.layoutManager = null;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        TextView textView = new TextView(viewGroup.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height));
        textView.setClickable(true);
        textView.setBackgroundResource(R.drawable.abc_btn_borderless_material);
        return new Holder(textView, this);
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        String data = source[i];
        holder.textView.setText(data);
    }

    @Override
    public int getItemCount() {
        return source.length;
    }

    @Override
    public void onClick(View v) {
        String address = source[layoutManager.getPosition(v)];

        mainActivity.setCurrent(address);
    }

}

class Holder extends RecyclerView.ViewHolder {

    TextView textView;

    public Holder(View itemView, View.OnClickListener listener) {
        super(itemView);
        textView = (TextView) itemView;
        textView.setOnClickListener(listener);
    }

}

