package com.krld.diet.base.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.krld.diet.Application;
import com.krld.diet.BuildConfig;
import com.krld.diet.base.Constants;
import com.krld.diet.common.helpers.FLog;

import butterknife.ButterKnife;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment extends DialogFragment {

    public static final String LAYOUT_ID_ARGUMENT = "layoutIdArg";

    protected int mFragmentLayoutId;

    private int mCurrentMode; //TODO remove

    //rx

    protected BehaviorSubject<Boolean> mFragmentVisibleObservable;
    protected PublishSubject<Object> mOnDestroyViewObservable;
    protected BehaviorSubject<Object> mOnViewCreatedObservable;
    protected CompositeSubscription compositeSubscriptionCreated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentLayoutId = 0;
        mCurrentMode = 0;

        Bundle extras = getArguments();
        if (extras != null) {
            mFragmentLayoutId = extras.getInt(LAYOUT_ID_ARGUMENT, mFragmentLayoutId);
        }
        if (savedInstanceState != null) {
            mFragmentLayoutId = savedInstanceState.getInt(LAYOUT_ID_ARGUMENT, mFragmentLayoutId);
        }
        createRxStuff();

        //noinspection ResourceType
        getActivity().setRequestedOrientation(
                getScreenOrientation());
    }

    protected int getScreenOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
    }

    private void createRxStuff() {
        mFragmentVisibleObservable = BehaviorSubject.create(false);

        mOnDestroyViewObservable = PublishSubject.create();
        mOnViewCreatedObservable = BehaviorSubject.create(); //TODO change to publish subject

        if (BuildConfig.DEBUG) {
            mOnViewCreatedObservable.subscribe(o1 -> {
                FLog.d(this.getClass().getSimpleName() + ": mOnViewCreatedObservable: " + o1);
            });
            mOnDestroyViewObservable.subscribe(o1 -> {
                FLog.d(this.getClass().getSimpleName() + ": mOnDestroyViewObservable: " + o1);
            });

            mFragmentVisibleObservable.subscribe(v -> FLog.d(this.getClass().getSimpleName() + ": fragmentVisible: " + v));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(mFragmentLayoutId, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFragmentVisibleObservable.onNext(true);
       /* if (BuildConfig.CRASHLYTICS_ENABLED)
            Crashlytics.setString(CrashlyticsHelper.EXTRA_LAST_FRAGMENT, getClass().getSimpleName());*/
    }

    @Override
    public void onPause() {
        super.onPause();
        mFragmentVisibleObservable.onNext(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mOnViewCreatedObservable.onNext(Constants.WHATEVER);
        compositeSubscriptionCreated = new CompositeSubscription();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAYOUT_ID_ARGUMENT, mFragmentLayoutId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mOnDestroyViewObservable.onNext(Constants.WHATEVER);
        mOnDestroyViewObservable.onCompleted();
        mFragmentVisibleObservable.onCompleted();

        compositeSubscriptionCreated.unsubscribe();
        compositeSubscriptionCreated = null;
       // Application.getInstance().getMainRequestQueue().cancelAll(this);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getView() != null) {
            onIsVisibleToUser();
        }
    }

    protected void onIsVisibleToUser() {
        //to override
    }

    protected void showKeyboard(EditText editText) {
        editText.requestFocus();
        editText.postDelayed(() -> {
            InputMethodManager keyboard = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(editText, 0);
        }, 200); //use 300 to make it run when coming back from lock screen  }
    }

    public BehaviorSubject<Boolean> getFragmentVisibleObservable() {
        return mFragmentVisibleObservable;
    }

    protected boolean hasView() {
        return getView() != null;
    }
}
