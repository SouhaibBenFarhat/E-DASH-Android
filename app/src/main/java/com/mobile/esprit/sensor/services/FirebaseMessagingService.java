package com.mobile.esprit.sensor.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.messaging.RemoteMessage;
import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Base;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.recipe_detail_activity.RecipeDetailActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Souhaib on 06/02/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getData().get("message"));
    }

    private void showNotification(String message) {

        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.recipe_notification);
        notification_id = (int) System.currentTimeMillis();


        try {
            JSONObject data = new JSONObject(message);
            int recipeId = data.getInt("recipeId");




            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL.GET_RECIPE_BY_ID + String.valueOf(recipeId), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            final Recipe recipe = new Recipe();
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
                                base.setMobileImageUrl(jsonBase.getString("mobileImageUrl"));
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

                                Picasso.with(context)
                                        .load(recipe.getImageUrl())
                                        .into(new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                RecipeDetailActivity.FROM_NOTIF = true;
                                                final Intent i = new Intent(FirebaseMessagingService.this, RecipeDetailActivity.class);
                                                i.putExtra("Recipe", recipe);
                                                remoteViews.setImageViewBitmap(R.id.recipe_img, bitmap);
                                                remoteViews.setTextViewText(R.id.recipe_name, recipe.getName());
                                                remoteViews.setTextViewText(R.id.recipe_description, recipe.getDescription());
                                               try{
                                                   remoteViews.setTextViewText(R.id.recipe_date, recipe.getDate().substring(0, 24));
                                               }catch (IndexOutOfBoundsException e){

                                               }
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                                                builder = new NotificationCompat.Builder(context);
                                                builder.setSmallIcon(R.mipmap.ic_launch)
                                                        .setAutoCancel(true)
                                                        .setCustomBigContentView(remoteViews)
                                                        .setPriority(Notification.PRIORITY_MAX)
                                                        .setLargeIcon(bitmap)
                                                        .setLights(Color.BLUE, 3000, 3000)
                                                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                                        .setContentTitle(recipe.getName())
                                                        .setContentIntent(pendingIntent);
                                                notificationManager.notify(notification_id, builder.build());
                                            }

                                            @Override
                                            public void onBitmapFailed(Drawable errorDrawable) {

                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                                            }
                                        });


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


        } catch (JSONException e) {

        }


    }

}
