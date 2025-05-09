package com.example.knowledgecheck;

public class Meal {
    private long id;
    private String mealType;
    private String time;
    private String description;
    private boolean alarmOn;
    private int iconRes;

    public Meal(long id, String mealType, String time, String description, boolean alarmOn, int iconRes) {
        this.id = id;
        this.mealType = mealType;
        this.time = time;
        this.description = description;
        this.alarmOn = alarmOn;
        this.iconRes = iconRes;
    }

    // Constructor without ID for new meals
    public Meal(String mealType, String time, String description, boolean alarmOn, int iconRes) {
        this(-1, mealType, time, description, alarmOn, iconRes);
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getMealType() {
        return mealType;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAlarmOn() {
        return alarmOn;
    }

    public int getIconRes() {
        return iconRes;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}