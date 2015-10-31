package com.krld.diet.base.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.krld.diet.R;
import com.krld.diet.base.adapters.NavigationAdapter;
import com.krld.diet.base.helpers.NavigationDrawerFragments;
import com.krld.diet.base.helpers.NavigationItemsHelper;
import com.krld.diet.common.helpers.FLog;
import com.krld.diet.main.activities.MainActivity;

public abstract class BaseNavDrawerActivity extends BaseActivity {

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String EXTRA_FRAGMENT_CODE = "extra_fragment_code";
    private final Handler mDrawerActionHandler = new Handler();

    protected DrawerLayout mDrawerLayout;
    private RecyclerView mNavigationView;
    private NavigationAdapter mAdapter;
    private BaseNavDrawerActivityCallback mNavigationDrawerCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FLog.d("onCreate BaseNavDrawerActivity");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (RecyclerView) findViewById(R.id.navigation_view);
        mNavigationView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mNavigationView.setLayoutManager(layoutManager);
        mNavigationDrawerCallback = new BaseNavDrawerActivityCallback();
        mAdapter = new NavigationAdapter(mNavigationDrawerCallback, this);
        mNavigationView.setAdapter(mAdapter);

        mDrawerLayout.setDrawerListener(new MyDrawerListener());
        mDrawerLayout.closeDrawer(GravityCompat.START);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.base_act_nav_drawer;
    }


    @Override
    protected void onResume() {
        super.onResume();
      /*  compositeSubscriptionResume.add(FuzdAccount.getLoginResponseObs()
                .observeOn(mainThread())
                .subscribe(mAdapter::updateHeader));*/
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        FLog.d("new intent: " + intent);
        int intExtraCode = intent.getIntExtra(EXTRA_FRAGMENT_CODE, -1);
        FLog.d("new intent EXTRA_FRAGMENT_CODE: " + intExtraCode);
        if (intExtraCode != -1) {
            mNavigationDrawerCallback.show(NavigationDrawerFragments.getByCode(intExtraCode), false);
        } else {
            FLog.d("new intent with wrong EXTRA_FRAGMENT_CODE: " + intExtraCode);
        }

    }

    public void toolbarBackPressed() {
        finish();
    }

    private class BaseNavDrawerActivityCallback implements NavigationItemsHelper.NavigationItemsCallback {

        public void show(NavigationDrawerFragments fragmentEnum, boolean withAnimation) {
            if (BaseNavDrawerActivity.this instanceof MainActivity) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (withAnimation) {
                    mDrawerActionHandler.postDelayed(() -> changeFragment(fragmentEnum.getFragment(), true), DRAWER_CLOSE_DELAY_MS);
                } else {
                    changeFragment(fragmentEnum.getFragment(), false);
                }
            } else {
                Intent intent = new Intent(BaseNavDrawerActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(EXTRA_FRAGMENT_CODE, fragmentEnum.code);
                startActivity(intent);
            }
        }

        @Override
        public void showDialog(NavigationDrawerFragments fragmentEnum) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            fragmentEnum.getFragment().show(getSupportFragmentManager(), "df");
        }
    }

    private class MyDrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            InputMethodManager inputMethodManager = (InputMethodManager) BaseNavDrawerActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(BaseNavDrawerActivity.this.getCurrentFocus().getWindowToken(), 0);
        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }
}
