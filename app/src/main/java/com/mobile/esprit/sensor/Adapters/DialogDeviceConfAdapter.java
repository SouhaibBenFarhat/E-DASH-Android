package com.mobile.esprit.sensor.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.DeviceConfig;
import com.mobile.esprit.sensor.R;

import java.util.List;

/**
 * Created by Gadour on 23/04/2017.
 */

public class DialogDeviceConfAdapter extends ArrayAdapter<DeviceConfig>{


    Context context;
    int resource;

    public DialogDeviceConfAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DeviceConfig> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;



        DialogDeviceConfHolder holder = new DialogDeviceConfHolder();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);

            holder.tvDialogDeviceConfName = (TextView) view.findViewById(R.id.tv_dialog_device_conf_name);
            holder.tvDialogDeviceConfDescription = (TextView) view.findViewById(R.id.tv_dialog_device_conf_description);
            holder.tvDilaogDeviceConfDate = (TextView) view.findViewById(R.id.tv_dialog_device_conf_date);

            view.setTag(holder);


        }else {

            holder = (DialogDeviceConfHolder) view.getTag();
        }


        holder.tvDialogDeviceConfName.setText(getItem(position).getName());
        holder.tvDilaogDeviceConfDate.setText(getItem(position).getDate());
        holder.tvDialogDeviceConfDescription.setText(getItem(position).getNote());





        return view;

    }



    class DialogDeviceConfHolder {

        TextView tvDialogDeviceConfName;
        TextView tvDilaogDeviceConfDate;
        TextView tvDialogDeviceConfDescription;
    }
}
