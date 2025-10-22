package com.example.prm392_to_dolisttaskmanagerusingslite;

public class Task {
    private int id;
    private String title;
    private String content;
    private String date;
    private boolean isCompleted;
    private String type;

    public Task(int id, String title, String content,String date, boolean isCompleted, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.isCompleted = isCompleted;
        this.type = type;
    }

    public Task(String title, String content,String date, boolean isCompleted, String type) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.isCompleted = isCompleted;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}