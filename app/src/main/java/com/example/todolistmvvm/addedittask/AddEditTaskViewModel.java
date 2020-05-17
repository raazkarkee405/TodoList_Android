package com.example.todolistmvvm.addedittask;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolistmvvm.database.AppDatabase;
import com.example.todolistmvvm.database.TaskEntry;

public class AddEditTaskViewModel extends AndroidViewModel {

    public LiveData<TaskEntry> getTask() {
        return task;
    }

    private LiveData<TaskEntry> task;

    public AddEditTaskViewModel(Application application, int taskId) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        task = database.taskDao().loadTAskById(taskId);
    }

    public void insertTask(TaskEntry task){

    }
    public void updateTask(TaskEntry task){

    }

}
