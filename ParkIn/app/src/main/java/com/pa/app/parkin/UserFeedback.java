package com.pa.app.parkin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserFeedback {
    public String userID;
    public String lat;
    public String lng;
    public String date;
    public String hour;
    public String status;
    public String userValidationDate;

    public UserFeedback(int userID, double lat, double lng, Calendar searchTime, int status) {
        this.userID = String.valueOf(userID);
        this.lat = String.valueOf(lat);
        this.lng = String.valueOf(lng);
        this.date = String.format("%02d/02%d/%d", searchTime.get(Calendar.DAY_OF_MONTH), searchTime.get(Calendar.MONTH), searchTime.get(Calendar.YEAR));
        this.hour = String.format("%02d:02%d", searchTime.get(Calendar.HOUR_OF_DAY), searchTime.get(Calendar.MINUTE));
        this.status = String.valueOf(status);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        userValidationDate = format.format(new Date());
    }
}
