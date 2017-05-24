package com.mobile.esprit.sensor.background_tasks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.add_aroma_activity.AddAromaActivity;
import com.mobile.esprit.sensor.user_aromas_activities.UserAromasActivity;
import com.mobile.esprit.sensor.user_aromas_activities.adapters.UserAromaAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Souhaib on 30/03/2017.
 */

public class AromaManager {

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    private Context context;

    public AromaManager(Context context) {
        this.context = context;
    }


    public void getUserAroma(final RecyclerView recyclerView, final SmoothProgressBar smoothProgressBar) {

        final ArrayList<Aroma> aromas = new ArrayList<>();
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.FIND_AROMA_BY_USER + User.getInstance().getId(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONParser jsonParser = new JSONParser();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        aromas.add(jsonParser.parseAroma(response.getJSONObject(i)));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                UserAromaAdapter userAromaAdapter = new UserAromaAdapter(context, aromas);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(userAromaAdapter);
                if (smoothProgressBar != null){
                    smoothProgressBar.setVisibility(View.INVISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

    public void addUserAroma(Aroma aroma) {

        JSONObject jsonAroma = new JSONObject();
        try {
            jsonAroma.put("arome", aroma.getName());
            jsonAroma.put("imageUrl", "");
            jsonAroma.put("description", aroma.getDescription());
            jsonAroma.put("date", now());
            jsonAroma.put("enabled", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int manufactureId = AddAromaActivity.choosenManufacturer.getIdManufacturer();
        int categoryId = AddAromaActivity.choosenCategory.getIdCategory();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.ADD_USER_AROMA + User.getInstance().getId() + "/" + manufactureId + "/" + categoryId, jsonAroma, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, UserAromasActivity.class));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

        ConnectionSingleton.getInstance(context).addToRequestque(req);

    }
}
