package com.example.todolistmvvm.tasks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todolistmvvm.MainActivity;
import com.example.todolistmvvm.database.AppDatabase;
import com.example.todolistmvvm.database.TaskEntity;
import com.example.todolistmvvm.database.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final static String TAG = TaskViewModel.class.getSimpleName();

    private MutableLiveData<List<TaskEntity>> tasks;
    private MutableLiveData<List<TaskEntity>> alltasks;

    private TaskRepository repository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        repository = new TaskRepository(database);
        tasks = repository.init(MainActivity.categoryID);
    }

    public void getAllTasks(int ID){
        alltasks = repository.getAllTasks(ID);
    }

    public void addTask(TaskEntity taskEntity) {
        repository.insertTask(taskEntity);
    }

    public void updateTask(TaskEntity taskEntity){
        repository.updateTask(taskEntity);
    }

    public void deleteTask(int taskID){
        repository.deleteTask(taskID);
    }

    public LiveData<List<TaskEntity>> getAlltasks(){
        return this.alltasks;
    }

    public LiveData<List<TaskEntity>> getTasks(){
        return this.tasks;
    }

}
