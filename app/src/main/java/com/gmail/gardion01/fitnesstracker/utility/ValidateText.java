package com.gmail.gardion01.fitnesstracker.utility;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class ValidateText {
    Context context;

    public ValidateText(Context context) {
        this.context = context;
    }

    public boolean validateEditText(EditText editText, String errorMessage) { //Check edittext
        String text = editText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) { //Check edittext if empty
            editText.setError(errorMessage);
            return false;
        }
        return true;
    }

    public boolean validateEmailEditText(EditText emailEditText, String errorMessage) { //Check valid email edit text
        CharSequence email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { //Check if the input is email
            emailEditText.setError(errorMessage);
            return false;
        }
        return true;
    }
}
