package com.krld.diet.common.models;

import com.krld.diet.R;

public class Profile {
    public Gender gender;
    public Integer age;
    public Integer height;
    public Integer weight;
    public LifeStyle lifeStyle;
    public Float bmi;
    public BMI bmiCategory;

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
        if (bmi == null) {
            calcBMI();
            isChanged = true;
        }

        return isChanged;
    }

    public void calcBMI() {
        bmi = (float) (weight / Math.pow(height / 100f, 2));
        bmiCategory = BMI.getByValue(bmi);
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
        PASSIVE(R.string.lifestyle_passive),;

        public int descriptionResId;

        LifeStyle(int descriptionResId) {

            this.descriptionResId = descriptionResId;
        }
    }

    public enum BMI {
        LOW(0, 18.5f, R.color.red_1),
        NORMAL(18.5f, 24.9f, R.color.green_1),
        HIGH(24.9f, 1000f, R.color.red_1),;

        private final float bot;
        private final float top;
        public final int color;

        BMI(float bot, float top, int color) {
            this.bot = bot;
            this.top = top;
            this.color = color;
        }

        public static BMI getByValue(float value) {
            for (BMI b : values()) {
                if (b.top >= value
                        &&
                        b.bot <= value) {
                    return b;
                }
            }
            return null;
        }
    }
}
