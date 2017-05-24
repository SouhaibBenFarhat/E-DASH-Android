package com.mobile.esprit.sensor.recipe_detail_activity.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Souhaib on 31/03/2017.
 */

public class SimilarRecipeAdapter extends RecyclerView.Adapter<SimilarRecipeAdapter.CustomViewHolder> {
    private List<Recipe> recipes;
    private Context mContext;

    public SimilarRecipeAdapter(Context context, List<Recipe> recipes) {
        this.recipes = recipes;
        this.mContext = context;



    }

    @Override
    public SimilarRecipeAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.similar_recipe_item, null);
        SimilarRecipeAdapter.CustomViewHolder viewHolder = new SimilarRecipeAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SimilarRecipeAdapter.CustomViewHolder customViewHolder, int i) {
        final Recipe recipe = recipes.get(i);

        customViewHolder.recipeName.setText(recipe.getName());


        Picasso.with(mContext).load(Uri.parse(recipe.getImageUrl())).fit().into(customViewHolder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return (null != recipes ? recipes.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView recipeImage;
        protected TextView recipeName;


        public CustomViewHolder(View view) {
            super(view);
            this.recipeImage = (CircleImageView) view.findViewById(R.id.similar_recipe_image);
            this.recipeName = (TextView) view.findViewById(R.id.similar_recipe_name);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


}
