package com.mobile.esprit.sensor.background_tasks;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.home_activity.adapters.CategorySmallAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Souhaib on 30/03/2017.
 */

public class CategoryManager {

    Context context;

    public CategoryManager(Context context) {
        this.context = context;
    }


    public void getAllCategory(final RecyclerView recyclerView) {

        final ArrayList<Category> categories = new ArrayList<>();


        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_ALL_CATEGORIES, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONParser jsonParser = new JSONParser();
                for (int i = 0; i < response.length(); i++) {
                    try {

                        categories.add(jsonParser.parseCategory(response.getJSONObject(i)));

                    } catch (JSONException e) {

                    }
                }
                recyclerView.setAdapter(new CategorySmallAdapter(context, categories));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }
}
