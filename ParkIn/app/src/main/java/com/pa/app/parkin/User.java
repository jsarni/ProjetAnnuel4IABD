package com.pa.app.parkin;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;


public class User implements Serializable {

    private int userId;
    private String lastname;
    private String firstname;
    private String phoneNumber;
    private String email;
    private String password;
    private Date subscriptionDate;

    public User(String lastname, String firstname, String phoneNumber, String email, String password) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public User(int userId, String lastname, String firstname, String phoneNumber, String email, String password, String subscriptionDate) {
        this.userId = userId;
        this.lastname = lastname;
        this.firstname = firstname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.subscriptionDate = Date.valueOf(subscriptionDate);
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
}
