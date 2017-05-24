package com.mobile.esprit.sensor.AddCustomRecipeActivity.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.esprit.sensor.AddCustomRecipeActivity.AddCustomRecipeActivity;
import com.mobile.esprit.sensor.Entities.Base;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Souhaib on 21/04/2017.
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.CustomViewHolder> {
    private List<Base> bases;
    private Context mContext;

    public BaseAdapter(Context context, List<Base> bases) {
        this.bases = bases;
        this.mContext = context;


    }

    @Override
    public BaseAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_base, null);
        BaseAdapter.CustomViewHolder viewHolder = new BaseAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BaseAdapter.CustomViewHolder customViewHolder, int i) {
        final Base base = bases.get(i);

        customViewHolder.baseName.setText("" + base.getPg() + ":" + base.getVg() + ":" + base.getNicotine());
        customViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCustomRecipeActivity.base = base;

                AddCustomRecipeActivity.baseNicotine.setText(base.getNicotine() + "mg");
                AddCustomRecipeActivity.basePg.setText(base.getPg() + "");
                AddCustomRecipeActivity.baseVg.setText(base.getVg() + "");

                AddCustomRecipeActivity.nicotineProgress.setProgress(base.getNicotine());
                AddCustomRecipeActivity.pgProgress.setProgress(base.getPg());
                AddCustomRecipeActivity.vgPogress.setProgress(base.getVg());

                Picasso.with(mContext).load(Uri.parse(base.getMobileImageUrl())).into(AddCustomRecipeActivity.baseImage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != bases ? bases.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView baseName;
        protected LinearLayout linearLayout;


        public CustomViewHolder(View view) {
            super(view);
            this.baseName = (TextView) view.findViewById(R.id.base_name);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.base_layout);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }
    }


}