package com.mobile.esprit.sensor.services;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mobile.esprit.sensor.Utils.URL;

/**
 * Created by Souhaib on 06/02/2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance()
                .getToken();
        System.out.println("********************************************************************************************************************************************");
        System.out.println(token);
        System.out.println("********************************************************************************************************************************************");

        SharedPreferences.Editor editor = getSharedPreferences(URL.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.commit();
    }


}
