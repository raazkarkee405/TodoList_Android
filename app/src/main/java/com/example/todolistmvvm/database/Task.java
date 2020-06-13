package com.example.todolistmvvm.database;

import java.util.Date;

public class Task {
    public String title;
    public String description;
    public int priority;
    public Date updatedAt;
    public int ID;

    public Task(String title, String description, int priority, Date updatedAt, int ID) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.updatedAt = updatedAt;
        this.ID = ID;
    }
}
