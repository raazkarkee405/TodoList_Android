package com.example.todolistmvvm.tasks;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolistmvvm.database.AppDatabase;
import com.example.todolistmvvm.database.Repository;
import com.example.todolistmvvm.database.TaskEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final static String TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<TaskEntry>> tasks;

    Repository repository;
    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        repository = new Repository(database);
        tasks = repository.getTasks();
    }

    public LiveData<List<TaskEntry>> getTasks(){
        return tasks;
    }

    public void deleteTask(final TaskEntry task) {
        repository.deleteTask(task);
    }
}
