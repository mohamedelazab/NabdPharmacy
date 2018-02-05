package com.example.mohamed.nabdpharmacy.utilities;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by mohamed on 24/10/17.
 */

public class ConnectionUtils {

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null;
    }
}
