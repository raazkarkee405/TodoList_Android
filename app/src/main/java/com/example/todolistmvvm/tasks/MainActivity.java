package com.example.todolistmvvm.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.todolistmvvm.R;

import com.example.todolistmvvm.addedittask.AddEditTaskActivity;
import com.example.todolistmvvm.database.AppDatabase;

import com.example.todolistmvvm.database.TaskEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity implements TaskAdapter.ItemClickListener {

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerViewTasks);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new TaskAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

         /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppDatabase.databaseWriteExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        TaskEntry task = mAdapter.getTasks().get(position);
                        database.taskDao().deleteTask(task);
                        retrieveTasks();
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        FloatingActionButton fabButton = findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                startActivity(addTaskIntent);
            }
        });
        database = AppDatabase.getInstance(getApplicationContext());

    }

    @Override
    public void onItemClickListener(int itemId){
        // Launch AddTaskActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(this, AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskActivity.EXTRA_TASK_ID, itemId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();
    }

    private void retrieveTasks() {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final List<TaskEntry> tasks = database.taskDao().loadAllTasks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setTasks(tasks);
                    }
                });
            }
        });
    }
}
