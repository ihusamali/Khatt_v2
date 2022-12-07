package com.husamali.khatt;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class SaveLocal extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
