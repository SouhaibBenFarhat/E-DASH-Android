package com.mobile.esprit.sensor.background_tasks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.esprit.sensor.Entities.Device;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.LoginActivity;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.CustomPreferences;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.home_activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Souhaib on 04/02/2017.
 */

public class AccountManager {

    private Context context;


    public AccountManager(Context context) {
        this.context = context;

    }

    public void authentificateUserWithFacebook(final User u, final AppCompatActivity activity) {


        JSONObject user = new JSONObject();
        try {
            user.put("login", "--");
            user.put("email", u.getEmail());
            user.put("password", "--");
            user.put("provider", u.getProvider());
            user.put("profileId", u.getProfileId());
            user.put("profilePicture", u.getProfilePicture());
            user.put("firstName", u.getFirstName());
            user.put("lastName", u.getLastName());
            user.put("linkUri", u.getLinkUri());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.ADD_USER, user,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONParser jsonParser = new JSONParser();

                        User user = jsonParser.parseUser(response);
                        User.setCurrentUser(user);

                        CustomPreferences customPreferences = new CustomPreferences(context, activity);
                        customPreferences.storeUserInSharedPreferences(user);


                        activity.startActivity(new Intent(activity, HomeActivity.class));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Authentification Failure", Toast.LENGTH_SHORT).show();
                System.out.println("-------------------------");
                System.out.println(error.getMessage());
                System.out.println("-------------------------");
                LoginManager.getInstance().logOut();


            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

    public void authentificateUserWithGoogle(final User u, final AppCompatActivity activity) {


        JSONObject user = new JSONObject();
        try {
            user.put("login", "--");
            user.put("email", u.getEmail());
            user.put("password", "--");
            user.put("provider", u.getProvider());
            user.put("profileId", u.getProfileId());
            user.put("profilePicture", u.getProfilePicture());
            user.put("firstName", u.getFirstName());
            user.put("lastName", u.getLastName());
            user.put("linkUri", u.getLinkUri());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.ADD_USER, user,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {



                            User user = new JSONParser().parseUser(response);
                            User.setCurrentUser(user);

                            CustomPreferences customPreferences = new CustomPreferences(context, activity);
                            customPreferences.storeUserInSharedPreferences(user);

                            activity.startActivity(new Intent(activity, HomeActivity.class));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Authentification Failure", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();


            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);
    }

    public void authentificateUserAnonymosly(String email, String password, final AppCompatActivity activity) {

        JSONObject user = new JSONObject();
        try {
            user.put("login", "--");
            user.put("email", email);
            user.put("password", password);
            user.put("provider", "--");
            user.put("profileId", "--");
            user.put("profilePicture", "--");
            user.put("firstName", "--");
            user.put("lastName", "--");
            user.put("linkUri", "--");


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.SIGNIN_USER, user,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        if (response.has("error")) {
                            Toast.makeText(activity, "Please check your information... user not found", Toast.LENGTH_SHORT).show();
                        } else {




                                User user = new JSONParser().parseUser(response);
                                User.setCurrentUser(user);

                                CustomPreferences customPreferences = new CustomPreferences(context, activity);
                                customPreferences.storeUserInSharedPreferences(user);

                                activity.startActivity(new Intent(activity, HomeActivity.class));


                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Authentification Failure", Toast.LENGTH_SHORT).show();
                User.setCurrentUser(null);


            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);


    }

    public void registerUserAnonymosly(final User u, final AppCompatActivity activity) {


        JSONObject user = new JSONObject();
        try {
            user.put("login", u.getLogin());
            user.put("email", u.getEmail());
            user.put("password", u.getPassword());
            user.put("provider", u.getProvider());
            user.put("profileId", u.getProfileId());
            user.put("profilePicture", u.getProfilePicture());
            user.put("firstName", u.getFirstName());
            user.put("lastName", u.getLastName());
            user.put("linkUri", u.getLinkUri());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.REGISTER_USER, user,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (!response.getString("error").equals("null")) {
                                Toast.makeText(context, "User already exist", Toast.LENGTH_SHORT).show();
                                System.out.println(response);
                            } else {

                                Toast.makeText(context, "Registration done", Toast.LENGTH_SHORT).show();
                                activity.startActivity(new Intent(activity, LoginActivity.class));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "Registration Failure", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Registration Failure", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();


            }
        });
        ConnectionSingleton.getInstance(context).addToRequestque(req);

    }

    public void registerToken(String token) {

        JSONObject user = new JSONObject();
        JSONObject device = new JSONObject();
        try {
            user.put("id", User.getInstance().getId());
            device.put("token", token);
            device.put("user", user);
            System.out.println(device);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.REGISTER_DEVICE_TOKEN, device,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Device device = new Device();
                        try {
                            device.setId(response.getString("id"));
                            device.setToken(response.getString("token"));


                        } catch (JSONException e) {

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
