package com.pa.app.parkin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.pa.app.parkin.DatabaseTasks.UpdateUserTask;
import com.pa.app.parkin.R;
import com.pa.app.parkin.Utils.DevUtils;

public class UserConnectionParametersActivity extends Activity {

    DevUtils devUtils = DevUtils.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_connection_parameters_activity);



        final EditText newEmail = (EditText) findViewById(R.id.user_connection_parameters_email_edittext);
        final EditText newPassword = (EditText) findViewById(R.id.user_connection_parameters_password_edittext);
        final EditText newPasswordConfirmation = (EditText) findViewById(R.id.user_connection_parameters_password_confirmation_edittext);

        Button returnButton = (Button) findViewById(R.id.user_connection_parameters_return_button);
        Button validationButton = (Button) findViewById(R.id.user_connection_parameters_validation_button);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(UserConnectionParametersActivity.this, UserActivity.class);
                startActivity(mapIntent);
            }
        });

        validationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("debug", "entered button");

                String newEmailContent = ConnectionActivity.appUser.getEmail();
                String newPasswordContent = ConnectionActivity.appUser.getPassword();

                int numberChangedFields = 0;
                boolean passwordConfirmed = false;

                if(newEmail.getText().length() != 0){
                    newEmailContent = newEmail.getText().toString();
                    numberChangedFields += 1;
                }

                if(newPassword.getText().length() != 0){
                    numberChangedFields += 1;
                    if(newPassword.getText().toString().equals(newPasswordConfirmation.getText().toString())){
                        newPasswordContent = newPassword.getText().toString();
                        passwordConfirmed = true;
                    } else {
                        devUtils.showToast(UserConnectionParametersActivity.this, getString(R.string.password_confirmation_error));
                    }
                }

                if (numberChangedFields == 0) {
                    devUtils.showToast(UserConnectionParametersActivity.this, getString(R.string.general_parameters_unchange_toast_message));
                } else if(passwordConfirmed){
                    ConnectionActivity.appUser.setPassword(newPasswordContent);
                    ConnectionActivity.appUser.setEmail(newEmailContent);

                    UpdateUserTask myUpdateTask = new UpdateUserTask();

                    String subscriptionResult = "1";
                    try {
                        subscriptionResult = myUpdateTask.execute(ConnectionActivity.appUser).get();
                    } catch (Exception e) {
                        Log.e("UpdateUserError", e.getMessage());
                    }

                    if (subscriptionResult.equals("0")){
                        Log.i("buttons", "clicked on subscription button");

                        Intent successIntent = new Intent(UserConnectionParametersActivity.this, UserUpdateSuccessActivity.class);
                        startActivity(successIntent);
                    } else {
                        devUtils.showToast(UserConnectionParametersActivity.this, getString(R.string.user_update_error_toast_message));
                    }
                }
            }
        });
    }
}
