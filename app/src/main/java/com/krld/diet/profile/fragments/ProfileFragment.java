package com.krld.diet.profile.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.krld.diet.R;
import com.krld.diet.base.fragments.BaseDrawerToggleToolbarFragment;
import com.krld.diet.common.helpers.DataHelper;
import com.krld.diet.common.models.Profile;

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
    private Profile profile;

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


        profile = DataHelper.getInstance().getProfile();

        setupGender();
        setupAge();

        genderVh.label.setText(R.string.gender);

        ageStub.inflate();
    }

    private void setupAge() {

    }

    private void setupGender() {
        genderVh = new SpinnerViewHolder();
        ButterKnife.bind(genderVh, genderStub.inflate());

        List<String> genders = new ArrayList<>();
        genders.add(getString(R.string.man));
        genders.add(getString(R.string.woman));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_right, genders);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_right);
        genderVh.value.setAdapter(adapter);

        genderVh.value.setSelection(profile.gender.equals(Profile.Gender.MAN) ? 0 : 1);

        genderVh.value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profile.gender = Profile.Gender.values()[position];
                DataHelper.getInstance().save(profile);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class SpinnerViewHolder {

        @Bind(R.id.textview_label)
        TextView label;

        @Bind(R.id.spinner_value)
        Spinner value;

    }
}
