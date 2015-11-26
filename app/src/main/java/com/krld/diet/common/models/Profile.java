package com.krld.diet.common.models;

import com.krld.diet.R;
import com.krld.diet.common.helpers.FLog;

public class Profile {
    public Gender gender;
    public Integer age;
    public Integer height;
    public Integer weight;
    public LifeStyle lifeStyle;
    public Float bmi;
    public BMI bmiCategory;
    public float dayCalories;

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
            calc();
            isChanged = true;
        }

        return isChanged;
    }

    public void calc() {
        bmi = (float) (weight / Math.pow(height / 100f, 2));
        bmiCategory = BMI.getByValue(bmi);

        //(k1 * weight + k2) * 240
        if (age <= 30) {
            if (gender.equals(Gender.WOMAN)) {
                dayCalories = (0.06f * weight + 2.037f);
            } else {
                dayCalories = (0.06f * weight + 2.9f);
            }
        } else if (age <= 60) {
            if (gender.equals(Gender.WOMAN)) {
                dayCalories = (0.034f * weight + 3.54f);
            } else {
                dayCalories = (0.05f * weight + 3.65f);
            }
        } else {
            if (gender.equals(Gender.WOMAN)) {
                dayCalories = (0.04f * weight + 2.76f);
            } else {
                dayCalories = (0.05f * weight + 2.46f);
            }
        }
        dayCalories *= 240;

        switch (lifeStyle) {
            case ACTIVE:
                dayCalories *= 1.5f;
                break;
            case NORMAL:
                dayCalories *= 1.3f;
                break;
            case PASSIVE:
                dayCalories *= 1.1f;
                break;
        }

        if (bmi > 24.9f) {
            dayCalories -= 600;
        } else if (bmi < 18.5f) {
            dayCalories += 600;
        }
        FLog.d(this, "dayCalories " + dayCalories);

    }

    public float getProteins(MealEnumeration meal) {
        return ((getCalories(meal)) * 0.25f) / Product.CALORIES_PROTEINS;
    }

    public float getFats(MealEnumeration meal) {
        return (getCalories(meal) * 0.25f) / Product.CALORIES_FATS;
    }

    public float getCarbs(MealEnumeration meal) {
        return ((getCalories(meal)) * 0.50f) / Product.CALORIES_CARBS;
    }

    public float getCalories(MealEnumeration meal) {
        return dayCalories * meal.caloriesPercent;
    }

    public enum Gender {
        MAN(R.string.man),
        WOMAN(R.string.woman);

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
