package com.example.todoapp;

public class Task {
    private int id;
    private int userId;
    private String title;
    private String notes;
    private boolean isComplete;
    private String createdAt;

    public Task() {}

    public Task(int id, int userId, String title, String notes, boolean isComplete, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.notes = notes;
        this.isComplete = isComplete;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isComplete() { return isComplete; }
    public void setComplete(boolean complete) { isComplete = complete; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
