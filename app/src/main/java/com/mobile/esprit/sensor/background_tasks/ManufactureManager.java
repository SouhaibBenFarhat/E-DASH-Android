package com.mobile.esprit.sensor.background_tasks;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.user_aromas_activities.adapters.SmallManufactureAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Souhaib on 30/03/2017.
 */

public class ManufactureManager {

    private Context context;

    public ManufactureManager(Context context) {
        this.context = context;
    }


    public void getAllManufacture(final RecyclerView recyclerView, final EditText editText) {

        final ArrayList<Manufacturer> manufacturers = new ArrayList<>();
        final JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_All_MANUFACTURERS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                final ArrayList<Manufacturer> filtredManufactrer = new ArrayList<>();

                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jmanufacturer = response.getJSONObject(i);
                        Manufacturer manufacturer = new Manufacturer();

                        manufacturer.setIdManufacturer(jmanufacturer.getInt("id"));
                        manufacturer.setName(jmanufacturer.getString("name"));
                        manufacturer.setAddress(jmanufacturer.getString("address"));
                        manufacturer.setDate(jmanufacturer.getString("date"));
                        manufacturer.setDescription(jmanufacturer.getString("description"));
                        manufacturer.setImage(jmanufacturer.getString("imageUrl"));

                        manufacturers.add(manufacturer);
                    }


                } catch (JSONException e) {

                }
                recyclerView.setAdapter(new SmallManufactureAdapter(context, manufacturers));
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        filtredManufactrer.clear();
                        for (Manufacturer m : manufacturers) {
                            if (m.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                filtredManufactrer.add(m);
                            }
                        }
                        recyclerView.setAdapter(new SmallManufactureAdapter(context, filtredManufactrer));
                        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        if (editable.toString().equals("")) {
                            recyclerView.setAdapter(new SmallManufactureAdapter(context, manufacturers));
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        }
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }
}
