package com.mobile.esprit.sensor.background_tasks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mobile.esprit.sensor.Adapters.AromeRecipeAdapter;
import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Base;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.DeviceConfigRecipe;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.ObjectToJsonParser;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.device_config_detail_activity.adapters.DeviceConfigRecipeAdapter;
import com.mobile.esprit.sensor.home_activity.adapters.RecipeRecyclerAdapter;
import com.mobile.esprit.sensor.recipe_detail_activity.RecipeDetailActivity;
import com.mobile.esprit.sensor.recipe_detail_activity.adapters.SimilarRecipeAdapter;
import com.mobile.esprit.sensor.user_profile_activity.UserProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Souhaib on 24/03/2017.
 */

public class RecipeManager {

    private Context context;
    private JSONParser jsonParser = new JSONParser();
    private ObjectToJsonParser objectToJsonParser = new ObjectToJsonParser();

    public RecipeManager(Context context) {
        this.context = context;
    }

    public void getRecipeById(int recipeId) {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL.GET_RECIPE_BY_ID + String.valueOf(recipeId), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Recipe recipe = new Recipe();
                        Base base = new Base();
                        ArrayList<AromePerRecipe> aromes = new ArrayList<>();


                        try {

                            recipe.setId(response.getInt("id"));
                            recipe.setDescription(response.getString("description"));
                            recipe.setDate(response.getString("date"));
                            recipe.setImageUrl(response.getString("imageUrl"));
                            recipe.setName(response.getString("name"));
                            recipe.setSteep(response.getInt("stip"));
                            recipe.setBaseQuantity((float) response.getDouble("baseQuantity"));
                            recipe.setVolume((float) response.getDouble("volume"));
                            recipe.setTotalAromeQuantity((float) response.getDouble("totalAromeQuantity"));

                            JSONObject jsonBase = response.getJSONObject("base");
                            base.setIdBase(jsonBase.getInt("id"));
                            base.setPg(jsonBase.getInt("pg"));
                            base.setVg(jsonBase.getInt("vg"));
                            base.setNicotine(jsonBase.getInt("nicotine"));
                            base.setDate(jsonBase.getString("date"));
                            base.setDescription(jsonBase.getString("description"));
                            base.setImage(jsonBase.getString("imageUrl"));
                            recipe.setBase(base);


                            JSONArray jsonAromes = response.getJSONArray("aromes");

                            int aromesCount = 0;
                            while (aromesCount < jsonAromes.length()) {

                                Aroma aroma = new Aroma();
                                Category category = new Category();
                                Manufacturer manufacturer = new Manufacturer();
                                AromePerRecipe aromePerRecipe = new AromePerRecipe();
                                JSONObject jsonAromaPerRecipe = jsonAromes.getJSONObject(aromesCount);

                                aromePerRecipe.setId(jsonAromaPerRecipe.getInt("id"));
                                aromePerRecipe.setQuantity((float) jsonAromaPerRecipe.getDouble("quantity"));

                                aroma.setIdAroma(jsonAromaPerRecipe.getJSONObject("arome").getInt("id"));
                                aroma.setName(jsonAromaPerRecipe.getJSONObject("arome").getString("arome"));
                                aroma.setImgArome(jsonAromaPerRecipe.getJSONObject("arome").getString("imageUrl"));
                                aroma.setDescription(jsonAromaPerRecipe.getJSONObject("arome").getString("description"));
                                aroma.setDate(jsonAromaPerRecipe.getJSONObject("arome").getString("date"));

                                manufacturer.setIdManufacturer(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("manufacture").getInt("id"));
                                manufacturer.setName(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("manufacture").getString("name"));
                                manufacturer.setDescription(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("manufacture").getString("description"));
                                manufacturer.setImage(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("manufacture").getString("imageUrl"));
                                manufacturer.setAddress(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("manufacture").getString("address"));
                                manufacturer.setDate(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("manufacture").getString("date"));


                                category.setIdCategory(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("category").getInt("id"));
                                category.setName(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("category").getString("category"));
                                category.setImageName(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("category").getString("imageName"));
                                category.setDescription(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("category").getString("description"));
                                category.setImage(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("category").getString("imageUrl"));
                                category.setDate(jsonAromaPerRecipe.getJSONObject("arome").getJSONObject("category").getString("date"));


                                aroma.setManufacturer(manufacturer);
                                aroma.setCategory(category);
                                aromePerRecipe.setArome(aroma);
                                aromes.add(aromePerRecipe);
                                aromesCount++;

                            }
                            recipe.setAromes(aromes);
                            System.out.println("----------------------");
                            System.out.println(recipe);
                            System.out.println("----------------------");

                        } catch (JSONException e) {
                            System.out.println(e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);


    }

    public void getAllRecipe(final RecyclerView recyclerView, final SmoothProgressBar smoothProgressBar) {
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.GET_ALL_RECIPES, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<Recipe> recipes = new ArrayList<>();

                        int responseLength = 0;
                        while (responseLength < response.length()) {

                            try {
                                recipes.add(jsonParser.parseRecipe(response.getJSONObject(responseLength)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            responseLength++;

                        }

                        if (context instanceof RecipeDetailActivity) {

                            SimilarRecipeAdapter similarRecipeAdapter = new SimilarRecipeAdapter(context, recipes);
                            recyclerView.setAdapter(similarRecipeAdapter);
                            if (smoothProgressBar != null) {
                                smoothProgressBar.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            RecipeRecyclerAdapter recipeRecyclerAdapter = new RecipeRecyclerAdapter(context, recipes);
                            recyclerView.setAdapter(recipeRecyclerAdapter);
                            if (smoothProgressBar != null) {
                                smoothProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);

    }

    public void getRecipeByArome(final ListView listView, int aromeId) {
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.FIND_RECIPE_BY_AROME + aromeId, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<Recipe> recipes = new ArrayList<>();

                        int responseLength = 0;
                        while (responseLength < response.length()) {

                            try {
                                recipes.add(jsonParser.parseRecipe(response.getJSONObject(responseLength)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            responseLength++;

                        }


                        AromeRecipeAdapter aromeRecipeAdapter = new AromeRecipeAdapter(context, recipes);
                        listView.setAdapter(aromeRecipeAdapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);

    }

    public void addRecipe(Recipe recipe) {

        String userId = User.getInstance().getId();
        JSONObject jsonRecipe = objectToJsonParser.parseRecipe(recipe);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.ADD_USER_RECIPE + userId, jsonRecipe, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                context.startActivity(new Intent(context, UserProfileActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

        ConnectionSingleton.getInstance(context).addToRequestque(req);

    }

    public void getRecipeByUser(final RecyclerView recyclerView) {
        final ArrayList<Recipe> recipes = new ArrayList<>();
        String userId = User.getInstance().getId();
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.FIND_RECIPE_BY_USER + userId, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int responseLength = response.length();
                for (int i = 0; i < responseLength; i++) {
                    try {
                        recipes.add(jsonParser.parseRecipe(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(new RecipeRecyclerAdapter(context, recipes));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);

    }

    public void getRecipeByTag(final RecyclerView recyclerView, int tag) {

        final ArrayList<Recipe> recipes = new ArrayList<>();
        final JSONParser jsonParser = new JSONParser();
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.FIND_RECIPE_BY_TAG + String.valueOf(tag), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        recipes.add(jsonParser.parseRecipe(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(new SimilarRecipeAdapter(context, recipes));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);

    }

    public void getRecipeByDeviceConfig(final RecyclerView recyclerView, int deviceConfigId) {

        final ArrayList<DeviceConfigRecipe> deviceConfigRecipes = new ArrayList<>();
        final JSONParser jsonParser = new JSONParser();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL.FIND_DEVICE_CONFIG_RECIPE_BY_DEVICE_CONFIG + String.valueOf(deviceConfigId), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        deviceConfigRecipes.add(jsonParser.parseDeviceConfigRecipe(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(new DeviceConfigRecipeAdapter(context,deviceConfigRecipes));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(request);


    }

}
