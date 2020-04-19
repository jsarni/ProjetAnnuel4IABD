package com.pa.app.parkin;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.sql.Date;
import java.util.Objects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class User implements Serializable {

    private int userId;
    private String lastname;
    private String firstname;
    private String phoneNumber;
    private String email;
    private String password;
    private Date subscriptionDate;

//    @SuppressLint("NewApi")
    public User(String lastname, String firstname, String phoneNumber, String email, String password) {
//        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String pattern = "yyyy/MM/dd HH:mm:ss";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String subscriptionDate = dateFormat.format(calendar.getTime().getTime());



        this.lastname = lastname;
        this.firstname = firstname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.subscriptionDate = null;
//                Date.valueOf(subscriptionDate);
//                LocalDateTime.parse(dateFormat.format(LocalDateTime.now()));
    }

    public User(int userId, String lastname, String firstname, String phoneNumber, String email, String password, Date subscriptionDate) {
        this.userId = userId;
        this.lastname = lastname;
        this.firstname = firstname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.subscriptionDate = subscriptionDate;
    }

    public int getUserId() {
        return userId;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getLastname().equals(user.getLastname()) &&
                getFirstname().equals(user.getFirstname()) &&
                getPhoneNumber().equals(user.getPhoneNumber()) &&
                getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLastname(), getFirstname(), getPhoneNumber(), getEmail());
    }

    @Override
    public String toString() {
        return "User{" +
                "lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", phonenumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public boolean saveUserToDatabase(User user) {
        try{
            String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);

            DatabaseConf mydb = DatabaseConf.getInstance();

            String url = mydb.getUrl();
            String username = mydb.getUsername();
            String password = mydb.getPassword();
            String usersTable = mydb.getUsersTable();

            Connection connection = DriverManager.getConnection(url, username, password);

            String insertQuery =
                    "INSERT INTO ? (user_email, user_lastname, user_firstname, user_phone, user_password, user_subscription_date) " +
                            "VALUES(?, ?, ?, ?, ?, ?);";

            PreparedStatement preparedStmt = connection.prepareStatement(insertQuery);
            preparedStmt.setString(1, usersTable );
            preparedStmt.setString(2, this.getEmail());
            preparedStmt.setString(3, this.getLastname());
            preparedStmt.setString(4, this.getFirstname());
            preparedStmt.setString(5, this.getPhoneNumber());
            preparedStmt.setString(6, this.password);
            preparedStmt.setDate(7, this.getSubscriptionDate());

            preparedStmt.execute();

            connection.close();

            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public User loadUserFromDatabase(String user_email, String user_password){
        try{
            String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);

            DatabaseConf mydb = DatabaseConf.getInstance();

            String url = mydb.getUrl();
            String username = mydb.getUsername();
            String password = mydb.getPassword();
            String usersTable = mydb.getUsersTable();

            Connection connection = DriverManager.getConnection(url, username, password);

            String insertQuery = "SELECT * FROM ? WHERE user_email = ? AND user_password = ?;";

            PreparedStatement preparedStmt = connection.prepareStatement(insertQuery);
            preparedStmt.setString (1, usersTable);
            preparedStmt.setString (2, user_email);
            preparedStmt.setString (3, user_password);

            ResultSet result = preparedStmt.executeQuery();

            User user = null;

            if(result.first()){
                user = new User(
                        result.getInt("user_id"),
                        result.getString("user_lastname"),
                        result.getString("user_first_name"),
                        result.getString("user_phone"),
                        result.getString("user_email"),
                        result.getString("user_password"),
                        result.getDate("user_subscription_date")
                );
                return user;
            }
            connection.close();

            return user;
        }
        catch (Exception e){
            return null;
        }
    }
}
