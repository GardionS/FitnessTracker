package com.example.fitnesstracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private TextView username, lvl, steps;
    private ProgressBar barExp, stepsBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = getView().findViewById(R.id.homeUsername);
        steps = getView().findViewById(R.id.homeSteps);
        lvl = getView().findViewById(R.id.homeLevel);
        barExp = getView().findViewById(R.id.homeExpBar);
        stepsBar = getView().findViewById(R.id.homeStepsBar);
        initUser();
    }

    public void initVariable() {
//        sharedPreferences =  getActivity().getApplicationContext().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
//        initUser();
    }
    private void initUser() {
        username.setText(sharedPreferences.getString(LoginActivity.USERNAME_KEY, ""));
        int exp = sharedPreferences.getInt(LoginActivity.EXP_KEY, 0);
        lvl.setText(Integer.toString(Math.floorDiv(exp, 1000)));
        steps.setText(Integer.toString(sharedPreferences.getInt(MainScreen.MAIN_STEP_COUNTER, 0)) + " steps");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            stepsBar.setProgress((int) Math.round(sharedPreferences.getInt(MainScreen.MAIN_STEP_COUNTER, 0) * 0.6),true);
            stepsBar.setProgress(sharedPreferences.getInt(MainScreen.MAIN_STEP_COUNTER, 0),true);
            barExp.setProgress(exp % 1000, true);
        } else{
            barExp.setProgress(exp % 1000);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
    }

}