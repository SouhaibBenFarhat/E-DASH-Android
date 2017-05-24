package com.mobile.esprit.sensor.recipe_detail_activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.Utils.CustomPreferences;
import com.mobile.esprit.sensor.background_tasks.CommentManager;
import com.mobile.esprit.sensor.background_tasks.RatingManager;
import com.mobile.esprit.sensor.background_tasks.RecipeManager;
import com.mobile.esprit.sensor.background_tasks.UserRecipeFavorisManager;
import com.mobile.esprit.sensor.comment_activity.CommentActivity;
import com.mobile.esprit.sensor.home_activity.HomeActivity;
import com.mobile.esprit.sensor.home_activity.adapters.AromaSmallAdapater;
import com.mobile.esprit.sensor.home_activity.adapters.CategorySmallAdapter;
import com.mobile.esprit.sensor.recipe_detail_activity.adapters.AromaRecipeDetailAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeDetailActivity extends AppCompatActivity {


    public static Boolean FROM_NOTIF = false;
    private Recipe recipe = null;
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
    private LinearLayout addCommentLayout;
    private TextView commentNumber;
    private RelativeLayout shareLayout;
    private ImageView userRated;
    private RelativeLayout ratingLayout;
    private TextView userRating;
    private RatingBar recipeRatingSum;
    private RecyclerView similarRecipesRecyclerView;
    private ImageView addToFavorisImage;
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
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        if (FROM_NOTIF) {
            User.setCurrentUser(new CustomPreferences(getApplicationContext(), RecipeDetailActivity.this).getUserFromSharedPreference());
        }


        if (getIntent().getExtras().getParcelable("Recipe") != null) {
            recipe = (Recipe) getIntent().getExtras().getParcelable("Recipe");
            setRecipeData();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            if (FROM_NOTIF) {
                finish();
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CommentManager(this).getCommentCount(recipe.getId(), commentNumber);
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
        commentNumber = (TextView) findViewById(R.id.comment_number);

        new CommentManager(this).getCommentCount(recipe.getId(), commentNumber);

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
        recipeVolume.setText((int) recipe.getVolume() + " ml");
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

        aromasRecyclerView.setAdapter(new AromaRecipeDetailAdapter(this, recipe.getAromes(), recipe));
        aromasRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        addCommentLayout = (LinearLayout) findViewById(R.id.add_comment_layout);
        addCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                intent.putExtra("Recipe", recipe);
                startActivity(intent);
            }
        });


        shareLayout = (RelativeLayout) findViewById(R.id.share_layout);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "http://51.15.135.234/public/public_recipe.html?recipe_id=" + recipe.getId();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        Picasso.with(this).load(Uri.parse(recipe.getBase().getMobileImageUrl())).into(baseMobileImage);
        Picasso.with(this).load(Uri.parse(recipe.getImageUrl())).into(recipeImage);

        userRated = (ImageView) findViewById(R.id.user_rated);

        userRating = (TextView) findViewById(R.id.user_rating);
        recipeRatingSum = (RatingBar) findViewById(R.id.recipe_rating_sum);
        final RatingManager ratingManager = new RatingManager(this);
        ratingManager.getRecipeRating(recipe.getId(), recipeRatingSum);
        ratingManager.getUserRating(recipe.getId(), userRating, userRated);

        ratingLayout = (RelativeLayout) findViewById(R.id.rating_layout);
        ratingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(RecipeDetailActivity.this);
                dialog.setContentView(R.layout.dialog_rating_bar);
                dialog.setCancelable(true);

                final RatingBar recipeRating = (RatingBar) dialog.findViewById(R.id.recipeRating);
                final RatingManager ratingManager1 = new RatingManager(RecipeDetailActivity.this);
                ratingManager1.getUserRating(recipe.getId(), recipeRating);
                Button cancelRating = (Button) dialog.findViewById(R.id.cancel_dialog);
                Button validateRating = (Button) dialog.findViewById(R.id.validate_rating);
                cancelRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });
                validateRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        ratingManager1.addUserRating(recipe.getId(), recipeRating.getRating(), userRating, recipeRatingSum);
                        ratingManager1.getUserRating(recipe.getId(), userRating, userRated);
                    }
                });
                dialog.show();
            }
        });


        similarRecipesRecyclerView = (RecyclerView) findViewById(R.id.rv_similar_recipes);
        RecipeManager recipeManager = new RecipeManager(RecipeDetailActivity.this);
        similarRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recipeManager.getAllRecipe(similarRecipesRecyclerView, null);


        addToFavorisImage = (ImageView) findViewById(R.id.add_to_favoris_button);
        UserRecipeFavorisManager userRecipeFavorisManager = new UserRecipeFavorisManager(RecipeDetailActivity.this);
        userRecipeFavorisManager.findUserRecipeFavoris(recipe.getId(), addToFavorisImage);
        addToFavorisImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRecipeFavorisManager userRecipeFavorisManager = new UserRecipeFavorisManager(RecipeDetailActivity.this);
                userRecipeFavorisManager.addUserRecipeFavoris(recipe.getId(), addToFavorisImage);


            }
        });
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
