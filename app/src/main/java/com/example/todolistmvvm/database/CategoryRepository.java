package com.example.todolistmvvm.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class CategoryRepository {

    CategoriesDAO dao;

    public CategoryRepository(AppDatabase database) {
        dao = database.CategoriesDAO();
    }

    public MutableLiveData<List<CategoriesEntity>> getCategories(int ID) {
        init(ID);
        final MutableLiveData<List<CategoriesEntity>> data = new MutableLiveData<>();
        data.setValue(dao.getCategories());
        return data;
    }

    public MutableLiveData<List<CategoriesEntity>> init(final int ID){
        final MutableLiveData<List<CategoriesEntity>> data = new MutableLiveData<>();
        data.setValue(dao.getCategoryInNav(ID));
        return data;
    }

    public CategoriesEntity getCategory(int categoryID) {
        return dao.getCategory(categoryID);
    }

    public void updateCategory(final CategoriesEntity categoriesEntity) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateCategory(categoriesEntity);
            }
        });
    }

    public void deleteCategory(final CategoriesEntity categoriesEntity) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteCategory(categoriesEntity);
            }
        });
    }

    public void addCategory(final CategoriesEntity categoriesEntity) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.addCategory(categoriesEntity);
            }
        });
    }
}
