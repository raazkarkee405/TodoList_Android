package com.example.todolistmvvm.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "tasks", indices = {@Index("categoryID")}, foreignKeys = @ForeignKey(entity = CategoriesEntity.class, parentColumns = "ID", childColumns = "categoryID", onDelete = CASCADE, onUpdate = CASCADE))
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    private int categoryID;

    private String title;

    private String description;

    private int priority;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

//    public TaskEntity(int ID, int categoryID, String title, String description, int priority, Date updatedAt) {
//        this.ID = ID;
//        this.categoryID = categoryID;
//        this.title = title;
//        this.description = description;
//        this.priority = priority;
//        this.updatedAt = updatedAt;
//    }

//    @Ignore
//    public TaskEntity(String title, String description, int priority, Date updatedAt) {
//        this.title = title;
//        this.description = description;
//        this.priority = priority;
//        this.updatedAt = updatedAt;
//    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
