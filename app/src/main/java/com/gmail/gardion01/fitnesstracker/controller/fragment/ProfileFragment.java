package com.gmail.gardion01.fitnesstracker.controller.fragment;

import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.ID_KEY;
import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.WALKING;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnesstracker.R;
import com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity;
import com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity;
import com.gmail.gardion01.fitnesstracker.database.DatabaseFitness;
import com.gmail.gardion01.fitnesstracker.service.ForegroundService;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView profileUsername, profileChangePassword, profileLogout;
    private SharedPreferences sharedPreferences;
    private ImageButton profileEditProfile;
    private DatabaseFitness databaseFitness;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
        initValue();
        initListener();
    }
    private void initVariable() { //Initialize all variable
        profileUsername = getView().findViewById(R.id.profileUsername);
        profileEditProfile = getView().findViewById(R.id.profileEditProfile);
        profileLogout = getView().findViewById(R.id.profileLogout);
        profileChangePassword = getView().findViewById(R.id.profileChangePassword);
    }
    private void initValue() { //Initialize all value
        profileUsername.setText(sharedPreferences.getString(LoginActivity.USERNAME_KEY, ""));
    }
    private void initListener() { //Initialize all listener
        profileEditProfile.setOnClickListener(this);
        profileLogout.setOnClickListener(this);
        profileChangePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) { //On click for all button
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
    private void logout(){ //Logout
        databaseFitness.updateFitness(sharedPreferences.getInt(ID_KEY, 0), WALKING.getValue(), sharedPreferences.getString(HomeActivity.MAIN_DATE_STEP, ""), sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0));
        sharedPreferences.edit().clear().commit();
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
    private void stopForegroundService() { //Stop foreground service
        Intent foregroundService = new Intent(getActivity(), ForegroundService.class);
        getActivity().stopService(foregroundService);
    }
}
