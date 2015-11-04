package com.krld.diet.meals.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.krld.diet.R;
import com.krld.diet.base.fragments.BaseDrawerToggleToolbarFragment;
import com.krld.diet.common.helpers.MetricsHelper;

import butterknife.Bind;

public class MealsListFragment extends BaseDrawerToggleToolbarFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    public static MealsListFragment newInstance() {
        MealsListFragment fragment = new MealsListFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID_ARGUMENT, R.layout.meals_f);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getScreenOrientation() {
        return MetricsHelper.getSmallestWidthDp() < 500 ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE : super.getScreenOrientation();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar.setTitle(R.string.meals);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }
}
