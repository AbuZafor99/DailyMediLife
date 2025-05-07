package com.example.knowledgecheck;

public class Task {
    private String id;
    private String title;
    private String description;
    private boolean isCompleted;
    private String dueTime;

    public Task(String title, String description, String dueTime) {
        this.title = title;
        this.description = description;
        this.dueTime = dueTime;
        this.isCompleted = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    public String getDueTime() { return dueTime; }
    public void setDueTime(String dueTime) { this.dueTime = dueTime; }
}