package com.example.prm392_to_dolisttaskmanagerusingslite;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private OnItemActionListener listener;

    public TaskAdapter(List<Task> tasks, OnItemActionListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.tvTaskTitle.setText(task.getTitle());
        holder.tvTaskDate.setText(task.getDate());
        holder.cbTaskStatus.setChecked(task.isCompleted());

        if (task.isCompleted()) {
            holder.tvTaskTitle.setPaintFlags(holder.tvTaskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvTaskTitle.setPaintFlags(holder.tvTaskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Background color change based on task type
        int backgroundColorResId;
        String taskType = task.getType();

        if(taskType != null) {
            switch (taskType) {
                case "Easy":
                    backgroundColorResId = R.color.task_easy_bg;
                    break;
                case "Medium":
                    backgroundColorResId = R.color.task_medium_bg;
                    break;
                case "Hard":
                    backgroundColorResId = R.color.task_hard_bg;
                    break;
                default:
                    backgroundColorResId = R.color.task_easy_bg;
                    break;
            }
        }
        else {
            backgroundColorResId = R.color.task_easy_bg;
        }

        // Set listeners for checkbox, edit, and delete icons
        holder.cbTaskStatus.setOnCheckedChangeListener(null); // Clear previous listener to avoid issues with recycling
        holder.cbTaskStatus.setChecked(task.isCompleted()); // Set state again after clearing listener
        holder.cbTaskStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onToggleTaskStatus(position, isChecked);
            }
        });

        holder.ivEditTask.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditTask(position);
            }
        });

        holder.ivDeleteTask.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteTask(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskTitle;
        TextView tvTaskDate;
        CheckBox cbTaskStatus;
        ImageView ivEditTask;
        ImageView ivDeleteTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tv_task_title);
            tvTaskDate = itemView.findViewById(R.id.tv_task_date);
            cbTaskStatus = itemView.findViewById(R.id.cb_task_status);
            ivEditTask = itemView.findViewById(R.id.iv_edit_task);
            ivDeleteTask = itemView.findViewById(R.id.iv_delete_task);
        }
    }

    public interface OnItemActionListener {
        void onEditTask(int position);
        void onDeleteTask(int position);
        void onToggleTaskStatus(int position, boolean isChecked);
    }
}