package com.pa.app.parkin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pa.app.parkin.R;
import com.pa.app.parkin.User;

public class UserActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        User appUser = ConnectionActivity.appUser;

        TextView userInterfaceMessage = (TextView) findViewById(R.id.user_interface_message);
        userInterfaceMessage.setText(getString(R.string.user_interface_message,appUser.getFirstname()));

        Button generalParametersButton = (Button) findViewById(R.id.user_general_parameters_button);
        Button connectionParametersButton = (Button) findViewById(R.id.user_connection_parameters_button);
        Button generalParametersReturnButton = (Button) findViewById(R.id.user_interface_return_button);

        generalParametersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(UserActivity.this, UserGeneralParametersActivity.class);
                startActivity(mapIntent);
            }
        });

        connectionParametersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(UserActivity.this, UserConnectionParametersActivity.class);
                startActivity(mapIntent);
            }
        });


        generalParametersReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(UserActivity.this, MapsActivity.class);
                startActivity(mapIntent);
            }
        });
    }
}
