package com.mobile.esprit.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mobile.esprit.sensor.Adapters.ManufacturerAdapter;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManufacturerActivity extends AppCompatActivity {

    ArrayList<Manufacturer> manufacturers;
    ListView lvManufacturer;
    public final static String MANUFACTURER_DETAIL = "manufacturer detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manufacturers");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        manufacturers = new ArrayList<>();
        lvManufacturer = (ListView) findViewById(R.id.lv_manufacturer);


        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_All_MANUFACTURERS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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

                            ManufacturerAdapter manufacturerAdapter = new ManufacturerAdapter(ManufacturerActivity.this, R.layout.item_manufacturer, manufacturers);
                            lvManufacturer.setAdapter(manufacturerAdapter);
                            lvManufacturer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    Intent intent = new Intent(ManufacturerActivity.this, ManufacturerDetailActivity.class);
                                    intent.putExtra(MANUFACTURER_DETAIL, manufacturers.get(i));
                                    startActivity(intent);

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ManufacturerActivity.this, "Error while parsing...", Toast.LENGTH_SHORT).show();

            }
        });

        ConnectionSingleton.getInstance(this).addToRequestque(req);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }




}
