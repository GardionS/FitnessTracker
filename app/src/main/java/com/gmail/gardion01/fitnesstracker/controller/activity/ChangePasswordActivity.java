package com.gmail.gardion01.fitnesstracker.controller.activity;

import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.ID_KEY;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.R;
import com.gmail.gardion01.fitnesstracker.database.DatabaseUser;
import com.gmail.gardion01.fitnesstracker.utility.ValidateText;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText passwordOld, passwordNew;
    Button changePassword;
    ValidateText validateText;
    DatabaseUser databaseUser;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        initVariable();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initVariable() { //Initialize all variable
        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        databaseUser = new DatabaseUser(this);
        validateText = new ValidateText(this);
        passwordOld = findViewById(R.id.editOldPassword);
        passwordNew = findViewById(R.id.editNewPassword);
        changePassword = findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword();
            }
        });
    }

    private void checkPassword() { //Check if password same
        if (validateText.validateEditText(passwordOld, getString(R.string.error_message_password_old)) && validateText.validateEditText(passwordNew, getString(R.string.error_message_password_new))) {
            if (databaseUser.comparePassword(sharedPreferences.getInt(ID_KEY, 0), passwordOld.getText().toString())) {
                databaseUser.insertNewPassword(sharedPreferences.getInt(ID_KEY, 0), passwordNew.getText().toString()); //Update password in database
                finish();
            }
        }
    }


}
