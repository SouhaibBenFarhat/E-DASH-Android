package com.mobile.esprit.sensor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gadour on 03/04/2017.
 */

public class MakeRecipeAromaAdapter extends RecyclerView.Adapter<MakeRecipeAromaAdapter.CustomViewHolder> {
    private List<AromePerRecipe> aromePerRecipes;
    private Context mContext;
    private Recipe recipe;

    private static ClickListener  clickListener;


    public MakeRecipeAromaAdapter(Context context, List<AromePerRecipe> aromePerRecipes) {
        this.aromePerRecipes = aromePerRecipes;
        this.mContext = context;


    }

    @Override
    public MakeRecipeAromaAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_make_recipe_aroma, null);
        MakeRecipeAromaAdapter.CustomViewHolder viewHolder = new MakeRecipeAromaAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MakeRecipeAromaAdapter.CustomViewHolder customViewHolder, int i) {
        final AromePerRecipe aromePerRecipe = aromePerRecipes.get(i);

        customViewHolder.tvMakeRecipeAromaName.setText(aromePerRecipe.getArome().getName());
        Picasso.with(mContext).load(aromePerRecipe.getArome().getImgArome()).noPlaceholder().fit().into(
                customViewHolder.ivMakeRecipeImage
        );
    }

    @Override
    public int getItemCount() {
        return (null != aromePerRecipes ? aromePerRecipes.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivMakeRecipeImage;
        protected TextView tvMakeRecipeAromaName;


        public CustomViewHolder(View view) {
            super(view);
            this.ivMakeRecipeImage = (ImageView) view.findViewById(R.id.iv_make_recipe_image);
            this.tvMakeRecipeAromaName = (TextView) view.findViewById(R.id.tv_make_recipe_aroma_name);




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickListener.onItemClick(getAdapterPosition(), v);

                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MakeRecipeAromaAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }



}
