package com.example.todolistmvvm.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todolistmvvm.database.AppDatabase;
import com.example.todolistmvvm.database.CategoriesEntity;
import com.example.todolistmvvm.database.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository repository;
    private MutableLiveData<List<CategoriesEntity>> categories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        repository = new CategoryRepository(database);
    }

    public void getCategory(int ID){
        categories = repository.getCategories(ID);
    }

    public LiveData<List<CategoriesEntity>> getCategories(){
        return this.categories;
    }

    public void addCategory(CategoriesEntity categoriesEntity) {
        repository.addCategory(categoriesEntity);
    }

    public void deleteCategory(CategoriesEntity categoriesEntity) {
        repository.deleteCategory(categoriesEntity);
    }

    public void updateCategory(CategoriesEntity categoriesEntity) {
        repository.updateCategory(categoriesEntity);
    }

//    public void getCategoryById(CategoriesEntity categoriesEntity) {
//        int categoryID = 0;
//        repository.getCategory(categoryID);
//    }


}
