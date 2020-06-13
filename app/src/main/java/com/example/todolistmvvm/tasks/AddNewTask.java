package com.example.todolistmvvm.tasks;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.example.todolistmvvm.R;
import com.example.todolistmvvm.database.AppDatabase;
import com.example.todolistmvvm.database.TaskEntity;

import java.util.Date;

public class AddNewTask extends AppCompatActivity {

    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    int categoryID;
    InputMethodManager imm;
    EditText editTextTitle;
    EditText editTextDescription;
    RadioGroup mRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        categoryID = getIntent().getIntExtra("categoryID", 0);

        setTitle("Add Task");

        // Set the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow);

        // Show keyboard
        imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    // Listener for toolbar button clicks

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "Task discarded.", Toast.LENGTH_SHORT).show();

                // Hide keyboard
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                finish();
                return true;
            case R.id.done:
                // Get Database
             //   AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "todolist").allowMainThreadQueries().build();

                // Get tasks entity
                editTextTitle = findViewById(R.id.editTextTaskTitle);
                String title = editTextTitle.getText().toString();
                editTextDescription = findViewById(R.id.editTextTaskDescription);
                String description = editTextDescription.getText().toString();
                mRadioGroup = findViewById(R.id.radioGroup);
                int priority = getPriorityFromViews();
                Date date = new Date();
                final TaskEntity task = new TaskEntity();

                task.setCategoryID(categoryID);
                task.setTitle(title);
                task.setDescription(description);
                task.setPriority(priority);
                task.setUpdatedAt(date);

                // Insert to database
                if (title.trim().length() != 0 && description.trim().length() != 0) {
                    final TaskViewModel viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
                    viewModel.addTask(task);
//                    db.TaskDAO().addTask(task);
                    Toast.makeText(getApplicationContext(), "Task Added", Toast.LENGTH_SHORT).show();

                    // Hide keyboard
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid entry", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Override back button press
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Task discarded.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_item_toolbar_view, menu);
        return true;
    }

    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }
}
