package com.mobile.esprit.sensor;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.Utils.BluetoothDataService;
import com.mobile.esprit.sensor.background_tasks.BoitierManager;
import com.skyfishjy.library.RippleBackground;

import net.cachapa.expandablelayout.ExpandableLayout;

public class SettingActivity extends AppCompatActivity {

    private EditText etSettingDeiveId;
    private Switch swConnect;
    private Button btnSettingSave;
    private ExpandableLayout elSetting;
    private SettingActivity settingActivity = null;
    private User user;
    private RippleBackground rippleBackground;
    private ImageView ivPhoneBluetooth, ivDeviceBluetooth;
    private static Boolean serviceOn;
    private static boolean firstMessage = true;




    //BLUETOOTH PARAMS
    private BluetoothAdapter btAdapter = null;
    BluetoothDataService bluetoothDataService;
    boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        serviceOn = isMyServiceRunning(BluetoothDataService.class);
        bluetoothDataService = new BluetoothDataService();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("isServiceRunning"));

        user = User.getInstance();
        settingActivity = this;

        rippleBackground = (RippleBackground) findViewById(R.id.content);

        ivPhoneBluetooth = (ImageView) findViewById(R.id.iv_phone_bluetooth);
        ivDeviceBluetooth = (ImageView) findViewById(R.id.iv_device_bluetooth);


        etSettingDeiveId = (EditText) findViewById(R.id.et_setting_device_id);
        swConnect = (Switch) findViewById(R.id.sw_connect);
        btnSettingSave = (Button) findViewById(R.id.btn_setting_save);
        elSetting = (ExpandableLayout) findViewById(R.id.el_setting);


        if (user.getBoitier() != null) {
            etSettingDeiveId.setText(user.getBoitier().getMacAddress());
            btnSettingSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
                    dialog.setTitle("Wanring");
                    dialog.setMessage("You are about to change you Device ID.");
                    dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BoitierManager boitierManager = new BoitierManager(SettingActivity.this, settingActivity);
                            boitierManager.setUserDeviceSetting(etSettingDeiveId.getText().toString().trim());
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();


                }
            });
            if (serviceOn) {
                ivDeviceBluetooth.setVisibility(View.VISIBLE);
                swConnect.setChecked(true);
                elSetting.expand();
                rippleBackground.startRippleAnimation();
                Toast.makeText(this, "Connected to E-Dash device", Toast.LENGTH_SHORT).show();

            }

            swConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(swConnect.isChecked()){

                        btAdapter = BluetoothAdapter.getDefaultAdapter();
                        checkBTOn();
                        swConnect.setChecked(true);


                    }else if(!swConnect.isChecked()){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
                        dialog.setTitle("Wraning");
                        dialog.setMessage("Do you want to disconect from the E-Dash device?");
                        dialog.setPositiveButton("Disconnect", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent serviceIntent = new Intent(SettingActivity.this, BluetoothDataService.class);
                                getApplicationContext().stopService(serviceIntent);
                                ivDeviceBluetooth.setVisibility(View.GONE);
                                rippleBackground.stopRippleAnimation();
                                elSetting.collapse();
                                Toast.makeText(settingActivity, "Disconected from E-DASH device", Toast.LENGTH_SHORT).show();
                                swConnect.setChecked(false);

                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                swConnect.setChecked(true);

                            }
                        });
                        dialog.show();


                    }
                }
            });

        } else if (user.getBoitier() == null) {
            swConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swConnect.setChecked(false);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
                    dialog.setTitle("Wanring");
                    dialog.setMessage("You have to identify your device before trying to connect on it !");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                }
            });
            btnSettingSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    BoitierManager boitierManager = new BoitierManager(SettingActivity.this, settingActivity);
                    boitierManager.setUserDeviceSetting(etSettingDeiveId.getText().toString().trim());

                }
            });
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkBTOn() {
        if (btAdapter == null) {


        } else {
            if (btAdapter.isEnabled()) {
                Intent serviceIntent = new Intent(SettingActivity.this, BluetoothDataService.class);
                getApplicationContext().startService(serviceIntent);
                ivDeviceBluetooth.setVisibility(View.VISIBLE);
                rippleBackground.startRippleAnimation();
                elSetting.expand();
                Intent intent = SettingActivity.this.getIntent();
                if( intent != null){
                    String strdata = intent.getExtras().getString("intentId");
                    if(strdata.equals("makeRecipeIntent")){
                        setResult(RESULT_OK);
                        finish();
                    }
                }

            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            Intent serviceIntent = new Intent(getApplicationContext(), BluetoothDataService.class);
            getApplicationContext().startService(serviceIntent);
            Intent intent = SettingActivity.this.getIntent();
            if( intent != null){
                String strdata = intent.getExtras().getString("intentId");
                if(strdata.equals("makeRecipeIntent")){
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }


        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if(firstMessage){
                String message = intent.getStringExtra("Status");
                if (message.equals("running")) {
                    ivDeviceBluetooth.setVisibility(View.VISIBLE);
                    elSetting.expand();
                    rippleBackground.startRippleAnimation();
                    Toast.makeText(settingActivity, "Connected to E-Dash device", Toast.LENGTH_SHORT).show();
                    firstMessage = false;
                }else if (message.equals("stopped")){
                    ivDeviceBluetooth.setVisibility(View.GONE);
                    elSetting.collapse();
                    rippleBackground.stopRippleAnimation();
                    swConnect.setChecked(false);
                    Toast.makeText(settingActivity, "Disconnected from E-Dash device", Toast.LENGTH_SHORT).show();
                    firstMessage = false;
                }else{
                    firstMessage = true;
                }

            }



        }
    };


}