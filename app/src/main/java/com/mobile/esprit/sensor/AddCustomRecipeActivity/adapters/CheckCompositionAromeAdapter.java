package com.mobile.esprit.sensor.AddCustomRecipeActivity.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.mobile.esprit.sensor.AddCustomRecipeActivity.AddCustomRecipeActivity;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Souhaib on 26/04/2017.
 */

public class CheckCompositionAromeAdapter extends ArrayAdapter<AromePerRecipe> {
    private Context context;

    public CheckCompositionAromeAdapter(Context context, ArrayList<AromePerRecipe> aromePerRecipes) {
        super(context, 0, aromePerRecipes);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AromePerRecipe aromePerRecipe = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_arome_check_composition, parent, false);
        }
        // Lookup view for data population
        TextView tvAromaName = (TextView) convertView.findViewById(R.id.tv_aroma_name);
        TextView tvAromeQuantity = (TextView) convertView.findViewById(R.id.tv_aroma_quantity);
        ImageView imgAroma = (ImageView) convertView.findViewById(R.id.img_aroma);
        RoundCornerProgressBar roundCornerProgressBar = (RoundCornerProgressBar) convertView.findViewById(R.id.arome_progress);

        float totalAromeQuantity = 0;
        for (AromePerRecipe apr : AddCustomRecipeActivity.selectedAromePerRecipes) {
            totalAromeQuantity = totalAromeQuantity + apr.getQuantity();
            System.out.println(totalAromeQuantity + " hhhhh");
        }


        roundCornerProgressBar.setProgressColor(Color.parseColor("#4090AD"));
        roundCornerProgressBar.setProgressBackgroundColor(Color.parseColor("#e8f1f6"));
        roundCornerProgressBar.setMax(totalAromeQuantity);
        roundCornerProgressBar.setProgress(aromePerRecipe.getQuantity());
        tvAromeQuantity.setText(aromePerRecipe.getQuantity()+" ml");


        // Populate the data into the template view using the data object
        tvAromaName.setText(aromePerRecipe.getArome().getName());
        Picasso.with(context).load(Uri.parse(aromePerRecipe.getArome().getImgArome())).into(imgAroma);
        // Return the completed view to render on screen
        return convertView;
    }
}
