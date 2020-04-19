package com.pa.app.parkin;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseConf {

    private static final DatabaseConf database = new DatabaseConf();

    private String serverName = "18.234.214.124";
    private String portNumber = "3306";
    private String sid = "parkin";
    private String username = "admin_parkin";
    private String password = "ParkIN15042020";
    private String url = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + sid + "?user=" + username + "&password=" + password;
//            "jdbc:mysql://" + serverName + ":" + portNumber + ":" + sid;

    public static final DatabaseConf getInstance(){
        return database;
    }

    public static DatabaseConf getDatabase() {
        return database;
    }

    public String getServerName() {
        return serverName;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public String getSid() {
        return sid;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsersTable() {
        return "PARKIN.USER_ACCOUNT";
    }

    public boolean saveUserToDatabase(User user) {
        try{
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);

            DatabaseConf mydb = DatabaseConf.getInstance();

            String url = mydb.getUrl();
            String usersTable = mydb.getUsersTable();

            Connection connection = DriverManager.getConnection(url);

            String insertQuery =
                    "INSERT INTO ? (user_email, user_lastname, user_firstname, user_phone, user_password, user_subscription_date) " +
                            "VALUES(?, ?, ?, ?, ?, ?);";

            PreparedStatement preparedStmt = connection.prepareStatement(insertQuery);
            preparedStmt.setString(1, usersTable );
            preparedStmt.setString(2, user.getEmail());
            preparedStmt.setString(3, user.getLastname());
            preparedStmt.setString(4, user.getFirstname());
            preparedStmt.setString(5, user.getPhoneNumber());
            preparedStmt.setString(6, user.getPassword());
            preparedStmt.setDate(7, user.getSubscriptionDate());

            preparedStmt.execute();

            connection.close();

            return true;
        }
        catch (Exception e){
            Log.e("error", e.getMessage(), e);
            return false;
        }
    }

    public User loadUserFromDatabase(String user_email, String user_password) {
        User user = null;

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("146.88.237.38/script.php");

            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("email", user_email));
            postParameters.add(new BasicNameValuePair("password", user_password));

            httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();

            String result = sb.toString();

            JSONArray users_jArray = new JSONArray(result);

            Log.e("ERROR_JSON", "Length of returned json array" + String.valueOf(users_jArray.length()));
            Log.e("ERROR_JSON", users_jArray.toString());

            if (users_jArray.length() != 1) {

                JSONObject user_data = users_jArray.getJSONObject(0);

                user = new User(
                    user_data.getInt("user_id"),
                    user_data.getString("user_lastname"),
                    user_data.getString("user_first_name"),
                    user_data.getString("user_phone"),
                    user_data.getString("user_email"),
                    user_data.getString("user_password"),
                    Date.valueOf(user_data.getString("user_subscription_date"))
                );
            }

        } catch (Exception e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        } finally {
            return user;
        }
    }

}
