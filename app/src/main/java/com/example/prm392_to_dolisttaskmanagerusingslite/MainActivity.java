package com.example.prm392_to_dolisttaskmanagerusingslite;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnItemActionListener {
    private EditText etTitle, etContent, etDate;
    private Spinner spinnerType;
    private Button btnAdd;
    private RecyclerView rvTaskList;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private ArrayAdapter<CharSequence> typeAdapter;
    private DatabaseHelper databaseHelper;

    private SimpleDateFormat dateFormater;

    private boolean isEditMode = false;
    private int taskIdToUpdate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dateFormater = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Initialize UI elements
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        etDate = findViewById(R.id.et_date);
        spinnerType = findViewById(R.id.spinner_type);
        btnAdd = findViewById(R.id.btn_add);
        rvTaskList = findViewById(R.id.rv_task_list);

        // Setup Spinner
        typeAdapter = ArrayAdapter.createFromResource(this, R.array.task_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerType.setAdapter(typeAdapter);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Setup RecyclerView
        rvTaskList.setLayoutManager(new LinearLayoutManager(this));
        taskList = databaseHelper.getAllTasks(); // Load tasks from database
        taskAdapter = new TaskAdapter(taskList, this); // Pass 'this' as listener
        rvTaskList.setAdapter(taskAdapter);

        // Set OnClickListener for Add/Save button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode) {
                    updateTask();
                } else {
                    addTask();
                }
            }
        });
        // Set OnClickListener for Date EditText
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                // Check if etDate is not empty before parsing
                try{
                    if(!etDate.getText().toString().isEmpty()) {
                        newCalendar.setTime(Objects.requireNonNull(dateFormater.parse(etDate.getText().toString())));
                    }
                } catch (ParseException e) {
                    // Log to logcat
                    e.printStackTrace();
                }
                // Display DatePicker
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(year, month, dayOfMonth);
                                etDate.setText(dateFormater.format(selectedDate.getTime()));
                            }
                        },
                        newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void addTask() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim(); // Assuming 'content' will be used later or mapped to 'date'
        String date = etDate.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString(); // Assuming 'type' will be used later or mapped to 'isCompleted'

        if (title.isEmpty() || date.isEmpty() || content.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Task newTask = new Task(title, date, false);
        long newId = databaseHelper.addTask(newTask);
        if (newId > 0) {
            newTask.setId((int) newId);
            taskList.add(newTask);
            taskAdapter.notifyItemInserted(taskList.size() - 1);
            clearInputFields();
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTask() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();

        if (title.isEmpty() || date.isEmpty() || content.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the existing task to preserve its isCompleted status
        Task existingTask = databaseHelper.getTask(taskIdToUpdate);
        if (existingTask != null) {
            Task updatedTask = new Task(taskIdToUpdate, title, date, existingTask.isCompleted(), type);
            int rowsAffected = databaseHelper.updateTask(updatedTask);
            if (rowsAffected > 0) {
                // Update the task in the list
                int index = -1;
                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getId() == taskIdToUpdate) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    taskList.set(index, updatedTask);
                    taskAdapter.notifyItemChanged(index);
                }
                clearInputFields();
                btnAdd.setText("ADD");
                isEditMode = false;
                taskIdToUpdate = -1;
                Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Task not found for update", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        etTitle.setText("");
        etContent.setText("");
        etDate.setText("");
        spinnerType.setSelection(0);
    }

    @Override
    public void onEditTask(int position) {
        Task task = taskList.get(position);
        etTitle.setText(task.getTitle());
        etDate.setText(task.getDate());
        spinnerType.setSelection(typeAdapter.getPosition(task.getType()));
        btnAdd.setText("SAVE");
        isEditMode = true;
        taskIdToUpdate = task.getId();
    }

    @Override
    public void onDeleteTask(int position) {
        Task task = taskList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteTask(task.getId());
                        taskList.remove(position);
                        taskAdapter.notifyItemRemoved(position);
                        Toast.makeText(MainActivity.this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                        if (isEditMode && taskIdToUpdate == task.getId()) {
                            clearInputFields();
                            btnAdd.setText("ADD");
                            isEditMode = false;
                            taskIdToUpdate = -1;
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onToggleTaskStatus(int position, boolean isChecked) {
        Task task = taskList.get(position);
        task.setCompleted(isChecked);
        databaseHelper.updateTask(task);

        rvTaskList.post(() -> {
            taskAdapter.notifyItemChanged(position);
        });
    }
}
