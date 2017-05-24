package com.mobile.esprit.sensor.home_activity.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.esprit.sensor.AromaActivity;
import com.mobile.esprit.sensor.AromaDetailActivity;
import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Souhaib on 26/03/2017.
 */

public class AromaSmallAdapater extends RecyclerView.Adapter<AromaSmallAdapater.CustomViewHolder> {
    private List<Aroma> aromas;
    private Context mContext;

    public AromaSmallAdapater(Context context, List<Aroma> aromas) {
        this.aromas = aromas;
        this.mContext = context;


    }

    @Override
    public AromaSmallAdapater.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.aroma_small_item, null);
        AromaSmallAdapater.CustomViewHolder viewHolder = new AromaSmallAdapater.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AromaSmallAdapater.CustomViewHolder customViewHolder, int i) {
        final Aroma aroma = aromas.get(i);

        Picasso.with(mContext).load(Uri.parse(aroma.getImgArome())).resize(800, 600).into(customViewHolder.aromaImageView);
    }

    @Override
    public int getItemCount() {
        return (null != aromas ? aromas.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView aromaImageView;


        public CustomViewHolder(View view) {
            super(view);
            this.aromaImageView = (CircleImageView) view.findViewById(R.id.aroma_image);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AromaDetailActivity.class);
                    intent.putExtra(AromaActivity.AROMA_DETAIL, aromas.get(getLayoutPosition()));
                    mContext.startActivity(intent);

                }
            });
        }
    }


}