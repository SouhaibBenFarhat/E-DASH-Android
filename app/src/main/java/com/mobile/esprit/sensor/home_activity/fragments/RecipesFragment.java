package com.mobile.esprit.sensor.home_activity.fragments;


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
import com.mobile.esprit.sensor.background_tasks.RecipeManager;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;


public class RecipesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout swipeRefreshLayout;
    SmoothProgressBar smoothProgressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println(this.getClass().toString());

        smoothProgressBar = (SmoothProgressBar) getActivity().findViewById(R.id.smoothProgressBar);
        if (smoothProgressBar != null) {
            smoothProgressBar.setVisibility(View.VISIBLE);
            smoothProgressBar.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(getActivity())
                    .colors(getResources().getIntArray(R.array.colors)).build());
        }


        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.recipes_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        RecyclerView recipesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.lv_recipes);
        RecipeManager recipeManager = new RecipeManager(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        recipeManager.getAllRecipe(recipesRecyclerView, smoothProgressBar);
    }

    @Override
    public void onRefresh() {
        if (smoothProgressBar != null) {
            smoothProgressBar.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                RecyclerView recipesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.lv_recipes);
                RecipeManager recipeManager = new RecipeManager(getActivity());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recipesRecyclerView.setLayoutManager(linearLayoutManager);
                recipeManager.getAllRecipe(recipesRecyclerView, smoothProgressBar);
                swipeRefreshLayout.setRefreshing(false);
            }

        }, 2000);
    }


}
