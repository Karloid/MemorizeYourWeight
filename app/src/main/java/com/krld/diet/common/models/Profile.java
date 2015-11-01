package com.krld.diet.common.models;

import com.krld.diet.R;

public class Profile {
    public Gender gender;

    public static Profile create() {
        Profile profile = new Profile();
        profile.gender = Gender.MAN;
        return profile;
    }

    public enum Gender {
        MAN(R.string.man),
        WOMAN(R.string.woman),;

        public int locString;

        Gender(int locString) {

            this.locString = locString;
        }
    }
}
