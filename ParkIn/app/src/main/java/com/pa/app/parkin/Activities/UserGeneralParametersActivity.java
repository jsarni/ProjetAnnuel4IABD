package com.pa.app.parkin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.pa.app.parkin.R;

public class UserGeneralParametersActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_general_parameters_activity);

        EditText newLastName = (EditText) findViewById(R.id.user_general_parameters_lastname_edittext);
        EditText newFirstName = (EditText) findViewById(R.id.user_general_parameters_firstname_edittext);
        EditText newPhoneNumber = (EditText) findViewById(R.id.user_general_parameters_phonenumber_edittext);

        Button returnButton = (Button) findViewById(R.id.user_general_parameters_return_button);
        Button validationButton = (Button) findViewById(R.id.user_general_parameters_validation_button);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(UserGeneralParametersActivity.this, UserActivity.class);
                startActivity(mapIntent);
            }
        });
    }
}
