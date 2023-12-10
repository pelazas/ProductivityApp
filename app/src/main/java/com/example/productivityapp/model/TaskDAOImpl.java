package com.example.productivityapp.model;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDAOImpl implements TaskDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void add(ToDo tarea) {
        Map<String, Object> tareaDB = new HashMap<>();

        tareaDB.put("userId", tarea.getUserId());
        tareaDB.put("title", tarea.getTitle());
        tareaDB.put("description", tarea.getDescription());
        tareaDB.put("limitDate", Converters.localDateToString(tarea.getLimitDate()));
        tareaDB.put("priority", Converters.priorityToString(tarea.getPriority()));
        tareaDB.put("state", tarea.getState().toString());


        db.collection("todos")
                .add(tareaDB);
    }

    @Override
    public void update(ToDo tarea) {
        Map<String, Object> tareaDB = new HashMap<>();

        tareaDB.put("title", tarea.getTitle());
        tareaDB.put("description", tarea.getDescription());
        tareaDB.put("limitDate", Converters.localDateToString(tarea.getLimitDate()));
        tareaDB.put("priority", Converters.priorityToString(tarea.getPriority()));
        tareaDB.put("state", tarea.getState().toString());

        db.collection("todos").document(tarea.getId()).update(tareaDB);
    }

    @Override
    public void delete(ToDo tarea) {
        db.collection("todos").document(tarea.getId()).delete();
    }

    @Override
    public List<ToDo> getAll(String userId) {
        ThreadGetAllTodos t = new ThreadGetAllTodos(userId);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return t.getTodos();
    }

    @Override
    public List<ToDo> getAllActive(String userId, String state) {
        ThreadGetAllActive t = new ThreadGetAllActive(userId, state);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return t.getTodos();
    }

    @Override
    public List<ToDo> getAllFinished(String userId) {
        ThreadGetAllFinished t = new ThreadGetAllFinished(userId);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return t.getTodos();
    }

    @Override
    public void updateFromRemove(String tareaId, String state) {
        db.collection("todos").document(tareaId).update("state", state);
    }

    private ToDo assembleTodo(DocumentSnapshot document) {
        String id = document.getId();
        String userId = (String) document.get("userId");
        String title = (String) document.get("title");
        String description = (String) document.get("description");
        LocalDate limitDate = Converters.fromLocalDateString((String) document.get("limitDate"));
        ToDo.Priority priority = Converters.fromPriorityString((String) document.get("priority"));
        ToDo.State state = Converters.fromStateString((String) document.get("state"));

        return new ToDo(id, userId, title, description, limitDate, priority, state);
    }

    class ThreadGetAllActive extends Thread {

        private List<ToDo> todos;
        private String state;
        private String userId;

        public ThreadGetAllActive(String userId, String state) {
            this.state = state;
            this.userId = userId;
        }

        public void run()
        {

            try {
                Task<QuerySnapshot> task = db.collection("todos")
                        .whereEqualTo("state", this.state)
                        .whereEqualTo("userId", this.userId)
                        .get();
                QuerySnapshot result = Tasks.await(task);

                if (result != null && !result.isEmpty()) {
                    List<ToDo> todos = new ArrayList<>();

                    for (DocumentSnapshot document : result.getDocuments()) {
                        todos.add(assembleTodo(document));
                    }

                    this.todos = todos;
                } else {
                    this.todos = new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<ToDo> getTodos() {
            return this.todos;
        }
    }

    class ThreadGetAllFinished extends Thread {

        private List<ToDo> todos;
        private String userId;

        public ThreadGetAllFinished(String userId) {
            this.userId = userId;
        }

        public void run()
        {

            try {
                Task<QuerySnapshot> task = db.collection("todos")
                        .whereEqualTo("state", "FINISHED")
                        .whereEqualTo("userId", this.userId)
                        .get();
                QuerySnapshot result = Tasks.await(task);

                if (result != null && !result.isEmpty()) {
                    List<ToDo> todos = new ArrayList<>();

                    for (DocumentSnapshot document : result.getDocuments()) {
                        todos.add(assembleTodo(document));
                    }

                    this.todos = todos;
                } else {
                    this.todos = new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<ToDo> getTodos() {
            return this.todos;
        }
    }

    class ThreadGetAllTodos extends Thread {

        private List<ToDo> todos;
        private String userId;

        public ThreadGetAllTodos(String userId) {
            this.userId = userId;
        }

        public void run()
        {

            try {
                Task<QuerySnapshot> task = db.collection("todos")
                        .whereEqualTo("userId", this.userId)
                        .get();
                QuerySnapshot result = Tasks.await(task);

                if (result != null && !result.isEmpty()) {
                    List<ToDo> todos = new ArrayList<>();

                    for (DocumentSnapshot document : result.getDocuments()) {
                        todos.add(assembleTodo(document));
                    }

                    this.todos = todos;
                } else {
                    this.todos = new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<ToDo> getTodos() {
            return this.todos;
        }
    }
}
