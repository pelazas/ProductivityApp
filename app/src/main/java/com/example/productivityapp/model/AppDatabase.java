package com.example.productivityapp.model;

public class AppDatabase {

    private TaskDAO taskDao = new TaskDAOImpl();
    private UserDAO userDao = new UserDAOImpl();
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
