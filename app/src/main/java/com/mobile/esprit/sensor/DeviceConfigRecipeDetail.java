package com.mobile.esprit.sensor;

import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.DeviceConfigRecipe;
import com.mobile.esprit.sensor.device_config_detail_activity.adapters.AromaDeviceConfigRecipeDetailAdapter;
import com.mobile.esprit.sensor.home_activity.adapters.AromaSmallAdapater;
import com.mobile.esprit.sensor.home_activity.adapters.CategorySmallAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeviceConfigRecipeDetail extends AppCompatActivity {

    private DeviceConfigRecipe recipe = null;
    private TextView basePourcentage;
    private TextView basePgVg;
    private TextView baseNicotine;
    private TextView totalBase;
    private ImageView baseMobileImage;
    private ImageView recipeImage;
    // private TextView deviceConfigName;
    private TextView recipeVolume;
    private TextView recipeSteep;
    private TextView totalAromas;
    private TextView aromasTaux;
    private TextView pgTag, vgTag, nicotineTag, tauxBaseTag;
    private TextView recipeDescription;
    private RecyclerView recipeCategoryRecyclerView;
    private RecyclerView recipeAromasRecyclerView;
    private RecyclerView aromasRecyclerView;
    private PieChart pieChart;
    public ArrayList<Float> aromaQuantities = new ArrayList<>();
    public float totalAromaQuantity = 0;
    public float totaRecipeVolume = 0;
    private ArrayList<String> aromasName = new ArrayList<>();
    private TextView userRecipeName;
    private CircleImageView userRecipeImage;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_config_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }





        if (getIntent().getExtras().getParcelable("DeviceConfigRecipe") != null) {
            recipe = (DeviceConfigRecipe) getIntent().getExtras().getParcelable("DeviceConfigRecipe");
            setRecipeData();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
          finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public void setRecipeData() {


        getSupportActionBar().setTitle(recipe.getName());
        basePourcentage = (TextView) findViewById(R.id.base_pourcentage);
        basePgVg = (TextView) findViewById(R.id.base_pg_vg);
        baseNicotine = (TextView) findViewById(R.id.base_nicotine);
        totalBase = (TextView) findViewById(R.id.base_totale);
        baseMobileImage = (ImageView) findViewById(R.id.base_mobile_image);
        recipeVolume = (TextView) findViewById(R.id.recipe_volume);
        recipeSteep = (TextView) findViewById(R.id.recipe_steep);
        totalAromas = (TextView) findViewById(R.id.total_aromas);
        aromasTaux = (TextView) findViewById(R.id.aromas_taux);
        recipeImage = (ImageView) findViewById(R.id.recipe_image);
        recipeDescription = (TextView) findViewById(R.id.recipe_description);
        recipeCategoryRecyclerView = (RecyclerView) findViewById(R.id.rv_recipe_category);
        recipeAromasRecyclerView = (RecyclerView) findViewById(R.id.rv_recipe_aromas);
        aromasRecyclerView = (RecyclerView) findViewById(R.id.rv_aromas);



        nicotineTag = (TextView) findViewById(R.id.nicotine_tag);
        vgTag = (TextView) findViewById(R.id.vg_tag);
        pgTag = (TextView) findViewById(R.id.pg_tag);
        tauxBaseTag = (TextView) findViewById(R.id.total_base_tag);
        tauxBaseTag.setText(recipe.getBaseQuantity() + " ml");

        int vg = (int) recipe.getBase().getVg();
        int pg = (int) recipe.getBase().getPg();
        int base = (int) recipe.getBaseQuantity();

        int vgPourcentage = Math.round((vg * base) / 100);
        int pgPourcentage = Math.round((pg * base) / 100);

        nicotineTag.setText(recipe.getBase().getNicotine() + " mg");
        pgTag.setText(pg + "  (" + pgPourcentage + "%)");
        vgTag.setText(vg + "  (" + vgPourcentage + "%)");


        basePourcentage.setText(String.valueOf(getPourcentage(recipe.getBaseQuantity())) + "%");
        basePgVg.setText(recipe.getBase().getPg() + "/" + recipe.getBase().getVg());
        baseNicotine.setText(recipe.getBase().getNicotine() + " mg");
        totalBase.setText(String.valueOf(recipe.getBaseQuantity()) + " ml");
        //  deviceConfigName.setText(recipeToAdd.getName());
        recipeVolume.setText(recipe.getVolume() + " ml");
        recipeSteep.setText((int) recipe.getSteep() + "");
        totalAromas.setText(recipe.getTotalAromeQuantity() + " ml");
        aromasTaux.setText(String.valueOf(getPourcentage(recipe.getTotalAromeQuantity())) + "%");
        recipeDescription.setText(recipe.getDescription());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<Aroma> aromas = new ArrayList<>();
        for (AromePerRecipe apr : recipe.getAromes()) {
            if (!categories.contains(apr.getArome().getCategory())) {
                categories.add(apr.getArome().getCategory());
            }
        }

        for (AromePerRecipe apr : recipe.getAromes()) {
            aromas.add(apr.getArome());
        }

        AromaSmallAdapater aromaSmallAdapater = new AromaSmallAdapater(this, aromas);
        recipeAromasRecyclerView.setLayoutManager(linearLayoutManager1);
        recipeAromasRecyclerView.setAdapter(aromaSmallAdapater);


        CategorySmallAdapter categorySmallAdapter = new CategorySmallAdapter(this, categories);
        recipeCategoryRecyclerView.setLayoutManager(linearLayoutManager);
        recipeCategoryRecyclerView.setAdapter(categorySmallAdapter);

        aromasRecyclerView.setAdapter(new AromaDeviceConfigRecipeDetailAdapter(this, recipe.getAromes(), recipe));
        aromasRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



        Picasso.with(this).load(Uri.parse(recipe.getBase().getMobileImageUrl())).into(baseMobileImage);
        Picasso.with(this).load(Uri.parse(recipe.getImageUrl())).into(recipeImage);



        pieChart = (PieChart) findViewById(R.id.chart);
        drawPieChart();

        userRecipeImage = (CircleImageView) findViewById(R.id.user_recipe_image);
        userRecipeName = (TextView) findViewById(R.id.user_recipe_name);


        try {
            Picasso.with(this).load(Uri.parse(recipe.getUser().getProfilePicture())).into(userRecipeImage);
            userRecipeName.setText(recipe.getUser().getFirstName() + " " + recipe.getUser().getLastName());
        } catch (NullPointerException e) {

        }


    }


    public float getPourcentage(float value) {
        return (100 * value) / recipe.getVolume();
    }


    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString(recipe.getName());
        s.setSpan(new RelativeSizeSpan(1.7f), 0, recipe.getName().length(), 0);
        return s;
    }

    public void drawPieChart() {


        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);


        pieChart.setCenterText(generateCenterSpannableText());

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);


        totalAromaQuantity = recipe.getTotalAromeQuantity();
        totaRecipeVolume = recipe.getVolume();
        for (AromePerRecipe apr : recipe.getAromes()) {
            aromaQuantities.add(apr.getQuantity());
            aromasName.add(apr.getArome().getName());
        }

        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < recipe.getAromes().size(); i++) {
            pieEntries.add(new PieEntry(aromaQuantities.get(i), aromasName.get(i)));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, recipe.getName());


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

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setDrawValues(false);


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(4f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);
        pieChart.invalidate();


    }
}
