package com.mobile.esprit.sensor.background_tasks;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.URL;

import org.json.JSONObject;

/**
 * Created by Souhaib on 04/05/2017.
 */

public class BoitierManager {

    Context context;
    AppCompatActivity activity;
    Dialog dialog;

    public BoitierManager(Context context, AppCompatActivity activity){
        this.context = context;
        this.activity = activity;

    }

    public BoitierManager(Context context, AppCompatActivity activity, Dialog dialog){
        this.context = context;
        this.activity = activity;
        this.dialog = dialog;
        
    }


    public void setUserDevice(String macAddress){

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.SET_USER_DEVICE + User.getInstance().getId() + "/" + macAddress, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONParser jsonParser = new JSONParser();
                        if ( response.has("error")){
                            final Snackbar snackBar = Snackbar.make(activity.findViewById(R.id.activity_home), "Device does not exist. Please check device ID.", Snackbar.LENGTH_LONG);
                            snackBar.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackBar.dismiss();
                                }
                            });
                            snackBar.show();
                        }else {
                            
                            User.setCurrentUser(jsonParser.parseUser(response));
                            Toast.makeText(context, "Device successfully identified!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
            }
        });

        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }
    public void setUserDeviceSetting(String macAddress){

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.SET_USER_DEVICE + User.getInstance().getId() + "/" + macAddress, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONParser jsonParser = new JSONParser();
                        if ( response.has("error")){
                            final Snackbar snackBar = Snackbar.make(activity.findViewById(R.id.activity_setting), "Device does not exist. Please check device ID.", Snackbar.LENGTH_LONG);
                            snackBar.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackBar.dismiss();
                                }
                            });
                            snackBar.show();
                        }else {

                            User.setCurrentUser(jsonParser.parseUser(response));
                            activity.finish();
                            activity.startActivity(activity.getIntent());
                            Toast.makeText(context, "Device successfully identified!", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }
}
