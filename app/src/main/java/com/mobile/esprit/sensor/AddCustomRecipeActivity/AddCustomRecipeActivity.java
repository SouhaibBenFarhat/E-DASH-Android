package com.mobile.esprit.sensor.AddCustomRecipeActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.mobile.esprit.sensor.AddCustomRecipeActivity.adapters.CheckCompositionAromeAdapter;
import com.mobile.esprit.sensor.AddCustomRecipeActivity.fragments.SelectAromaFragment;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Base;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.background_tasks.BaseManager;
import com.mobile.esprit.sensor.background_tasks.RecipeManager;

import java.util.ArrayList;
import java.util.Calendar;

public class AddCustomRecipeActivity extends AppCompatActivity {

    RecyclerView rvBases;
    Button btnAddRecipe;
    public static Recipe recipeToAdd = new Recipe();
    public static RecyclerView rvCategories;
    public static TextView baseNicotine, baseVg, basePg;
    public static RoundCornerProgressBar nicotineProgress, pgProgress, vgPogress;
    public static ArrayList<AromePerRecipe> selectedAromePerRecipes = new ArrayList<>();
    public static ArrayList<Category> selectedAromaCategory = new ArrayList<>();
    public static Base base;
    public static ListView lvSelectedAromas;
    public RelativeLayout addAromaLayout;
    public LinearLayout checkCompositionLayout;
    public ImageButton btnCheckComposition;
    public static ListView checkCompositionRecyclerView;
    public ExpandableLinearLayout expandableRelativeLayout;
    public EditText recipeName;
    public EditText recipeSteep;
    public static EditText recipeAmond;
    public static EditText baseVolume;
    public static ImageView baseImage;
    public EditText recipeDescription;

    TextView checkBaseVolume;
    TextView checkPgVg;
    TextView checkTotalAroma;

    public static float totalQuantity = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        selectedAromePerRecipes.clear();
        selectedAromaCategory.clear();
        recipeDescription = (EditText) findViewById(R.id.et_recipe_description);

        baseImage = (ImageView) findViewById(R.id.base_mobile_image);

        checkBaseVolume = (TextView) findViewById(R.id.base_totale);
        checkPgVg = (TextView) findViewById(R.id.base_pg_vg);
        checkTotalAroma = (TextView) findViewById(R.id.total_aromas);


