package com.mobile.esprit.sensor.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.mobile.esprit.sensor.Entities.User;

/**
 * Created by Souhaib on 02/04/2017.
 */

public class CustomPreferences {
    private Context context;
    private AppCompatActivity appCompatActivity;

    public CustomPreferences(Context context, AppCompatActivity appCompatActivity) {

        this.appCompatActivity = appCompatActivity;
        this.context = context;
    }


    public void storeUserInSharedPreferences(User user) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("profilePicture", user.getProfilePicture());
        prefsEditor.putString("provider", user.getProvider());
        prefsEditor.putString("profileId", user.getProfileId());
        prefsEditor.putString("password", user.getPassword());
        prefsEditor.putString("email", user.getEmail());
        prefsEditor.putString("firstName", user.getFirstName());
        prefsEditor.putString("lastName", user.getLastName());
        prefsEditor.putString("id", user.getId());
        prefsEditor.putString("linkUri", user.getLinkUri());
        prefsEditor.putString("login", user.getLogin());
        prefsEditor.apply();
        User.setCurrentUser(user);

    }

    public User getUserFromSharedPreference() {

        User user = new User();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        user.setProfilePicture(mPrefs.getString("profilePicture", ""));
        user.setProvider(mPrefs.getString("provider", ""));
        user.setProfileId(mPrefs.getString("profileId", ""));
        user.setPassword(mPrefs.getString("password", ""));
        user.setEmail(mPrefs.getString("email", ""));
        user.setFirstName(mPrefs.getString("firstName", ""));
        user.setLastName(mPrefs.getString("lastName", ""));
        user.setId(mPrefs.getString("id", ""));
        user.setLinkUri(mPrefs.getString("linkUri", ""));
        user.setLogin(mPrefs.getString("login", ""));
        User.setCurrentUser(user);

        return user;

    }
}
