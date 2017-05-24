package com.mobile.esprit.sensor.background_tasks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mobile.esprit.sensor.Entities.Comment;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.comment_activity.adapters.CommentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Souhaib on 27/03/2017.
 */

public class  CommentManager {

    private Context context;
    private static int size = 0;

    public CommentManager(Context context) {
        this.context = context;
    }


    public void getCommentByRecipe(int recipeId, final RecyclerView recyclerView) {

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.GET_COMMENT_BY_RECIPE + String.valueOf(recipeId), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Comment> comments = new ArrayList<>();
                int count = 0;
                size = response.length();
                JSONParser jsonParser = new JSONParser();
                while (count < response.length()) {
                    try {

                        comments.add(jsonParser.parseComment(response.getJSONObject(count)));
                        count++;

                    } catch (JSONException e) {
                        Toast.makeText(context, "Data Error", Toast.LENGTH_SHORT).show();
                    }

                }
                recyclerView.setAdapter(new CommentAdapter(context, comments));
                recyclerView.getLayoutManager().scrollToPosition(comments.size()-1);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

    public void deleteComment(int commentId) {

    }

    public void addComment(final int recipeId, Comment comment, final RecyclerView recyclerView) {

        JSONObject jsonComment = new JSONObject();
        try {
            jsonComment.put("content", comment.getContent());
            jsonComment.put("date", comment.getDate());

        } catch (JSONException e) {

        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.ADD_COMMENT + String.valueOf(User.getInstance().getId()) + "/" + recipeId, jsonComment, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                getCommentByRecipe(recipeId, recyclerView);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

    public void getCommentCount(int recipeId, final TextView textView) {

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.GET_COMMENT_BY_RECIPE + String.valueOf(recipeId), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Comment> comments = new ArrayList<>();
                int count = 0;
                textView.setText("("+response.length()+")");

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
