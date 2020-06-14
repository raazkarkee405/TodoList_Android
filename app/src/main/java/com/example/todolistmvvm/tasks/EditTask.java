package com.example.todolistmvvm.tasks;

import android.os.Bundle;
import android.util.Log;
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
import androidx.core.app.ShareCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.example.todolistmvvm.MainActivity;
import com.example.todolistmvvm.R;
import com.example.todolistmvvm.database.AppDatabase;

import com.example.todolistmvvm.database.TaskEntity;

import java.util.Date;

public class EditTask extends AppCompatActivity {

    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    int taskID;
    AppDatabase db;
    TaskEntity task;
    InputMethodManager imm;
    EditText editTextTitle;
    EditText editTextDescription;
    RadioGroup mRadioGroup;
    private static final String TAG = MainActivity.class.getSimpleName();
    TaskViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        setTitle("Edit task");

        taskID = getIntent().getIntExtra("taskID", 0);

        //Set the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow);

        // Get database
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "todolist").allowMainThreadQueries().build();
        task = db.TaskDAO().getTask(taskID);
                // Get task detail
                editTextTitle = findViewById(R.id.editTextTaskTitle);
                editTextDescription = findViewById(R.id.editTextTaskDescription);
                mRadioGroup = findViewById(R.id.radioGroup);

                if (task == null){
                    return;
                }else {

                    editTextTitle.setText(task.getTitle());
                    editTextDescription.setText(task.getDescription());
                    setPriorityInViews(task.getPriority());
                }

        // Show keyboard
        imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

    }

    // Listener for toolbar button clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                // Finish activity
                Toast.makeText(getApplicationContext(), "Changes discarded.", Toast.LENGTH_SHORT).show();

                // Hide keyboard
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                finish();
                return true;

            case R.id.share:
                String Description = editTextDescription.getText().toString();
                String mimeType = "text/plain";
                ShareCompat.IntentBuilder
                        .from(this)
                        .setType(mimeType)
                        .setChooserTitle("Share this task with:")
                        .setText(Description)
                        .startChooser();
                return true;

            case R.id.delete:

                // Delete task from database
//                db.TaskDAO().deleteTask(taskID);
                viewModel.deleteTask(taskID);
                Toast.makeText(getApplicationContext(), "Task deleted.", Toast.LENGTH_SHORT).show();
                // Hide keyboard
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                finish();
                return true;

            case R.id.done:
                // Get content of text field
                editTextTitle = findViewById(R.id.editTextTaskTitle);
                String title = editTextTitle.getText().toString();
                editTextDescription = findViewById(R.id.editTextTaskDescription);
                String description = editTextDescription.getText().toString();
                mRadioGroup = findViewById(R.id.radioGroup);
                int priority = getPriorityFromViews();
                Date date = new Date();

                task.setTitle(title);
                task.setDescription(description);
                task.setPriority(priority);
                task.setUpdatedAt(date);

                // Update entry in database
//                db.TaskDAO().updateTask(task);
                viewModel.updateTask(task);
                Toast.makeText(getApplicationContext(), "Task updated.", Toast.LENGTH_SHORT).show();

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
        getMenuInflater().inflate(R.menu.share_item_toolbar_view, menu);
        getMenuInflater().inflate(R.menu.edit_item_toolbar_view, menu);
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

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }
}
