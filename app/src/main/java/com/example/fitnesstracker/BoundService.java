package com.example.fitnesstracker;

import static com.example.fitnesstracker.MainScreen.MAIN_STEP_COUNTER;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BoundService extends Service {
    private IBinder iBinder = new LocalBinder();
    private Handler handler;
    private int stepCount;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        handler = new Handler();
        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        stepCount = 0;
        super.onCreate();
    }

    public void updateCounter() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                stepCount = sharedPreferences.getInt(MAIN_STEP_COUNTER, 0);
                handler.postDelayed(this, 500);
            }

        };
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
    public class LocalBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }
}
