package com.greylabs.yoda.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Jaybhay Vijay on 8/21/2015.
 */
public class ConnectionUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showNetworkNotAvailableDialog(Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage("Check your Internet Connection and try again");
        builder.setPositiveButton("Ok",null);
        builder.create().show();
    }
}