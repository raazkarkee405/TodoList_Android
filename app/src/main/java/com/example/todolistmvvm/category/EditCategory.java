package com.example.todolistmvvm.category;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.example.todolistmvvm.MainActivity;
import com.example.todolistmvvm.R;
import com.example.todolistmvvm.database.AppDatabase;
import com.example.todolistmvvm.database.CategoriesEntity;

public class EditCategory extends AppCompatActivity {

    int categoryID;
    AppDatabase db;
    InputMethodManager imm;
    CategoryViewModel viewModel;
    CategoriesEntity category;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        setTitle("Edit category");

        categoryID = getIntent().getIntExtra("categoryID", 0);

        // Set the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow);

        // Get database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "todolist").allowMainThreadQueries().build();
//
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        category = db.CategoriesDAO().getCategory(categoryID);
//        category.observe(this, new Observer<CategoriesEntity>() {
//            @Override
//            public void onChanged(CategoriesEntity categoriesEntity) {
//                Log.d(TAG, "Receiving update from categoryVIewModel");
//                category.removeObserver(this);
        // Get category name
        EditText categoryName = findViewById(R.id.categoryName);

        categoryName.setText(category.getCategory());

        // Show keyboard
        imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
//        });


//    }

    // Listener for toolbar button clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Finish activity
                Toast.makeText(getApplicationContext(), "Changes discarded.", Toast.LENGTH_SHORT).show();

                // Hide keyboard
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                finish();
                return true;

            case R.id.delete:
                // Delete category from database

                viewModel.deleteCategory(category);
                Toast.makeText(getApplicationContext(), "Category deleted.", Toast.LENGTH_SHORT).show();

                // Hide keyboard
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                finish();
                return true;

            case R.id.done:
                // Get content of text field
                EditText categoryName = findViewById(R.id.categoryName);
                String categoryNameText = categoryName.getText().toString();

                category.setCategory(categoryNameText);

                // Update entry in database
                viewModel.updateCategory(category);
                Toast.makeText(getApplicationContext(), "Category updated.", Toast.LENGTH_SHORT).show();

                // Hide keyboard
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Override back button press
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Changes discarded.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_item_toolbar_view, menu);
        return true;
    }
}
