package com.example.praktikum8.database;

import android.provider.BaseColumns;

import org.w3c.dom.Text;

public class DatabaseContract {
    public static String TABLE_NAME = "mahasiswa";
    public static final class StudentColumns implements BaseColumns {
        public static String NAME = "name";
        public static String NIM = "nim";
        public static String CREATED_AT = "created_at";
        public static String UPDATED_AT = "updated_at";
    }
}
