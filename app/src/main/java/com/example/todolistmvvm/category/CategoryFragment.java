package com.example.todolistmvvm.category;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.todolistmvvm.MainActivity;
import com.example.todolistmvvm.R;
import com.example.todolistmvvm.database.AppDatabase;
import com.example.todolistmvvm.database.Task;
import com.example.todolistmvvm.database.TaskEntity;
import com.example.todolistmvvm.tasks.AddNewTask;
import com.example.todolistmvvm.tasks.TaskViewModel;
import com.example.todolistmvvm.tasks.TasksAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    AppDatabase db;
    TasksAdapter tasksAdapter;
    View view;
    TaskViewModel viewModel;
    private static final String TAG = MainActivity.class.getSimpleName();
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.fragment_category, parent, false);
        final RecyclerView recyclerView = view.findViewById(R.id.notes_container);

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        // Construct the data source
        final List<Task> arrayOfTasks = new ArrayList<Task>();
        // Create the adapter to convert the array to views
        tasksAdapter = new TasksAdapter(this.getContext(), arrayOfTasks);
        recyclerView.setAdapter(tasksAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

         /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                 final int position = viewHolder.getAdapterPosition();
                 final Task task = tasksAdapter.remove(position);

                 Snackbar snackbar = Snackbar.make(view, "Task Deleted", Snackbar.LENGTH_LONG);
                 snackbar.setAction("UNDO", new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         tasksAdapter.add(position, task);
                         recyclerView.scrollToPosition(position);
                     }
                 });
                 snackbar.addCallback(new Snackbar.Callback(){
                     @Override
                     public void onDismissed(Snackbar transientBottomBar, int event) {
                         if (event == DISMISS_EVENT_TIMEOUT) {
//                           super.onDismissed(transientBottomBar, event);
                             viewModel.getTasks().observe(getActivity(), new Observer<List<TaskEntity>>() {
                                 @Override
                                 public void onChanged(List<TaskEntity> taskEntities) {
                                     Log.d(TAG, "Receiving database update from ViewModel");
                                     for (TaskEntity n : taskEntities) {
                                         if( n.getID() == task.ID) {
                                             int taskID = n.getID();
//                                             tasksAdapter.getTasks();
//                                             tasksAdapter.remove(position);
//                                             db.TaskDAO().deleteTask(taskID);
                                             viewModel.deleteTask(taskID);
                                         }
                                     }
                                 }
                             });

                         }
                     }
                 });
                 snackbar.show();
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Add listener to floating action button
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        fab.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddNewTask.class);
                intent.putExtra("categoryID", MainActivity.categoryID);
                startActivity(intent);
            }
        });

    }

    // Update notes list the fragment when it becomes visible
    @Override
    public void onStart() {
        super.onStart();
        retrieveTasks();
    }

    private void retrieveTasks() {
        // Get database
   //   db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "todolist").allowMainThreadQueries().build();
   //     final LiveData<List<TaskEntity>> tasks;
   //     tasks = db.TaskDAO().getTasks(MainActivity.categoryID);
        viewModel.getTasks().observe(this, new Observer<List<TaskEntity>>() {
            @Override
            public void onChanged(List<TaskEntity> taskEntities) {
                // Add items to adapter
                Log.d(TAG, "Receiving database update from ViewModel");
                for (TaskEntity n : taskEntities) {
                    Task newTask = new Task(n.getTitle(), n.getDescription(),n.getPriority(), n.getUpdatedAt(), n.getID());
                    int i = 0;
                    tasksAdapter.add(i, newTask);
                }
            }
        });

    }
}
