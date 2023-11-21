package com.example.productivityapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void add(Todo tarea);

    @Update
    void update(Todo tarea);

    @Delete
    void delete(Todo tarea);

    @Query("select * from todos")
    List<Todo> getAll();

    @Query("select * from todos where state like :state")
    List<Todo> getAllActive(String state);

    @Query("update todos set state= :state where id= :tareaId")
    void updateFromRemove(int tareaId, String state);
}

