package com.pa.app.parkin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.pa.app.parkin.R;

public class UserConnectionParametersActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_connection_parameters_activity);



        EditText newEmail = (EditText) findViewById(R.id.user_connection_parameters_email_edittext);
        EditText newPassword = (EditText) findViewById(R.id.user_connection_parameters_password_edittext);
        EditText newPasswordConfirmation = (EditText) findViewById(R.id.user_connection_parameters_password_confirmation_edittext);

        Button returnButton = (Button) findViewById(R.id.user_connection_parameters_return_button);
        Button validationButton = (Button) findViewById(R.id.user_connection_parameters_validation_button);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(UserConnectionParametersActivity.this, UserActivity.class);
                startActivity(mapIntent);
            }
        });
    }
}
