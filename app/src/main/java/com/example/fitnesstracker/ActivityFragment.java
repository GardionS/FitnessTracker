package com.example.fitnesstracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityFragment extends Fragment implements View.OnClickListener, SensorEventListener {

    private SharedPreferences sharedPreferences;
    private ImageView activityPlayJogging;
    private TextView activityStepCounter, activityCalorie;
    private DatabaseFitness databaseFitness;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private boolean running;
    private int totalSteps, previousStep;

    public ActivityFragment(DatabaseFitness databaseFitness) {
        this.databaseFitness = databaseFitness;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
        initValue();
    }
    private void initVariable() {
        running = false;
        activityPlayJogging = getView().findViewById(R.id.activityPlayJogging);
        activityStepCounter = getView().findViewById(R.id.activityStepCounter);
        activityCalorie = getView().findViewById(R.id.activityCalorie);
    }
    private void initValue() {
        Log.d(null, getSystemInfo(getActivity().getPackageManager()));
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        Fitness fitness = databaseFitness.getFitness(sharedPreferences.getInt(LoginActivity.ID_KEY, 0), format.format(date));
        activityStepCounter.setText(Integer.toString(fitness.getWalk()) + "/6000 steps");
        activityCalorie.setText(Integer.toString(convertToCalorie(fitness.getWalk())) + " cal");
    }
    private int convertToCalorie(int value) {
        return (int) Math.round(value * 0.4);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.activityPlayJogging):
               if(running) { //Deactivate
                   running = false;
                   int diffSteps = totalSteps - previousStep;
                   totalSteps = 0;
                   previousStep = 0;
               } else{

                   running = true;
                   previousStep = Integer.parseInt((String) activityStepCounter.getText());
               }
                break;
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        databaseFitness = new DatabaseFitness(getActivity());
        sharedPreferences =  getActivity().getApplicationContext().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        float [] values = sensorEvent.values;
        int value = -1;
        if(values.length > 0) {
            value = (int) values[0];
        }
        if(sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            totalSteps ++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, stepSensor);
    }
    public String getSystemInfo(PackageManager pm/* activity.getPackageManager() */) {
        return ""
                + "\nSDK: " + Build.VERSION.SDK_INT
                + "\nMODEL: " + Build.MODEL
                + "\nBrand: " + Build.BRAND
                + "\nManufacture: " + Build.MANUFACTURER
                + "\nAndroid Version: " + Build.VERSION.RELEASE
                + "\nSen-Fingerprint: " + hasFP()

                + "\nSen-Light: " + hasSen(pm, PackageManager.FEATURE_SENSOR_LIGHT)
                + "\nSen-Compass: " + hasSen(pm, PackageManager.FEATURE_SENSOR_COMPASS)
                + "\nSen-Proximity: " + hasSen(pm, PackageManager.FEATURE_SENSOR_PROXIMITY)
                + "\nSen-ECG(API 21): " + hasSen(pm, PackageManager.FEATURE_SENSOR_HEART_RATE_ECG)
                + "\nSen-Temp(API 21): " + hasSen(pm, PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE)
                + "\nSen-Accelerometer: " + hasSen(pm, PackageManager.FEATURE_SENSOR_ACCELEROMETER)
                + "\nSen-Humidity(API 21): " + hasSen(pm, PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY)
                + "\nSen-Gyroscope(API 9): " + hasSen(pm, PackageManager.FEATURE_SENSOR_GYROSCOPE)
                + "\nSen-Barometer(API 9): " + hasSen(pm, PackageManager.FEATURE_SENSOR_BAROMETER)
                + "\nSen-HeartRate(API 20): " + hasSen(pm, PackageManager.FEATURE_SENSOR_HEART_RATE)
                + "\nSen-StepCounter(API 19): " + hasSen(pm, PackageManager.FEATURE_SENSOR_STEP_COUNTER)
                + "\nSen-StepDetector(API 19): " + hasSen(pm, PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
    }
    public boolean hasSen(PackageManager packageManager, String sensor) {
        try {
            return packageManager.hasSystemFeature(sensor);
        } catch (Exception ignored) {
            return false;
        }
    }
    public boolean hasFP() {
        return (Build.FINGERPRINT != null && !Build.FINGERPRINT.equals(""));
    }
}