package com.krld.diet.base.helpers;

import com.krld.diet.base.fragments.BaseFragment;
import com.krld.diet.profile.fragments.ProfileFragment;

import rx.functions.Func0;

public enum NavigationDrawerFragments {
    PROFILE(ProfileFragment::newInstance),
    DIET(ProfileFragment::newInstance),
    MEMORIZE(ProfileFragment::newInstance);


    public int code;

    private final Func0<BaseFragment> createFragmentFunction;

    NavigationDrawerFragments(Func0<BaseFragment> createFragmentFunction) {
        this.createFragmentFunction = createFragmentFunction;
        code = LittleHackyHelper.id++;
    }

    public BaseFragment getFragment() {
        return createFragmentFunction.call();
    }

    public static NavigationDrawerFragments getByCode(int code) {
        return rx.Observable.from(values()).toBlocking().firstOrDefault(null, value -> value.code == code);
    }

    private static class LittleHackyHelper {
        public static int id = 10;
    }
}
