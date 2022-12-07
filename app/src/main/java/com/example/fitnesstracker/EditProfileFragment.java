package com.example.fitnesstracker;

import static com.example.fitnesstracker.LoginActivity.AGE_KEY;
import static com.example.fitnesstracker.LoginActivity.EMAIL_KEY;
import static com.example.fitnesstracker.LoginActivity.ID_KEY;
import static com.example.fitnesstracker.LoginActivity.USERNAME_KEY;
import static com.example.fitnesstracker.LoginActivity.WEIGHT_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class EditProfileFragment extends AppCompatActivity {
    private EditText editUsername, editEmail, editWeight, editAge;
    private SharedPreferences sharedPreferences;
    private Button editProfile;
    private DatabaseUser databaseUser;
    private ValidateText validateText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_edit);
        initVariable();
    }
    private void initVariable() {
        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        databaseUser = new DatabaseUser(this);
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editWeight = findViewById(R.id.editWeight);
        editAge = findViewById(R.id.editAge);
        editProfile = findViewById(R.id.editProfile);
        validateText = new ValidateText(this);

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
    private void changeProfileData() {
        if(!validateText.validateEditText(editUsername, getString(R.string.error_message_username))
        ||!validateText.validateEditText(editEmail, getString(R.string.error_message_email))
        ||!validateText.validateEditText(editAge, getString(R.string.error_message_age))
        ||!validateText.validateEditText(editWeight, getString(R.string.error_message_weight))) {
            databaseUser.updateUser(sharedPreferences.getInt(ID_KEY, 0),
                    editUsername.getText().toString(),
                    editEmail.getText().toString(),
                    Integer.parseInt(editAge.getText().toString()),
                    Integer.parseInt(editWeight.getText().toString()));

            emptyEditText();
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
            finish();
        }
    }

    private void emptyEditText() {
        editUsername.setText(null);
        editEmail.setText(null);
        editAge.setText(null);
        editWeight.setText(null);
    }
}
