package com.pa.app.parkin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.pa.app.parkin.R;


public class UserUpdateSuccessActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_update_success_activity);

        Button redirectionButton = (Button) findViewById(R.id.user_update_to_profile_button);

        redirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent welcomeIntent = new Intent(UserUpdateSuccessActivity.this, UserActivity.class);
                startActivity(welcomeIntent);
            }
        });
    }
}
