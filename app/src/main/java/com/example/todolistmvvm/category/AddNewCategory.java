package com.example.todolistmvvm.category;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.example.todolistmvvm.MainActivity;
import com.example.todolistmvvm.R;
import com.example.todolistmvvm.database.AppDatabase;
import com.example.todolistmvvm.database.CategoriesEntity;

public class AddNewCategory extends AppCompatActivity {

    EditText categoryName;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);

        setTitle("Add category");

        // set the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);

        //Get content of text field
        categoryName = findViewById(R.id.categoryName);

        //Show keyboard by default
        categoryName.requestFocus();

        //Show keyboard
        imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //LiStener for toolbar button click

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "Entry discarded.", Toast.LENGTH_SHORT).show();

                // Hide keyboard
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                finish();
                return true;
            case R.id.done:
                //Get database
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "todolist").allowMainThreadQueries().build();

                // Get categories entity
                CategoriesEntity category = new CategoriesEntity();

                String categoryNameText = categoryName.getText().toString();
                category.setCategory(categoryNameText);

                // Insert to database
                if (categoryNameText.trim().length() != 0) {

                    final CategoryViewModel viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
                    viewModel.addCategory(category);
                    Toast.makeText(getApplicationContext(), "New Category added",Toast.LENGTH_SHORT).show();

                    //Switch to new category in main activity
                    MainActivity.categoryID = db.CategoriesDAO().getLastCategory().getID();

                    //Show edit button when going back to category
                    MainActivity.showEditButton = true;

                    //Hide keyboard
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid entry", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Entry discarded", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_item_toolbar_view, menu);
        return true;
    }
}

