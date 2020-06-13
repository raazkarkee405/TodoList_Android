package com.example.todolistmvvm.tasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistmvvm.R;
import com.example.todolistmvvm.database.Task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder>{
    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private LayoutInflater inflater;
    private Context context;
    private List<Task> tasks;

    public TasksAdapter(Context context, List<Task> tasks){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.tasks = tasks;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_task, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.MyViewHolder holder, int position) {

        // Determine the values of the wanted data
        Task task = tasks.get(position);
        String title = task.title;
        String description = task.description;
        int priority = task.priority;
        String updatedAt = dateFormat.format(task.updatedAt);

        //Set values
        holder.taskTitleView.setText(title);
        holder.taskDescriptionView.setText(description);
        holder.updatedAtView.setText(updatedAt);

        // Programmatically set the text and color for the priority TextView
        String priorityString = "" + priority; // converts int to String
        holder.priorityView.setText(priorityString);

        GradientDrawable priorityCircle = (GradientDrawable) holder.priorityView.getBackground();
        // Get the appropriate background color based on the priority
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);
    }

    @Override
    public int getItemCount() {
        if (tasks == null) {
            return 0;
        }

        return tasks.size();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> task){

        tasks = task;
        notifyDataSetChanged();
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void add(int position, Task task){
        tasks.add(position,task);
//        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void undoDelete(int position, Task task){
        tasks.add(position,task);
        notifyItemInserted(position);

    }

    public Task remove(int position) {
        if (position < getItemCount() && position >= 0) {
            Task task = tasks.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
            return task;
        }
//       tasks.remove(position);
//       notifyItemRemoved(position);
        return null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Class variables for the task description and priority TextViews
        TextView taskTitleView;
        TextView taskDescriptionView;
        TextView updatedAtView;
        TextView priorityView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleView = itemView.findViewById(R.id.taskTitle);
            taskDescriptionView = itemView.findViewById(R.id.taskDescription);
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt);
            priorityView = itemView.findViewById(R.id.priorityTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Task task = tasks.get(getAdapterPosition());
            Intent intent = new Intent(context, EditTask.class);
            intent.putExtra("taskID", task.ID);
            context.startActivity(intent);
        }
    }
    /*
 Helper method for selecting the correct priority circle color.
 P1 = red, P2 = orange, P3 = yellow
 */
    private int getPriorityColor(int priority) {
        int priorityColor = 0;

        switch (priority) {
            case 1:
                priorityColor = ContextCompat.getColor(context, R.color.materialRed);
                break;
            case 2:
                priorityColor = ContextCompat.getColor(context, R.color.materialOrange);
                break;
            case 3:
                priorityColor = ContextCompat.getColor(context, R.color.materialYellow);
                break;
            default:
                break;
        }
        return priorityColor;
    }
}