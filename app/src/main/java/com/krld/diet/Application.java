package com.krld.diet;


import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.activeandroid.ActiveAndroid;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.krld.diet.common.helpers.FLog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class Application extends android.app.Application {


    private static Application singleton;

    private int foregroundActivitiesCount;
    private BehaviorSubject<Integer> foregroundActivitiesCountObs;
    private Subscription disconnectSubscription;

    public RxSharedPreferences getRxPrefs() {
        return rxPrefs;
    }

    private RxSharedPreferences rxPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        FLog.d(getClass().getSimpleName() + " onCreate");
        ActiveAndroid.initialize(this);
        printKeyHash();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        rxPrefs = RxSharedPreferences.create(prefs);
    }

    private void printKeyHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                FLog.e("hash key" + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            FLog.e("name not found" + e1.toString());
        } catch (NoSuchAlgorithmException e) {
            FLog.e("no such an algorithm" + e.toString());
        } catch (Exception e) {
            FLog.e("exception" + e.toString());
        }
    }

    public void activityOnResume(AppCompatActivity baseActivity) {  //TODO BaseActivity
        foregroundActivitiesCount++;
        getForegroundActivitiesCountObs().onNext(foregroundActivitiesCount);
    }

    public void activityOnPause(AppCompatActivity baseActivity) {   //TODO BaseActivity
        foregroundActivitiesCount--;
        getForegroundActivitiesCountObs().onNext(foregroundActivitiesCount);
    }

    public BehaviorSubject<Integer> getForegroundActivitiesCountObs() {
        if (foregroundActivitiesCountObs == null) {
            foregroundActivitiesCountObs = BehaviorSubject.create(foregroundActivitiesCount);
        }
        return foregroundActivitiesCountObs;
    }

    public static Application getInstance() {
        return singleton;
    }

    public boolean isVisible() {
        return getForegroundActivitiesCountObs().getValue() != 0;
    }

    public SharedPreferences getSharedPrefs() {
        return getSharedPreferences("DEFAULT_SHARED_PREFS", MODE_PRIVATE);
    }
}
