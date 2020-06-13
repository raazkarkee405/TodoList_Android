package com.example.todolistmvvm.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class TaskRepository {

    TaskDAO dao;

    public TaskRepository(AppDatabase database) {
        dao = database.TaskDAO();
    }

    public MutableLiveData<List<TaskEntity>> getAllTasks(int ID) {
        init(ID);
        final MutableLiveData<List<TaskEntity>> data = new MutableLiveData<>();
        data.setValue(dao.getAllTasks());
//        return dao.getAllTasks();
        return data;
    }

    public MutableLiveData<List<TaskEntity>> init(final int ID){
        final MutableLiveData<List<TaskEntity>> data = new MutableLiveData<>();
        data.setValue(dao.getTasks(ID));
        return data;
    }


    public void updateTask(final TaskEntity task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateTask(task);
            }
        });
    }

    public void deleteTask(final int taskID){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteTask(taskID);
            }
        });
    }

    public  void  insertTask(final TaskEntity task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.addTask(task);
            }
        });
    }
}
