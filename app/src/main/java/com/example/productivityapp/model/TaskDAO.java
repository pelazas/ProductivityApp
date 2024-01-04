package com.example.productivityapp.model;

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

