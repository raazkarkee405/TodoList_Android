package com.example.todolistmvvm.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoriesDAO {

    @Insert
    public void addCategory(CategoriesEntity category);

    @Query("SELECT * FROM categories")
    public List<CategoriesEntity> getCategories();

    @Query("SELECT * FROM categories WHERE ID=:ID")
    public CategoriesEntity getCategory(int ID);

//    @Query("SELECT * FROM categories WHERE ID=:ID")
//    public LiveData<CategoriesEntity> getCategory1(int ID);

    @Query("SELECT * FROM categories WHERE ID=:ID")
    List<CategoriesEntity> getCategoryInNav(int ID);

    @Query("SELECT COUNT(ID) FROM categories")
    public int getNumberOfCategories();

    @Query("SELECT * FROM categories ORDER BY ID DESC LIMIT 1 ")
    public CategoriesEntity getLastCategory();

    @Delete
    public void deleteCategory(CategoriesEntity category);

//    @Query("DELETE FROM categories WHERE ID=:ID")
//    public void deleteCategory(int ID);

    @Update
    public void updateCategory(CategoriesEntity category);
}