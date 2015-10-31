package com.krld.diet.base.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.krld.diet.Application;
import com.krld.diet.BuildConfig;
import com.krld.diet.R;
import com.krld.diet.base.helpers.Toasts;
import com.krld.diet.common.helpers.FLog;

import java.lang.Override;import rx.subscriptions.CompositeSubscription;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public abstract class BaseActivity extends AppCompatActivity {


    protected CompositeSubscription compositeSubscriptionResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        setupTaskStyle();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            if (fragment != null)
                fm.beginTransaction()
                        .add(R.id.fragmentContainer, fragment)
                        .commit();
        }
    }

    protected int getLayoutResourceId() {
        return R.layout.base_act_one_pane;
    }

    protected abstract Fragment createFragment();

    protected void pushFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right);
        transaction.replace(R.id.fragmentContainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    protected void changeFragment(Fragment newFragment, boolean withAnimation) {

        View currentFocus = getCurrentFocus();
        if (getCurrentFocus() != null) {
            IBinder windowToken = currentFocus.getWindowToken();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (withAnimation)
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.fragmentContainer, newFragment);
        transaction.commit();
    }

    protected void popFragmentBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application.getInstance().activityOnResume(this);
        FLog.d(getClass().getSimpleName() + " On Resume");
       // if (BuildConfig.CRASHLYTICS_ENABLED)
       //     Crashlytics.setString(CrashlyticsHelper.EXTRA_LAST_ACTIVITY, getClass().getSimpleName());
        compositeSubscriptionResume = new CompositeSubscription();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Application.getInstance().activityOnPause(this);
        FLog.d(getClass().getSimpleName() + " On Pause");
        compositeSubscriptionResume.unsubscribe();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        FLog.d(getClass().getSimpleName() + " onNewIntent! " + intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getCurrentFragment().onActivityResult(requestCode, resultCode, data);
    }

    private void setupTaskStyle() {
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int color = getResources().getColor(R.color.primary_600);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_fuzd_recent);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(getString(R.string.fz_app_name), bm, color);

            setTaskDescription(td);
            bm.recycle();

        }*/
    }
}
