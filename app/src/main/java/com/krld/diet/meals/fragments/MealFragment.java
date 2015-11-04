package com.krld.diet.meals.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.krld.diet.R;
import com.krld.diet.base.Constants;
import com.krld.diet.base.fragments.BaseDrawerToggleToolbarFragment;
import com.krld.diet.base.helpers.Toasts;
import com.krld.diet.common.helpers.FLog;
import com.krld.diet.common.helpers.MetricsHelper;
import com.krld.diet.common.models.Meal;
import com.krld.diet.meals.adapters.MealsAdapter;

import butterknife.Bind;

public class MealFragment extends BaseDrawerToggleToolbarFragment {
    @Bind(R.id.meal_recycler_view)
    RecyclerView recyclerView;
    private Meal meal;

    public static MealFragment newInstance() {
        MealFragment fragment = new MealFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID_ARGUMENT, R.layout.meal_f);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String mealString = getActivity().getIntent().getStringExtra(Constants.EXTRA_MEAL);
        if (TextUtils.isEmpty(mealString)) {
            FLog.e(this, " meal is empty!");
            Toasts.safeShowLongToast(getActivity(), R.string.label_error);
            getActivity().finish();
        }
        meal = new Gson().fromJson(mealString, Meal.class);
    }

    @Override
    protected int getScreenOrientation() {
        return MetricsHelper.getSmallestWidthDp() < 500 ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE : super.getScreenOrientation();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar.setTitle(meal.nameLocResId);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // recyclerView.setAdapter(new MealsAdapter(this));
    }
}
