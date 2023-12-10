package com.example.productivityapp.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

public class AppDatabase {

    private TaskDAO taskDao = new TaskDAOImpl();
    private static AppDatabase db;

    public static AppDatabase getDatabase() {
        if (db == null) {
            db = new AppDatabase();
        }

        return db;
    }

    public TaskDAO getTaskDAO() {
        return taskDao;
    }

}
