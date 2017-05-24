package com.mobile.esprit.sensor.user_profile_activity.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.esprit.sensor.R;
import com.mobile.esprit.sensor.background_tasks.RecipeManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserRecipesFragment extends Fragment {


    RecyclerView rvUserRecipes;

    public UserRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_recipes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvUserRecipes = (RecyclerView) getActivity().findViewById(R.id.rv_user_recipes);

        RecipeManager recipeManager = new RecipeManager(getActivity());
        recipeManager.getRecipeByUser(rvUserRecipes);

    }
}
