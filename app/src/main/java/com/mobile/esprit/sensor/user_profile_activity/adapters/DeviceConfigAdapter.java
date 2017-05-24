package com.mobile.esprit.sensor.user_profile_activity.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.DeviceConfig;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.device_config_detail_activity.DeviceConfigDetailActivity;
import com.mobile.esprit.sensor.home_activity.adapters.AromaSmallAdapater;
import com.mobile.esprit.sensor.home_activity.adapters.CategorySmallAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Souhaib on 01/04/2017.
 */

public class DeviceConfigAdapter extends RecyclerView.Adapter<DeviceConfigAdapter.CustomViewHolder> {
    private List<DeviceConfig> deviceConfigs;
    private Context mContext;
    private float totalRecipeQuantity = 0;
    private float totalBaseQuantity = 0;
    private int progressLayoutWidth = 300;

    public DeviceConfigAdapter(Context context, List<DeviceConfig> deviceConfigs) {
        this.deviceConfigs = deviceConfigs;
        this.mContext = context;


    }

    @Override
    public DeviceConfigAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_config_item, null);
        DeviceConfigAdapter.CustomViewHolder viewHolder = new DeviceConfigAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DeviceConfigAdapter.CustomViewHolder customViewHolder, int i) {
        final DeviceConfig deviceConfig = deviceConfigs.get(i);


        totalRecipeQuantity = deviceConfig.getVolume();
        totalBaseQuantity = deviceConfig.getBaseQuantity();


        customViewHolder.deviceConfigName.setText(deviceConfig.getName());

        try{
            customViewHolder.deviceConfigDate.setText(deviceConfig.getDate().substring(0, 24));
        }catch (StringIndexOutOfBoundsException e){
            customViewHolder.deviceConfigDate.setText(deviceConfig.getDate());
        }

        ArrayList<Aroma> aromas = new ArrayList<>();
        ArrayList<Category> categories = new ArrayList<>();
        for (AromePerRecipe apr : deviceConfig.getAromas()) {
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

        customViewHolder.baseName.setText(deviceConfig.getBase().getPg() + ":" + deviceConfig.getBase().getVg() + ":" + deviceConfig.getBase().getNicotine() + "mg");
        Picasso.with(mContext).load(Uri.parse(deviceConfig.getBase().getImage())).into(customViewHolder.baseImageView);


        for (int count = 0; count < aromas.size(); count++) {

            if (count == 0) {
                float basePourcentage = Math.round((totalBaseQuantity * 100) / totalRecipeQuantity);
                int newWidth = Math.round((basePourcentage * progressLayoutWidth) / 100);
                View v = (View) customViewHolder.progressLayout.getChildAt(0);
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.width = getDp(newWidth);
                v.setLayoutParams(params);

            }

            float aromaPourcentage = Math.round(((deviceConfig.getAromas().get(count).getQuantity() * 100) / totalRecipeQuantity));
            int newWidth = Math.round((aromaPourcentage * progressLayoutWidth) / 100);


            View view = (View) customViewHolder.progressLayout.getChildAt(count + 1);
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
        return (null != deviceConfigs ? deviceConfigs.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView deviceConfigName;
        protected TextView deviceConfigDate;
        protected TextView baseName;
        protected RecyclerView aromaRecyclerView;
        protected RecyclerView categoryRecyclerView;
        protected CircleImageView baseImageView;
        protected LinearLayout progressLayout;


        public CustomViewHolder(View view) {
            super(view);
            this.deviceConfigName = (TextView) view.findViewById(R.id.device_config_name_item);
            this.deviceConfigDate = (TextView) view.findViewById(R.id.device_config_date_item);
            this.aromaRecyclerView = (RecyclerView) view.findViewById(R.id.aroma_recycler_view);
            this.categoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);
            this.baseImageView = (CircleImageView) view.findViewById(R.id.base_image);
            this.baseName = (TextView) view.findViewById(R.id.base_name_item);
            this.progressLayout = (LinearLayout) view.findViewById(R.id.progress_layout);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, DeviceConfigDetailActivity.class);
                    intent.putExtra("DeviceConfig", deviceConfigs.get(getLayoutPosition()));
                    mContext.startActivity(intent);


                }
            });
        }
    }
}