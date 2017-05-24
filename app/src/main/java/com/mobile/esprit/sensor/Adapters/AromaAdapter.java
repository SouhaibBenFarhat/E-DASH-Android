package com.mobile.esprit.sensor.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gadour on 12/03/2017.
 */

public class AromaAdapter extends ArrayAdapter<Aroma> {

    Context context;
    int resource;
    public AromaAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Aroma> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        AromaHolder holder = new AromaHolder();
        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(resource, parent, false);

            holder.imgAroma = (ImageView) view.findViewById(R.id.img_aroma);
            holder.imgAromaCategory = (ImageView) view.findViewById(R.id.img_aroma_category);
            holder.tvAromaName = (TextView) view.findViewById(R.id.tv_aroma_name);
            holder.tvAromaManufacturer = (TextView) view.findViewById(R.id.tv_aroma_manufacturer);
            holder.tvAromaRate = (TextView) view.findViewById(R.id.tv_aroma_rate);
            holder.tvAromaComments = (TextView) view.findViewById(R.id.tv_aroma_comments);

            view.setTag(holder);
        }
        else{
            holder =(AromaHolder) view.getTag();
        }

        Picasso.with(context).load(getItem(position).getImgArome()).noPlaceholder().centerCrop().fit().into(
                holder.imgAroma
        );
        Picasso.with(context).load(getItem(position).getCategory().getImage()).noPlaceholder().fit().into(
                holder.imgAromaCategory
        );
        holder.tvAromaName.setText(getItem(position).getName());
        holder.tvAromaManufacturer.setText(getItem(position).getManufacturer().getName());





        return view;
    }

    class AromaHolder {
        ImageView imgAroma;
        ImageView imgAromaCategory;
        TextView tvAromaName;
        TextView tvAromaManufacturer;
        TextView tvAromaRate;
        TextView tvAromaComments;
    }
}
