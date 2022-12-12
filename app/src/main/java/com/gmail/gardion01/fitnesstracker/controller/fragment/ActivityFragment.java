package com.gmail.gardion01.fitnesstracker.controller.fragment;

import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_DAILY_RUNNING_TARGET;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.SHARED_PREFS;
import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.RUNNING;
import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_STEP_COUNTER;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.ID_KEY;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnesstracker.R;
import com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity;
import com.gmail.gardion01.fitnesstracker.database.DatabaseFitness;
import com.gmail.gardion01.fitnesstracker.database.DatabaseMain;
import com.gmail.gardion01.fitnesstracker.model.Fitness;

public class ActivityFragment extends Fragment implements View.OnClickListener {

    public static final String ACTIVITY_PREVIOUS_STEP_KEY = "activity_step_key";
    public static final String ACTIVITY_RUNNING_KEY = "activity_running_key";

    private SharedPreferences sharedPreferencesUser, sharedPreferencesActivity;
    private ImageButton activityPlayJogging;
    private TextView activityStepCounter, activityCalorie;
    private DatabaseFitness databaseFitness;
    private boolean running;

    public ActivityFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
        initValue();
        initListener();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        databaseFitness = new DatabaseFitness(getActivity());
        sharedPreferencesActivity = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferencesUser = getActivity().getApplicationContext().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.activityPlayJogging):
                SharedPreferences.Editor editor = sharedPreferencesActivity.edit();
                if (running) { //Deactivate
                    running = false;
                    activityPlayJogging.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    int diffSteps = sharedPreferencesUser.getInt(MAIN_STEP_COUNTER, 0) - sharedPreferencesActivity.getInt(ACTIVITY_PREVIOUS_STEP_KEY, 0);
                    int previousRunning = databaseFitness.getFitness(sharedPreferencesUser.getInt(ID_KEY, 0), RUNNING.getValue(), DatabaseMain.getCurrentDate("dd-MM-yyyy")).getValue();
                    databaseFitness.updateFitness(sharedPreferencesUser.getInt(ID_KEY, 0), RUNNING.getValue(), DatabaseMain.getCurrentDate("dd-MM-yyyy"), previousRunning + diffSteps); //Update database on running
                    editor.putInt(ACTIVITY_PREVIOUS_STEP_KEY, 0);
                    editor.putBoolean(ACTIVITY_RUNNING_KEY, false);
                } else {
                    running = true;
                    activityPlayJogging.setImageResource(R.drawable.ic_baseline_pause_24);
                    int previousStep =  databaseFitness.getFitness(sharedPreferencesUser.getInt(ID_KEY, 0), RUNNING.getValue(), DatabaseMain.getCurrentDate("dd-MM-yyyy")).getValue();
                    editor.putInt(ACTIVITY_PREVIOUS_STEP_KEY, previousStep); //Store current step count for running
                    editor.putBoolean(ACTIVITY_RUNNING_KEY, true);
                }
                editor.apply();
                break;
        }
    }

    private void initVariable() { //Initialize all variable
        running = sharedPreferencesActivity.getBoolean(ACTIVITY_RUNNING_KEY, false);
        activityPlayJogging = getView().findViewById(R.id.activityPlayJogging);
        activityStepCounter = getView().findViewById(R.id.activityStepCounter);
        activityCalorie = getView().findViewById(R.id.activityCalorie);
    }

    private void initValue() { //Initialize all value
        Fitness fitness = databaseFitness.getFitness(sharedPreferencesUser.getInt(ID_KEY, 0), RUNNING.getValue(), DatabaseMain.getCurrentDate("dd-MM-yyyy"));
        activityStepCounter.setText(fitness.getValue() + "/ " + sharedPreferencesUser.getInt(MAIN_DAILY_RUNNING_TARGET, 0) + " steps");
        activityCalorie.setText(convertToCalorie(fitness.getValue()) + " cal");
    }

    private void initListener() { //Initialize all listener
        activityPlayJogging.setOnClickListener(this);
    }

    private int convertToCalorie(int value) { //Convert steps to calories
        return (int) Math.round(value * 0.4);
    }

}