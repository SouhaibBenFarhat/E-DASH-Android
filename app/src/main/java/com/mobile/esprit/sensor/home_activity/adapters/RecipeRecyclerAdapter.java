package com.mobile.esprit.sensor.home_activity.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.recipe_detail_activity.RecipeDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Souhaib on 24/03/2017.
 */

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.CustomViewHolder> {
    private List<Recipe> recipes;
    private Context mContext;
    private float totalRecipeQuantity = 0;
    private float totalBaseQuantity = 0;
    private int progressLayoutWidth = 300;

    public RecipeRecyclerAdapter(Context context, List<Recipe> recipes) {
        this.recipes = recipes;
        this.mContext = context;


    }

    @Override
    public RecipeRecyclerAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_item, null);
        RecipeRecyclerAdapter.CustomViewHolder viewHolder = new RecipeRecyclerAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecipeRecyclerAdapter.CustomViewHolder customViewHolder, int i) {
        final Recipe recipe = recipes.get(i);


        totalRecipeQuantity = recipe.getVolume();
        totalBaseQuantity = recipe.getBaseQuantity();


        customViewHolder.recipeName.setText(recipe.getName());
        if (recipe.getDate().length()>23){
            customViewHolder.recipeDate.setText(recipe.getDate().substring(0, 24));
        }else{
            customViewHolder.recipeDate.setText(recipe.getDate());
        }
        Picasso.with(mContext).load(Uri.parse(recipe.getImageUrl())).resize(800, 600).into(customViewHolder.recipeImageView);
        ArrayList<Aroma> aromas = new ArrayList<>();
        ArrayList<Category> categories = new ArrayList<>();
        for (AromePerRecipe apr : recipe.getAromes()) {
            aromas.add(apr.getArome());
            if (!categories.contains(apr.getArome().getCategory())) {
                categories.add(apr.getArome().getCategory());
            }
        }
        AromaSmallAdapater aromaSmallAdapater = new AromaSmallAdapater(mContext, aromas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        customViewHolder.aromaRecyclerView.setLayoutManager(linearLayoutManager);
        customViewHolder.aromaRecyclerView.setAdapter(aromaSmallAdapater);

        CategorySmallAdapter categorySmallAdapter = new CategorySmallAdapter(mContext, categories);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        customViewHolder.categoryRecyclerView.setLayoutManager(linearLayoutManager1);
        customViewHolder.categoryRecyclerView.setAdapter(categorySmallAdapter);
        customViewHolder.comments.setText("(" + recipe.getComments() + ")");

        customViewHolder.baseName.setText(recipe.getBase().getPg() + ":" + recipe.getBase().getVg() + ":" + recipe.getBase().getNicotine() + "mg");
        Picasso.with(mContext).load(Uri.parse(recipe.getBase().getImage())).into(customViewHolder.baseImageView);

        if (recipe.getUser() != null) {
            customViewHolder.userRecipeName.setText(recipe.getUser().getFirstName() + " " + recipe.getUser().getLastName());
            Picasso.with(mContext).load(Uri.parse(recipe.getUser().getProfilePicture())).into(customViewHolder.recipeUserImage);
        }

        for (int count = 0; count < aromas.size(); count++) {

            if (count == 0) {
//                float basePourcentage = Math.round((totalBaseQuantity * 100) / totalRecipeQuantity);
//                int newWidth = Math.round((basePourcentage * progressLayoutWidth) / 100);
//                View v = (View) customViewHolder.progressLayout.getChildAt(0);
//                ViewGroup.LayoutParams params = v.getLayoutParams();
//                params.width = getDp(newWidth);
//                v.setLayoutParams(params);

            }

            float aromaPourcentage = Math.round(((recipe.getAromes().get(count).getQuantity() * 100) / recipe.getTotalAromeQuantity()));
            int newWidth = Math.round((aromaPourcentage * progressLayoutWidth) / 100);


            View view = (View) customViewHolder.progressLayout.getChildAt(count);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = getDp(newWidth);
            view.setLayoutParams(layoutParams);
        }


    }

    public int getDp(int value) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        int pixels = (int) (value * scale + 0.5f);
        return pixels;
    }

    @Override
    public int getItemCount() {
        return (null != recipes ? recipes.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView recipeImageView;
        protected TextView recipeName;
        protected TextView recipeDate;
        protected TextView baseName;
        protected RecyclerView aromaRecyclerView;
        protected RecyclerView categoryRecyclerView;
        protected CircleImageView baseImageView;
        protected LinearLayout progressLayout;
        protected TextView comments;
        protected CircleImageView recipeUserImage;
        protected TextView userRecipeName;


        public CustomViewHolder(View view) {
            super(view);
            this.recipeImageView = (ImageView) view.findViewById(R.id.recipe_image);
            this.recipeName = (TextView) view.findViewById(R.id.recipe_name_item);
            this.recipeDate = (TextView) view.findViewById(R.id.recipe_date_item);
            this.aromaRecyclerView = (RecyclerView) view.findViewById(R.id.aroma_recycler_view);
            this.categoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);
            this.baseImageView = (CircleImageView) view.findViewById(R.id.base_image);
            this.baseName = (TextView) view.findViewById(R.id.base_name_item);
            this.progressLayout = (LinearLayout) view.findViewById(R.id.progress_layout);
            this.comments = (TextView) view.findViewById(R.id.comments);
            this.recipeUserImage = (CircleImageView) view.findViewById(R.id.recipe_user_image);
            this.userRecipeName = (TextView) view.findViewById(R.id.user_recipe_name);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                    intent.putExtra("Recipe", recipes.get(getLayoutPosition()));
                    System.out.println("Adapter");
                    mContext.startActivity(intent);

                }
            });
        }
    }
}