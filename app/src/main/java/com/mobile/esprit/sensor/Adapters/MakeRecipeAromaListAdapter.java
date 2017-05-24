package com.mobile.esprit.sensor.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gadour on 23/04/2017.
 */

public class MakeRecipeAromaListAdapter extends ArrayAdapter<AromePerRecipe> {
    Context context;
    int resource;
    List<AromePerRecipe> objects;

    public MakeRecipeAromaListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<AromePerRecipe> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        final ArrayList<Float> quantities = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            quantities.add(objects.get(i).getQuantity());
        }

        MakeRecipeAromaListHolder holder = new MakeRecipeAromaListHolder();

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);

            holder.ivMakeRecipeItemAroma = (ImageView) view.findViewById(R.id.iv_make_recipe_item_aroma);
            holder.tvMakeRecipeItemAromaName = (TextView) view.findViewById(R.id.tv_make_recipe_item_aroma_name);
            holder.tvMakeRecipeItemAromaManufacturer = (TextView) view.findViewById(R.id.tv_make_recipe_item_aroma_manufacturer);
            holder.etMakeRecipeItemAromaVolume = (EditText) view.findViewById(R.id.et_make_recipe_item_aroma_volume);

            holder.etMakeRecipeItemAromaVolume.setHint(objects.get(position).getQuantity() + "");

            view.setTag(holder);
        } else {
            holder = (MakeRecipeAromaListHolder) view.getTag();
        }


        Picasso.with(context).load(getItem(position).getArome().getImgArome()).noPlaceholder().centerCrop().fit().into(
                holder.ivMakeRecipeItemAroma
        );

        holder.tvMakeRecipeItemAromaName.setText(getItem(position).getArome().getName());
        holder.tvMakeRecipeItemAromaManufacturer.setText(getItem(position).getArome().getManufacturer().getName());
        final MakeRecipeAromaListHolder finalHolder = holder;
        holder.etMakeRecipeItemAromaVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().equals("")) {

                    if (Float.parseFloat(editable.toString()) > quantities.get(position)) {

                        AlertDialog.Builder dial = new AlertDialog.Builder(getContext());
                        dial.setTitle("Warning");
                        dial.setMessage("Aroma amount can not be superior to your current aroma volume (" + (quantities.get(position)) + ").");
                        dial.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finalHolder.etMakeRecipeItemAromaVolume.getText().clear();
                            }
                        });
                        dial.show();

                    } else {
                        objects.get(position).setQuantity(quantities.get(position) - Float.parseFloat(editable.toString()));
                    }


                }

            }
        });


        return view;
    }


    class MakeRecipeAromaListHolder {
        ImageView ivMakeRecipeItemAroma;
        TextView tvMakeRecipeItemAromaName;
        TextView tvMakeRecipeItemAromaManufacturer;
        EditText etMakeRecipeItemAromaVolume;

    }
}