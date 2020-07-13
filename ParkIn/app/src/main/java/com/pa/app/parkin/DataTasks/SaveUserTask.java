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

public class SaveUserTask extends AsyncTask<User, Void, String> {

    private String subscriptionScript = "http://ec2-54-174-245-36.compute-1.amazonaws.com/user_inscription.php";

    @Override
    protected String doInBackground(User... users) {
        String insertResult = "2";
        if (users.length != 1) {
            return "2";
        } else {

            User myUser = users[0];
            try {
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(myUser.getEmail(), "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(myUser.getPassword(), "UTF-8");
                data += "&" + URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(myUser.getFirstname(), "UTF-8");
                data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(myUser.getLastname(), "UTF-8");
                data += "&" + URLEncoder.encode("phonenumber", "UTF-8") + "=" + URLEncoder.encode(myUser.getPhoneNumber(), "UTF-8");

                BufferedReader reader;

                URL url = new URL(subscriptionScript);

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
                JSONArray result_jArray = new JSONArray(result);

                JSONObject insert_result_info = result_jArray.getJSONObject(0);
                insertResult = insert_result_info.getString("code");
            } catch (Exception ex) {
                Log.e("InscriptionError", ex.toString());
            } finally {
                return insertResult;
            }
        }
    }
}
