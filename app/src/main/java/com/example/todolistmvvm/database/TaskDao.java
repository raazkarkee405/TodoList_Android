package com.example.todolistmvvm.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("select * from task order by priority")
    List<TaskEntry> loadAllTasks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(TaskEntry task);

    @Update
    void update(TaskEntry task);

    @Delete
    void deleteTask(TaskEntry task);

    @Query("DELETE FROM task")
    void deleteAllTasks();

    @Query("Select * from task where id =:taskId")
    TaskEntry loadTAskById(int taskId);
}
