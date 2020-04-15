package com.pa.app.parkin;

public class DatabaseConf {

    private static final DatabaseConf database = new DatabaseConf();

    private String serverName = "127.0.0.1";
    private String portNumber = "1521";
    private String sid = "parkin";
    private String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
    private String username = "parkin_admin";
    private String password = "Parkin_admin";
    private String usersTable = "PARKIN.USER_ACCOUNT";

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
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsersTable() {
        return usersTable;
    }
}
