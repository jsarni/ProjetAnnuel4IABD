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

public class ConnectionActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion_activity);

        Button connectionButton = (Button) findViewById(R.id.connexion_button);

        final EditText userEmail = (EditText) findViewById(R.id.con_email);
        final EditText userPassword = (EditText) findViewById(R.id.con_password);

        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("buttons", "clicked on connexion button to validate connexion");

                DatabaseConf mydb = DatabaseConf.getInstance();

                Log.i("buttons", userEmail.getText().toString());
                Log.i("buttons", userPassword.getText().toString());
                User user = mydb.loadUserFromDatabase(
                        userEmail.getText().toString(),
                        userPassword.getText().toString()
                );


                if (user == null) {
                    Context context = getApplicationContext();
                    CharSequence errorText = getString(R.string.connexion_error_message);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, errorText, duration);
                    toast.show();
                } else {
                    Intent mapIntent = new Intent(ConnectionActivity.this, MapsActivity.class);
                    startActivity(mapIntent);
                }
            }
        });
    }
}
