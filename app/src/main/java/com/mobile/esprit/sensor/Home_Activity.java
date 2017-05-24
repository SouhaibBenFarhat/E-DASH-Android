package com.mobile.esprit.sensor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.background_tasks.AccountManager;


public class Home_Activity extends AppCompatActivity {


    private ImageButton btnAromas,imageButton2,btnManufacturer,btnCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_home);


//        SharedPreferences shareData = getSharedPreferences(URL.MY_PREFS_NAME, MODE_PRIVATE);
//        String newness = shareData.getString("newness", "");
//        if (newness.equals("")){
//            SharedPreferences.Editor editor = getSharedPreferences(URL.MY_PREFS_NAME, MODE_PRIVATE).edit();
//            editor.putString("newness", "NONE");
//            editor.commit();
//        }
//


        AccountManager accountManager = new AccountManager(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences(URL.MY_PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        if (!token.equals("")) {
            accountManager.registerToken(token);
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("NotificationCenter");
            String refToken = FirebaseInstanceId.getInstance()
                    .getToken();
            SharedPreferences.Editor editor = getSharedPreferences(URL.MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("token", refToken);
            editor.commit();
            SharedPreferences rePrefs = getSharedPreferences(URL.MY_PREFS_NAME, MODE_PRIVATE);
            accountManager.registerToken(rePrefs.getString("token", ""));
        }







        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, BaseActivity.class);
                startActivity(intent);
            }
        });

        btnAromas = (ImageButton) findViewById(R.id.imageButton1);
        btnAromas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_Activity.this, AromaActivity.class));
            }
        });

        btnManufacturer =(ImageButton) findViewById(R.id.imageButton3);
        btnManufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_Activity.this, ManufacturerActivity.class));
            }
        });

        btnCategory = (ImageButton) findViewById(R.id.imageButton4);
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_Activity.this, CategoryActivity.class));
            }
        });


        Toast.makeText(this, User.getInstance().getId(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        User.setCurrentUser(null);
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
    }
}
