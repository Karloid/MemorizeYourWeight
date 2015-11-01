package com.krld.diet.profile.fragments;

import android.app.Notification;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import rx.Observable;
import rx.functions.Action4;
import rx.observables.BlockingObservable;

public class ProfileFragment extends BaseDrawerToggleToolbarFragment {

    @Bind(R.id.gender_stub)
    ViewStub genderStub;

    @Bind(R.id.age_stub)
    ViewStub ageStub;

    private Profile profile;
    private SpinnerViewHolder genderVh;
    private SpinnerViewHolder ageVh;
    private DataHelper dataHelper;

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


        dataHelper = DataHelper.getInstance();
        profile = dataHelper.getProfile();

        setupGender();
        setupAge();
    }

    private void setupAge() {
        ageVh = new SpinnerViewHolder();
        ButterKnife.bind(ageVh, ageStub.inflate());
        ageVh.label.setText(R.string.age);

        List<String> ages = Observable.range(0, 130).map(Object::toString).toList().toBlocking().first();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_right, ages);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_right);
        ageVh.value.setAdapter(adapter);
        if (profile.age == null) {
            profile.age = 18;
            dataHelper.save(profile);
        }

        ageVh.value.setSelection(ages.indexOf(profile.age.toString()));

        ageVh.value.setOnItemSelectedListener(new SpinnerListener(
                (adapterView, view, position, aLong) -> {
                    profile.age = Integer.valueOf(ages.get(position));
                    dataHelper.save(profile);
                }));
    }

    private void setupGender() {
        genderVh = new SpinnerViewHolder();
        ButterKnife.bind(genderVh, genderStub.inflate());
        genderVh.label.setText(R.string.gender);

        List<String> genders = new ArrayList<>();
        genders.add(getString(R.string.man));
        genders.add(getString(R.string.woman));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_right, genders);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_right);
        genderVh.value.setAdapter(adapter);

        genderVh.value.setSelection(profile.gender.equals(Profile.Gender.MAN) ? 0 : 1);

        genderVh.value.setOnItemSelectedListener(new SpinnerListener(
                (adapterView, view, position, aLong) -> {
                    profile.gender = Profile.Gender.values()[position];
                    DataHelper.getInstance().save(profile);
                }));
    }

    public class SpinnerViewHolder {

        @Bind(R.id.textview_label)
        TextView label;

        @Bind(R.id.spinner_value)
        Spinner value;

    }

    private class SpinnerListener implements AdapterView.OnItemSelectedListener {
        private Action4<AdapterView, View, Integer, Long> callback;

        public SpinnerListener(Action4<AdapterView, View, Integer, Long> callback) {
            this.callback = callback;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            callback.call(parent, view, position, id);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
