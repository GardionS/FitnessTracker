package com.gmail.gardion01.fitnesstracker.viewmodel;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity;
import com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity;
import com.gmail.gardion01.fitnesstracker.service.BoundService;

public class HomeFragmentViewModel extends AndroidViewModel {
    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<BoundService.LocalBinder> mBinder = new MutableLiveData<>();
    private MutableLiveData<Integer> mStepData = new MutableLiveData<>();
    private boolean isBound = false;
    private BoundService boundService;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BoundService.LocalBinder binder = (BoundService.LocalBinder) iBinder;
            boundService = binder.getService();
            mBinder.postValue(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            boundService = null;
        }
    };
    private final Handler handler;

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = getApplication().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        handler = new Handler();
    }

    public MutableLiveData<BoundService.LocalBinder> getBinder() {
        return mBinder;
    }

    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }

    public void startGettingStepData() { //get step data
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mStepData == null) {
                    mStepData = new MutableLiveData<>();
                }
                setStepData();
                handler.postDelayed(this, 500);

            }
        };
        handler.postDelayed(runnable, 500);
    }

    public MutableLiveData<Integer> getStepData() {
        return mStepData;
    }

    public void setStepData() {
        mStepData.setValue(sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0));
    }
}
