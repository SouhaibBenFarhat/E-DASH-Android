package com.mobile.esprit.sensor.background_tasks;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mobile.esprit.sensor.AddCustomRecipeActivity.adapters.BaseAdapter;
import com.mobile.esprit.sensor.Entities.Base;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.URL;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Souhaib on 21/04/2017.
 */

public class BaseManager {

    Context context;

    public BaseManager(Context context) {
        this.context = context;
    }

    public void getAllBases(final RecyclerView recyclerView) {

        final JSONParser jsonParser = new JSONParser();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_ALL_BASES, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                ArrayList<Base> bases = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        bases.add(jsonParser.parseBase(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(new BaseAdapter(context, bases));


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
