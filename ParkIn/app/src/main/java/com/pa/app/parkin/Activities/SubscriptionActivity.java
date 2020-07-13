package com.pa.app.parkin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.pa.app.parkin.DataTasks.SaveUserTask;
import com.pa.app.parkin.R;
import com.pa.app.parkin.User;
import com.pa.app.parkin.Utils.DevUtils;


public class SubscriptionActivity extends Activity {

    DevUtils devUtils = DevUtils.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity);

        Button validationButton = (Button) findViewById(R.id.subscription_validation_button);

        final EditText userLastName = (EditText) findViewById(R.id.lastname_input_box);
        final EditText userFirstName = (EditText) findViewById(R.id.firstname_input_box);
        final EditText userPhoneNumber = (EditText) findViewById(R.id.phone_input_box);
        final EditText userEmail = (EditText) findViewById(R.id.email_input_box);
        final EditText userPassword = (EditText) findViewById(R.id.password_input_box);
        final EditText userPasswordConfirmation = (EditText) findViewById(R.id.password_confirmation_input_box);

        validationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPassword.getText().toString().equals(userPasswordConfirmation.getText().toString())) {

                    User user = new User(
                            userLastName.getText().toString(),
                            userFirstName.getText().toString(),
                            userPhoneNumber.getText().toString(),
                            userEmail.getText().toString(),
                            userPassword.getText().toString()
                    );

                    ConnectionActivity.appUser = user;

                    SaveUserTask mySaveTask = new SaveUserTask();

                    String subscriptionResult = "2";
                    try {
                        subscriptionResult = mySaveTask.execute(user).get();
                    } catch (Exception e) {
                        Log.e("SubscriptionError", e.getMessage());
                    }

                    if (subscriptionResult.equals("0")){
                        Intent successIntent = new Intent(SubscriptionActivity.this, SubscriptionSuccessActivity.class);
                        startActivity(successIntent);
                    } else {
                        if (subscriptionResult.equals("2")){
                             devUtils.showToast(SubscriptionActivity.this, getString(R.string.subscription_failure_message_1));
                        } else {
                            devUtils.showToast(SubscriptionActivity.this, getString(R.string.subscription_failure_message_2));
                        }
                    }

                } else {
                    devUtils.showToast(SubscriptionActivity.this, getString(R.string.password_confirmation_error));
                }
            }
        });
    }
}
