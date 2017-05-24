package com.mobile.esprit.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mobile.esprit.sensor.Adapters.AromaAdapter;
import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class AromaActivity extends AppCompatActivity {

    ArrayList<Aroma> aromas;
    ListView lvAroma;
    public static final String AROMA_DETAIL = "aroma detail";
    public static ArrayList<Aroma> globalAromas;
    SmoothProgressBar smoothProgressBar;
    private EditText etAromaSearch;
    private ArrayList<Aroma> filtredAromas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aroma);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Aromas");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        smoothProgressBar = (SmoothProgressBar) findViewById(R.id.smoothProgressBar);
        if (smoothProgressBar != null) {
            smoothProgressBar.setVisibility(View.VISIBLE);
            smoothProgressBar.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(this)
                    .colors(getResources().getIntArray(R.array.colors)).build());
        }




        aromas = new ArrayList<>();
        lvAroma = (ListView) findViewById(R.id.lv_aroma);




        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_All_AROMAS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jaroma = response.getJSONObject(i);
                                Aroma aroma = new Aroma();
                                Manufacturer manufacturer = new Manufacturer();
                                Category category = new Category();

                                aroma.setIdAroma(jaroma.getInt("id"));
                                aroma.setName(jaroma.getString("arome"));
                                aroma.setImgArome(jaroma.getString("imageUrl"));
                                aroma.setDescription(jaroma.getString("description"));
                                aroma.setDate(jaroma.getString("date"));

                                JSONObject jmanufacturer = jaroma.getJSONObject("manufacture");
                                manufacturer.setIdManufacturer(jmanufacturer.getInt("id"));
                                manufacturer.setName(jmanufacturer.getString("name"));
                                manufacturer.setDescription(jmanufacturer.getString("description"));
                                manufacturer.setImage(jmanufacturer.getString("imageUrl"));
                                manufacturer.setAddress(jmanufacturer.getString("address"));
                                manufacturer.setDate(jmanufacturer.getString("date"));
                                aroma.setManufacturer(manufacturer);

                                JSONObject jcategory = jaroma.getJSONObject("category");
                                category.setIdCategory(jcategory.getInt("id"));
                                category.setName(jcategory.getString("category"));
                                category.setImageName(jcategory.getString("imageName"));
                                category.setDescription(jcategory.getString("description"));
                                category.setImage(jcategory.getString("imageUrl"));
                                category.setDate(jcategory.getString("date"));
                                aroma.setCategory(category);

                                aromas.add(aroma);


                            }

                            globalAromas = aromas;

                            filtredAromas = new ArrayList<>();



                            etAromaSearch = (EditText) findViewById(R.id.et_aroma_search);

                            etAromaSearch.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    filtredAromas.clear();
                                    for (Aroma a : aromas){


                                        if (a.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                            filtredAromas.add(a);
                                        }
                                    }
                                    AromaAdapter aromaAdapter = new AromaAdapter(AromaActivity.this, R.layout.item_aroma, filtredAromas);
                                    lvAroma.setAdapter(aromaAdapter);
                                    lvAroma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            Intent intent = new Intent(AromaActivity.this, AromaDetailActivity.class);
                                            intent.putExtra(AROMA_DETAIL, filtredAromas.get(i));
                                            startActivity(intent);
                                        }
                                    });

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                    if(editable.toString().toLowerCase().equals("")){
                                        AromaAdapter aromaAdapter = new AromaAdapter(AromaActivity.this, R.layout.item_aroma, aromas);
                                        lvAroma.setAdapter(aromaAdapter);

                                        lvAroma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent = new Intent(AromaActivity.this, AromaDetailActivity.class);
                                                intent.putExtra(AROMA_DETAIL, aromas.get(i));
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                }
                            });

                            AromaAdapter aromaAdapter = new AromaAdapter(AromaActivity.this, R.layout.item_aroma, aromas);
                            lvAroma.setAdapter(aromaAdapter);



                            smoothProgressBar.setVisibility(View.INVISIBLE);

                            lvAroma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(AromaActivity.this, AromaDetailActivity.class);
                                    intent.putExtra(AROMA_DETAIL, aromas.get(i));
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
                Toast.makeText(AromaActivity.this, "Error while parsing...", Toast.LENGTH_SHORT).show();

            }
        }
        );

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
