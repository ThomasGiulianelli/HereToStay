package com.giulianelli.heretostay;

import android.content.Context;
import android.content.pm.PackageManager;
import android.app.Activity;

public class MyUtilities extends Activity {

    //ascertains if the device has a camera
    public static boolean checkForCamera(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
