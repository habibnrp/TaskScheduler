package com.example.taskscheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context context;
    private int resource;
    private List<Task> tasks;

    public TaskAdapter(Context context, int resource, List<Task> tasks) {
        super(context, resource, tasks);
        this.context = context;
        this.resource = resource;
        this.tasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        Task task = tasks.get(position);

        TextView taskTitle = convertView.findViewById(R.id.taskTitle);
        TextView taskDate = convertView.findViewById(R.id.taskDate);

        taskTitle.setText(task.getTitle());
        taskDate.setText(task.getDate());

        return convertView;
    }
}

