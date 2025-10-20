package com.example.prm392_to_dolisttaskmanagerusingslite;

public class Task {
    String title;
    String date;
    boolean isCompleted;

    public Task(String title, String date, boolean isCompleted) {
        this.title = title;
        this.date = date;
        this.isCompleted = isCompleted;
    }

    // Getters and setters (optional, but good practice)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}