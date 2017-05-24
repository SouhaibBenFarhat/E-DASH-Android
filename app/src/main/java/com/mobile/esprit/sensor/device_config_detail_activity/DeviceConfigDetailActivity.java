package com.mobile.esprit.sensor.device_config_detail_activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.DeviceConfig;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.background_tasks.RecipeManager;
import com.mobile.esprit.sensor.device_config_detail_activity.adapters.AromaDeviceConfigAdapter;
import com.mobile.esprit.sensor.home_activity.adapters.AromaSmallAdapater;
import com.mobile.esprit.sensor.home_activity.adapters.CategorySmallAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeviceConfigDetailActivity extends AppCompatActivity {


    private BarChart barChart;
    private DeviceConfig deviceConfig = null;
    private TextView basePgVg;
    private TextView baseNicotine;
    private TextView totalBase;
    private ImageView baseMobileImage;
    // private TextView deviceConfigName;
    private TextView deviceConfigVolume;

    private TextView totalAromas;
    private TextView pgTag, vgTag, nicotineTag, tauxBaseTag;
    private TextView deviceConfigNote;
    private RecyclerView deviceConfigCategoryRecyclerView;
    private RecyclerView deviceConfigAromasRecyclerView;
    private RecyclerView aromasRecyclerView;
    private PieChart pieChart;
    public ArrayList<Float> aromaQuantities = new ArrayList<>();
    public float totalAromaQuantity = 0;
    public float totaRecipeVolume = 0;
    private ArrayList<String> aromasName = new ArrayList<>();
    private RecyclerView rvSimilarRecipes;
    private RecyclerView rvCreatedRecipes;

    private CircleImageView userImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_config_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        if (getIntent().getExtras() != null) {
            deviceConfig = (DeviceConfig) getIntent().getExtras().getParcelable("DeviceConfig");
            setDeviceConfigData();
        }

    }


    public void setDeviceConfigData() {

        TextView deviceConfigUsername = (TextView) findViewById(R.id.device_config_username);
        deviceConfigUsername.setText(deviceConfig.getUser().getFirstName());
        userImageView = (CircleImageView) findViewById(R.id.device_config_user_image);
        Picasso.with(this).load(Uri.parse(deviceConfig.getUser().getProfilePicture())).into(userImageView);


        getSupportActionBar().setTitle(deviceConfig.getName());
        basePgVg = (TextView) findViewById(R.id.base_pg_vg);
        baseNicotine = (TextView) findViewById(R.id.base_nicotine);
        totalBase = (TextView) findViewById(R.id.base_totale);
        baseMobileImage = (ImageView) findViewById(R.id.device_config_base_mobile_image);
        deviceConfigVolume = (TextView) findViewById(R.id.device_config_total_volume);
        totalAromas = (TextView) findViewById(R.id.total_aromas);
        deviceConfigNote = (TextView) findViewById(R.id.device_config_note);


        deviceConfigCategoryRecyclerView = (RecyclerView) findViewById(R.id.rv_device_config_category);
        deviceConfigAromasRecyclerView = (RecyclerView) findViewById(R.id.horizental_rv_device_config_aromas_tag);
        aromasRecyclerView = (RecyclerView) findViewById(R.id.vertical_rv_device_config_aromas);


        nicotineTag = (TextView) findViewById(R.id.device_config_nicotine_tag);
        vgTag = (TextView) findViewById(R.id.device_config_vg_tag);
        pgTag = (TextView) findViewById(R.id.device_config_pg_tag);


        tauxBaseTag = (TextView) findViewById(R.id.device_config_total_base_tag);
        tauxBaseTag.setText(deviceConfig.getBaseQuantity() + " ml");
        nicotineTag.setText(deviceConfig.getBase().getNicotine() + " mg");


        pgTag.setText(deviceConfig.getBase().getPg() + "");
        vgTag.setText(deviceConfig.getBase().getVg() + "");


        basePgVg.setText(deviceConfig.getBase().getPg() + "/" + deviceConfig.getBase().getVg());
        baseNicotine.setText(deviceConfig.getBase().getNicotine() + " mg");
        totalBase.setText(String.valueOf(deviceConfig.getBaseQuantity()) + " ml");
        //  deviceConfigName.setText(recipeToAdd.getName());
        deviceConfigVolume.setText((int) deviceConfig.getVolume() + " ml");
        totalAromas.setText(deviceConfig.getTotalAromeQuantity() + " ml");
        deviceConfigNote.setText(deviceConfig.getNote());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<Aroma> aromas = new ArrayList<>();
        for (AromePerRecipe apr : deviceConfig.getAromas()) {
            if (!categories.contains(apr.getArome().getCategory())) {
                categories.add(apr.getArome().getCategory());
            }
        }

        for (AromePerRecipe apr : deviceConfig.getAromas()) {
            aromas.add(apr.getArome());
        }

        AromaSmallAdapater aromaSmallAdapater = new AromaSmallAdapater(this, aromas);
        deviceConfigAromasRecyclerView.setLayoutManager(linearLayoutManager1);
        deviceConfigAromasRecyclerView.setAdapter(aromaSmallAdapater);


        CategorySmallAdapter categorySmallAdapter = new CategorySmallAdapter(this, categories);
        deviceConfigCategoryRecyclerView.setLayoutManager(linearLayoutManager);
        deviceConfigCategoryRecyclerView.setAdapter(categorySmallAdapter);

        aromasRecyclerView.setAdapter(new AromaDeviceConfigAdapter(this, deviceConfig.getAromas(), deviceConfig));
        aromasRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        Picasso.with(this).load(Uri.parse(deviceConfig.getBase().getMobileImageUrl())).into(baseMobileImage);

        barChart = (BarChart) findViewById(R.id.barChart);
        setupBarChar();


        rvSimilarRecipes = (RecyclerView) findViewById(R.id.rv_similar_recipes);
        RecipeManager recipeManager = new RecipeManager(DeviceConfigDetailActivity.this);
        recipeManager.getRecipeByTag(rvSimilarRecipes, deviceConfig.getDeviceConfigTag());


        rvCreatedRecipes = (RecyclerView) findViewById(R.id.rv_created_recipes);
        recipeManager.getRecipeByDeviceConfig(rvCreatedRecipes,deviceConfig.getId());
    }


    public float getPourcentage(float value) {
        return (100 * value) / deviceConfig.getVolume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    public void setupBarChar() {


        ArrayList<String> aromasName = new ArrayList<>();
        aromasName.add("Base");
        for (AromePerRecipe apr : deviceConfig.getAromas()) {
            aromasName.add(apr.getArome().getName());
        }

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (AromePerRecipe apr : deviceConfig.getAromas()) {
            barEntries.add(new BarEntry(apr.getPosition(), apr.getQuantity()));

        }

        barEntries.add(new BarEntry(0, deviceConfig.getBaseQuantity()));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Quantity");

        barDataSet.setValueTextSize(10f);


        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());


        barDataSet.setColors(colors);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(aromasName));


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);


        barChart.invalidate();


    }
}
