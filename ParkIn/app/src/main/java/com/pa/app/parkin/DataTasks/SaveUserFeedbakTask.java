package com.pa.app.parkin.DataTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pa.app.parkin.UserFeedback;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SaveUserFeedbakTask extends AsyncTask<UserFeedback, Void, Boolean> {

    private String feedbackSaveScript = "http://projetannuel4iabd.yj.fr/user_feedback.php";

    @Override
    protected Boolean doInBackground(UserFeedback... feedbacks) {

        boolean res = false;

        UserFeedback myfeedback = feedbacks[0];
        try {
            String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(myfeedback.userID, "UTF-8");
            data += "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(myfeedback.lat, "UTF-8");
            data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(myfeedback.lng, "UTF-8");
            data += "&" + URLEncoder.encode("day", "UTF-8") + "=" + URLEncoder.encode(myfeedback.date, "UTF-8");
            data += "&" + URLEncoder.encode("hour", "UTF-8") + "=" + URLEncoder.encode(myfeedback.hour, "UTF-8");
            data += "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(myfeedback.status, "UTF-8");
            data += "&" + URLEncoder.encode("validation_datetime", "UTF-8") + "=" + URLEncoder.encode(myfeedback.userValidationDate, "UTF-8");

            BufferedReader reader;

            URL url = new URL(feedbackSaveScript);

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            res = true;
        } catch (Exception ex) {
            Log.i("log_tag", "Error " + ex.toString());
        } finally {
            return res;
        }
    }
}
