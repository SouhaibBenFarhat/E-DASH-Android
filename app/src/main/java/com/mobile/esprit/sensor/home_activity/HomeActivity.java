package com.mobile.esprit.sensor.home_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mobile.esprit.sensor.AddCustomRecipeActivity.AddCustomRecipeActivity;
import com.mobile.esprit.sensor.AromaActivity;
import com.mobile.esprit.sensor.CategoryActivity;
import com.mobile.esprit.sensor.DeviceConfActivity;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.MakeRecipeActivity;
import com.mobile.esprit.sensor.ManufacturerActivity;
import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.SettingActivity;
import com.mobile.esprit.sensor.Utils.URL;
import com.mobile.esprit.sensor.background_tasks.AccountManager;
import com.mobile.esprit.sensor.background_tasks.BoitierManager;
import com.mobile.esprit.sensor.home_activity.fragments.RecipesFragment;
import com.mobile.esprit.sensor.recipe_detail_activity.RecipeDetailActivity;
import com.mobile.esprit.sensor.user_aromas_activities.UserAromasActivity;
import com.mobile.esprit.sensor.user_favoris_activity.UserFavorisAcitivityActivity;
import com.mobile.esprit.sensor.user_profile_activity.UserProfileActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HomeActivity homeActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        homeActivity = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(HomeActivity.this, AddCustomRecipeActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        CircleImageView circleImageView = (CircleImageView) headerView.findViewById(R.id.user_profile_image);
        TextView tvUsername = (TextView) headerView.findViewById(R.id.tv_username_header);
        TextView tvEmail = (TextView) headerView.findViewById(R.id.tv_user_email_header);

        Picasso.with(this).load(User.getInstance().getProfilePicture()).into(circleImageView);
        if (User.getInstance().getProvider().equals("facebook")) {
            tvUsername.setText(User.getInstance().getFirstName() + " " + User.getInstance().getLastName());
            tvEmail.setText(User.getInstance().getEmail());
        }
        if (User.getInstance().getProvider().equals("google")) {
            tvUsername.setText(User.getInstance().getFirstName());
            tvEmail.setText(User.getInstance().getEmail());
        }

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

        Toast.makeText(this, User.getInstance().getId(), Toast.LENGTH_SHORT).show();


        try {
            System.out.println(User.getInstance().getBoitier().getMacAddress());
        }catch (NullPointerException e){

        }





        SharedPreferences preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE );
        Boolean isFirstUse = preferences.getBoolean("firstUse", true);

        if (isFirstUse){
            final Dialog dialog = new Dialog(HomeActivity.this);
            dialog.setContentView(R.layout.dialog_first_use);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstUse", false);
            editor.commit();

            TextView tvDialogFirstUseYes = (TextView) dialog.findViewById(R.id.tv_dialog_first_use_yes);
            TextView tvDialgoFirstUseNo = (TextView) dialog.findViewById(R.id.tv_dialog_first_use_no);
            final TextView tvDialgoHaveDeviceEnter = (TextView) dialog.findViewById(R.id.tv_dialog_have_device_enter);
            final TextView tvDialogHaveDeviceCancel = (TextView) dialog.findViewById(R.id.tv_dialog_have_device_cancel);
            final LinearLayout llDialogFirstUse = (LinearLayout) dialog.findViewById(R.id.ll_dialog_first_use);
            final LinearLayout llDialogHaveDevice = (LinearLayout) dialog.findViewById(R.id.ll_dialog_have_device);
            final EditText etDeviceId = (EditText) dialog.findViewById(R.id.et_device_id);


            tvDialogFirstUseYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llDialogFirstUse.setVisibility(View.GONE);
                    llDialogHaveDevice.setVisibility(View.VISIBLE);

                    tvDialgoHaveDeviceEnter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            BoitierManager boitierManager = new BoitierManager(HomeActivity.this, homeActivity, dialog);
                            boitierManager.setUserDevice(etDeviceId.getText().toString().trim());



                        }
                    });
                    tvDialogHaveDeviceCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            etDeviceId.getText().clear();
                            etDeviceId.clearFocus();
                            llDialogHaveDevice.setVisibility(View.GONE);
                            llDialogFirstUse.setVisibility(View.VISIBLE);
                        }
                    });



                }
            });
            tvDialgoFirstUseNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });




            dialog.show();

        }

        getSupportFragmentManager().beginTransaction().add(R.id.content_home, new RecipesFragment()).addToBackStack(RecipesFragment.class.toString()).commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            User.setCurrentUser(null);
            LoginManager.getInstance().logOut();
            FirebaseAuth.getInstance().signOut();
            RecipeDetailActivity.FROM_NOTIF = false;
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recipe) {
            // Handle the camera action
        } else if (id == R.id.nav_arome) {
            startActivity(new Intent(HomeActivity.this, AromaActivity.class));

        } else if (id == R.id.nav_category) {
            startActivity(new Intent(HomeActivity.this, CategoryActivity.class));


        } else if (id == R.id.nav_manufacture) {
            startActivity(new Intent(HomeActivity.this, ManufacturerActivity.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_device_config) {
            startActivity(new Intent(HomeActivity.this, DeviceConfActivity.class));

        } else if (id == R.id.my_aroma) {
            startActivity(new Intent(HomeActivity.this, UserAromasActivity.class));

        } else if (id == R.id.favoris) {
            startActivity(new Intent(HomeActivity.this, UserFavorisAcitivityActivity.class));

        } else if (id == R.id.nav_user_profile) {
            startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));

        } else if (id == R.id.nav_make_recipe) {
            startActivity(new Intent(HomeActivity.this, MakeRecipeActivity.class));

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
            intent.putExtra("intentId", "homeIntent");
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
