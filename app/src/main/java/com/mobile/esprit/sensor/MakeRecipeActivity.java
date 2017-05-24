package com.mobile.esprit.sensor;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mobile.esprit.sensor.Adapters.DialogDeviceConfAdapter;
import com.mobile.esprit.sensor.Adapters.MakeRecipeAromaAdapter;
import com.mobile.esprit.sensor.Adapters.MakeRecipeAromaListAdapter;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.DeviceConfig;
import com.mobile.esprit.sensor.Entities.DeviceConfigRecipe;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.Utils.BluetoothDataService;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.ObjectToJsonParser;
import com.mobile.esprit.sensor.Utils.SystemCurrentDate;
import com.mobile.esprit.sensor.Utils.URL;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MakeRecipeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout tabLayout;
    private static Boolean serviceOn;

    private static boolean firstMessage = true;
    private static boolean saveRecipe = false;

    private BluetoothAdapter btAdapter = null;

    static EditText etDeviceConfRecipeName ;
    static EditText etDeviceConfRecipeSteep;
    static EditText etDeviceConfRecipeDescription;
    static DeviceConfig[] devCon;

    static DeviceConfig[] deviceConfig;
    static DeviceConfig[] deviceConfigReset;
    static ArrayList<AromePerRecipe> aromes;
    static Float[] totalVolume;
    static User user;

    public static final int REQUEST_CONNECTION = 1;

    static ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Make Recipe");
        }

        progressDialog = new ProgressDialog(this);



        serviceOn = isMyServiceRunning(BluetoothDataService.class);

        deviceConfig = new DeviceConfig[1];
        deviceConfigReset = new DeviceConfig[]{new DeviceConfig()};
        devCon = new DeviceConfig[]{new DeviceConfig()};
        aromes = new ArrayList<>();
        totalVolume = new Float[1];
        user = User.getInstance();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        createTabIcons();


    }


    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.customize, 0, 0, 0);
        tabOne.setText("Customize your E-liquid");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ready, 0, 0, 0);
        tabTwo.setText("Ready E-liquid recipes");
        tabLayout.getTabAt(1).setCustomView(tabTwo);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = new View(getContext());

            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                    mMessageReceiver, new IntentFilter("isRecipeMade"));


            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.fragment_make_recipe, container, false);
                RelativeLayout rlMain = (RelativeLayout) rootView.findViewById(R.id.rl_main);
                final ExpandableLayout elMakeRecipeConf = (ExpandableLayout) rootView.findViewById(R.id.el_make_recipe_conf);

                final ImageView ivBaseOne = (ImageView) rootView.findViewById(R.id.iv_make_base_one);
                final ImageView ivBaseTwo = (ImageView) rootView.findViewById(R.id.iv_make_base_two);
                final ImageView ivBaseThree = (ImageView) rootView.findViewById(R.id.iv_make_base_three);
                final ImageView ivAromaOne = (ImageView) rootView.findViewById(R.id.iv_make_aroma_one);
                final ImageView ivAromaTwo = (ImageView) rootView.findViewById(R.id.iv_make_aroma_two);
                final ImageView ivAromaThree = (ImageView) rootView.findViewById(R.id.iv_make_aroma_three);
                final ImageView ivAromaFour = (ImageView) rootView.findViewById(R.id.iv_make_aroma_four);
                final ImageView ivAromaFive = (ImageView) rootView.findViewById(R.id.iv_make_aroma_five);


                final TextView tvPgMl = (TextView) rootView.findViewById(R.id.tv_make_recipe_pg_ml);
                final TextView tvPgPer = (TextView) rootView.findViewById(R.id.tv_make_recipe_pg_per);
                final TextView tvVgMl = (TextView) rootView.findViewById(R.id.tv_make_recipe_vg_ml);
                final TextView tvVgPer = (TextView) rootView.findViewById(R.id.tv_make_recipe_vg_per);
                final TextView tvNicoMl = (TextView) rootView.findViewById(R.id.tv_make_recipe_nico_ml);
                final TextView tvNicoPer = (TextView) rootView.findViewById(R.id.tv_make_recipe_nico_per);

                final TextView tvAroma1 = (TextView) rootView.findViewById(R.id.tv_make_recipe_first_aroma);
                final TextView tvAroma1Ml = (TextView) rootView.findViewById(R.id.tv_make_recipe_a1_ml);
                final TextView tvAroma1Per = (TextView) rootView.findViewById(R.id.tv_make_recipe_a1_per);

                final TextView tvAroma2 = (TextView) rootView.findViewById(R.id.tv_make_recipe_second_aroma);
                final TextView tvAroma2Ml = (TextView) rootView.findViewById(R.id.tv_make_recipe_a2_ml);
                final TextView tvAroma2Per = (TextView) rootView.findViewById(R.id.tv_make_recipe_a2_per);

                final TextView tvAroma3 = (TextView) rootView.findViewById(R.id.tv_make_recipe_third_aroma);
                final TextView tvAroma3Ml = (TextView) rootView.findViewById(R.id.tv_make_recipe_a3_ml);
                final TextView tvAroma3Per = (TextView) rootView.findViewById(R.id.tv_make_recipe_a3_per);

                final TextView tvAroma4 = (TextView) rootView.findViewById(R.id.tv_make_recipe_fourth_aroma);
                final TextView tvAroma4Ml = (TextView) rootView.findViewById(R.id.tv_make_recipe_a4_ml);
                final TextView tvAroma4Per = (TextView) rootView.findViewById(R.id.tv_make_recipe_a4_per);

                final TextView tvAroma5 = (TextView) rootView.findViewById(R.id.tv_make_recipe_fifth_aroma);
                final TextView tvAroma5Ml = (TextView) rootView.findViewById(R.id.tv_make_recipe_a5_ml);
                final TextView tvAroma5Per = (TextView) rootView.findViewById(R.id.tv_make_recipe_a5_per);

                final LinearLayout llA1 = (LinearLayout) rootView.findViewById(R.id.ll_a1);
                final LinearLayout llA2 = (LinearLayout) rootView.findViewById(R.id.ll_a2);
                final LinearLayout llA3 = (LinearLayout) rootView.findViewById(R.id.ll_a3);
                final LinearLayout llA4 = (LinearLayout) rootView.findViewById(R.id.ll_a4);
                final LinearLayout llA5 = (LinearLayout) rootView.findViewById(R.id.ll_a5);


                //select device config
                ImageView imgMakeRecipeDeviceConf = (ImageView) rootView.findViewById(R.id.img_make_recipe_device_conf);
                final TextView tvMakeRecipeDeviceConfName = (TextView) rootView.findViewById(R.id.tv_make_recipe_device_conf_name);
                final RecyclerView rvMakeRecipeAroma = (RecyclerView) rootView.findViewById(R.id.rv_make_recipe_aroma);
                final TextView tvMakeRecipeBase = (TextView) rootView.findViewById(R.id.tv_make_recipe_base);
                final ListView lvMakeRecipeAroma = (ListView) rootView.findViewById(R.id.lv_make_recipe_aroma);
                final MakeRecipeAromaListAdapter[] adapter = new MakeRecipeAromaListAdapter[1];
                final EditText etMakeRecipeBaseVolume = (EditText) rootView.findViewById(R.id.et_make_recipe_base_amount);
                final EditText etMakeRecipeTotalAmount = (EditText) rootView.findViewById(R.id.et_make_recipe_total_amount);
                final Button btnMakeRecipe = (Button) rootView.findViewById(R.id.btn_make_recipe);
                final Button btnCheckRecipe = (Button) rootView.findViewById(R.id.btn_check_recipe);
                final Boolean[] isChecked = {false};
                final Float[] totalAromaVolume = {0f};
                etDeviceConfRecipeName = (EditText) rootView.findViewById(R.id.et_device_conf_recipe_name);
                etDeviceConfRecipeSteep = (EditText) rootView.findViewById(R.id.et_device_conf_recipe_steep);
                etDeviceConfRecipeDescription = (EditText) rootView.findViewById(R.id.et_device_conf_recipe_description);



                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL.UPDATE_DEVICE_CONF_IS_DEFAULT + user.getId(), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                JSONParser jsonParser = new JSONParser();
                                devCon[0] = jsonParser.parseDeviceConfigHistory(response);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                ConnectionSingleton.getInstance(getContext()).addToRequestque(objectRequest);

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL.UPDATE_DEVICE_CONF_HISTORY_IS_DEFAULT + user.getId(), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONParser jsonParser = new JSONParser();
                                deviceConfig[0] = jsonParser.parseDeviceConfigHistory(response);
                                deviceConfigReset[0] = jsonParser.parseDeviceConfigHistory(response);

                                etMakeRecipeBaseVolume.setHint(deviceConfig[0].getBaseQuantity() + "");

                                etMakeRecipeBaseVolume.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                        if (!editable.toString().equals("")) {
                                            if (Float.parseFloat(editable.toString()) > deviceConfig[0].getBaseQuantity()) {

                                                AlertDialog.Builder dial = new AlertDialog.Builder(getContext());
                                                dial.setTitle("Warning");
                                                dial.setMessage("Base amount can not be superior to your current base volume (" + (deviceConfigReset[0].getBaseQuantity()) + ").");
                                                dial.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        etMakeRecipeBaseVolume.getText().clear();
                                                    }
                                                });
                                                dial.show();

                                            } else {
                                                deviceConfig[0].setBaseQuantity(deviceConfigReset[0].getBaseQuantity() - Float.parseFloat(editable.toString()));
                                            }
                                        }

                                    }
                                });

                                etMakeRecipeTotalAmount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                        if (!editable.toString().equals("")) {
                                            if (Float.parseFloat(editable.toString()) > deviceConfig[0].getVolume()) {

                                                AlertDialog.Builder dial = new AlertDialog.Builder(getContext());
                                                dial.setTitle("Warning");
                                                dial.setMessage("E-liquid amount can not be superior to total device configuration volume (" + (deviceConfig[0].getVolume()) + ").");
                                                dial.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        etMakeRecipeTotalAmount.getText().clear();
                                                    }
                                                });
                                                dial.show();

                                            }
                                        }

                                    }
                                });

                                tvMakeRecipeDeviceConfName.setText(deviceConfig[0].getName());
                                tvMakeRecipeBase.setText(deviceConfig[0].getBase().getPg() + "/" + deviceConfig[0].getBase().getVg() + "/" + deviceConfig[0].getBase().getNicotine());

                                MakeRecipeAromaAdapter makeRecipeAdapter = new MakeRecipeAromaAdapter(getContext(), deviceConfig[0].getAromas());
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                rvMakeRecipeAroma.setLayoutManager(linearLayoutManager);
                                rvMakeRecipeAroma.setAdapter(makeRecipeAdapter);


                                makeRecipeAdapter.setOnItemClickListener(new MakeRecipeAromaAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {

                                        if (isChecked[0]) {

                                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                            alertDialog.setTitle("Warning");
                                            alertDialog.setMessage("You cannot add a new aroma to an already checked composition, you need to REST composition first.");
                                            alertDialog.setPositiveButton("REST", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    getActivity().finish();
                                                    startActivity(getActivity().getIntent());
                                                }
                                            });
                                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            alertDialog.show();


                                        } else {
                                            if (!aromes.contains(deviceConfig[0].getAromas().get(position))) {
                                                aromes.add(deviceConfig[0].getAromas().get(position));
                                                adapter[0] = new MakeRecipeAromaListAdapter(getContext(), R.layout.item_make_recipe, aromes);
                                                lvMakeRecipeAroma.setAdapter(adapter[0]);
                                                adapter[0].notifyDataSetChanged();


                                            } else {
                                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                                alertDialog.setTitle("Warning");
                                                alertDialog.setMessage(R.string.device_conf_aroma_used);
                                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                });
                                                alertDialog.show();
                                            }

                                        }


                                    }

                                    @Override
                                    public void onItemLongClick(int position, View v) {

                                    }
                                });


                            }


                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                ConnectionSingleton.getInstance(getContext()).addToRequestque(req);

                imgMakeRecipeDeviceConf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog_device_conf);

                        final ListView lvDialogDeviceConf = (ListView) dialog.findViewById(R.id.lv_dialog_device_conf);

                        TextView tvDialogDeviceConfCancel = (TextView) dialog.findViewById(R.id.tv_dialog_device_conf_cancel);

                        final ArrayList<DeviceConfig> deviceConfigs = new ArrayList<>();

                        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_DEVICE_CONFIG_HISTORY + user.getId(), null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        JSONParser jsonParser = new JSONParser();

                                        try {
                                            for (int i = 0; i < response.length(); i++) {
                                                deviceConfigs.add(jsonParser.parseDeviceConfigHistory(response.getJSONObject(i)));
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        DialogDeviceConfAdapter dialogAdapter = new DialogDeviceConfAdapter(getContext(), R.layout.item_dialog_device_conf, deviceConfigs);
                                        lvDialogDeviceConf.setAdapter(dialogAdapter);


                                        lvDialogDeviceConf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                getActivity().finish();
                                                startActivity(getActivity().getIntent());


                                                tvMakeRecipeDeviceConfName.setText(deviceConfigs.get(i).getName());
                                                tvMakeRecipeBase.setText(deviceConfigs.get(i).getBase().getPg() + "/" + deviceConfigs.get(i).getBase().getVg() + "/" + deviceConfigs.get(i).getBase().getNicotine());
                                                deviceConfig[0] = deviceConfigs.get(i);


                                                MakeRecipeAromaAdapter makeRecipeAdapter = new MakeRecipeAromaAdapter(getContext(), deviceConfig[0].getAromas());
                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                                rvMakeRecipeAroma.setLayoutManager(linearLayoutManager);
                                                rvMakeRecipeAroma.setAdapter(makeRecipeAdapter);


                                                dialog.dismiss();
                                            }
                                        });


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();

                            }
                        });

                        ConnectionSingleton.getInstance(getContext()).addToRequestque(req);

                        tvDialogDeviceConfCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                });


                btnCheckRecipe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (elMakeRecipeConf.isExpanded()) {
                            elMakeRecipeConf.collapse();
                        }
                        isChecked[0] = true;

                        Float aromaVolume = 0.0f;
                        for (int i = 0; i < deviceConfig[0].getAromas().size(); i++) {
                            aromaVolume += (deviceConfigReset[0].getAromas().get(i).getQuantity()) - deviceConfig[0].getAromas().get(i).getQuantity();
                        }
                        totalVolume[0] = (deviceConfigReset[0].getBaseQuantity() - deviceConfig[0].getBaseQuantity()) + aromaVolume;

                        System.out.println("-------------------------");
                        System.out.println("-------------------------");
                        System.out.println(deviceConfig[0].getAromas().size());
                        for (int i = 0; i < deviceConfig[0].getAromas().size(); i++) {
                            System.out.println("Arome " + i + " : " + deviceConfig[0].getAromas().get(i).getArome().getName());
                            System.out.println("Quantity : " + deviceConfig[0].getAromas().get(i).getQuantity());


                            totalAromaVolume[0] += deviceConfig[0].getAromas().get(i).getQuantity();

                        }
                        System.out.println("Base REST : " + deviceConfigReset[0].getBaseQuantity());
                        System.out.println("Base restante:" + deviceConfig[0].getBaseQuantity());
                        System.out.println("Base utlisé : " + (deviceConfigReset[0].getBaseQuantity() - deviceConfig[0].getBaseQuantity()));
                        System.out.println("Volume total : " + totalVolume[0]);
                        System.out.println("-------------------------");
                        System.out.println("-------------------------");


                        deviceConfig[0].setTotalAromeQuantity(totalAromaVolume[0]);
                        deviceConfig[0].setVolume(totalAromaVolume[0] + deviceConfig[0].getBaseQuantity());


                        if ((totalVolume[0] > Float.parseFloat(etMakeRecipeTotalAmount.getText().toString()))) {

                            AlertDialog.Builder dialo = new AlertDialog.Builder(getContext());
                            dialo.setTitle("Warning");
                            dialo.setMessage("E-Liquid total volume does not much composition total volume. Please check values.");
                            dialo.setPositiveButton("Fix", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    isChecked[0] = false;

                                }
                            });
                            dialo.setNegativeButton("Auto Correct", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    etMakeRecipeTotalAmount.setText(totalVolume[0] + "");

                                }
                            });
                            dialo.show();


                        } else {


                            Float pgValue = (float) deviceConfig[0].getBase().getPg();
                            Float vgValue = (float) deviceConfig[0].getBase().getVg();
                            Float nicValue = (float) deviceConfig[0].getBase().getNicotine();

                            Float a1Value = 0.0f;
                            Float a2Value = 0.0f;
                            Float a3Value = 0.0f;
                            Float a4Value = 0.0f;
                            Float a5Value = 0.0f;


                            a1Value = (float) ((deviceConfigReset[0].getAromas().get(0).getQuantity()) - (deviceConfig[0].getAromas().get(0).getQuantity()));
                            if (deviceConfig[0].getAromas().size() > 1) {
                                a2Value = (float) ((deviceConfigReset[0].getAromas().get(1).getQuantity()) - (deviceConfig[0].getAromas().get(1).getQuantity()));
                            }
                            if (deviceConfig[0].getAromas().size() > 2) {
                                a3Value = (float) ((deviceConfigReset[0].getAromas().get(2).getQuantity()) - (deviceConfig[0].getAromas().get(2).getQuantity()));
                            }
                            if (deviceConfig[0].getAromas().size() > 3) {
                                a4Value = (float) ((deviceConfigReset[0].getAromas().get(3).getQuantity()) - (deviceConfig[0].getAromas().get(3).getQuantity()));
                            }
                            if (deviceConfig[0].getAromas().size() > 4) {
                                a5Value = (float) ((deviceConfigReset[0].getAromas().get(4).getQuantity()) - (deviceConfig[0].getAromas().get(4).getQuantity()));
                            }

                            int preBaseTotal = deviceConfig[0].getBase().getPg() + deviceConfig[0].getBase().getVg() + deviceConfig[0].getBase().getNicotine();

                            int basePer = Math.round(((((float) (deviceConfigReset[0].getBaseQuantity() - deviceConfig[0].getBaseQuantity()) * (float) 100) / (float) totalVolume[0])));
                            int pgPer = Math.round(((((float) pgValue * (float) basePer) / (float) preBaseTotal)));
                            int vgPer = Math.round(((((float) vgValue * (float) basePer) / (float) preBaseTotal)));
                            int nicPer = Math.round(((((float) nicValue * (float) basePer) / (float) preBaseTotal)));
                            int a1Per = Math.round(((((float) a1Value * (float) 100) / (float) totalVolume[0])));
                            int a2Per = Math.round(((((float) a2Value * (float) 100) / (float) totalVolume[0])));
                            int a3Per = Math.round(((((float) a3Value * (float) 100) / (float) totalVolume[0])));
                            int a4Per = Math.round(((((float) a4Value * (float) 100) / (float) totalVolume[0])));
                            int a5Per = Math.round(((((float) a5Value * (float) 100) / (float) totalVolume[0])));

                            float scale = getContext().getResources().getDisplayMetrics().density;

                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivBaseOne.getLayoutParams();
                            params.width = ivBaseOne.getWidth();
                            params.height = (int) ((Math.round(((float) vgPer * (float) 116) / (float) 100)) * scale + 0.5f);

                            ivBaseOne.setLayoutParams(params);

                            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ivBaseTwo.getLayoutParams();
                            params2.width = ivBaseTwo.getWidth();
                            params2.height = (int) ((Math.round(((float) nicPer * (float) 116) / (float) 100)) * scale + 0.5f);

                            ivBaseTwo.setLayoutParams(params2);

                            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) ivAromaOne.getLayoutParams();
                            params3.width = ivAromaOne.getWidth();
                            params3.height = (int) ((Math.round(((float) a1Per * (float) 116) / (float) 100)) * scale + 0.5f);

                            ivAromaOne.setLayoutParams(params3);

                            RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) ivAromaTwo.getLayoutParams();
                            params4.width = ivAromaTwo.getWidth();
                            params4.height = (int) ((Math.round(((float) a2Per * (float) 116) / (float) 100)) * scale + 0.5f);

                            ivAromaTwo.setLayoutParams(params4);

                            RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) ivAromaThree.getLayoutParams();
                            params5.width = ivAromaThree.getWidth();
                            params5.height = (int) ((Math.round(((float) a3Per * (float) 116) / (float) 100)) * scale + 0.5f);

                            ivAromaThree.setLayoutParams(params5);

                            RelativeLayout.LayoutParams params6 = (RelativeLayout.LayoutParams) ivAromaFour.getLayoutParams();
                            params6.width = ivAromaFour.getWidth();
                            params6.height = (int) ((Math.round(((float) a4Per * (float) 116) / (float) 100)) * scale + 0.5f);

                            ivAromaFour.setLayoutParams(params6);

                            RelativeLayout.LayoutParams params7 = (RelativeLayout.LayoutParams) ivAromaFive.getLayoutParams();
                            params7.width = ivAromaFive.getWidth();
                            params7.height = (int) ((Math.round(((float) a5Per * (float) 116) / (float) 100)) * scale + 0.5f);

                            ivAromaFive.setLayoutParams(params7);

                            RelativeLayout.LayoutParams params8 = (RelativeLayout.LayoutParams) ivBaseThree.getLayoutParams();
                            params8.width = ivBaseThree.getWidth();
                            params8.height = (int) ((Math.round(((float) pgPer * (float) 116) / (float) 100)) * scale + 0.5f);

                            ivBaseThree.setLayoutParams(params8);


                            //set schema keys (hide / visible)
                            tvPgPer.setText(pgPer + " %");
                            tvPgMl.setText(((pgPer * totalVolume[0]) / 100) + " ml");
                            tvVgPer.setText(vgPer + " %");
                            tvVgMl.setText(((vgPer * totalVolume[0]) / 100) + " ml");
                            tvNicoPer.setText(nicPer + " %");
                            tvNicoMl.setText(((nicPer * totalVolume[0]) / 100) + " ml");


                            if (a1Value == 0) {
                                llA1.setVisibility(View.GONE);
                            } else {
                                llA1.setVisibility(View.VISIBLE);

                                tvAroma1.setText(deviceConfig[0].getAromas().get(0).getArome().getName());
                                tvAroma1Ml.setText(a1Value + " ml");
                                tvAroma1Per.setText(a1Per + " %");
                                ;
                            }

                            if (a2Value == 0) {
                                llA2.setVisibility(View.GONE);
                            } else {
                                llA2.setVisibility(View.VISIBLE);

                                tvAroma2.setText(deviceConfig[0].getAromas().get(1).getArome().getName());
                                tvAroma2Ml.setText(a2Value + " ml");
                                tvAroma2Per.setText(a2Per + " %");
                            }
                            if (a3Value == 0) {
                                llA3.setVisibility(View.GONE);

                            } else {
                                llA3.setVisibility(View.VISIBLE);

                                tvAroma3.setText(deviceConfig[0].getAromas().get(2).getArome().getName());
                                tvAroma3Ml.setText(a3Value + " ml");
                                tvAroma3Per.setText(a3Per + " %");
                            }
                            if (a4Value == 0) {

                                llA4.setVisibility(View.GONE);

                            } else {

                                llA4.setVisibility(View.VISIBLE);
                                tvAroma4.setText(deviceConfig[0].getAromas().get(3).getArome().getName());
                                tvAroma4Ml.setText(a4Value + " ml");
                                tvAroma4Per.setText(a4Per + " %");
                            }
                            if (a5Value == 0) {
                                llA5.setVisibility(View.GONE);

                            } else {
                                llA5.setVisibility(View.VISIBLE);
                                tvAroma5.setText(deviceConfig[0].getAromas().get(4).getArome().getName());
                                tvAroma5Ml.setText(a5Value + " ml");
                                tvAroma5Per.setText(a5Per + " %");
                            }

                            elMakeRecipeConf.expand();


                        }


                    }
                });


                btnMakeRecipe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (isChecked[0]) {

                            if (user.getBoitier() != null) {





                                if (serviceOn) {

                                    ArrayList<AromePerRecipe> aromasPerRec = new ArrayList<AromePerRecipe>();

                                    for (AromePerRecipe a : aromes) {
                                        aromasPerRec.add(a.clone());
                                    }

                                    for (int i = 0; i < aromes.size(); i++) {
                                        for (int j = 0; j < deviceConfig[0].getAromas().size(); j++) {
                                            if (aromasPerRec.get(i).equals(deviceConfig[0].getAromas().get(j))) {
                                                aromasPerRec.get(i).setQuantity((deviceConfigReset[0].getAromas().get(j).getQuantity()) - (deviceConfig[0].getAromas().get(j).getQuantity()));
                                            }
                                        }
                                    }
                                    progressDialog.setMessage("Making in progress...");
                                    progressDialog.show();
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    Intent serviceIntent = new Intent(getActivity(), BluetoothDataService.class);


                                    String A1Quantity = "0";
                                    String A2Quantity = "0";
                                    String A3Quantity = "0";
                                    String A4Quantity = "0";
                                    String A5Quantity = "0";
                                    String BQuantity = (((int)(deviceConfigReset[0].getBaseQuantity() - deviceConfig[0].getBaseQuantity())) * 500)+"";

                                    ArrayList<String> list = new ArrayList<String>();
                                    list.add(A1Quantity);
                                    list.add(A2Quantity);
                                    list.add(A3Quantity);
                                    list.add(A4Quantity);
                                    list.add(A5Quantity);

                                    for(int i = 0; i < aromasPerRec.size(); i++){
                                        list.set(((aromasPerRec.get(i).getPosition())-1), ((int)(aromasPerRec.get(i).getQuantity() * 1000)+""));
                                    }


                                    serviceIntent.putExtra("btData", "1,"+list.get(0) + ",2,"+list.get(1)+",3,"+list.get(2)+",4,"+list.get(3)+",5,"+list.get(4)+",6,"+BQuantity);
                                    getActivity().startService(serviceIntent);
                                    firstMessage = true;

                                }else if (!serviceOn){

                                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                    dialog.setTitle("Warning");
                                    dialog.setMessage("You should connect to your E-Dash device before launching a make process.");
                                    dialog.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            Intent intent = new Intent(getActivity(), SettingActivity.class);
                                            intent.putExtra("intentId", "makeRecipeIntent");
                                            getActivity().startActivityForResult(intent, REQUEST_CONNECTION);

                                        }
                                    });
                                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    dialog.show();

                                }


                            } else {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                dialog.setTitle("Device ID is not set");
                                dialog.setMessage("You need to set your device ID in order to connect to it. You can do this by going to settings interface.");
                                dialog.setPositiveButton("Go now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        startActivity(new Intent(getActivity(), SettingActivity.class));

                                    }
                                });
                                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                dialog.show();
                            }


                        } else {
                            AlertDialog.Builder dia = new AlertDialog.Builder(getContext());
                            dia.setTitle("Warning");
                            dia.setMessage("Please check composition before making the E-liquid.");
                            dia.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            dia.show();
                        }


                    }
                });


            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {

                rootView = inflater.inflate(R.layout.fragment_ready_eliquid, container, false);

            }


            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Customize recipe";
                case 1:
                    return "Ready recipes";
            }
            return null;
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

    private static BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(final Context context, final Intent intent) {
            // Get extra data included in the Intent



            if (firstMessage) {
                String message = intent.getStringExtra("result");
                if (message.equals("K")) {


                    Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();

                    DeviceConfig dev = new DeviceConfig();
                    dev = deviceConfig[0];
                    Float aromaTotalVolume = 0f;

                    ArrayList<AromePerRecipe> aromePerRecipes = new ArrayList<>();


                    for (AromePerRecipe a : aromes) {
                        aromePerRecipes.add(a.clone());
                    }

                    for (int i = 0; i < aromes.size(); i++) {
                        for (int j = 0; j < dev.getAromas().size(); j++) {
                            if (aromePerRecipes.get(i).equals(dev.getAromas().get(j))) {
                                aromePerRecipes.get(i).setQuantity((deviceConfigReset[0].getAromas().get(j).getQuantity()) - (dev.getAromas().get(j).getQuantity()));
                                aromaTotalVolume += aromePerRecipes.get(i).getQuantity();
                            }
                        }
                    }


                    DeviceConfigRecipe deviceConfigRecipe = new DeviceConfigRecipe();

                    deviceConfigRecipe.setDescription(etDeviceConfRecipeDescription.getText().toString());
                    deviceConfigRecipe.setDate(SystemCurrentDate.now().toString());
                    deviceConfigRecipe.setImageUrl(URL.DEFAULT_RECIPE);
                    deviceConfigRecipe.setName(etDeviceConfRecipeName.getText().toString());
                    deviceConfigRecipe.setSteep(Integer.parseInt(etDeviceConfRecipeSteep.getText().toString()));
                    deviceConfigRecipe.setBaseQuantity((deviceConfigReset[0].getBaseQuantity() - deviceConfig[0].getBaseQuantity()));
                    deviceConfigRecipe.setVolume(totalVolume[0]);
                    deviceConfigRecipe.setTotalAromeQuantity(aromaTotalVolume);
                    deviceConfigRecipe.setBase(deviceConfig[0].getBase());
                    deviceConfigRecipe.setUser(user);
                    deviceConfigRecipe.setComments(0);
                    deviceConfigRecipe.setLikes(0);
                    deviceConfigRecipe.setVotes(0);
                    deviceConfigRecipe.setAromes(aromePerRecipes);
                    deviceConfigRecipe.setDeviceConfig(null);


                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL.ADD_DEVICE_CONF_RECIPE + user.getId() + "/" + devCon[0].getId(),
                            new ObjectToJsonParser().parseDeviceConfigRecipe(deviceConfigRecipe),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Toast.makeText(context, "Recipe Added", Toast.LENGTH_SHORT).show();

                                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.LOAD_DEVICE_CONFIG_HISTORY + user.getId(), new ObjectToJsonParser().updateDeviceConfig(deviceConfig[0]),
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(context, "Device Conf Updated", Toast.LENGTH_SHORT).show();


                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Error while updating device conf", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    ConnectionSingleton.getInstance(context).addToRequestque(req);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed to add Recipe", Toast.LENGTH_SHORT).show();
                        }
                    });

                    ConnectionSingleton.getInstance(context).addToRequestque(request);

                    firstMessage = false;

                } else if (!message.equals("K")) {
                    progressDialog.dismiss();

                    Toast.makeText(context, "KO", Toast.LENGTH_SHORT).show();
                    firstMessage = false;
                } else {
                    progressDialog.dismiss();
                    firstMessage = true;
                }

            }


        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CONNECTION && resultCode == RESULT_OK){
            serviceOn = isMyServiceRunning(BluetoothDataService.class);
        }
    }
}


