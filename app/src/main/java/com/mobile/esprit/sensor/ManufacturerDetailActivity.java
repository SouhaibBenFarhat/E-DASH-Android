package com.mobile.esprit.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.mobile.esprit.sensor.Utils.ObservableScrollView;
import com.mobile.esprit.sensor.Utils.URL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManufacturerDetailActivity extends AppCompatActivity implements ObservableScrollView.OnScrollChangedListener{

    private ImageView imgDetailManufacturer;
    private TextView tvDetailManufacturerName, tvDetailManufacturerAddress, tvDetailManufacturerDescription;
    private Manufacturer manuf;
    private ListView lvDetailManufacturerAroma;
    private ArrayList<Aroma> aromas;

    public static final String MANUFACTURER_AROMA_DETAIL = "manufacturer aroma detail";

    private ObservableScrollView mScrollView;
    private View imgContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer_detail);


        mScrollView = (ObservableScrollView)findViewById(R.id.scroll_view);
        mScrollView.setOnScrollChangedListener(this);

        manuf = new Manufacturer();

        Bundle bundle = getIntent().getExtras();
        manuf = bundle.getParcelable(ManufacturerActivity.MANUFACTURER_DETAIL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        aromas = new ArrayList<>();



        tvDetailManufacturerName = (TextView) findViewById(R.id.tv_detail_manufacturer_name);
        tvDetailManufacturerAddress = (TextView) findViewById(R.id.tv_detail_manufacturer_address);
        tvDetailManufacturerDescription = (TextView) findViewById(R.id.tv_detail_manufacturer_description);
        imgDetailManufacturer = (ImageView) findViewById(R.id.iv_manufacturer_detail);
        lvDetailManufacturerAroma = (ListView) findViewById(R.id.lv_detail_manufacturer_aroma);



        tvDetailManufacturerName.setText(manuf.getName());
        tvDetailManufacturerAddress.setText(manuf.getAddress());
        tvDetailManufacturerDescription.setText(manuf.getDescription());

        Picasso.with(this).load(manuf.getImage()).fit().into(
                imgDetailManufacturer
        );


        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_ALL_AROMAS_BY_MANUFACTURER + manuf.getIdManufacturer(), null,
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

                            AromaAdapter adapter = new AromaAdapter(ManufacturerDetailActivity.this, R.layout.item_aroma, aromas);
                            lvDetailManufacturerAroma.setAdapter(adapter);
                            lvDetailManufacturerAroma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(ManufacturerDetailActivity.this, AromaDetailActivity.class);
                                    intent.putExtra(MANUFACTURER_AROMA_DETAIL, aromas.get(i));
                                    startActivity(intent);
                                }
                            });
                            lvDetailManufacturerAroma.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    int action = motionEvent.getAction();
                                    switch (action) {
                                        case MotionEvent.ACTION_DOWN:
                                            // Disallow ScrollView to intercept touch events.
                                            view.getParent().requestDisallowInterceptTouchEvent(true);
                                            break;

                                        case MotionEvent.ACTION_UP:
                                            // Allow ScrollView to intercept touch events.
                                            view.getParent().requestDisallowInterceptTouchEvent(false);
                                            break;
                                    }

                                    // Handle ListView touch events.
                                    view.onTouchEvent(motionEvent);
                                    return true;
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ManufacturerDetailActivity.this, "Error while parsing...", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        int scrollY = mScrollView.getScrollY();
        // Add parallax effect
        imgDetailManufacturer.setTranslationY(scrollY * 0.5f);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setTitle(manuf.getName());
    }
}
