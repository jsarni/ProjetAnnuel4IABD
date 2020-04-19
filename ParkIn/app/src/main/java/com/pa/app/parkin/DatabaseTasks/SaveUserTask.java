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

public class SaveUserTask extends AsyncTask<User, Void, Boolean> {

    private String subscriptionScript = "http://projetannuel4iabd.yj.fr/insert_test.php";

    @Override
    protected Boolean doInBackground(User... users) {
        Boolean insertSuccess = false;
        if (users.length != 1) {
            return false;
        } else {

            User myUser = users[0];
            try {
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(myUser.getEmail(), "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(myUser.getPassword(), "UTF-8");
                data += "&" + URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(myUser.getFirstname(), "UTF-8");
                data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(myUser.getLastname(), "UTF-8");
                data += "&" + URLEncoder.encode("phonenumber", "UTF-8") + "=" + URLEncoder.encode(myUser.getPhoneNumber(), "UTF-8");

                BufferedReader reader;

                // Defined URL  where to send data
                Log.i("Connexion", "URL Createing");

                URL url = new URL(subscriptionScript);
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
                JSONArray result_jArray = new JSONArray(result);

                JSONObject insert_result_info = result_jArray.getJSONObject(0);

                if (insert_result_info.getString("code").equals("success")){
                    insertSuccess = true;
                } else {
                    insertSuccess = false;
                }
            } catch (Exception ex) {
                Log.i("log_tag", "Error " + ex.toString());
            } finally {
                return insertSuccess;
            }
        }
    }
}
