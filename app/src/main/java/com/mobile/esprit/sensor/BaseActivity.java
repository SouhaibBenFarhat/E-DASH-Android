package com.mobile.esprit.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mobile.esprit.sensor.Entities.Base;
import com.mobile.esprit.sensor.Utils.ConnectionSingleton;
import com.mobile.esprit.sensor.Utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    private SeekBar sbPgvg;
    private TextView tvPg, tvVg, btnFind, tvDescription, btnSelectBase;
    private RelativeLayout rlMain;
    private LinearLayout llContent;
    private ImageView ivOne, ivTwo, ivThree;
    private RadioButton rbNicoOne, rbNicoTwo;
    private RadioGroup rgNicotine;

    private ArrayList<Base> bases;

    private int nicotineValue = 0;
    private int pgValue = 20;
    private int vgValue = 80;
    private int total = 0;

    private boolean nicotineChecked = false;

    public static final String SELECTED_BASE = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        rlMain = (RelativeLayout) findViewById(R.id.rl_main);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        ivOne = (ImageView) findViewById(R.id.iv_one);
        ivTwo = (ImageView) findViewById(R.id.iv_two);
        ivThree = (ImageView) findViewById(R.id.iv_three);

        rgNicotine = (RadioGroup) findViewById(R.id.rg_nicotine);
        rbNicoOne = (RadioButton) findViewById(R.id.rb_nico_one);
        rbNicoTwo = (RadioButton) findViewById(R.id.rb_nico_two);

        tvDescription = (TextView) findViewById(R.id.tv_base_description);

        btnFind = (TextView) findViewById(R.id.btn_find);
        btnSelectBase = (TextView) findViewById(R.id.btn_select);


        rgNicotine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rb_nico_one) {
                    nicotineValue = 3;
                } else if (i == R.id.rb_nico_two) {
                    nicotineValue = 6;
                }

                nicotineChecked = true;


            }
        });


        tvPg = (TextView) findViewById(R.id.tv_pg);
        tvVg = (TextView) findViewById(R.id.tv_vg);

        sbPgvg = (SeekBar) findViewById(R.id.sb_pgvg);
        sbPgvg.setProgress(0);
        sbPgvg.setMax(2);

        sbPgvg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                int current = sbPgvg.getProgress();

                if (current == 0) {
                    String pg = "20";
                    String vg = "80";

                    tvPg.setText(pg);
                    tvVg.setText(vg);

                    pgValue = Integer.parseInt(tvPg.getText().toString());
                    vgValue = Integer.parseInt(tvVg.getText().toString());


                }
                if (current == 1) {
                    String pg = "30";
                    String vg = "70";

                    tvPg.setText(pg);
                    tvVg.setText(vg);

                    pgValue = Integer.parseInt(tvPg.getText().toString());
                    vgValue = Integer.parseInt(tvVg.getText().toString());

                }
                if (current == 2) {
                    String pg = "50";
                    String vg = "50";

                    tvPg.setText(pg);
                    tvVg.setText(vg);

                    pgValue = Integer.parseInt(tvPg.getText().toString());
                    vgValue = Integer.parseInt(tvVg.getText().toString());
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nicotineChecked) {
                    total = pgValue + vgValue + nicotineValue;

                    int pgPer = Math.round(((((float) pgValue * (float) 100) / (float) total)));
                    int vgPer = Math.round(((((float) vgValue * (float) 100) / (float) total)));
                    int nicoPer = Math.round(((((float) nicotineValue * (float) 100) / (float) total)));

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivOne.getLayoutParams();
                    params.width = ivOne.getWidth();
                    params.height = getDp(Math.round(((float) vgPer * (float) 116) / (float) 100));

                    ivOne.setLayoutParams(params);

                    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ivTwo.getLayoutParams();
                    params2.width = ivTwo.getWidth();
                    params2.height = getDp(Math.round(((float) nicoPer * (float) 116) / (float) 100));

                    ivTwo.setLayoutParams(params2);

                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) ivThree.getLayoutParams();
                    params1.width = ivThree.getWidth();
                    params1.height = getDp(Math.round(((float) pgPer * (float) 116) / (float) 100));

                    ivThree.setLayoutParams(params1);


                    bases = new ArrayList<>();
                    JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL.LOAD_ALL_BASES, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jbase = response.getJSONObject(i);
                                    Base base = new Base();

                                    base.setIdBase(jbase.getInt("id"));
                                    base.setPg(jbase.getInt("pg"));
                                    base.setVg(jbase.getInt("vg"));
                                    base.setNicotine(jbase.getInt("nicotine"));
                                    base.setDate(jbase.getString("date"));
                                    base.setImage(jbase.getString("imageUrl"));
                                    base.setDescription(jbase.getString("description"));

                                    bases.add(base);
                                }



                            } catch (JSONException e) {
                                Toast.makeText(BaseActivity.this, "Error Parsing", Toast.LENGTH_SHORT).show();
                            }

                            Base base = new Base(pgValue, vgValue, nicotineValue);
                            for (final Base b : bases) {

                                if (b.equals(base)){
                                    tvDescription.setText(b.getDescription());
                                    llContent.setVisibility(View.VISIBLE);

                                    final Base ba = new Base(b.getIdBase(), b.getPg(),b.getVg(), b.getNicotine(), b.getDate(), b.getDescription(), b.getImage());

                                    btnSelectBase.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent intent = getIntent();
                                            intent.putExtra(SELECTED_BASE, ba);
                                            setResult(RESULT_OK, intent);
                                            finish();


                                        }
                                    });

                                }

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(BaseActivity.this, "Error while parsing...", Toast.LENGTH_SHORT).show();
                        }
                    });

                    ConnectionSingleton.getInstance(BaseActivity.this).addToRequestque(req);
                }
                else{
                    Toast.makeText(BaseActivity.this, "Please select PG/VG and Nicotine", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public int getDp(int value) {
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (value * scale + 0.5f);
        return pixels;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
