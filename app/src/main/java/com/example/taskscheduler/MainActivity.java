package com.example.taskscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ListView taskListView;
    private Button addTaskButton;
    private Button deleteTaskButton;
    private Button logoutButton; // Logout button
    private EditText taskInput;

    private ArrayList<Task> taskList;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user is authenticated before proceeding
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // If user is not authenticated, redirect to AuthActivity
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize UI components
        calendarView = findViewById(R.id.calendarView);
        taskListView = findViewById(R.id.taskListView);
        addTaskButton = findViewById(R.id.addTaskButton);
        deleteTaskButton = findViewById(R.id.deleteTaskButton);
        logoutButton = findViewById(R.id.logoutButton); // Logout button initialization
        taskInput = findViewById(R.id.taskInput);

        // Initialize task list and adapter
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, R.layout.list_item, taskList);
        taskListView.setAdapter(taskAdapter);

        // Calendar functionality
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Handle selected date
        //change 1
        // Add task functionality
        addTaskButton.setOnClickListener(v -> {
            String taskTitle = taskInput.getText().toString();
            if (!taskTitle.isEmpty()) {
                taskList.add(new Task(taskTitle, "some date"));
                taskAdapter.notifyDataSetChanged();
                taskInput.setText("");
            }
        });

        // Delete task functionality
        deleteTaskButton.setOnClickListener(v -> {
            // Handle task deletion
        });

        // Logout functionality
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Sign out the user
            Intent intent = new Intent(MainActivity.this, AuthActivity.class); // Redirect to AuthActivity
            startActivity(intent);
            finish();
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        });
    }
}
