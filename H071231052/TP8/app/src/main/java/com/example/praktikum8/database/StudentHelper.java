package com.example.praktikum8.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentHelper {
    private static final String TABLE_NAME = DatabaseContract.TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;
    private static volatile StudentHelper INSTANCE;

    private StudentHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static StudentHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StudentHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen()) {
            database.close();
        }
    }

    public Cursor queryAll() {
        return database.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DatabaseContract.StudentColumns._ID + " ASC"
        );
    }
    public Cursor queryById(String id) {
        return database.query(
                TABLE_NAME,
                null,
                DatabaseContract.StudentColumns._ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
    }
    public Cursor queryByTitle(String keyword) {
        return database.query(
                TABLE_NAME,
                null,
                DatabaseContract.StudentColumns.NAME + " LIKE ?",
                new String[]{"%" + keyword + "%"},
                null,
                null,
                DatabaseContract.StudentColumns._ID + " ASC"
        );
    }

    public long insert(ContentValues values) {
        return database.insert(TABLE_NAME, null, values);
    }

    public int update(String id, ContentValues values) {
        return database.update(TABLE_NAME, values, DatabaseContract.StudentColumns._ID
                + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(TABLE_NAME, DatabaseContract.StudentColumns._ID + " = ?",
                new String[]{id});
    }

}
