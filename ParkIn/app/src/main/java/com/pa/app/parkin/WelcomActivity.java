package com.pa.app.parkin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WelcomActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcom_activity);

        Button connexionButton = (Button) findViewById(R.id.connexion_button);
        final Button subscriptionButton = (Button) findViewById(R.id.subscription_button);

        connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("buttons", "clicked on connexion button");

                Intent connectionIntent = new Intent(WelcomActivity.this, ConnectionActivity.class);
                startActivity(connectionIntent);
            }
        });

        subscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("buttons", "clicked on subscription button");

                Intent subscriptionIntent = new Intent(WelcomActivity.this, SubscriptionActivity.class);
                startActivity(subscriptionIntent);
            }
        });
    }
}
