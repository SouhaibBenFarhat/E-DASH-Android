package com.mobile.esprit.sensor.user_aromas_activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.add_aroma_activity.AddAromaActivity;
import com.mobile.esprit.sensor.background_tasks.AromaManager;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class UserAromasActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView userAromaRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SmoothProgressBar smoothProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_aromas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        smoothProgressBar = (SmoothProgressBar) findViewById(R.id.smoothProgressBar);
        if (smoothProgressBar != null) {
            smoothProgressBar.setVisibility(View.VISIBLE);
            smoothProgressBar.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(this)
                    .colors(getResources().getIntArray(R.array.colors)).build());
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserAromasActivity.this, AddAromaActivity.class));
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.user_aroma_swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        userAromaRecyclerView = (RecyclerView) findViewById(R.id.rv_user_aroma);
        AromaManager aromaManager = new AromaManager(this);
        aromaManager.getUserAroma(userAromaRecyclerView,smoothProgressBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {
        smoothProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                AromaManager aromaManager = new AromaManager(UserAromasActivity.this);
                aromaManager.getUserAroma(userAromaRecyclerView,smoothProgressBar);
                swipeRefreshLayout.setRefreshing(false);
            }

        }, 2000);




    }
}
