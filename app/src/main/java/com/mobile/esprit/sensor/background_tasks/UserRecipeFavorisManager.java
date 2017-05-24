package com.mobile.esprit.sensor.background_tasks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.SystemCurrentDate;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.home_activity.adapters.RecipeRecyclerAdapter;
import com.mobile.esprit.sensor.recipe_detail_activity.RecipeDetailActivity;
import com.mobile.esprit.sensor.recipe_detail_activity.adapters.SimilarRecipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Souhaib on 01/04/2017.
 */

public class UserRecipeFavorisManager {

    private Context context;

    public UserRecipeFavorisManager(Context context) {
        this.context = context;
    }


    public void addUserRecipeFavoris(final int recipeId, final ImageView imageView) {


        JSONObject userRecipeFavoris = new JSONObject();
        try {
            userRecipeFavoris.put("date", SystemCurrentDate.now());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String userId = User.getInstance().getId();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.ADD_USER_RECIPE_FAVORIS + userId + "/" + recipeId, userRecipeFavoris, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                findUserRecipeFavoris(recipeId, imageView);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

    public void findUserRecipeFavoris(final int recipeId, final ImageView imageView) {

        String userId = User.getInstance().getId();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL.FIND_RECIPE_FAVORIS + userId + "/" + recipeId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("id")) {
                    imageView.setImageResource(R.drawable.ic_full_favoris);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteUserFavoris(recipeId, imageView);
                        }
                    });

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

    public void deleteUserFavoris(final int recipeId, final ImageView imageView) {

        String userId = User.getInstance().getId();
        StringRequest req = new StringRequest(Request.Method.DELETE, URL.DELETE_USER_RECIPE_FAVORIS + userId + "/" + recipeId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addUserRecipeFavoris(recipeId, imageView);
                        }
                    });
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

    public void getAllRecipeFavoris(final RecyclerView recyclerView, final ImageView imageView) {
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.GET_USER_RECIPE_FAVORIS + User.getInstance().getId(), null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<Recipe> recipes = new ArrayList<>();
                        JSONParser jsonParser = new JSONParser();
                        int responseLength = 0;
                        while (responseLength < response.length()) {

                            try {
                                recipes.add(jsonParser.parseRecipe(response.getJSONObject(responseLength)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            responseLength++;

                        }
                        if (recipes.size() == 0) {
                            imageView.setVisibility(View.VISIBLE);
                        }

                        if (context instanceof RecipeDetailActivity) {

                            SimilarRecipeAdapter similarRecipeAdapter = new SimilarRecipeAdapter(context, recipes);
                            recyclerView.setAdapter(similarRecipeAdapter);
                        } else {
                            RecipeRecyclerAdapter recipeRecyclerAdapter = new RecipeRecyclerAdapter(context, recipes);
                            recyclerView.setAdapter(recipeRecyclerAdapter);
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
}
