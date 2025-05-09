package com.example.knowledgecheck;

public class Prescription {
    private long id;
    private String medicationType;
    private String time;
    private String details;
    private boolean hasAlarm;
    private int iconRes;

    public Prescription(long id, String medicationType, String time, String details, boolean hasAlarm, int iconRes) {
        this.id = id;
        this.medicationType = medicationType;
        this.time = time;
        this.details = details;
        this.hasAlarm = hasAlarm;
        this.iconRes = iconRes;
    }

    public Prescription(String medicationType, String time, String details, boolean hasAlarm, int iconRes) {
        this(-1, medicationType, time, details, hasAlarm, iconRes);
    }

    // Getters
    public long getId() { return id; }
    public String getMedicationType() { return medicationType; }
    public String getTime() { return time; }
    public String getDetails() { return details; }
    public boolean hasAlarm() { return hasAlarm; }
    public int getIconRes() { return iconRes; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setMedicationType(String medicationType) { this.medicationType = medicationType; }
    public void setTime(String time) { this.time = time; }
    public void setDetails(String details) { this.details = details; }
    public void setHasAlarm(boolean hasAlarm) { this.hasAlarm = hasAlarm; }
    public void setIconRes(int iconRes) { this.iconRes = iconRes; }
}