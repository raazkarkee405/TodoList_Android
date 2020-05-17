package com.example.todolistmvvm.addedittask;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddEditTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    public AddEditTaskViewModelFactory(Application application, int taskId) {
        this.application = application;
        this.taskId = taskId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddEditTaskViewModel(application, taskId);
    }

    Application application;
    int taskId;
}
