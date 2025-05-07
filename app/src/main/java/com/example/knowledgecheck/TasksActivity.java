package com.example.knowledgecheck;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.knowledgecheck.TaskAdapter;
import com.example.knowledgecheck.Task;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private RecyclerView tasksRecyclerView;
    private Button btnAddTask;
    private List<Task> taskList = new ArrayList<>();
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        // Initialize views
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        btnAddTask = findViewById(R.id.btnAddTask);

        // Setup RecyclerView
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(taskList, this);
        tasksRecyclerView.setAdapter(adapter);

        // Load sample data
        loadSampleTasks();

        // Set click listeners
        btnAddTask.setOnClickListener(v -> showAddTaskDialog());
    }

    private void loadSampleTasks() {
        taskList.add(new Task("Morning Walk", "30 minutes around the park", "08:00 AM"));
        taskList.add(new Task("Buy groceries", "Milk, eggs, and vegetables", "05:00 PM"));
        taskList.add(new Task("Read book", "Chapter 5 of current novel", "09:00 PM"));
        adapter.notifyDataSetChanged();
    }

    private void showAddTaskDialog() {
        /// Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        // Inflate and set the layout for the dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_task, null);
        builder.setView(dialogView);

        // Get references to the dialog views
        EditText etTaskTitle = dialogView.findViewById(R.id.etTaskTitle);
        EditText etTaskDescription = dialogView.findViewById(R.id.etTaskDescription);
        EditText etDueTime = dialogView.findViewById(R.id.etDueTime);

        // Set up the buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = etTaskTitle.getText().toString().trim();
            String description = etTaskDescription.getText().toString().trim();
            String dueTime = etDueTime.getText().toString().trim();

            if (!title.isEmpty()) {
                Task newTask = new Task(title, description, dueTime);
                taskList.add(newTask);
                adapter.notifyItemInserted(taskList.size() - 1);
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Show the dialog
        builder.create().show();
    }

    @Override
    public void onTaskChecked(int position, boolean isChecked) {
        Task task = taskList.get(position);
        task.setCompleted(isChecked);
        tasksRecyclerView.post(() -> adapter.notifyItemChanged(position));
    }

    @Override
    public void onTaskDeleted(int position) {
        taskList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onTaskEdit(int position) {
        Task task = taskList.get(position);
        showEditTaskDialog(position, task);
    }

    private void showEditTaskDialog(int position, Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        // Inflate and set the layout for the dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_task, null);
        builder.setView(dialogView);

        // Get references to the dialog views
        EditText etTaskTitle = dialogView.findViewById(R.id.etTaskTitle);
        EditText etTaskDescription = dialogView.findViewById(R.id.etTaskDescription);
        EditText etDueTime = dialogView.findViewById(R.id.etDueTime);

        // Pre-fill the fields with existing task data
        etTaskTitle.setText(task.getTitle());
        etTaskDescription.setText(task.getDescription());
        etDueTime.setText(task.getDueTime());

        // Set up the buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = etTaskTitle.getText().toString().trim();
            String description = etTaskDescription.getText().toString().trim();
            String dueTime = etDueTime.getText().toString().trim();

            if (!title.isEmpty()) {
                task.setTitle(title);
                task.setDescription(description);
                task.setDueTime(dueTime);
                adapter.notifyItemChanged(position);
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Show the dialog
        builder.create().show();
    }
}