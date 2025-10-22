package com.example.prm392_to_dolisttaskmanagerusingslite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "taskmanager.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IS_COMPLETED = "is_completed";
    public static final String COLUMN_TYPE = "type";

    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_CONTENT + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_IS_COMPLETED + " INTEGER DEFAULT 0, " +
            COLUMN_TYPE + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Removed the recursive call that was here.
    }

    private void addTaskInternal(SQLiteDatabase db, Task task) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_CONTENT, task.getContent());
        values.put(COLUMN_DATE, task.getDate());
        values.put(COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COLUMN_TYPE, task.getType());
        db.insert(TABLE_TASKS, null, values);
    }

    private void writeSampleData(SQLiteDatabase db) {
        // Add sample data to the database
        addTaskInternal(db, new Task("Do math",  "Agebra","2023-02-28", false, "Easy"));
        addTaskInternal(db, new Task("Do homework", "Math","2023-02-27", true, "Medium"));
        addTaskInternal(db, new Task("Go to gym", "Bodybuilding","2023-02-26", false, "Hard"));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS);
        writeSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Add a new task to the database - this method will be used for adding tasks *after* initial creation
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_CONTENT, task.getContent());
        values.put(COLUMN_DATE, task.getDate());
        values.put(COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        long id = db.insert(TABLE_TASKS, null, values);
        db.close(); // Keep db.close() here as getWritableDatabase() opens a new connection
        return id;
    }

    // Get all tasks from the database
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getWritableDatabase(); // Changed to getWritableDatabase for consistency
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1;
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
                taskList.add(new Task(id, title, content,date, isCompleted, type));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Close the database connection
        return taskList;
    }

    // Update a task in the database
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_CONTENT, task.getContent());
        values.put(COLUMN_DATE, task.getDate());
        values.put(COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        int rowsAffected = db.update(TABLE_TASKS,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
        return rowsAffected;
    }

    // Delete a task from the database
    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)});
        db.close();
    }

    // Get a single task by ID
    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DATE, COLUMN_IS_COMPLETED},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        Task task = null;
        if (cursor != null && cursor.moveToFirst()) {
            task = new Task(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1,
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
            cursor.close();
        }
        db.close(); // Close the database connection
        return task;
    }
}