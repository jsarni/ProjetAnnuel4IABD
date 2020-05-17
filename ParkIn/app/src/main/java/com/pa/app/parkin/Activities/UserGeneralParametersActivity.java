package com.pa.app.parkin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.pa.app.parkin.DataTasks.UpdateUserTask;
import com.pa.app.parkin.R;
import com.pa.app.parkin.Utils.DevUtils;

public class UserGeneralParametersActivity extends Activity {

    DevUtils devUtils = DevUtils.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_general_parameters_activity);

        final EditText newLastName = (EditText) findViewById(R.id.user_general_parameters_lastname_edittext);
        final EditText newFirstName = (EditText) findViewById(R.id.user_general_parameters_firstname_edittext);
        final EditText newPhoneNumber = (EditText) findViewById(R.id.user_general_parameters_phonenumber_edittext);

        Button returnButton = (Button) findViewById(R.id.user_general_parameters_return_button);
        Button validationButton = (Button) findViewById(R.id.user_general_parameters_validation_button);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(UserGeneralParametersActivity.this, UserActivity.class);
                startActivity(mapIntent);
            }
        });

        validationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFirstNameContent = ConnectionActivity.appUser.getFirstname();
                String newLastNameContent = ConnectionActivity.appUser.getLastname();
                String newPhoneNumberContent = ConnectionActivity.appUser.getPhoneNumber();;

                int numberChangedFields = 0;

                if(newFirstName.getText().length() != 0){
                    newFirstNameContent = newFirstName.getText().toString();
                    numberChangedFields += 1;
                }

                if(newLastName.getText().length() != 0){
                    newLastNameContent = newLastName.getText().toString();
                    numberChangedFields += 1;
                }

                if(newPhoneNumber.getText().length() != 0){
                    newPhoneNumberContent = newPhoneNumber.getText().toString();
                    numberChangedFields += 1;
                }

                if (numberChangedFields == 0) {
                    devUtils.showToast(UserGeneralParametersActivity.this, getString(R.string.general_parameters_unchange_toast_message));
                } else {
                    ConnectionActivity.appUser.setLastname(newLastNameContent);
                    ConnectionActivity.appUser.setFirstname(newFirstNameContent);
                    ConnectionActivity.appUser.setPhoneNumber(newPhoneNumberContent);

                    UpdateUserTask myUpdateTask = new UpdateUserTask();

                    String subscriptionResult = "1";
                    try {
                        subscriptionResult = myUpdateTask.execute(ConnectionActivity.appUser).get();
                    } catch (Exception e) {
                        Log.e("UpdateUserError", e.getMessage());
                    }

                    if (subscriptionResult.equals("0")){
                        Log.i("buttons", "clicked on subscription button");

                        Intent successIntent = new Intent(UserGeneralParametersActivity.this, UserUpdateSuccessActivity.class);
                        startActivity(successIntent);
                    } else {
                        devUtils.showToast(UserGeneralParametersActivity.this, getString(R.string.user_update_error_toast_message));
                    }
                }
            }
        });
    }
}