        recipeName = (EditText) findViewById(R.id.et_recipe_name);
        recipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable != null) {
                    recipeToAdd.setName(editable.toString());
                }

            }
        });

        recipeSteep = (EditText) findViewById(R.id.et_recipe_steep);
        recipeSteep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    recipeToAdd.setSteep(Integer.parseInt(editable.toString()));
                } catch (NumberFormatException e) {
                    recipeToAdd.setSteep(0);
                }

            }
        });

        recipeAmond = (EditText) findViewById(R.id.et_recipe_volume);
        recipeAmond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    recipeToAdd.setVolume(Float.parseFloat(editable.toString()));
                } catch (NumberFormatException e) {
                    recipeToAdd.setVolume(0);
                }

            }
        });

        baseVolume = (EditText) findViewById(R.id.et_base_volume);
        baseVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    recipeToAdd.setBaseQuantity(Float.parseFloat(editable.toString()));
                } catch (NumberFormatException e) {
                    recipeToAdd.setBaseQuantity(0);
                }

            }
        });


        btnCheckComposition = (ImageButton) findViewById(R.id.btn_check_composition);
        checkCompositionRecyclerView = (ListView) findViewById(R.id.rv_check_composition);

        checkCompositionRecyclerView.setVisibility(View.VISIBLE);

        expandableRelativeLayout = (ExpandableLinearLayout) findViewById(R.id.expandableLayout);

        btnCheckComposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandableRelativeLayout.isExpanded()) {

                    expandableRelativeLayout.expand();
                    checkBaseVolume.setText(baseVolume.getText().toString() + " ml");
                    checkPgVg.setText(base.getPg() + "/" + base.getVg());
                    totalQuantity = 0;
                    for (AromePerRecipe apr : selectedAromePerRecipes) {
                        totalQuantity = totalQuantity + apr.getQuantity();
                    }
                    checkTotalAroma.setText(totalQuantity + " ml");
                    AddCustomRecipeActivity.checkCompositionRecyclerView.setAdapter(new CheckCompositionAromeAdapter(AddCustomRecipeActivity.this, AddCustomRecipeActivity.selectedAromePerRecipes));


                } else {
                    expandableRelativeLayout.expand();
                    AddCustomRecipeActivity.checkCompositionRecyclerView.setAdapter(new CheckCompositionAromeAdapter(AddCustomRecipeActivity.this, AddCustomRecipeActivity.selectedAromePerRecipes));


                }
            }
        });


        base = new Base();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        getSupportActionBar().setTitle("");

        rvBases = (RecyclerView) findViewById(R.id.rv_bases);
        BaseManager baseManager = new BaseManager(AddCustomRecipeActivity.this);
        baseManager.getAllBases(rvBases);


        baseNicotine = (TextView) findViewById(R.id.base_nicotine);
        basePg = (TextView) findViewById(R.id.base_pg);
        baseVg = (TextView) findViewById(R.id.base_vg);


        nicotineProgress = (RoundCornerProgressBar) findViewById(R.id.nicotine_progress);
        nicotineProgress.setProgressColor(Color.parseColor("#e5b717"));
        nicotineProgress.setProgressBackgroundColor(Color.parseColor("#dfe8e5"));
        nicotineProgress.setMax(100);
        nicotineProgress.setProgress(0);

        pgProgress = (RoundCornerProgressBar) findViewById(R.id.pg_progress);
        pgProgress.setProgressColor(Color.parseColor("#e7d38c"));
        pgProgress.setProgressBackgroundColor(Color.parseColor("#dfe8e5"));
        pgProgress.setMax(100);
        pgProgress.setProgress(0);

        vgPogress = (RoundCornerProgressBar) findViewById(R.id.vg_progress);
        vgPogress.setProgressColor(Color.parseColor("#64d286"));
        vgPogress.setProgressBackgroundColor(Color.parseColor("#dfe8e5"));
        vgPogress.setMax(100);
        vgPogress.setProgress(0);


        lvSelectedAromas = (ListView) findViewById(R.id.rv_selected_aromas);


        lvSelectedAromas.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        addAromaLayout = (RelativeLayout) findViewById(R.id.add_aroma_layout);
        addAromaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().add(R.id.main_container, new SelectAromaFragment()).addToBackStack(SelectAromaFragment.class.toString()).commit();
            }
        });

        rvCategories = (RecyclerView) findViewById(R.id.rv_categories);

        btnAddRecipe = (Button) findViewById(R.id.btn_add_recipe);
        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                recipeToAdd.setDescription(recipeDescription.getText().toString());
                recipeToAdd.setDate(mydate);
                recipeToAdd.setImageUrl(URL.DEFAULT_RECIPE);
                recipeToAdd.setName(recipeName.getText().toString());
                recipeToAdd.setSteep(Integer.parseInt(recipeSteep.getText().toString()));
                recipeToAdd.setBaseQuantity(Float.parseFloat(baseVolume.getText().toString()));
                recipeToAdd.setVolume(Float.parseFloat(recipeAmond.getText().toString()));
                recipeToAdd.setTotalAromeQuantity(totalQuantity);
                recipeToAdd.setBase(base);
                recipeToAdd.setUser(User.getInstance());
                recipeToAdd.setAromes(selectedAromePerRecipes);

                RecipeManager recipeManager = new RecipeManager(AddCustomRecipeActivity.this);
                recipeManager.addRecipe(recipeToAdd);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {

            SelectAromaFragment fragment = (SelectAromaFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
            if (fragment != null && fragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            } else {
                finish(); // close this activity and return to preview activity (if there is any)

            }

        }

        return super.onOptionsItemSelected(item);
    }


}
