package com.mobile.esprit.sensor.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Souhaib on 10/11/2016.
 */

public class ConnectionSingleton {

    private static ConnectionSingleton mInstance;
    private static RequestQueue requestQueue;
    private static Context mCtx;

    private ConnectionSingleton(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();

    }


    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized ConnectionSingleton getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new ConnectionSingleton(context);

        }
        return mInstance;
    }

    public <T> void addToRequestque(Request<T> request) {

        requestQueue.add(request);

    }
}
