package com.pa.app.parkin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.pa.app.parkin.DatabaseTasks.LoadUserTask;
import com.pa.app.parkin.R;
import com.pa.app.parkin.User;
import com.pa.app.parkin.Utils.DevUtils;

public class ConnectionActivity extends Activity {

    public static User appUser = null;
    DevUtils devUtils = DevUtils.getInstance();

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

                LoadUserTask myLoadTask = new LoadUserTask();

                try {
                    Log.i("Connexion", "launched load Task");
                    appUser = myLoadTask.execute(
                            userEmail.getText().toString(),
                            userPassword.getText().toString()
                    ).get();
                    Log.i("Connexion", "finished load Task with no error");
                } catch (Exception e) {
                    Log.i("Connexion", "finished load Task with error");
                    Log.i("LoadingException", e.getMessage());
                }


                if (appUser == null) {
                    devUtils.showToast(ConnectionActivity.this, getString(R.string.connexion_error_message));

                } else {
                    Intent mapIntent = new Intent(ConnectionActivity.this, MapsActivity.class);
                    startActivity(mapIntent);
                }
            }
        });
    }
}
