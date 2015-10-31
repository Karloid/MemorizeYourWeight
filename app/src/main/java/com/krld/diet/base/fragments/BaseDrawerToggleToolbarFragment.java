package com.krld.diet.base.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.krld.diet.R;
import com.krld.diet.base.activities.BaseNavDrawerActivity;
import com.krld.diet.main.activities.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class BaseDrawerToggleToolbarFragment extends BaseFragment {

    private ActionBarDrawerToggle mDrawerToggle;

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    private View notificationCircle;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO

        ButterKnife.bind(this, view);
        setupActionBarDrawerToggle();

     /*   notificationCircle = getView().findViewById(R.id.notifications_circle);
        if (!(getActivity() instanceof MainActivity)) {
            showNavIconAsBackArrow(v -> ((BaseNavDrawerActivity) getActivity()).toolbarBackPressed());
            if (notificationCircle != null) {
                notificationCircle.setVisibility(View.INVISIBLE);
            }
        } else {
            if (notificationCircle != null) {
                Observable.combineLatest(DBHelper.getInstance().getUnreadMessagesCount(),
                        mFragmentVisibleObservable,
                        (count, isVisible) -> isVisible ? count : null)
                        .takeUntil(mOnDestroyViewObservable)
                        .filter(integer -> integer != null)
                        .map(i -> i > 0)
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isVisible -> {
                            if (notificationCircle != null)
                                notificationCircle.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                        });
            }
        }*/
    }

    protected void setupActionBarDrawerToggle() {
        mToolbar.setNavigationOnClickListener(null);

        DrawerLayout drawerLayout = ((BaseNavDrawerActivity) getActivity()).getDrawerLayout();
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, mToolbar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {  //TODO hide keyaboard when nav drawer start opened
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View currentFocus = activity.getCurrentFocus();
                    if (currentFocus != null)
                        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }

    protected void showNavIconAsBackArrow(View.OnClickListener onBackAction) {
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(onBackAction);
    }

}
