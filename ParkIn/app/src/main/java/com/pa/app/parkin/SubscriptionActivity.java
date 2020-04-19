package com.pa.app.parkin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SubscriptionActivity extends Activity {

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
                Log.i("buttons", "Clicked on subscription button");
                if (userPassword.getText().toString().equals(userPasswordConfirmation.getText().toString())) {

                    User user = new User(
                            userLastName.getText().toString(),
                            userFirstName.getText().toString(),
                            userPhoneNumber.getText().toString(),
                            userEmail.getText().toString(),
                            userPassword.getText().toString()
                    );

                    DatabaseConf mydb = DatabaseConf.getInstance();
                    boolean subscriptionResult = mydb.saveUserToDatabase(user);

                    if (subscriptionResult){
                        Log.i("buttons", "clicked on subscription button");

                        Intent successIntent = new Intent(SubscriptionActivity.this, SubscriptionSuccessActivity.class);
                        startActivity(successIntent);
                    } else {
                        Context context = getApplicationContext();
                        CharSequence errorText = getString(R.string.subscription_failure_message);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, errorText, duration);
                        toast.show();
                    }

                } else {
                    Context context = getApplicationContext();
                    CharSequence errorText = getString(R.string.password_confirmation_error);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, errorText, duration);
                    toast.show();
                }
            }
        });
    }
}
