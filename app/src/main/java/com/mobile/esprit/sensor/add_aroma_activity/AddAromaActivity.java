package com.mobile.esprit.sensor.add_aroma_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.background_tasks.AromaManager;
import com.mobile.esprit.sensor.background_tasks.CategoryManager;
import com.mobile.esprit.sensor.background_tasks.ManufactureManager;

public class AddAromaActivity extends AppCompatActivity {

    RecyclerView rvCategory;
    RecyclerView rvManufacture;
    public static TextView choosenCategoryName, choosenManufactureName, choosenCategoryDescription;
    public static ImageView choosenCategoryImage, chooseManufactureImage;
    private EditText searchManufacture;
    public static Manufacturer choosenManufacturer = new Manufacturer();
    public static Category choosenCategory = new Category();
    private EditText aromaName;
    private EditText aromaDescription;
    private Button addAromaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aroma);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        rvCategory = (RecyclerView) findViewById(R.id.rv_category);
        rvManufacture = (RecyclerView) findViewById(R.id.rv_manufacture);

        chooseManufactureImage = (ImageView) findViewById(R.id.choosen_manufacture_image);
        choosenCategoryImage = (ImageView) findViewById(R.id.choosen_category_image);
        choosenCategoryName = (TextView) findViewById(R.id.choosen_category_name);
        choosenManufactureName = (TextView) findViewById(R.id.choosen_manufacture_name);
        choosenCategoryDescription = (TextView) findViewById(R.id.choosen_category_description);

        aromaDescription = (EditText) findViewById(R.id.aromaDescription);
        aromaName = (EditText) findViewById(R.id.aromaName);

        addAromaButton = (Button) findViewById(R.id.add_aroma_btn);
        addAromaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("zab");
                System.out.println(choosenCategory.getName());

                if (aromaName.getText().toString().equals("") || aromaDescription.getText().toString().equals("")) {
                    Toast.makeText(AddAromaActivity.this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (choosenCategory.getName() == null || choosenManufacturer.getName() == null) {
                        if (choosenCategory.getName() == null) {
                            Toast.makeText(AddAromaActivity.this, "Please Select a Category", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddAromaActivity.this, "Please Select a Manufacture", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Aroma aroma = new Aroma();
                        aroma.setName(aromaName.getText().toString());
                        aroma.setDescription(aromaDescription.getText().toString());
                        AromaManager aromaManager = new AromaManager(AddAromaActivity.this);
                        aromaManager.addUserAroma(aroma);
                    }


                }


            }
        });


        searchManufacture = (EditText) findViewById(R.id.search_manufacture);
        CategoryManager categoryManager = new CategoryManager(this);
        categoryManager.getAllCategory(rvCategory);

        ManufactureManager manufactureManager = new ManufactureManager(this);
        manufactureManager.getAllManufacture(rvManufacture, searchManufacture);


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
