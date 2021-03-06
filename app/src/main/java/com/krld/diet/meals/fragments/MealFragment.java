package com.krld.diet.meals.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.gson.Gson;
import com.krld.diet.R;
import com.krld.diet.base.Constants;
import com.krld.diet.base.fragments.BaseDrawerToggleToolbarFragment;
import com.krld.diet.base.helpers.Toasts;
import com.krld.diet.common.helpers.FLog;
import com.krld.diet.common.helpers.MetricsHelper;
import com.krld.diet.common.helpers.ViewHelper;
import com.krld.diet.common.models.MealEnumeration;
import com.krld.diet.meals.adapters.ProductsAdapter;

import butterknife.Bind;

public class MealFragment extends BaseDrawerToggleToolbarFragment {

    @Bind(R.id.meal_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.toolbar)
    View toolbar;

    @Bind(R.id.toolbar_shadow)
    View toolbarShadow;

    private MealEnumeration meal;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardListener;

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
        meal = new Gson().fromJson(mealString, MealEnumeration.class);

        keyboardListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            private int mPreviousHeight;

            @Override
            public void onGlobalLayout() {
                int newHeight = getView().getHeight();
                if (mPreviousHeight != 0) {
                    if (mPreviousHeight > newHeight) {
                        // Height decreased: keyboard was shown
                        keyboardVisible(true);
                    } else if (mPreviousHeight < newHeight) {
                        // Height increased: keyboard was hidden
                        keyboardVisible(false);
                    } else {
                        // No change
                    }
                }
                mPreviousHeight = newHeight;
            }
        };
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
        ProductsAdapter adapter = new ProductsAdapter(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().getViewTreeObserver().addOnGlobalLayoutListener(keyboardListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getView().getViewTreeObserver().removeGlobalOnLayoutListener(keyboardListener);
    }

    private void keyboardVisible(boolean isVisible) {
         if (isVisible) {
             toolbarShadow.setVisibility(View.GONE);
             toolbar.setVisibility(View.GONE);
         }   else {
             toolbarShadow.setVisibility(View.VISIBLE);
             toolbar.setVisibility(View.VISIBLE);
         }
    }

    public MealEnumeration getMeal() {
        return meal;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
