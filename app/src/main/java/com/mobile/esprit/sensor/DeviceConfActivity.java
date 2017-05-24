package com.mobile.esprit.sensor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.mobile.esprit.sensor.Adapters.DeviceConfAromaAdapter;
import com.mobile.esprit.sensor.Adapters.DialogAromaAdapter;
import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Base;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.DeviceConfig;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.Entities.User;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.JSONParser;
import com.mobile.esprit.sensor.Utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DeviceConfActivity extends AppCompatActivity {

    private ImageView ivDeviceAddAroma;
    private ArrayList<Aroma> aromas;
    private ArrayList<Aroma> filteredAromas;
    private ArrayList<DeviceConfig> deviceConfigs;
    private ListView lvDialogAroma, lvDeviceConfAroma;
    private ArrayList<AromePerRecipe> deviceAromas;
    private AromePerRecipe deviceAroma;
    private TextView tvDeviceAddBase, btnSetUpDevice;
    private Base selectedBase;
    private EditText etConfName, etConfDescription, etDeviceConfBaseQuantity;
    private User user;

    private String note, date, name;
    private float baseQuantity, volume;
    private Boolean visibility = false;


    public static final int REQUEST_BASE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_conf);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setup Device");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        user = User.getInstance();

        deviceConfigs = new ArrayList<>();

        etConfName = (EditText) findViewById(R.id.et_device_conf_name);
        etConfDescription = (EditText) findViewById(R.id.et_device_conf_description);
        etDeviceConfBaseQuantity = (EditText) findViewById(R.id.et_device_conf_base_quantity);


        btnSetUpDevice = (TextView) findViewById(R.id.btn_set_up_device);


        selectedBase = new Base();

        deviceAromas = new ArrayList<>();

        tvDeviceAddBase = (TextView) findViewById(R.id.tv_device_add_base);

        tvDeviceAddBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceConfActivity.this, BaseActivity.class);
                startActivityForResult(intent, REQUEST_BASE);
            }
        });

        ivDeviceAddAroma = (ImageView) findViewById(R.id.iv_device_add_aroma);


        ivDeviceAddAroma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deviceAromas.size() == 5) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeviceConfActivity.this);
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage(R.string.device_conf_max_aroma);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.setCancelable(true);
                    alertDialog.show();


                } else if (deviceAromas.size() < 5) {
                    final Dialog dialog = new Dialog(DeviceConfActivity.this);
                    dialog.setContentView(R.layout.dialog_find_aroma);
                    dialog.setCancelable(true);

                    lvDialogAroma = (ListView) dialog.findViewById(R.id.lv_dialog_aroma);


                    TextView tvDialogCancel = (TextView) dialog.findViewById(R.id.tv_dialog_cancel);
                    final EditText etDialogAromaSearch = (EditText) dialog.findViewById(R.id.et_dialog_aroma_search);


                    aromas = new ArrayList<>();
                    JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_All_AROMAS, null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {

                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject jaroma = response.getJSONObject(i);
                                            Aroma aroma = new Aroma();
                                            Manufacturer manufacturer = new Manufacturer();
                                            Category category = new Category();

                                            aroma.setIdAroma(jaroma.getInt("id"));
                                            aroma.setName(jaroma.getString("arome"));
                                            aroma.setImgArome(jaroma.getString("imageUrl"));
                                            aroma.setDescription(jaroma.getString("description"));
                                            aroma.setDate(jaroma.getString("date"));

                                            JSONObject jmanufacturer = jaroma.getJSONObject("manufacture");
                                            manufacturer.setIdManufacturer(jmanufacturer.getInt("id"));
                                            manufacturer.setName(jmanufacturer.getString("name"));
                                            manufacturer.setDescription(jmanufacturer.getString("description"));
                                            manufacturer.setImage(jmanufacturer.getString("imageUrl"));
                                            manufacturer.setAddress(jmanufacturer.getString("address"));
                                            manufacturer.setDate(jmanufacturer.getString("date"));
                                            aroma.setManufacturer(manufacturer);

                                            JSONObject jcategory = jaroma.getJSONObject("category");
                                            category.setIdCategory(jcategory.getInt("id"));
                                            category.setName(jcategory.getString("category"));
                                            category.setImageName(jcategory.getString("imageName"));
                                            category.setDescription(jcategory.getString("description"));
                                            category.setImage(jcategory.getString("imageUrl"));
                                            category.setDate(jcategory.getString("date"));
                                            aroma.setCategory(category);

                                            aromas.add(aroma);


                                        }


                                        filteredAromas = new ArrayList<>();

                                        etDialogAromaSearch.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                filteredAromas.clear();
                                                for (Aroma a : aromas) {
                                                    if (a.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                                        filteredAromas.add(a);
                                                    }
                                                }

                                                DialogAromaAdapter adapter = new DialogAromaAdapter(DeviceConfActivity.this, R.layout.item_dialog_aroma, filteredAromas);
                                                lvDialogAroma.setAdapter(adapter);
                                                lvDialogAroma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                        dialog.dismiss();

                                                        deviceAroma = new AromePerRecipe(000, filteredAromas.get(i), 0);
                                                        deviceAromas.add(deviceAroma);
                                                        lvDeviceConfAroma = (ListView) DeviceConfActivity.this.findViewById(R.id.lv_device_conf_aroma);
                                                        final DeviceConfAromaAdapter adapter = new DeviceConfAromaAdapter(DeviceConfActivity.this, R.layout.item_device_conf_aroma, deviceAromas);
                                                        lvDeviceConfAroma.setAdapter(adapter);
                                                        lvDeviceConfAroma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                                final AlertDialog.Builder removeAlert = new AlertDialog.Builder(DeviceConfActivity.this);
                                                                removeAlert.setTitle("Warning");
                                                                removeAlert.setMessage(R.string.removeAromaConf);
                                                                removeAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        deviceAromas.remove(-i - 1);
                                                                        adapter.notifyDataSetChanged();

                                                                    }
                                                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                                    }
                                                                });
                                                                removeAlert.show();
                                                            }
                                                        });


                                                    }
                                                });

                                            }

                                            @Override
                                            public void afterTextChanged(Editable editable) {
                                                if (editable.toString().toLowerCase().equals("")) {
                                                    DialogAromaAdapter adapter = new DialogAromaAdapter(DeviceConfActivity.this, R.layout.item_dialog_aroma, new ArrayList<Aroma>());
                                                    lvDialogAroma.setAdapter(adapter);

                                                }

                                            }
                                        });


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                        }
                    }
                    );

                    ConnectionSingleton.getInstance(DeviceConfActivity.this).addToRequestque(req);


                    tvDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        btnSetUpDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_DEVICE_CONFIG_HISTORY+user.getId(), null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                JSONParser jsonParser = new JSONParser();

                                for (int i = 0; i < response.length(); i++) {

                                    try {
                                        deviceConfigs.add(jsonParser.parseDeviceConfig(response.getJSONObject(i)));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                if (deviceConfigs.size() == 0) {
                                    addDeviceConfDefault();
                                    addDeviceConfHistoryDefault();

                                } else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(DeviceConfActivity.this);
                                    dialog.setTitle("Important");
                                    dialog.setMessage(R.string.device_conf_default);
                                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            updateDeviceConf();
                                            updateDeviceConfHistory();

                                        }
                                    });
                                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            addDeviceConfNotDefault();
                                            addDeviceConfHistoryNotDefault();

                                        }
                                    });
                                    dialog.show();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                ConnectionSingleton.getInstance(DeviceConfActivity.this).addToRequestque(req);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_BASE && resultCode == RESULT_OK) {
            selectedBase = data.getParcelableExtra(BaseActivity.SELECTED_BASE);
            tvDeviceAddBase.setText((selectedBase.getPg() + "/" + selectedBase.getVg() + "/" + selectedBase.getNicotine()).toString());
            tvDeviceAddBase.setTextColor(ContextCompat.getColor(DeviceConfActivity.this, R.color.colorBlack));
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


    public void addDeviceConfNotDefault() {


        Float totalAromeQuantity = 0f;

        long yourmilliseconds = System.currentTimeMillis();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        final Date resultdate = new Date(yourmilliseconds);

        note = etConfDescription.getText().toString();
        date = sdf.format(resultdate);
        name = etConfName.getText().toString();
        baseQuantity = Float.parseFloat(etDeviceConfBaseQuantity.getText().toString());

        for (AromePerRecipe aromaQuantity : deviceAromas) {
            totalAromeQuantity += aromaQuantity.getQuantity();
        }

        volume = totalAromeQuantity + baseQuantity;


        JSONArray jsonAromasArray = new JSONArray();
        for (int i = 0; i < deviceAromas.size(); i++) {
            try {
                JSONObject jsonAromaObject = new JSONObject();

                jsonAromaObject.put("quantity", Float.toString(deviceAromas.get(i).getQuantity()));
                jsonAromaObject.put("position", Integer.toString(deviceAromas.get(i).getPosition()));

                JSONObject jsonAroma = new JSONObject();
                jsonAroma.put("id", deviceAromas.get(i).getArome().getIdAroma());
                jsonAroma.put("arome", deviceAromas.get(i).getArome().getName());
                jsonAroma.put("imageUrl", deviceAromas.get(i).getArome().getImgArome());
                jsonAroma.put("description", deviceAromas.get(i).getArome().getDescription());
                jsonAroma.put("date", deviceAromas.get(i).getArome().getDate());
                jsonAroma.put("enabled", deviceAromas.get(i).getArome().isEnabled());

                JSONObject jsonManufacturer = new JSONObject();
                jsonManufacturer.put("id", deviceAromas.get(i).getArome().getManufacturer().getIdManufacturer());
                jsonManufacturer.put("name", deviceAromas.get(i).getArome().getManufacturer().getName());
                jsonManufacturer.put("description", deviceAromas.get(i).getArome().getManufacturer().getDescription());
                jsonManufacturer.put("imageUrl", deviceAromas.get(i).getArome().getManufacturer().getImage());
                jsonManufacturer.put("address", deviceAromas.get(i).getArome().getManufacturer().getAddress());
                jsonManufacturer.put("date", deviceAromas.get(i).getArome().getManufacturer().getDate());
                jsonManufacturer.put("lang", deviceAromas.get(i).getArome().getManufacturer().getLang());
                jsonManufacturer.put("lat", deviceAromas.get(i).getArome().getManufacturer().getLat());
                jsonAroma.put("manufacture", jsonManufacturer);

                JSONObject jsonCategory = new JSONObject();
                jsonCategory.put("id", deviceAromas.get(i).getArome().getCategory().getIdCategory());
                jsonCategory.put("category", deviceAromas.get(i).getArome().getCategory().getName());
                jsonCategory.put("imageName", deviceAromas.get(i).getArome().getCategory().getImageName());
                jsonCategory.put("description", deviceAromas.get(i).getArome().getCategory().getDescription());
                jsonCategory.put("imageUrl", deviceAromas.get(i).getArome().getCategory().getImage());
                jsonCategory.put("date", deviceAromas.get(i).getArome().getCategory().getDate());
                jsonAroma.put("category", jsonCategory);

                JSONObject jsonAromaUser = new JSONObject();
                if (deviceAromas.get(i).getArome().getUser() == null) {
                    jsonAroma.put("user", null);
                } else {
                    jsonAromaUser.put("id", deviceAromas.get(i).getArome().getUser().getId());
                    jsonAromaUser.put("login", deviceAromas.get(i).getArome().getUser().getLogin());
                    jsonAromaUser.put("email", deviceAromas.get(i).getArome().getUser().getEmail());
                    jsonAromaUser.put("password", deviceAromas.get(i).getArome().getUser().getPassword());
                    jsonAromaUser.put("provider", deviceAromas.get(i).getArome().getUser().getProvider());
                    jsonAromaUser.put("profileId", deviceAromas.get(i).getArome().getUser().getProfileId());
                    jsonAromaUser.put("profilePicture", deviceAromas.get(i).getArome().getUser().getProfilePicture());
                    jsonAromaUser.put("firstName", deviceAromas.get(i).getArome().getUser().getFirstName());
                    jsonAromaUser.put("lastName", deviceAromas.get(i).getArome().getUser().getLastName());
                    jsonAromaUser.put("linkUri", deviceAromas.get(i).getArome().getUser().getLinkUri());
                    jsonAroma.put("user", jsonAromaUser);
                }


                jsonAromaObject.put("arome", jsonAroma);

                jsonAromasArray.put(jsonAromaObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        JSONObject jsonBase = new JSONObject();

        try {
            jsonBase.put("id", selectedBase.getIdBase());
            jsonBase.put("pg", selectedBase.getPg());
            jsonBase.put("vg", selectedBase.getVg());
            jsonBase.put("nicotine", selectedBase.getNicotine());
            jsonBase.put("date", selectedBase.getDate());
            jsonBase.put("description", selectedBase.getDescription());
            jsonBase.put("imageUrl", selectedBase.getImage());
            jsonBase.put("mobileImageUrl", selectedBase.getMobileImageUrl());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("id", user.getId());
            jsonUser.put("login", user.getLogin());
            jsonUser.put("email", user.getEmail());
            jsonUser.put("password", user.getPassword());
            jsonUser.put("provider", user.getProvider());
            jsonUser.put("profileId", user.getProfileId());
            jsonUser.put("profilePicture", user.getProfilePicture());
            jsonUser.put("firstName", user.getFirstName());
            jsonUser.put("lastName", user.getLastName());
            jsonUser.put("linkUri", user.getLinkUri());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonDeviceConfig = new JSONObject();
        try {
            jsonDeviceConfig.put("note", note);
            jsonDeviceConfig.put("date", date);
            jsonDeviceConfig.put("name", name);
            jsonDeviceConfig.put("baseQuantity", Float.toString(baseQuantity));
            jsonDeviceConfig.put("volume", Float.toString(volume));
            jsonDeviceConfig.put("totalAromeQuantity", Float.toString(totalAromeQuantity));
            jsonDeviceConfig.put("visibility", visibility);
            jsonDeviceConfig.put("isDefault", false);
            jsonDeviceConfig.put("base", jsonBase);
            jsonDeviceConfig.put("user", jsonUser);
            jsonDeviceConfig.put("aromes", jsonAromasArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.LOAD_DEVICE_CONFIG + "/" + user.getId(), jsonDeviceConfig,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(DeviceConfActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error.getMessage());

            }
        });


        ConnectionSingleton.getInstance(DeviceConfActivity.this).addToRequestque(req);

    }
    public void addDeviceConfHistoryNotDefault() {

        long yourmilliseconds = System.currentTimeMillis();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        final Date resultdate = new Date(yourmilliseconds);

        Float totalAromeQuantity = 0f;

        note = etConfDescription.getText().toString();
        date = sdf.format(resultdate);
        name = etConfName.getText().toString();
        baseQuantity = Float.parseFloat(etDeviceConfBaseQuantity.getText().toString());

        for (AromePerRecipe aromaQuantity : deviceAromas) {
            totalAromeQuantity += aromaQuantity.getQuantity();
        }

        volume = totalAromeQuantity + baseQuantity;


        JSONArray jsonAromasArray = new JSONArray();
        for (int i = 0; i < deviceAromas.size(); i++) {
            try {
                JSONObject jsonAromaObject = new JSONObject();

                jsonAromaObject.put("quantity", Float.toString(deviceAromas.get(i).getQuantity()));
                jsonAromaObject.put("position", Integer.toString(deviceAromas.get(i).getPosition()));

                JSONObject jsonAroma = new JSONObject();
                jsonAroma.put("id", deviceAromas.get(i).getArome().getIdAroma());
                jsonAroma.put("arome", deviceAromas.get(i).getArome().getName());
                jsonAroma.put("imageUrl", deviceAromas.get(i).getArome().getImgArome());
                jsonAroma.put("description", deviceAromas.get(i).getArome().getDescription());
                jsonAroma.put("date", deviceAromas.get(i).getArome().getDate());
                jsonAroma.put("enabled", deviceAromas.get(i).getArome().isEnabled());

                JSONObject jsonManufacturer = new JSONObject();
                jsonManufacturer.put("id", deviceAromas.get(i).getArome().getManufacturer().getIdManufacturer());
                jsonManufacturer.put("name", deviceAromas.get(i).getArome().getManufacturer().getName());
                jsonManufacturer.put("description", deviceAromas.get(i).getArome().getManufacturer().getDescription());
                jsonManufacturer.put("imageUrl", deviceAromas.get(i).getArome().getManufacturer().getImage());
                jsonManufacturer.put("address", deviceAromas.get(i).getArome().getManufacturer().getAddress());
                jsonManufacturer.put("date", deviceAromas.get(i).getArome().getManufacturer().getDate());
                jsonManufacturer.put("lang", deviceAromas.get(i).getArome().getManufacturer().getLang());
                jsonManufacturer.put("lat", deviceAromas.get(i).getArome().getManufacturer().getLat());
                jsonAroma.put("manufacture", jsonManufacturer);

                JSONObject jsonCategory = new JSONObject();
                jsonCategory.put("id", deviceAromas.get(i).getArome().getCategory().getIdCategory());
                jsonCategory.put("category", deviceAromas.get(i).getArome().getCategory().getName());
                jsonCategory.put("imageName", deviceAromas.get(i).getArome().getCategory().getImageName());
                jsonCategory.put("description", deviceAromas.get(i).getArome().getCategory().getDescription());
                jsonCategory.put("imageUrl", deviceAromas.get(i).getArome().getCategory().getImage());
                jsonCategory.put("date", deviceAromas.get(i).getArome().getCategory().getDate());
                jsonAroma.put("category", jsonCategory);

                JSONObject jsonAromaUser = new JSONObject();
                if (deviceAromas.get(i).getArome().getUser() == null) {
                    jsonAroma.put("user", null);
                } else {
                    jsonAromaUser.put("id", deviceAromas.get(i).getArome().getUser().getId());
                    jsonAromaUser.put("login", deviceAromas.get(i).getArome().getUser().getLogin());
                    jsonAromaUser.put("email", deviceAromas.get(i).getArome().getUser().getEmail());
                    jsonAromaUser.put("password", deviceAromas.get(i).getArome().getUser().getPassword());
                    jsonAromaUser.put("provider", deviceAromas.get(i).getArome().getUser().getProvider());
                    jsonAromaUser.put("profileId", deviceAromas.get(i).getArome().getUser().getProfileId());
                    jsonAromaUser.put("profilePicture", deviceAromas.get(i).getArome().getUser().getProfilePicture());
                    jsonAromaUser.put("firstName", deviceAromas.get(i).getArome().getUser().getFirstName());
                    jsonAromaUser.put("lastName", deviceAromas.get(i).getArome().getUser().getLastName());
                    jsonAromaUser.put("linkUri", deviceAromas.get(i).getArome().getUser().getLinkUri());
                    jsonAroma.put("user", jsonAromaUser);
                }


                jsonAromaObject.put("arome", jsonAroma);

                jsonAromasArray.put(jsonAromaObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        JSONObject jsonBase = new JSONObject();

        try {
            jsonBase.put("id", selectedBase.getIdBase());
            jsonBase.put("pg", selectedBase.getPg());
            jsonBase.put("vg", selectedBase.getVg());
            jsonBase.put("nicotine", selectedBase.getNicotine());
            jsonBase.put("date", selectedBase.getDate());
            jsonBase.put("description", selectedBase.getDescription());
            jsonBase.put("imageUrl", selectedBase.getImage());
            jsonBase.put("mobileImageUrl", selectedBase.getMobileImageUrl());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("id", user.getId());
            jsonUser.put("login", user.getLogin());
            jsonUser.put("email", user.getEmail());
            jsonUser.put("password", user.getPassword());
            jsonUser.put("provider", user.getProvider());
            jsonUser.put("profileId", user.getProfileId());
            jsonUser.put("profilePicture", user.getProfilePicture());
            jsonUser.put("firstName", user.getFirstName());
            jsonUser.put("lastName", user.getLastName());
            jsonUser.put("linkUri", user.getLinkUri());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonDeviceConfig = new JSONObject();
        try {
            jsonDeviceConfig.put("note", note);
            jsonDeviceConfig.put("date", date);
            jsonDeviceConfig.put("name", name);
            jsonDeviceConfig.put("baseQuantity", Float.toString(baseQuantity));
            jsonDeviceConfig.put("volume", Float.toString(volume));
            jsonDeviceConfig.put("totalAromeQuantity", Float.toString(totalAromeQuantity));
            jsonDeviceConfig.put("visibility", visibility);
            jsonDeviceConfig.put("isDefault", false);
            jsonDeviceConfig.put("base", jsonBase);
            jsonDeviceConfig.put("user", jsonUser);
            jsonDeviceConfig.put("aromes", jsonAromasArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.LOAD_DEVICE_CONFIG_HISTORY  + user.getId(), jsonDeviceConfig,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(DeviceConfActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error.getMessage());

            }
        });


        ConnectionSingleton.getInstance(DeviceConfActivity.this).addToRequestque(req);

    }

    public void addDeviceConfDefault() {
        long yourmilliseconds = System.currentTimeMillis();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        final Date resultdate = new Date(yourmilliseconds);

        Float totalAromeQuantity = 0f;
        note = etConfDescription.getText().toString();
        date = sdf.format(resultdate);
        name = etConfName.getText().toString();
        baseQuantity = Float.parseFloat(etDeviceConfBaseQuantity.getText().toString());

        for (AromePerRecipe aromaQuantity : deviceAromas) {
            totalAromeQuantity += aromaQuantity.getQuantity();
        }

        volume = totalAromeQuantity + baseQuantity;


        JSONArray jsonAromasArray = new JSONArray();
        for (int i = 0; i < deviceAromas.size(); i++) {
            try {
                JSONObject jsonAromaObject = new JSONObject();

                jsonAromaObject.put("quantity", Float.toString(deviceAromas.get(i).getQuantity()));
                jsonAromaObject.put("position", Integer.toString(deviceAromas.get(i).getPosition()));

                JSONObject jsonAroma = new JSONObject();
                jsonAroma.put("id", deviceAromas.get(i).getArome().getIdAroma());
                jsonAroma.put("arome", deviceAromas.get(i).getArome().getName());
                jsonAroma.put("imageUrl", deviceAromas.get(i).getArome().getImgArome());
                jsonAroma.put("description", deviceAromas.get(i).getArome().getDescription());
                jsonAroma.put("date", deviceAromas.get(i).getArome().getDate());
                jsonAroma.put("enabled", deviceAromas.get(i).getArome().isEnabled());

                JSONObject jsonManufacturer = new JSONObject();
                jsonManufacturer.put("id", deviceAromas.get(i).getArome().getManufacturer().getIdManufacturer());
                jsonManufacturer.put("name", deviceAromas.get(i).getArome().getManufacturer().getName());
                jsonManufacturer.put("description", deviceAromas.get(i).getArome().getManufacturer().getDescription());
                jsonManufacturer.put("imageUrl", deviceAromas.get(i).getArome().getManufacturer().getImage());
                jsonManufacturer.put("address", deviceAromas.get(i).getArome().getManufacturer().getAddress());
                jsonManufacturer.put("date", deviceAromas.get(i).getArome().getManufacturer().getDate());
                jsonManufacturer.put("lang", deviceAromas.get(i).getArome().getManufacturer().getLang());
                jsonManufacturer.put("lat", deviceAromas.get(i).getArome().getManufacturer().getLat());
                jsonAroma.put("manufacture", jsonManufacturer);

                JSONObject jsonCategory = new JSONObject();
                jsonCategory.put("id", deviceAromas.get(i).getArome().getCategory().getIdCategory());
                jsonCategory.put("category", deviceAromas.get(i).getArome().getCategory().getName());
                jsonCategory.put("imageName", deviceAromas.get(i).getArome().getCategory().getImageName());
                jsonCategory.put("description", deviceAromas.get(i).getArome().getCategory().getDescription());
                jsonCategory.put("imageUrl", deviceAromas.get(i).getArome().getCategory().getImage());
                jsonCategory.put("date", deviceAromas.get(i).getArome().getCategory().getDate());
                jsonAroma.put("category", jsonCategory);

                JSONObject jsonAromaUser = new JSONObject();
                if (deviceAromas.get(i).getArome().getUser() == null) {
                    jsonAroma.put("user", null);
                } else {
                    jsonAromaUser.put("id", deviceAromas.get(i).getArome().getUser().getId());
                    jsonAromaUser.put("login", deviceAromas.get(i).getArome().getUser().getLogin());
                    jsonAromaUser.put("email", deviceAromas.get(i).getArome().getUser().getEmail());
                    jsonAromaUser.put("password", deviceAromas.get(i).getArome().getUser().getPassword());
                    jsonAromaUser.put("provider", deviceAromas.get(i).getArome().getUser().getProvider());
                    jsonAromaUser.put("profileId", deviceAromas.get(i).getArome().getUser().getProfileId());
                    jsonAromaUser.put("profilePicture", deviceAromas.get(i).getArome().getUser().getProfilePicture());
                    jsonAromaUser.put("firstName", deviceAromas.get(i).getArome().getUser().getFirstName());
                    jsonAromaUser.put("lastName", deviceAromas.get(i).getArome().getUser().getLastName());
                    jsonAromaUser.put("linkUri", deviceAromas.get(i).getArome().getUser().getLinkUri());
                    jsonAroma.put("user", jsonAromaUser);
                }


                jsonAromaObject.put("arome", jsonAroma);

                jsonAromasArray.put(jsonAromaObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        JSONObject jsonBase = new JSONObject();

        try {
            jsonBase.put("id", selectedBase.getIdBase());
            jsonBase.put("pg", selectedBase.getPg());
            jsonBase.put("vg", selectedBase.getVg());
            jsonBase.put("nicotine", selectedBase.getNicotine());
            jsonBase.put("date", selectedBase.getDate());
            jsonBase.put("description", selectedBase.getDescription());
            jsonBase.put("imageUrl", selectedBase.getImage());
            jsonBase.put("mobileImageUrl", selectedBase.getMobileImageUrl());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("id", user.getId());
            jsonUser.put("login", user.getLogin());
            jsonUser.put("email", user.getEmail());
            jsonUser.put("password", user.getPassword());
            jsonUser.put("provider", user.getProvider());
            jsonUser.put("profileId", user.getProfileId());
            jsonUser.put("profilePicture", user.getProfilePicture());
            jsonUser.put("firstName", user.getFirstName());
            jsonUser.put("lastName", user.getLastName());
            jsonUser.put("linkUri", user.getLinkUri());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonDeviceConfig = new JSONObject();
        try {
            jsonDeviceConfig.put("note", note);
            jsonDeviceConfig.put("date", date);
            jsonDeviceConfig.put("name", name);
            jsonDeviceConfig.put("baseQuantity", Float.toString(baseQuantity));
            jsonDeviceConfig.put("volume", Float.toString(volume));
            jsonDeviceConfig.put("totalAromeQuantity", Float.toString(totalAromeQuantity));
            jsonDeviceConfig.put("visibility", visibility);
            jsonDeviceConfig.put("isDefault", true);
            jsonDeviceConfig.put("base", jsonBase);
            jsonDeviceConfig.put("user", jsonUser);
            jsonDeviceConfig.put("aromes", jsonAromasArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.LOAD_DEVICE_CONFIG + "/" + user.getId(), jsonDeviceConfig,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(DeviceConfActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error.getMessage());

            }
        });


        ConnectionSingleton.getInstance(DeviceConfActivity.this).addToRequestque(req);
    }

    public void addDeviceConfHistoryDefault() {
        long yourmilliseconds = System.currentTimeMillis();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        final Date resultdate = new Date(yourmilliseconds);
        Float totalAromeQuantity = 0f;

        note = etConfDescription.getText().toString();
        date = sdf.format(resultdate);
        name = etConfName.getText().toString();
        baseQuantity = Float.parseFloat(etDeviceConfBaseQuantity.getText().toString());

        for (AromePerRecipe aromaQuantity : deviceAromas) {
            totalAromeQuantity += aromaQuantity.getQuantity();
        }

        volume = totalAromeQuantity + baseQuantity;


        JSONArray jsonAromasArray = new JSONArray();
        for (int i = 0; i < deviceAromas.size(); i++) {
            try {
                JSONObject jsonAromaObject = new JSONObject();

                jsonAromaObject.put("quantity", Float.toString(deviceAromas.get(i).getQuantity()));
                jsonAromaObject.put("position", Integer.toString(deviceAromas.get(i).getPosition()));

                JSONObject jsonAroma = new JSONObject();
                jsonAroma.put("id", deviceAromas.get(i).getArome().getIdAroma());
                jsonAroma.put("arome", deviceAromas.get(i).getArome().getName());
                jsonAroma.put("imageUrl", deviceAromas.get(i).getArome().getImgArome());
                jsonAroma.put("description", deviceAromas.get(i).getArome().getDescription());
                jsonAroma.put("date", deviceAromas.get(i).getArome().getDate());
                jsonAroma.put("enabled", deviceAromas.get(i).getArome().isEnabled());

                JSONObject jsonManufacturer = new JSONObject();
                jsonManufacturer.put("id", deviceAromas.get(i).getArome().getManufacturer().getIdManufacturer());
                jsonManufacturer.put("name", deviceAromas.get(i).getArome().getManufacturer().getName());
                jsonManufacturer.put("description", deviceAromas.get(i).getArome().getManufacturer().getDescription());
                jsonManufacturer.put("imageUrl", deviceAromas.get(i).getArome().getManufacturer().getImage());
                jsonManufacturer.put("address", deviceAromas.get(i).getArome().getManufacturer().getAddress());
                jsonManufacturer.put("date", deviceAromas.get(i).getArome().getManufacturer().getDate());
                jsonManufacturer.put("lang", deviceAromas.get(i).getArome().getManufacturer().getLang());
                jsonManufacturer.put("lat", deviceAromas.get(i).getArome().getManufacturer().getLat());
                jsonAroma.put("manufacture", jsonManufacturer);

                JSONObject jsonCategory = new JSONObject();
                jsonCategory.put("id", deviceAromas.get(i).getArome().getCategory().getIdCategory());
                jsonCategory.put("category", deviceAromas.get(i).getArome().getCategory().getName());
                jsonCategory.put("imageName", deviceAromas.get(i).getArome().getCategory().getImageName());
                jsonCategory.put("description", deviceAromas.get(i).getArome().getCategory().getDescription());
                jsonCategory.put("imageUrl", deviceAromas.get(i).getArome().getCategory().getImage());
                jsonCategory.put("date", deviceAromas.get(i).getArome().getCategory().getDate());
                jsonAroma.put("category", jsonCategory);

                JSONObject jsonAromaUser = new JSONObject();
                if (deviceAromas.get(i).getArome().getUser() == null) {
                    jsonAroma.put("user", null);
                } else {
                    jsonAromaUser.put("id", deviceAromas.get(i).getArome().getUser().getId());
                    jsonAromaUser.put("login", deviceAromas.get(i).getArome().getUser().getLogin());
                    jsonAromaUser.put("email", deviceAromas.get(i).getArome().getUser().getEmail());
                    jsonAromaUser.put("password", deviceAromas.get(i).getArome().getUser().getPassword());
                    jsonAromaUser.put("provider", deviceAromas.get(i).getArome().getUser().getProvider());
                    jsonAromaUser.put("profileId", deviceAromas.get(i).getArome().getUser().getProfileId());
                    jsonAromaUser.put("profilePicture", deviceAromas.get(i).getArome().getUser().getProfilePicture());
                    jsonAromaUser.put("firstName", deviceAromas.get(i).getArome().getUser().getFirstName());
                    jsonAromaUser.put("lastName", deviceAromas.get(i).getArome().getUser().getLastName());
                    jsonAromaUser.put("linkUri", deviceAromas.get(i).getArome().getUser().getLinkUri());
                    jsonAroma.put("user", jsonAromaUser);
                }


                jsonAromaObject.put("arome", jsonAroma);

                jsonAromasArray.put(jsonAromaObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        JSONObject jsonBase = new JSONObject();

        try {
            jsonBase.put("id", selectedBase.getIdBase());
            jsonBase.put("pg", selectedBase.getPg());
            jsonBase.put("vg", selectedBase.getVg());
            jsonBase.put("nicotine", selectedBase.getNicotine());
            jsonBase.put("date", selectedBase.getDate());
            jsonBase.put("description", selectedBase.getDescription());
            jsonBase.put("imageUrl", selectedBase.getImage());
            jsonBase.put("mobileImageUrl", selectedBase.getMobileImageUrl());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("id", user.getId());
            jsonUser.put("login", user.getLogin());
            jsonUser.put("email", user.getEmail());
            jsonUser.put("password", user.getPassword());
            jsonUser.put("provider", user.getProvider());
            jsonUser.put("profileId", user.getProfileId());
            jsonUser.put("profilePicture", user.getProfilePicture());
            jsonUser.put("firstName", user.getFirstName());
            jsonUser.put("lastName", user.getLastName());
            jsonUser.put("linkUri", user.getLinkUri());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonDeviceConfig = new JSONObject();
        try {
            jsonDeviceConfig.put("note", note);
            jsonDeviceConfig.put("date", date);
            jsonDeviceConfig.put("name", name);
            jsonDeviceConfig.put("baseQuantity", Float.toString(baseQuantity));
            jsonDeviceConfig.put("volume", Float.toString(volume));
            jsonDeviceConfig.put("totalAromeQuantity", Float.toString(totalAromeQuantity));
            jsonDeviceConfig.put("visibility", visibility);
            jsonDeviceConfig.put("isDefault", true);
            jsonDeviceConfig.put("base", jsonBase);
            jsonDeviceConfig.put("user", jsonUser);
            jsonDeviceConfig.put("aromes", jsonAromasArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL.LOAD_DEVICE_CONFIG_HISTORY + user.getId(), jsonDeviceConfig,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(DeviceConfActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error.getMessage());

            }
        });


        ConnectionSingleton.getInstance(DeviceConfActivity.this).addToRequestque(req);
    }




    public void updateDeviceConf() {

        StringRequest req = new StringRequest(Request.Method.PUT, URL.UPDATE_DEVICE_CONF_IS_DEFAULT + user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        addDeviceConfDefault();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ConnectionSingleton.getInstance(DeviceConfActivity.this).addToRequestque(req);
    }
    public void updateDeviceConfHistory() {

        StringRequest req = new StringRequest(Request.Method.PUT, URL.UPDATE_DEVICE_CONF_HISTORY_IS_DEFAULT+ user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        addDeviceConfHistoryDefault();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ConnectionSingleton.getInstance(DeviceConfActivity.this).addToRequestque(req);
    }






}
