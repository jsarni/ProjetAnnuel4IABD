package com.pa.app.parkin.DataTasks;

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

    private String connexionScript = "http://ec2-54-174-245-36.compute-1.amazonaws.com/user_connection.php";

    @Override
    protected User doInBackground(String... strings) {
        User user = null;

        if (strings.length != 2) {
            return null;
        } else {

            try {
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8");

                BufferedReader reader;

                URL url = new URL(connexionScript);

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String result = sb.toString();
                JSONArray users_jArray = new JSONArray(result);

                if (users_jArray.length() == 1) {

                    JSONObject user_data = users_jArray.getJSONObject(0);

                    user = new User(
                            user_data.getInt("user_id"),
                            user_data.getString("user_lastname"),
                            user_data.getString("user_firstname"),
                            user_data.getString("user_phone"),
                            user_data.getString("user_email"),
                            user_data.getString("user_password"),
                            user_data.getString("user_subscription_date")
                    );
                }

            } catch (Exception ex) {
                Log.e("LoadUserError", ex.getMessage());
            } finally {
                return user;
            }
        }
    }
}
