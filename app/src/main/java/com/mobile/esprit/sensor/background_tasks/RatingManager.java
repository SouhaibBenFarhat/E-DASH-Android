package com.mobile.esprit.sensor.background_tasks;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Souhaib on 28/03/2017.
 */

public class RatingManager {

    private Context context;

    public RatingManager(Context context) {
        this.context = context;
    }


    public void getRecipeRating(int recipeId, final RatingBar ratingBar) {

        StringRequest req = new StringRequest(Request.Method.GET, URL.GET_RECIPE_RATING + String.valueOf(recipeId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("rating");
                System.out.println(response.toString());
                ratingBar.setRating(Float.parseFloat(response.toString()));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

    public void getUserRating(int recipeId, final TextView textView, final ImageView imageView) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL.GET_USER_RATING_BY_RECIPE + String.valueOf(User.getInstance().getId()) + "/" + String.valueOf(recipeId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    float value = (float) response.getDouble("value");
                    imageView.setImageResource(R.drawable.ic_star);
                    textView.setText(String.valueOf(value));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("0");
                imageView.setImageResource(R.drawable.ic_emty_star);
            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

    public void getUserRating(int recipeId, final RatingBar ratingBar) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL.GET_USER_RATING_BY_RECIPE + String.valueOf(User.getInstance().getId()) + "/" + String.valueOf(recipeId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    float value = (float) response.getDouble("value");
                    ratingBar.setRating(value);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ratingBar.setRating(0);
            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

    public void addUserRating(final int recipeId, float value, final TextView textView, final RatingBar ratingBar) {
        JSONObject rating = new JSONObject();
        try {
            rating.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.ADD_RATING + String.valueOf(User.getInstance().getId()) + "/" + recipeId, rating, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    float value = (float) response.getDouble("value");
                    textView.setText(String.valueOf(value));
                    getRecipeRating(recipeId,ratingBar);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

}
