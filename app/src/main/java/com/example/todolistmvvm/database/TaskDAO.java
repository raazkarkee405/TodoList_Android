package com.example.todolistmvvm.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    public void addTask(TaskEntity task);

    @Query("SELECT * FROM tasks WHERE categoryID=:ID order by priority DESC")
    public List<TaskEntity> getTasks(int ID);

    @Query("SELECT * FROM tasks WHERE ID=:ID")
    public TaskEntity getTask(int ID);

    @Query("SELECT * FROM tasks order by priority DESC")
    public List<TaskEntity> getAllTasks();

    @Query("SELECT COUNT(ID) FROM tasks")
    public int getNumberOfTasks();

    @Query("SELECT COUNT(ID) FROM tasks WHERE categoryID=:ID")
    public int getNumberOfTasksInCategory(int ID);

//    @Delete
//    public void deleteTask(TaskEntity task);

    @Query("DELETE FROM tasks WHERE ID=:ID")
    public void deleteTask(int ID);

//    @Query("DELETE FROM tasks WHERE ID=:ID")
//    public void swipeToDelete(int ID);

    @Update
    public void updateTask(TaskEntity task);
}
