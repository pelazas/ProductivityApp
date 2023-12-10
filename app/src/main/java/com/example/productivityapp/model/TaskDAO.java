package com.example.productivityapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface TaskDAO {
    void add(ToDo tarea);

    void update(ToDo tarea);

    void delete(ToDo tarea);

    List<ToDo> getAll(String userId);

    List<ToDo> getAllActive(String userId, String state);

    List<ToDo> getAllFinished(String userId);

    void updateFromRemove(String tareaId, String state);
}

