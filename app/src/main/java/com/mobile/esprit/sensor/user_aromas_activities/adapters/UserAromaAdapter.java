package com.mobile.esprit.sensor.user_aromas_activities.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Souhaib on 30/03/2017.
 */

public class UserAromaAdapter extends RecyclerView.Adapter<UserAromaAdapter.CustomViewHolder> {
    private List<Aroma> aromas;
    private Context mContext;

    public UserAromaAdapter(Context context, List<Aroma> aromas) {
        this.aromas = aromas;
        this.mContext = context;


    }

    @Override
    public UserAromaAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_aroma_adapter, null);
        UserAromaAdapter.CustomViewHolder viewHolder = new UserAromaAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UserAromaAdapter.CustomViewHolder customViewHolder, int i) {
        final Aroma aroma = aromas.get(i);


        if (aroma.isEnabled()) {
            customViewHolder.enabled.setText("Approved");
            customViewHolder.stateImage.setImageResource(R.drawable.ic_done);

        } else {
            customViewHolder.enabled.setText("Rejected");
            customViewHolder.stateImage.setImageResource(R.drawable.ic_rejected);

        }

        customViewHolder.manufacturerName.setText(aroma.getManufacturer().getName());
        customViewHolder.aromaName.setText(aroma.getName());
        Picasso.with(mContext).load(Uri.parse(aroma.getImgArome())).into(customViewHolder.aromaImage);
    }

    @Override
    public int getItemCount() {
        return (null != aromas ? aromas.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView aromaImage;
        protected TextView manufacturerName, aromaName;
        protected TextView enabled;
        protected ImageView stateImage;


        public CustomViewHolder(View view) {
            super(view);
            this.aromaImage = (CircleImageView) view.findViewById(R.id.aroma_image);
            this.manufacturerName = (TextView) view.findViewById(R.id.manufacturer_name);
            this.aromaName = (TextView) view.findViewById(R.id.aroma_name);
            this.enabled = (TextView) view.findViewById(R.id.enabled);
            this.stateImage = (ImageView) view.findViewById(R.id.state_image);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


}
