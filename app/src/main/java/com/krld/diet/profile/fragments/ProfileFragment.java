package com.krld.diet.profile.fragments;

import android.os.Bundle;

import com.krld.diet.R;
import com.krld.diet.base.fragments.BaseDrawerToggleToolbarFragment;
import com.krld.diet.base.fragments.BaseFragment;

public class ProfileFragment extends BaseDrawerToggleToolbarFragment {
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID_ARGUMENT, R.layout.profile_f);
        fragment.setArguments(args);
        return fragment;
    }
}
