package com.example.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        EditText username = findViewById(R.id.registerUsername);
        EditText email = findViewById(R.id.registerEmail);
        EditText password = findViewById(R.id.registerPassword);
        EditText age = findViewById(R.id.registerAge);
        EditText weight = findViewById(R.id.registerWeight);

        Button register = findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData(username, email, password, age, weight);
            }
        });
        TextView goToLogin = findViewById(R.id.changeToLogin);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void checkData(EditText username, EditText email, EditText password, EditText age, EditText weight) {
        if (checkEmpty(username)){
            Toast toast = Toast.makeText(this, "You must enter username to register", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (checkEmail(email)){
            email.setError("Email is required");
        }
        if (checkEmail(password)) {
            password.setError("Password is required");
        }
        if(checkEmpty(age)) {
            age.setError("Age is required");
        }
        if(checkEmpty(weight)){
            weight.setError("Weight is required");
        }
    }
    private Boolean checkEmail(EditText emailEdit){
        CharSequence email = emailEdit.getText().toString();
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private Boolean checkEmpty(EditText textEdit) {
        CharSequence text = textEdit.getText().toString();
        return TextUtils.isEmpty(text);
    }


}
