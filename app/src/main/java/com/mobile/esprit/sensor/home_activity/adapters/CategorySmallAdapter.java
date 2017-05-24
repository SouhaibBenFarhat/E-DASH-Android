package com.mobile.esprit.sensor.home_activity.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.add_aroma_activity.AddAromaActivity;
import com.mobile.esprit.sensor.home_activity.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Souhaib on 26/03/2017.
 */

public class CategorySmallAdapter extends RecyclerView.Adapter<CategorySmallAdapter.CustomViewHolder> {
    private List<Category> categories;
    private Context mContext;

    public CategorySmallAdapter(Context context, List<Category> categories) {
        this.categories = categories;
        this.mContext = context;


    }

    @Override
    public CategorySmallAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_small_item, null);
        CategorySmallAdapter.CustomViewHolder viewHolder = new CategorySmallAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final CategorySmallAdapter.CustomViewHolder customViewHolder, int i) {
        final Category category = categories.get(i);

        Picasso.with(mContext).load(Uri.parse(category.getImage())).fit().into(customViewHolder.categoryImageView);
        customViewHolder.categoryName.setText(category.getName());

        if (mContext instanceof HomeActivity) {
            customViewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.tag));
        } else {
            customViewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.recipe_detail_tag));

        }
    }

    @Override
    public int getItemCount() {
        return (null != categories ? categories.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView categoryImageView;
        protected TextView categoryName;
        protected RelativeLayout relativeLayout;


        public CustomViewHolder(View view) {
            super(view);
            this.categoryImageView = (CircleImageView) view.findViewById(R.id.category_image);
            this.categoryName = (TextView) view.findViewById(R.id.category_name_item);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.category_tag);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mContext instanceof AddAromaActivity) {
                        AddAromaActivity.choosenCategoryName.setText(categories.get(getLayoutPosition()).getName());
                        AddAromaActivity.choosenCategoryDescription.setText(categories.get(getLayoutPosition()).getDescription());
                        AddAromaActivity.choosenCategory = categories.get(getLayoutPosition());
                        Picasso.with(mContext).load(Uri.parse(categories.get(getLayoutPosition()).getImage())).into(AddAromaActivity.choosenCategoryImage);
                    }

                }
            });
        }
    }


}

