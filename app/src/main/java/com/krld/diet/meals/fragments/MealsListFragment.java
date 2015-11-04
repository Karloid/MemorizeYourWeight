package com.krld.diet.meals.fragments;

import android.os.Bundle;
import android.view.View;

import com.krld.diet.R;
import com.krld.diet.base.fragments.BaseDrawerToggleToolbarFragment;

public class MealsListFragment extends BaseDrawerToggleToolbarFragment {
    public static MealsListFragment newInstance() {
        MealsListFragment fragment = new MealsListFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID_ARGUMENT, R.layout.meals_f);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar.setTitle(R.string.meals);

    }
}
