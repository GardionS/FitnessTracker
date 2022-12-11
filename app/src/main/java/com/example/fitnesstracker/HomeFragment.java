package com.example.fitnesstracker;

import static com.example.fitnesstracker.MainScreen.MAIN_DAILY_QUEST;

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
        getActivity().startService(intent);
        getActivity().bindService(intent, model.getServiceConnection(), getContext().BIND_AUTO_CREATE);
        model.startGettingStepData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (model.getBinder() != null) {
            getActivity().unbindService(model.getServiceConnection());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(getContext(), BoundService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, model.getServiceConnection(), getContext().BIND_AUTO_CREATE);
    }

    private void setObservers() {
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
                                steps.setText(model.getStepData().getValue() + " steps");
                                dailyQuestProgress.setText(model.getStepData().getValue() + "/6000 steps");
                            }
                            handler.postDelayed(this, 1000);
                        } else {
                            handler.removeCallbacks(this);
                        }
                    }
                };
                handler.postDelayed(runnable, 500);
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

    public void initVariable() {
        username = getView().findViewById(R.id.homeUsername);
        steps = getView().findViewById(R.id.homeSteps);
        lvl = getView().findViewById(R.id.homeLevel);
        barExp = getView().findViewById(R.id.homeExpBar);
        stepsBar = getView().findViewById(R.id.homeStepsBar);
        dailyQuestProgress = getView().findViewById(R.id.dailyQuestProgress);
        dailyQuestExp = getView().findViewById(R.id.dailyQuestExp);
    }

    private void initUser() {

        setObservers();

        username.setText(sharedPreferences.getString(LoginActivity.USERNAME_KEY, ""));
        int exp = sharedPreferences.getInt(LoginActivity.EXP_KEY, 0);
        lvl.setText(Integer.toString(Math.floorDiv(exp, 1000)));
        model = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        if (sharedPreferences.getBoolean(MAIN_DAILY_QUEST, false)) {
            dailyQuestExp.setPaintFlags(dailyQuestExp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            dailyQuestExp.setPaintFlags(0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stepsBar.setProgress(sharedPreferences.getInt(MainScreen.MAIN_STEP_COUNTER, 0), true);
            barExp.setProgress(exp % 1000, true);
        } else {
            barExp.setProgress(exp % 1000);
        }
    }
}