package com.gmail.gardion01.fitnesstracker.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnesstracker.R;
import com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity;
import com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity;
import com.gmail.gardion01.fitnesstracker.service.BoundService;
import com.gmail.gardion01.fitnesstracker.viewmodel.HomeFragmentViewModel;

public class HomeFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private TextView username, lvl, steps, dailyQuestProgress, dailyQuestExp;
    private ProgressBar barExp, stepsBar;
    private HomeFragmentViewModel model;
    private BoundService boundService;

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), BoundService.class);
        getActivity().startService(intent); //Start the service
        getActivity().bindService(intent, model.getServiceConnection(), getContext().BIND_AUTO_CREATE);
        model.startGettingStepData(); //Start live data
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (model.getBinder() != null) {
            getActivity().unbindService(model.getServiceConnection()); //Destroy service
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(getContext(), BoundService.class);
        getActivity().startService(intent); //Start the service again
        getActivity().bindService(intent, model.getServiceConnection(), getContext().BIND_AUTO_CREATE);
    }

    private void setObservers() { //Set observer to update data
        model.getBinder().observe(getViewLifecycleOwner(), new Observer<BoundService.LocalBinder>() {
            @Override
            public void onChanged(BoundService.LocalBinder localBinder) {
                if (localBinder != null) {
                    boundService = localBinder.getService();
                }
            }
        });
        model.getStepData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (model.getBinder().getValue() != null) {
                            if (model.getStepData() != null) {
                                steps.setText(model.getStepData().getValue() + " steps"); //Update the step data
                                dailyQuestProgress.setText(model.getStepData().getValue() + "/6000 steps"); //Update the quest data
                            }
                            handler.postDelayed(this, 500); //reset every step 1000ms
                        } else {
                            handler.removeCallbacks(this);
                        }
                    }
                };
                handler.postDelayed(runnable, 500); //reset every step 500ms
            }
        });
        model.getStepData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
        initUser();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void initVariable() { //Initialize all variable
        username = getView().findViewById(R.id.homeUsername);
        steps = getView().findViewById(R.id.homeSteps);
        lvl = getView().findViewById(R.id.homeLevel);
        barExp = getView().findViewById(R.id.homeExpBar);
        stepsBar = getView().findViewById(R.id.homeStepsBar);
        dailyQuestProgress = getView().findViewById(R.id.dailyQuestProgress);
        dailyQuestExp = getView().findViewById(R.id.dailyQuestExp);
    }

    private void initUser() { //Initialize user

        setObservers(); //Set the observer for live data

        username.setText(sharedPreferences.getString(LoginActivity.USERNAME_KEY, ""));
        int exp = sharedPreferences.getInt(LoginActivity.EXP_KEY, 0);
        lvl.setText(Integer.toString(Math.floorDiv(exp, 1000))); //Each level needs 1000xp
        model = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        if (sharedPreferences.getBoolean(HomeActivity.MAIN_DAILY_QUEST, false)) { //The exp number will be strikethrough indicating already complete
            dailyQuestExp.setPaintFlags(dailyQuestExp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            dailyQuestExp.setPaintFlags(0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //Use animation for newer version
            stepsBar.setProgress(sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0), true); //Set step bar
            barExp.setProgress(exp % 1000, true); //Set exp bar
        } else { //OLder version cannot user animation
            stepsBar.setProgress(sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0)); //Set step bar
            barExp.setProgress(exp % 1000); //Set exp bar
        }
    }
}