package com.mobile.esprit.sensor.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobile.esprit.sensor.AddCustomRecipeActivity.AddCustomRecipeActivity;
import com.mobile.esprit.sensor.DeviceConfActivity;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gadour on 28/03/2017.
 */

public class DeviceConfAromaAdapter extends ArrayAdapter<AromePerRecipe> implements AdapterView.OnItemClickListener {

    Context context;
    int resource;
    List<AromePerRecipe> objects;
    float aromaAmount = 0.0f;

    public DeviceConfAromaAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<AromePerRecipe> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        DeviceConfAromaHolder holder = new DeviceConfAromaHolder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);

            holder.ivDeviceConfAroma = (ImageView) view.findViewById(R.id.iv_device_conf_aroma);
            holder.tvDeviceConfAromaName = (TextView) view.findViewById(R.id.tv_device_conf_aroma_name);
            holder.tvDeviceConfAromaManufacturere = (TextView) view.findViewById(R.id.tv_device_conf_aroma_manufacturer);
            holder.spDeviceConfAromaPosition = (Spinner) view.findViewById(R.id.sp_device_conf_position);
            holder.etDeviceConfAromaVolume = (EditText) view.findViewById(R.id.et_device_conf_volume);
            holder.btnDelete = (ImageView) view.findViewById(R.id.btn_delete);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AddCustomRecipeActivity.selectedAromePerRecipes.remove(position);
                    AddCustomRecipeActivity.lvSelectedAromas.setAdapter(new DeviceConfAromaAdapter(context, R.layout.item_device_conf_aroma, AddCustomRecipeActivity.selectedAromePerRecipes));


                }
            });
            if(objects.get(position).getQuantity() != 0.0){
                holder.etDeviceConfAromaVolume.setText(objects.get(position).getQuantity() + "");
            }


         if (context instanceof DeviceConfActivity){
             holder.btnDelete = (ImageView) view.findViewById(R.id.btn_delete);
             holder.btnDelete.setVisibility(View.INVISIBLE);
             String[] array = context.getResources().getStringArray(R.array.aromaPosition);
             Integer[] spinnerPosition = new Integer[array.length];
             for (int i = 0; i < array.length; i++) {
                 spinnerPosition[i] = Integer.parseInt(array[i]);
             }

             ArrayAdapter adapterPostion = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, spinnerPosition);
             holder.spDeviceConfAromaPosition.setAdapter(adapterPostion);
             if (objects.get(position).getPosition() != 0 ) {
                 holder.spDeviceConfAromaPosition.setSelection(objects.get(position).getPosition());
             } else {
                 holder.spDeviceConfAromaPosition.setSelection(0);
             }
         }


            view.setTag(holder);

        } else {
            holder = (DeviceConfAromaHolder) view.getTag();

        }


        Picasso.with(context).load(getItem(position).getArome().getImgArome()).noPlaceholder().centerCrop().fit().into(
                holder.ivDeviceConfAroma
        );
        holder.tvDeviceConfAromaName.setText(getItem(position).getArome().getName());
        holder.tvDeviceConfAromaManufacturere.setText(getItem(position).getArome().getManufacturer().getName());


        final DeviceConfAromaHolder finalHolder = holder;
        holder.spDeviceConfAromaPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                objects.get(position).setPosition((Integer) finalHolder.spDeviceConfAromaPosition.getSelectedItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                objects.get(position).setPosition(0);


            }
        });

        holder.etDeviceConfAromaVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().equals("")) {
                    aromaAmount = Float.parseFloat(editable.toString());
                    objects.get(position).setQuantity(aromaAmount);
                    System.out.println("zzzzzzzzzzzzzzz");
                    System.out.println(aromaAmount);
                }


            }
        });


        return view;


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



    }


    class DeviceConfAromaHolder {
        ImageView ivDeviceConfAroma;
        TextView tvDeviceConfAromaName;
        TextView tvDeviceConfAromaManufacturere;
        Spinner spDeviceConfAromaPosition;
        EditText etDeviceConfAromaVolume;
        ImageView btnDelete;
    }
}
