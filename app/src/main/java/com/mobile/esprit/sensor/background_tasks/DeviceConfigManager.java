package com.mobile.esprit.sensor.background_tasks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mobile.esprit.sensor.Entities.DeviceConfig;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.user_profile_activity.adapters.DeviceConfigAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Souhaib on 01/04/2017.
 */

public class DeviceConfigManager {

    private Context context;

    public DeviceConfigManager(Context context) {
        this.context = context;
    }

    public void getDeviceConfigByUser(final RecyclerView recyclerView, final SmoothProgressBar smoothProgressBar) {
        String userId = User.getInstance().getId();
        final JSONParser jsonParser = new JSONParser();
        final ArrayList<DeviceConfig> deviceConfigs = new ArrayList<>();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.GET_USER_DEVICE_CONFIG + userId, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        deviceConfigs.add(jsonParser.parseDeviceConfig(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setAdapter(new DeviceConfigAdapter(context, deviceConfigs));
                smoothProgressBar.setVisibility(View.INVISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }
}
