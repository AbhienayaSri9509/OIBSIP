package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todoapp.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_NAME = "name";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASSWORD = "password_hash";

    // Tasks table
    private static final String TABLE_TASKS = "tasks";
    private static final String COL_TASK_ID = "id";
    private static final String COL_TASK_USER_ID = "user_id";
    private static final String COL_TASK_TITLE = "title";
    private static final String COL_TASK_NOTES = "notes";
    private static final String COL_TASK_IS_COMPLETE = "is_complete";
    private static final String COL_TASK_CREATED_AT = "created_at";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
            COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USER_NAME + " TEXT NOT NULL, " +
            COL_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
            COL_USER_PASSWORD + " TEXT NOT NULL)";

    private static final String CREATE_TABLE_TASKS =
            "CREATE TABLE " + TABLE_TASKS + " (" +
            COL_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TASK_USER_ID + " INTEGER NOT NULL, " +
            COL_TASK_TITLE + " TEXT NOT NULL, " +
            COL_TASK_NOTES + " TEXT, " +
            COL_TASK_IS_COMPLETE + " INTEGER DEFAULT 0, " +
            COL_TASK_CREATED_AT + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // ---- MD5 hashing ----
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return input; // fallback (should never happen)
        }
    }

    // ---- User operations ----
    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, md5(password));
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    /**
     * Returns the user_id if credentials match, -1 otherwise.
     */
    public int loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = md5(password);
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?",
                new String[]{email, hashedPassword},
                null, null, null);
        int userId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
            cursor.close();
        }
        db.close();
        return userId;
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID},
                COL_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }

    // ---- Task operations ----
    public boolean addTask(int userId, String title, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK_USER_ID, userId);
        values.put(COL_TASK_TITLE, title);
        values.put(COL_TASK_NOTES, notes);
        values.put(COL_TASK_IS_COMPLETE, 0);
        values.put(COL_TASK_CREATED_AT,
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        java.util.Locale.getDefault()).format(new java.util.Date()));
        long result = db.insert(TABLE_TASKS, null, values);
        db.close();
        return result != -1;
    }

    public List<Task> getTasksForUser(int userId) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null,
                COL_TASK_USER_ID + "=?", new String[]{String.valueOf(userId)},
                null, null, COL_TASK_CREATED_AT + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TASK_ID)));
                task.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TASK_USER_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_TITLE)));
                task.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_NOTES)));
                task.setComplete(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TASK_IS_COMPLETE)) == 1);
                task.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_CREATED_AT)));
                tasks.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public boolean updateTaskComplete(int taskId, boolean isComplete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK_IS_COMPLETE, isComplete ? 1 : 0);
        int rows = db.update(TABLE_TASKS, values, COL_TASK_ID + "=?",
                new String[]{String.valueOf(taskId)});
        db.close();
        return rows > 0;
    }

    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_TASKS, COL_TASK_ID + "=?",
                new String[]{String.valueOf(taskId)});
        db.close();
        return rows > 0;
    }
}
