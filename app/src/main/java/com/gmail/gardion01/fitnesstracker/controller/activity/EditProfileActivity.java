package com.gmail.gardion01.fitnesstracker.controller.activity;

import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.AGE_KEY;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.EMAIL_KEY;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.ID_KEY;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.USERNAME_KEY;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.WEIGHT_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.R;
import com.gmail.gardion01.fitnesstracker.database.DatabaseUser;
import com.gmail.gardion01.fitnesstracker.utility.ValidateText;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editUsername, editEmail, editWeight, editAge;
    private SharedPreferences sharedPreferences;
    private Button editProfile;
    private DatabaseUser databaseUser;
    private ValidateText validateText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        initVariable();
        initValue();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initValue() { //Initialize all value
        editUsername.setText(sharedPreferences.getString(USERNAME_KEY, ""));
        editEmail.setText(sharedPreferences.getString(EMAIL_KEY, ""));
        editAge.setText(Integer.toString(sharedPreferences.getInt(AGE_KEY, 0)));
        editWeight.setText(Integer.toString(sharedPreferences.getInt(WEIGHT_KEY, 0)));
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfileData();
            }
        });
    }

    private void initVariable() { //Initialize all variable
        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        databaseUser = new DatabaseUser(this);
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editWeight = findViewById(R.id.editWeight);
        editAge = findViewById(R.id.editAge);
        editProfile = findViewById(R.id.editProfile);
        validateText = new ValidateText(this);
    }

    private void changeProfileData() { //Edit the profile data
        if (validateText.validateEditText(editUsername, getString(R.string.error_message_username)) && validateText.validateEditText(editEmail, getString(R.string.error_message_email)) && validateText.validateEditText(editAge, getString(R.string.error_message_age)) && validateText.validateEditText(editWeight, getString(R.string.error_message_weight))) { //Check all the edittext
            databaseUser.updateUser(sharedPreferences.getInt(ID_KEY, 0), editUsername.getText().toString(), editEmail.getText().toString(), Integer.parseInt(editAge.getText().toString()), Integer.parseInt(editWeight.getText().toString())); //Update user data

            emptyEditText();
            Intent intent = new Intent(this, HomeActivity.class); //Start again in the home
            startActivity(intent);
            finish(); //Finish so they cannot go back
        }
    }

    private void emptyEditText() {
        editUsername.setText(null);
        editEmail.setText(null);
        editAge.setText(null);
        editWeight.setText(null);
    }
}
