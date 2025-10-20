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

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
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

        holder.cbTaskStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            if (isChecked) {
                holder.tvTaskTitle.setPaintFlags(holder.tvTaskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.tvTaskTitle.setPaintFlags(holder.tvTaskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
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
}