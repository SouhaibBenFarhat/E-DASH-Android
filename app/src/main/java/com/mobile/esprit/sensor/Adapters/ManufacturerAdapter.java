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

import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gadour on 23/03/2017.
 */

public class ManufacturerAdapter extends ArrayAdapter<Manufacturer> {

    Context context;
    int resource;
    public ManufacturerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Manufacturer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        ManufacturerHolder holder = new ManufacturerHolder();

        if(view == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(resource, parent, false);


            holder.imgManufacturer = (ImageView) view.findViewById(R.id.iv_manufacturer);
            holder.tvManufacturer = (TextView) view.findViewById(R.id.tv_manufacturer);

            view.setTag(holder);

        }

        else {
            holder = (ManufacturerHolder) view.getTag();
        }

        Picasso.with(context).load(getItem(position).getImage()).noPlaceholder().fit().into(
                holder.imgManufacturer
        );
        holder.tvManufacturer.setText(getItem(position).getName());





        return view;
    }

    class ManufacturerHolder {
        ImageView imgManufacturer;
        TextView tvManufacturer;
    }
}
