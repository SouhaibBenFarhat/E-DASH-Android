package com.mobile.esprit.sensor.user_profile_activity.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.background_tasks.DeviceConfigManager;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDeviceConfigFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private SmoothProgressBar smoothProgressBar;

    public UserDeviceConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_device_config, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println(this.getClass().toString());
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.device_config_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        smoothProgressBar = (SmoothProgressBar) getActivity().findViewById(R.id.smoothProgressBar);
        if (smoothProgressBar != null) {
            smoothProgressBar.setVisibility(View.VISIBLE);
            smoothProgressBar.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(getActivity())
                    .colors(getResources().getIntArray(R.array.colors)).build());
        }

        RecyclerView recipesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.lv_device_config);
        DeviceConfigManager deviceConfigManager = new DeviceConfigManager(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        deviceConfigManager.getDeviceConfigByUser(recipesRecyclerView,smoothProgressBar);
    }

    @Override
    public void onRefresh() {
        smoothProgressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                RecyclerView recipesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.lv_device_config);
                DeviceConfigManager deviceConfigManager = new DeviceConfigManager(getActivity());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recipesRecyclerView.setLayoutManager(linearLayoutManager);
                deviceConfigManager.getDeviceConfigByUser(recipesRecyclerView,smoothProgressBar);
                swipeRefreshLayout.setRefreshing(false);
            }

        }, 2000);
    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerView recipesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.lv_device_config);
        DeviceConfigManager deviceConfigManager = new DeviceConfigManager(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        deviceConfigManager.getDeviceConfigByUser(recipesRecyclerView,smoothProgressBar);
    }
}
