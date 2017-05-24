package com.mobile.esprit.sensor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Utils.RoundedCornersTransformation;
import com.mobile.esprit.sensor.background_tasks.RecipeManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class AromaDetailActivity extends AppCompatActivity {

    private ImageView imageDetailAroma;
    private TextView tvDetailAromaManufacturer, tvDetailAromaCategory, tvDetailAromaDescription;
    private RatingBar rbDetailAroma;
    private Aroma aroma;
    private ListView lvRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aroma_detail);

        aroma = new Aroma();

        Bundle bundle = getIntent().getExtras();

        if (bundle.getParcelable(AromaActivity.AROMA_DETAIL) != null) {
            aroma = bundle.getParcelable(AromaActivity.AROMA_DETAIL);
        } else if (bundle.getParcelable(ManufacturerDetailActivity.MANUFACTURER_AROMA_DETAIL) != null) {
            aroma = bundle.getParcelable(ManufacturerDetailActivity.MANUFACTURER_AROMA_DETAIL);
        } else {
            aroma = bundle.getParcelable(CategoryActivity.CATEGORY_AROMA);
        }

        imageDetailAroma = (ImageView) findViewById(R.id.img_detail_aroma);
        tvDetailAromaManufacturer = (TextView) findViewById(R.id.tv_detail_aroma_manufacturer);
        tvDetailAromaCategory = (TextView) findViewById(R.id.tv_detail_aroma_category);
        tvDetailAromaDescription = (TextView) findViewById(R.id.tv_detail_aroma_description);
        rbDetailAroma = (RatingBar) findViewById(R.id.rb_detail_aroma);
        lvRecipes = (ListView) findViewById(R.id.lv_detail_aroma_recipes);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }







        RecipeManager recipeManager = new RecipeManager(this);
        recipeManager.getRecipeByArome(lvRecipes, aroma.getIdAroma());

        final int radius = 88;
        final int margin = 2;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.with(this).load(aroma.getImgArome()).transform(transformation).noPlaceholder().fit().into(imageDetailAroma);
        tvDetailAromaManufacturer.setText(aroma.getManufacturer().getName());

        tvDetailAromaCategory.setText(aroma.getCategory().getName());
        tvDetailAromaDescription.setText(aroma.getDescription());


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
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setTitle(aroma.getName());
    }
}
