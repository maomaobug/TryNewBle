package me.zhanghailin.trynewble;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by zhanghailin on 9/18/15.
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
