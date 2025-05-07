package com.example.knowledgecheck;

public class Meal {
    private String mealType;
    private String time;
    private String description;
    private boolean alarmOn;
    private int iconRes;

    public Meal(String mealType, String time, String description,
                boolean alarmOn, int iconRes) {
        this.mealType = mealType;
        this.time = time;
        this.description = description;
        this.alarmOn = alarmOn;
        this.iconRes = iconRes;
    }

    // Getters and Setters
    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAlarmOn() {
        return alarmOn;
    }

    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}
