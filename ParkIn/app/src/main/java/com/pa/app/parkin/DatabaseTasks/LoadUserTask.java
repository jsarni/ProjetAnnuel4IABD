package com.pa.app.parkin.DatabaseTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pa.app.parkin.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoadUserTask extends AsyncTask<String, Void, User> {

    private String connexionScript = "http://projetannuel4iabd.yj.fr/user_connection.php";

    @Override
    protected User doInBackground(String... strings) {
        User user = null;

        if (strings.length != 2) {
            return null;
        } else {

            Log.i("Connexion", "Started");
            try {
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8");

                BufferedReader reader;

                // Defined URL  where to send data
                Log.i("Connexion", "URL Createing");

                URL url = new URL(connexionScript);
                Log.i("Connexion", "URL Created");

                // Send POST data request

                URLConnection conn = url.openConnection();
                Log.i("Connexion", "Connexion Opened");
                conn.setDoOutput(true);
                Log.i("Connexion", "1");
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                Log.i("Connexion", "2");
                wr.write(data);
                Log.i("Connexion", "3");
                wr.flush();
                Log.i("Connexion", "4");

                // Get the server response

                Log.i("Connexion", "Started reading");
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "\n");
                }

                String result = sb.toString();

                Log.i("Connexion", "Transforming to json");
                JSONArray users_jArray = new JSONArray(result);

                Log.i("Connecion", "Length of returned json array" + String.valueOf(users_jArray.length()));
                Log.i("Connexion", users_jArray.toString());

                if (users_jArray.length() == 1) {

                    Log.i("Connexion", "Getting user data");
                    JSONObject user_data = users_jArray.getJSONObject(0);

                    Log.i("Connexion", "Creating user");
                    user = new User(
                            user_data.getInt("user_id"),
                            user_data.getString("user_lastname"),
                            user_data.getString("user_firstname"),
                            user_data.getString("user_phone"),
                            user_data.getString("user_email"),
                            user_data.getString("user_password"),
                            user_data.getString("user_subscription_date")
                    );
                    Log.i("Connexion", "User Created");
                }

            } catch (Exception ex) {
                Log.i("log_tag", "Error " + ex.getMessage() + ex.toString());
            } finally {
                return user;
            }
        }
    }
}
