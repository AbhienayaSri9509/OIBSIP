package com.example.todoapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskChangeListener {
        void onTaskChanged();
    }

    private final Context context;
    private final List<Task> taskList;
    private final DatabaseHelper dbHelper;
    private final OnTaskChangeListener listener;

    public TaskAdapter(Context context, List<Task> taskList, DatabaseHelper dbHelper, OnTaskChangeListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.dbHelper = dbHelper;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.tvTitle.setText(task.getTitle());
        if (task.getNotes() != null && !task.getNotes().trim().isEmpty()) {
            holder.tvNotes.setVisibility(View.VISIBLE);
            holder.tvNotes.setText(task.getNotes());
        } else {
            holder.tvNotes.setVisibility(View.GONE);
        }

        holder.cbComplete.setOnCheckedChangeListener(null);
        holder.cbComplete.setChecked(task.isComplete());
        applyStrikethrough(holder.tvTitle, task.isComplete());

        holder.cbComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setComplete(isChecked);
            dbHelper.updateTaskComplete(task.getId(), isChecked);
            applyStrikethrough(holder.tvTitle, isChecked);
        });

        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                dbHelper.deleteTask(task.getId());
                taskList.remove(currentPos);
                notifyItemRemoved(currentPos);
                Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onTaskChanged();
                }
            }
        });
    }

    private void applyStrikethrough(TextView textView, boolean isComplete) {
        if (isComplete) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textView.setAlpha(0.6f);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            textView.setAlpha(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbComplete;
        TextView tvTitle;
        TextView tvNotes;
        ImageButton btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbComplete = itemView.findViewById(R.id.cbComplete);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvNotes = itemView.findViewById(R.id.tvTaskNotes);
            btnDelete = itemView.findViewById(R.id.btnDeleteTask);
        }
    }
}
