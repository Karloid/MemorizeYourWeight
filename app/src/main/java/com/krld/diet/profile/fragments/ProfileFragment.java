package com.krld.diet.profile.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.krld.diet.R;
import com.krld.diet.base.fragments.BaseDrawerToggleToolbarFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileFragment extends BaseDrawerToggleToolbarFragment {

    @Bind(R.id.gender_stub)
    ViewStub genderStub;

    @Bind(R.id.age_stub)
    ViewStub ageStub;

    private SpinnerViewHolder genderVh;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID_ARGUMENT, R.layout.profile_f);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar.setTitle(R.string.profile);

        setupGender();

        genderVh.label.setText(R.string.gender);

        ageStub.inflate();
    }

    private void setupGender() {
        genderVh = new SpinnerViewHolder();
        ButterKnife.bind(genderVh, genderStub.inflate());

        List<String> genders = new ArrayList<>();
        genders.add(getString(R.string.man));
        genders.add(getString(R.string.woman));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_li, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderVh.value.setAdapter(adapter);
      //  genderVh.value.setSelection(ages.indexOf(currentValue));
    }

    public class SpinnerViewHolder {

        @Bind(R.id.textview_label)
        TextView label;

        @Bind(R.id.spinner_value)
        Spinner value;

    }
}
