package com.example.todolistmvvm.addedittask;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolistmvvm.database.AppDatabase;
import com.example.todolistmvvm.database.Repository;
import com.example.todolistmvvm.database.TaskEntry;

public class AddEditTaskViewModel extends AndroidViewModel {

    Repository repository;
    LiveData<TaskEntry> task;

    AddEditTaskViewModel(Application application, int taskId){
        super(application);
        repository = new Repository(AppDatabase.getInstance(application));
        if(taskId != -1)
            task = repository.getTaskById(taskId);
    }


    public LiveData<TaskEntry> getTask(){
        return task;
    }

    public void insertTask(TaskEntry task){
        repository.insertTask(task);
    }

    public void updateTask(TaskEntry task){
        repository.insertTask(task);
    }

}
