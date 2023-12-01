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
    void add(ToDo tarea);

    @Update
    void update(ToDo tarea);

    @Delete
    void delete(ToDo tarea);

    @Query("select * from todos")
    List<ToDo> getAll();

    @Query("select * from todos where state like :state")
    List<ToDo> getAllActive(String state);

    @Query("select * from todos where state like 'FINISHED'")
    List<ToDo> getAllFinished();

    @Query("update todos set state= :state where id= :tareaId")
    void updateFromRemove(int tareaId, String state);
}

