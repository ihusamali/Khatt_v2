package com.husamali.khatt;

import android.app.Application;

import com.onesignal.OneSignal;

public class PushNotification extends Application {
    String appID = "de8d2fd4-146e-4481-aa6c-263f99d50e01";
    String id;

    @Override
    public void onCreate() {
        super.onCreate();

        OneSignal.initWithContext(this);

        OneSignal.setAppId(appID);
    }
}
