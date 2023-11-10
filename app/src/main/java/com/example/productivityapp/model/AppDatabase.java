package com.example.productivityapp.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(version= 1, entities = {Todo.class})
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDAO getTaskDAO();

    public static final String DB_NOMBRE = "tareas.db";
    private static AppDatabase db;

    public static AppDatabase getDatabase(Context applicationContext) {
        if (db == null) {
            db = Room.databaseBuilder(applicationContext, AppDatabase.class, DB_NOMBRE)
                    .allowMainThreadQueries()
                    .build();
        }
        return db;
    }

}
