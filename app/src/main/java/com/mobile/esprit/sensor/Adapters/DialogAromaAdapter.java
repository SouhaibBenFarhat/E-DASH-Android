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
 * Created by Gadour on 27/03/2017.
 */

public class DialogAromaAdapter extends ArrayAdapter<Aroma> {


    Context context;
    int resource;

    public DialogAromaAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Aroma> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        DialogAromaHolder holder = new DialogAromaHolder();

        if(view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);

            holder.tvDialogAromaName = (TextView) view.findViewById(R.id.tv_dialog_aroma_name);
            holder.tvDialogAromaManufacturer = (TextView) view.findViewById(R.id.tv_dialog_aroma_manufacturer);
            holder.ivDialogAroma = (ImageView) view.findViewById(R.id.iv_dialog_aroma);

            view.setTag(holder);


        }else{
            holder = (DialogAromaHolder) view.getTag();
        }

        holder.tvDialogAromaName.setText(getItem(position).getName());
        holder.tvDialogAromaManufacturer.setText(getItem(position).getManufacturer().getName());
        Picasso.with(context).load(getItem(position).getImgArome()).noPlaceholder().centerCrop().fit().into(
                holder.ivDialogAroma
        );

        return view;
    }

    class DialogAromaHolder {
        ImageView ivDialogAroma;
        TextView tvDialogAromaName;
        TextView tvDialogAromaManufacturer;
    }
}
