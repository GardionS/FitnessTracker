package com.example.fitnesstracker;

import static com.example.fitnesstracker.ActivityFragment.SHARED_PREFS_ACTIVITY;
import static com.example.fitnesstracker.LoginActivity.ID_KEY;
import static com.example.fitnesstracker.LoginActivity.SHARED_PREFS;
import static com.example.fitnesstracker.MainScreen.MAIN_DATE_STEP;
import static com.example.fitnesstracker.MainScreen.MAIN_STEP_COUNTER;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView profileUsername, profileChangePassword, profileLogout;
    private SharedPreferences sharedPreferences;
    private ImageButton profileEditProfile;
    private DatabaseFitness databaseFitness;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
        initValue();
        initListener();
    }
    private void initVariable() {
        profileUsername = getView().findViewById(R.id.profileUsername);
        profileEditProfile = getView().findViewById(R.id.profileEditProfile);
        profileLogout = getView().findViewById(R.id.profileLogout);
        profileChangePassword = getView().findViewById(R.id.profileChangePassword);
    }
    private void initValue() {
        profileUsername.setText(sharedPreferences.getString(LoginActivity.USERNAME_KEY, ""));

    }
    private void initListener() {
        profileEditProfile.setOnClickListener(this);
        profileLogout.setOnClickListener(this);
        profileChangePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profileEditProfile:
                goToEditProfile();
                break;
            case R.id.profileLogout:
                logout();
                break;
            case R.id.profileChangePassword:
                goToChangePassword();
                break;
        }
    }
    private void goToEditProfile() {
        Intent intent = new Intent(getActivity(), EditProfileFragment.class);
        startActivity(intent);
    }
    private void logout(){
        databaseFitness.updateFitnessWalk(sharedPreferences.getInt(ID_KEY, 0), sharedPreferences.getString(MAIN_DATE_STEP, ""), sharedPreferences.getInt(MAIN_STEP_COUNTER, 0));
        sharedPreferences.edit().clear().commit();
        SharedPreferences sharedPreferencesActivity = getActivity().getSharedPreferences(SHARED_PREFS_ACTIVITY, Context.MODE_PRIVATE);
        sharedPreferencesActivity.edit().clear().commit();
        stopForegroundService();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
    private void goToChangePassword() {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        startActivity(intent);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        databaseFitness = new DatabaseFitness(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
    }
    private void stopForegroundService() {
        Intent foregroundService = new Intent(getActivity(), ForegroundService.class);
        getActivity().stopService(foregroundService);
    }
}
