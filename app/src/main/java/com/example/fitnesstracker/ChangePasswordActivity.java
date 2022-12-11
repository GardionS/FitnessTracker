package com.example.fitnesstracker;

import static com.example.fitnesstracker.LoginActivity.ID_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText passwordOld, passwordNew;
    Button changePassword;
    ValidateText validateText;
    DatabaseUser databaseUser;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_password);
        initVariable();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initVariable() {
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
    private void  checkPassword() {
        if(validateText.validateEditText(passwordOld, getString(R.string.error_message_password_old))
        && validateText.validateEditText(passwordNew, getString(R.string.error_message_password_new))) {
            if(databaseUser.comparePassword(sharedPreferences.getInt(ID_KEY, 0), passwordOld.getText().toString())) {
                databaseUser.insertNewPassword(sharedPreferences.getInt(ID_KEY, 0), passwordNew.getText().toString());
                finish();
            }
        }
    }

    
}
