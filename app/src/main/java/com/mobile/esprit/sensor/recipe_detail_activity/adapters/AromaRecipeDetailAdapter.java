package com.mobile.esprit.sensor.recipe_detail_activity.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Souhaib on 27/03/2017.
 */

public class AromaRecipeDetailAdapter extends RecyclerView.Adapter<AromaRecipeDetailAdapter.CustomViewHolder> {
    private List<AromePerRecipe> aromePerRecipes;
    private Context mContext;
    private Recipe recipe;

    public AromaRecipeDetailAdapter(Context context, List<AromePerRecipe> aromePerRecipes, Recipe recipe) {
        this.aromePerRecipes = aromePerRecipes;
        this.mContext = context;
        this.recipe = recipe;


    }

    @Override
    public AromaRecipeDetailAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.aroma_recipe_detail_item, null);
        AromaRecipeDetailAdapter.CustomViewHolder viewHolder = new AromaRecipeDetailAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AromaRecipeDetailAdapter.CustomViewHolder customViewHolder, int i) {
        final AromePerRecipe aromePerRecipe = aromePerRecipes.get(i);

        customViewHolder.manufacturerName.setText(aromePerRecipe.getArome().getManufacturer().getName());

        float totalRecipeVolume = recipe.getVolume();
        int aromaPourcentage = Math.round((100 * aromePerRecipe.getQuantity()) / totalRecipeVolume);
        customViewHolder.aromaPourcentage.setText(aromaPourcentage + "%");
        customViewHolder.aromaQuantity.setText(aromePerRecipe.getQuantity() + " ml");
        customViewHolder.aromaName.setText(aromePerRecipe.getArome().getName());

        Picasso.with(mContext).load(Uri.parse(aromePerRecipe.getArome().getImgArome())).resize(800, 600).into(customViewHolder.aromaImage);
    }

    @Override
    public int getItemCount() {
        return (null != aromePerRecipes ? aromePerRecipes.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView aromaImage;
        protected TextView manufacturerName, aromaPourcentage, aromaQuantity, aromaName;


        public CustomViewHolder(View view) {
            super(view);
            this.aromaImage = (CircleImageView) view.findViewById(R.id.aroma_image);
            this.manufacturerName = (TextView) view.findViewById(R.id.manufacturer_name);
            this.aromaPourcentage = (TextView) view.findViewById(R.id.aroma_pourcentage);
            this.aromaQuantity = (TextView) view.findViewById(R.id.aroma_quantity);
            this.aromaName = (TextView) view.findViewById(R.id.aroma_name);



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


}
