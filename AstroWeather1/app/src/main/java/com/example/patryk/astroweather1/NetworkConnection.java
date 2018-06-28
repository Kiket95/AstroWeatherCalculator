package com.example.patryk.astroweather1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkConnection {
    static Context context;

    public NetworkConnection(Context context){
        this.context = context;
    }

    static public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    static public void NetworkState() {
        if (NetworkConnection.isOnline())
            Toast.makeText(context, "Network is available", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "Network is not available", Toast.LENGTH_LONG).show();
    }

}