package com.mobile.esprit.sensor.user_aromas_activities.adapters;

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

import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.add_aroma_activity.AddAromaActivity;
import com.mobile.esprit.sensor.home_activity.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Souhaib on 30/03/2017.
 */

public class SmallManufactureAdapter extends RecyclerView.Adapter<SmallManufactureAdapter.CustomViewHolder> {
    private List<Manufacturer> manufacturers;
    private Context mContext;

    public SmallManufactureAdapter(Context context, List<Manufacturer> manufacturers) {
        this.manufacturers = manufacturers;
        this.mContext = context;


    }

    @Override
    public SmallManufactureAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_small_item, null);
        SmallManufactureAdapter.CustomViewHolder viewHolder = new SmallManufactureAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final SmallManufactureAdapter.CustomViewHolder customViewHolder, int i) {
        final Manufacturer manufacturer = manufacturers.get(i);

        Picasso.with(mContext).load(Uri.parse(manufacturer.getImage())).fit().into(customViewHolder.categoryImageView);
        customViewHolder.categoryName.setText(manufacturer.getName());

        if (mContext instanceof HomeActivity) {
            customViewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.tag));
        } else {
            customViewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.recipe_detail_tag));

        }
    }

    @Override
    public int getItemCount() {
        return (null != manufacturers ? manufacturers.size() : 0);
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
                        AddAromaActivity.choosenManufactureName.setText(manufacturers.get(getLayoutPosition()).getName());
                        AddAromaActivity.choosenManufacturer = manufacturers.get(getLayoutPosition());
                        Picasso.with(mContext).load(Uri.parse(manufacturers.get(getLayoutPosition()).getImage())).into(AddAromaActivity.chooseManufactureImage);
                    }

                }
            });
        }
    }


}

