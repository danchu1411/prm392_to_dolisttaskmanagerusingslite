package com.example.prm392_to_dolisttaskmanagerusingslite;

public class Task {
    private int id;
    private String title;
    private String date;
    private boolean isCompleted;

    public Task(int id, String title, String date, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.isCompleted = isCompleted;
    }

    public Task(String title, String date, boolean isCompleted) {
        this.title = title;
        this.date = date;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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