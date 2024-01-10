package com.example.productivityapp.model;

public class AppDatabase {

    private final TaskDAO taskDao = new TaskDAOImpl();
    private final UserDAO userDao = new UserDAOImpl();
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

    public UserDAO getUserDAO() {
        return userDao;
    }
}
