package com.mobile.esprit.sensor.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Souhaib on 20/04/2017.
 */

public class AromeRecipeAdapter  extends ArrayAdapter<Recipe> {
    private Context context;
    public AromeRecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Recipe recipe = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_arome_recipe, parent, false);
        }
        // Lookup view for data population
        TextView tvRecipeName = (TextView) convertView.findViewById(R.id.tv_recipe_name);
        ImageView imgRecipe = (ImageView) convertView.findViewById(R.id.img_recipe);
        // Populate the data into the template view using the data object
        tvRecipeName.setText(recipe.getName());
        Picasso.with(context).load(Uri.parse(recipe.getImageUrl())).into(imgRecipe);
        // Return the completed view to render on screen
        return convertView;
    }
}
