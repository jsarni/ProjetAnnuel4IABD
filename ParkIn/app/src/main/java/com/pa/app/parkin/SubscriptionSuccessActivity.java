package com.pa.app.parkin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class SubscriptionSuccessActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_success_activity);

        Button redirectionButton = (Button) findViewById(R.id.inscription_to_connexion);

        redirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("buttons", "clicked on connexion redirection button");

                Intent welcomeIntent = new Intent(SubscriptionSuccessActivity.this, WelcomActivity.class);
                startActivity(welcomeIntent);
            }
        });
    }
}
