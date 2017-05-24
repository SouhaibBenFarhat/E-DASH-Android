package com.mobile.esprit.sensor.user_favoris_activity.fragments;


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
import android.widget.ImageView;

import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.background_tasks.UserRecipeFavorisManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFavoris extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    public RecipeFavoris() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_favoris, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println(this.getClass().toString());
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.favoris_recipes_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        ImageView emptyList = (ImageView) getActivity().findViewById(R.id.empty_image);

        RecyclerView recipesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.lv_favoris_recipes);
        UserRecipeFavorisManager recipeManager = new UserRecipeFavorisManager(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        recipeManager.getAllRecipeFavoris(recipesRecyclerView, emptyList);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ImageView emptyList = (ImageView) getActivity().findViewById(R.id.empty_image);
                RecyclerView recipesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.lv_favoris_recipes);
                UserRecipeFavorisManager recipeManager = new UserRecipeFavorisManager(getActivity());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recipesRecyclerView.setLayoutManager(linearLayoutManager);
                recipeManager.getAllRecipeFavoris(recipesRecyclerView, emptyList);
                swipeRefreshLayout.setRefreshing(false);
            }

        }, 2000);
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView emptyList = (ImageView) getActivity().findViewById(R.id.empty_image);
        RecyclerView recipesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.lv_favoris_recipes);
        UserRecipeFavorisManager recipeManager = new UserRecipeFavorisManager(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        recipeManager.getAllRecipeFavoris(recipesRecyclerView, emptyList);
    }
}
