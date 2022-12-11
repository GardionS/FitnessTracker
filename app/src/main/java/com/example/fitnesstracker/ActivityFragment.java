package com.example.fitnesstracker;

import static com.example.fitnesstracker.LoginActivity.ID_KEY;
import static com.example.fitnesstracker.MainScreen.MAIN_STEP_COUNTER;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityFragment extends Fragment implements View.OnClickListener {

    public static final String SHARED_PREFS_ACTIVITY = "shared_prefs_activity";
    public static final String ACTIVITY_PREVIOUS_STEP_KEY = "activity_step_key";
    public static final String ACTIVITY_RUNNING_KEY = "activity_running_key";
    private SharedPreferences sharedPreferencesUser, sharedPreferencesActivity;
    private ImageButton activityPlayJogging;
    private TextView activityStepCounter, activityCalorie;
    private DatabaseFitness databaseFitness;
    private boolean running;

    public ActivityFragment(DatabaseFitness databaseFitness) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initListener();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        databaseFitness = new DatabaseFitness(getActivity());
        sharedPreferencesActivity = getActivity().getSharedPreferences(SHARED_PREFS_ACTIVITY, Context.MODE_PRIVATE);
        sharedPreferencesUser = getActivity().getApplicationContext().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.activityPlayJogging):
                if (running) { //Deactivate
                    running = false;
                    activityPlayJogging.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = new Date();
                    int diffSteps = sharedPreferencesUser.getInt(MAIN_STEP_COUNTER, 0) - sharedPreferencesActivity.getInt(ACTIVITY_PREVIOUS_STEP_KEY, 0);
                    int previousRunning = databaseFitness.getRunning(sharedPreferencesUser.getInt(ID_KEY, 0), format.format(date));
                    databaseFitness.updateFitnessRunning(sharedPreferencesUser.getInt(ID_KEY, 0), format.format(date), previousRunning + diffSteps);
                    SharedPreferences.Editor editor = sharedPreferencesActivity.edit();
                    editor.putInt(ACTIVITY_PREVIOUS_STEP_KEY, 0);
                    editor.putBoolean(ACTIVITY_RUNNING_KEY, false);
                } else {
                    running = true;

                    activityPlayJogging.setImageResource(R.drawable.ic_baseline_pause_24);
                    int previousStep = databaseFitness.getRunning(sharedPreferencesUser.getInt(ID_KEY, 0),
                            new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                    SharedPreferences.Editor editor = sharedPreferencesActivity.edit();
                    editor.putInt(ACTIVITY_PREVIOUS_STEP_KEY, previousStep);
                    editor.putBoolean(ACTIVITY_RUNNING_KEY, true);
                    editor.apply();
                }
                break;
        }
    }

    private void initVariable() {
        running = false;
        activityPlayJogging = getView().findViewById(R.id.activityPlayJogging);
        activityStepCounter = getView().findViewById(R.id.activityStepCounter);
        activityCalorie = getView().findViewById(R.id.activityCalorie);
    }

    private void initValue() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        Fitness fitness = databaseFitness.getFitness(sharedPreferencesUser.getInt(ID_KEY, 0), format.format(date));
        activityStepCounter.setText(fitness.getRunning() + "/6000 steps");
        activityCalorie.setText(convertToCalorie(fitness.getRunning()) + " cal");
    }

    private void initListener() {
        activityPlayJogging.setOnClickListener(this);
    }

    private int convertToCalorie(int value) {
        return (int) Math.round(value * 0.4);
    }

}