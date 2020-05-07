package com.pa.app.parkin.Utils;

public class PermissionManager {

    public int COARSE_AND_FINE_LOCATION_PERMISSION_CODE = 1;

    private static PermissionManager INSTANCE = new PermissionManager();

    public static PermissionManager getInstance()
    {
        return INSTANCE;
    }
}
