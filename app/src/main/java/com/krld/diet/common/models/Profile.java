package com.krld.diet.common.models;

import com.krld.diet.R;

public class Profile {
    public Gender gender;
    public Integer age;
    public Integer height;
    public Integer weight;
    public LifeStyle lifeStyle;

    public static Profile create() {
        Profile profile = new Profile();
        profile.init();
        return profile;
    }

    public boolean init() {
        boolean isChanged = false;
        if (gender == null) {
            gender = Gender.MAN;
            isChanged = true;
        }
        if (age == null) {
            age = 18;
            isChanged = true;
        }
        if (height == null) {
            height = 170;
            isChanged = true;
        }
        if (weight == null) {
            weight = 60;
            isChanged = true;
        }

        if (lifeStyle == null) {
            lifeStyle = LifeStyle.NORMAL;
            isChanged = true;
        }

        return isChanged;
    }

    public enum Gender {
        MAN(R.string.man),
        WOMAN(R.string.woman),;

        public int locString;

        Gender(int locString) {

            this.locString = locString;
        }
    }

    public enum LifeStyle {
        ACTIVE(R.string.lifestyle_active),
        NORMAL(R.string.lifestyle_normal),
        PASSIVE(R.string.lifestyle_passive),
        ;

        public int descriptionResId;

        LifeStyle(int descriptionResId) {

            this.descriptionResId = descriptionResId;
        }
    }
}
