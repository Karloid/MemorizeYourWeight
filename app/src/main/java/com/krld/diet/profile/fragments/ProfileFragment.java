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
import com.krld.diet.common.models.DataStore;
import com.krld.diet.common.models.Profile;
import com.krld.diet.memorize.common.FormatterHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action4;
import rx.schedulers.Schedulers;

import static rx.android.schedulers.AndroidSchedulers.*;
import static rx.schedulers.Schedulers.*;

public class ProfileFragment extends BaseDrawerToggleToolbarFragment {

    @Bind(R.id.gender_stub)
    ViewStub genderStub;

    @Bind(R.id.age_stub)
    ViewStub ageStub;

    @Bind(R.id.height_stub)
    ViewStub heightStub;

    @Bind(R.id.weight_stub)
    ViewStub weightStub;

    @Bind(R.id.lifestyle_stub)
    ViewStub lifestyleStub;

    @Bind(R.id.bmi_stub)
    ViewStub bmiStub;

    private Profile profile;
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
        setupHeight();
        setupWeight();
        setupLifeStyle();
        setupBMI();
    }

    private void setupBMI() {
        TextViewHolder vh = new TextViewHolder();
        ButterKnife.bind(vh, bmiStub.inflate());
        vh.label.setText(R.string.bmi);

        Action1<Profile> bind = profile -> {
            vh.value.setText(FormatterHelper.formatDouble(profile.bmi));
            vh.value.setTextColor(getResources().getColor(profile.bmiCategory.color));
        };
        bind.call(profile);
        compositeSubscriptionCreated.add(
                dataHelper.getUpdatedProfileObs()
                        .subscribeOn(io())
                        .map(dataStore -> profile)
                        .observeOn(mainThread())
                        .subscribe(bind));
    }

    private void setupLifeStyle() {
        SpinnerViewHolder vh = new SpinnerViewHolder();
        ButterKnife.bind(vh, lifestyleStub.inflate());
        vh.label.setText(R.string.lifestyle);

        List<String> values = Observable.from(Profile.LifeStyle.values()).map(lifeStyle -> getString(lifeStyle.descriptionResId)).toList().toBlocking().first();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_right, values);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_right);
        vh.value.setAdapter(adapter);

        vh.value.setSelection(values.indexOf(getString(profile.lifeStyle.descriptionResId)), false);

        vh.value.setOnItemSelectedListener(new SpinnerListener(
                (adapterView, view, position, aLong) -> {
                    profile.lifeStyle = Profile.LifeStyle.values()[position];
                    DataHelper.getInstance().save(profile);
                }));
        ((View)vh.value.getParent()).setOnClickListener(v -> vh.value.performClick());
    }

    private void setupWeight() {
        SpinnerViewHolder vh = new SpinnerViewHolder();
        ButterKnife.bind(vh, weightStub.inflate());
        vh.label.setText(R.string.weight);

        List<String> values = Observable.range(30, 190).map(Object::toString).toList().toBlocking().first();
        Collections.reverse(values);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_right, values);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_right);
        vh.value.setAdapter(adapter);

        vh.value.setSelection(values.indexOf(profile.weight.toString()), false);

        vh.value.setOnItemSelectedListener(new SpinnerListener(
                (adapterView, view, position, aLong) -> {
                    profile.weight = Integer.valueOf(values.get(position));
                    dataHelper.save(profile);
                }));
        ((View)vh.value.getParent()).setOnClickListener(v -> vh.value.performClick());
    }

    private void setupHeight() {
        SpinnerViewHolder vh = new SpinnerViewHolder();
        ButterKnife.bind(vh, heightStub.inflate());
        vh.label.setText(R.string.height);

        List<String> values = Observable.range(130, 230).map(Object::toString).toList().toBlocking().first();
        Collections.reverse(values);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_right, values);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_right);
        vh.value.setAdapter(adapter);

        vh.value.setSelection(values.indexOf(profile.height.toString()), false);

        vh.value.setOnItemSelectedListener(new SpinnerListener(
                (adapterView, view, position, aLong) -> {
                    profile.height = Integer.valueOf(values.get(position));
                    dataHelper.save(profile);
                }));
        ((View)vh.value.getParent()).setOnClickListener(v -> vh.value.performClick());
    }

    private void setupAge() {
        SpinnerViewHolder vh = new SpinnerViewHolder();
        ButterKnife.bind(vh, ageStub.inflate());
        vh.label.setText(R.string.age);

        List<String> values = Observable.range(0, 130).map(Object::toString).toList().toBlocking().first();
        Collections.reverse(values);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_right, values);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_right);
        vh.value.setAdapter(adapter);
        if (profile.age == null) {
            profile.age = 18;
            dataHelper.save(profile);
        }

        vh.value.setSelection(values.indexOf(profile.age.toString()), false);

        vh.value.setOnItemSelectedListener(new SpinnerListener(
                (adapterView, view, position, aLong) -> {
                    profile.age = Integer.valueOf(values.get(position));
                    dataHelper.save(profile);
                }));
        ((View)vh.value.getParent()).setOnClickListener(v -> vh.value.performClick());
    }

    private void setupGender() {
        SpinnerViewHolder vh = new SpinnerViewHolder();
        ButterKnife.bind(vh, genderStub.inflate());
        vh.label.setText(R.string.gender);

        List<String> genders = new ArrayList<>();
        genders.add(getString(Profile.Gender.MAN.locString));
        genders.add(getString(Profile.Gender.WOMAN.locString));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_right, genders);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_right);
        vh.value.setAdapter(adapter);

        vh.value.setSelection(profile.gender.equals(Profile.Gender.MAN) ? 0 : 1, false);

        vh.value.setOnItemSelectedListener(new SpinnerListener(
                (adapterView, view, position, aLong) -> {
                    profile.gender = Profile.Gender.values()[position];
                    DataHelper.getInstance().save(profile);
                }));

        ((View)vh.value.getParent()).setOnClickListener(v -> vh.value.performClick());
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

    public class TextViewHolder {
        @Bind(R.id.textview_label)
        TextView label;

        @Bind(R.id.textview_value)
        TextView value;
    }
}
