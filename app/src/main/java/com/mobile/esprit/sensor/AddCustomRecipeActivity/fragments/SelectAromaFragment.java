package com.mobile.esprit.sensor.AddCustomRecipeActivity.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mobile.esprit.sensor.Adapters.AromaAdapter;
import com.mobile.esprit.sensor.Adapters.DeviceConfAromaAdapter;
import com.mobile.esprit.sensor.AddCustomRecipeActivity.AddCustomRecipeActivity;
import com.mobile.esprit.sensor.AddCustomRecipeActivity.adapters.CheckCompositionAromeAdapter;
import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.home_activity.adapters.CategorySmallAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectAromaFragment extends Fragment {

    ListView lvAroma;
    ArrayList<Aroma> aromas;
    SmoothProgressBar smoothProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_aroma, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        smoothProgressBar = (SmoothProgressBar) getActivity().findViewById(R.id.smoothProgressBar);
        if (smoothProgressBar != null) {
            smoothProgressBar.setVisibility(View.VISIBLE);
            smoothProgressBar.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(getActivity())
                    .colors(getResources().getIntArray(R.array.colors)).build());
        }


        aromas = new ArrayList<>();
        lvAroma = (ListView) getActivity().findViewById(R.id.lv_aroma);


        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_All_AROMAS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jaroma = response.getJSONObject(i);
                                Aroma aroma = new Aroma();
                                Manufacturer manufacturer = new Manufacturer();
                                Category category = new Category();

                                aroma.setIdAroma(jaroma.getInt("id"));
                                aroma.setName(jaroma.getString("arome"));
                                aroma.setImgArome(jaroma.getString("imageUrl"));
                                aroma.setDescription(jaroma.getString("description"));
                                aroma.setDate(jaroma.getString("date"));

                                JSONObject jmanufacturer = jaroma.getJSONObject("manufacture");
                                manufacturer.setIdManufacturer(jmanufacturer.getInt("id"));
                                manufacturer.setName(jmanufacturer.getString("name"));
                                manufacturer.setDescription(jmanufacturer.getString("description"));
                                manufacturer.setImage(jmanufacturer.getString("imageUrl"));
                                manufacturer.setAddress(jmanufacturer.getString("address"));
                                manufacturer.setDate(jmanufacturer.getString("date"));
                                aroma.setManufacturer(manufacturer);

                                JSONObject jcategory = jaroma.getJSONObject("category");
                                category.setIdCategory(jcategory.getInt("id"));
                                category.setName(jcategory.getString("category"));
                                category.setImageName(jcategory.getString("imageName"));
                                category.setDescription(jcategory.getString("description"));
                                category.setImage(jcategory.getString("imageUrl"));
                                category.setDate(jcategory.getString("date"));
                                aroma.setCategory(category);

                                aromas.add(aroma);


                            }


                            AromaAdapter aromaAdapter = new AromaAdapter(getActivity(), R.layout.item_aroma, aromas);
                            lvAroma.setAdapter(aromaAdapter);

                            smoothProgressBar.setVisibility(View.INVISIBLE);
                            lvAroma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    AromePerRecipe aromePerRecipe = new AromePerRecipe();
                                    aromePerRecipe.setArome(aromas.get(i));
                                    aromePerRecipe.setQuantity(0);
                                    if (AddCustomRecipeActivity.selectedAromePerRecipes.contains(aromePerRecipe)) {
                                        Toast.makeText(getActivity(), "Already Selected", Toast.LENGTH_SHORT).show();
                                    } else {
                                        AddCustomRecipeActivity.selectedAromePerRecipes.add(aromePerRecipe);
                                        AddCustomRecipeActivity.lvSelectedAromas.setAdapter(new DeviceConfAromaAdapter(getActivity(), R.layout.item_device_conf_aroma, AddCustomRecipeActivity.selectedAromePerRecipes));
                                        AddCustomRecipeActivity.checkCompositionRecyclerView.setAdapter(new CheckCompositionAromeAdapter(getActivity(), AddCustomRecipeActivity.selectedAromePerRecipes));

                                    }


                                    if (!AddCustomRecipeActivity.selectedAromaCategory.contains(aromas.get(i).getCategory())) {
                                        AddCustomRecipeActivity.selectedAromaCategory.add(aromas.get(i).getCategory());
                                        AddCustomRecipeActivity.rvCategories.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                                        AddCustomRecipeActivity.rvCategories.setAdapter(new CategorySmallAdapter(getActivity(), AddCustomRecipeActivity.selectedAromaCategory));
                                    }


                                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error while parsing...", Toast.LENGTH_SHORT).show();

            }
        }
        );

        ConnectionSingleton.getInstance(getActivity()).addToRequestque(req);

    }
}
