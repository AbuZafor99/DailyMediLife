package com.example.knowledgecheck;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.knowledgecheck.R;
import com.example.knowledgecheck.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;
    private final OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskChecked(int position, boolean isChecked);
        void onTaskDeleted(int position);
        void onTaskEdit(int position);
    }

    public TaskAdapter(List<Task> taskList, OnTaskClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox cbTaskCompleted;
        private final TextView tvTaskTitle;
        private final TextView tvTaskDescription;
        private final TextView tvDueTime;
        private final ImageView ivEditTask;
        private final ImageButton btnDeleteTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbTaskCompleted = itemView.findViewById(R.id.cbTaskCompleted);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);
            tvDueTime = itemView.findViewById(R.id.tvDueTime);
            ivEditTask = itemView.findViewById(R.id.editTask);
            btnDeleteTask = itemView.findViewById(R.id.btnDeleteTask);
        }

        public void bind(Task task) {
            tvTaskTitle.setText(task.getTitle());
            tvTaskDescription.setText(task.getDescription());
            tvDueTime.setText(task.getDueTime());
            cbTaskCompleted.setChecked(task.isCompleted());

            // Strike through text if completed
            if (task.isCompleted()) {
                tvTaskTitle.setPaintFlags(tvTaskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvTaskDescription.setPaintFlags(tvTaskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                tvTaskTitle.setPaintFlags(tvTaskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                tvTaskDescription.setPaintFlags(tvTaskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            cbTaskCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onTaskChecked(getAdapterPosition(), isChecked);
                }
            });

            btnDeleteTask.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaskDeleted(getAdapterPosition());
                }
            });

            ivEditTask.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaskEdit(getAdapterPosition());
                }
            });
        }
    }
}
