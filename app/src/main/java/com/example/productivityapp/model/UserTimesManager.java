package com.example.productivityapp.model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserTimesManager {
    private static final String TAG = "FirebaseManager";
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "times";
    public UserTimesManager() {
        // Inicializar firebase
        db = FirebaseFirestore.getInstance();
    }

    public void updateTimeForEmail(final String email, final long seconds) {
        // Check if the email already exists in the collection
        DocumentReference documentReference = db.collection(COLLECTION_NAME).document(email);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Email exists, update the seconds field
                        long currentSeconds = document.getLong("seconds");
                        long newSeconds = currentSeconds + seconds;

                        documentReference.update("seconds", newSeconds)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Document updated successfully!");
                                        } else {
                                            Log.e(TAG, "Error updating document", task.getException());
                                        }
                                    }
                                });
                    } else {
                        // Email doesn't exist, create a new document
                        createNewDocument(email, seconds);
                    }
                } else {
                    Log.e(TAG, "Error getting document", task.getException());
                }
            }
        });
    }

    private void createNewDocument(String email, long seconds) {
        // Create a new document with the given email and seconds
        TimeData timeData = new TimeData(email, seconds);
        db.collection(COLLECTION_NAME).document(email)
                .set(timeData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Document created successfully!");
                        } else {
                            Log.e(TAG, "Error creating document", task.getException());
                        }
                    }
                });
    }

    public List<TimeData> getAllEmailsAndTimes() {
        ThreadGetAllEmailsAndTimes t = new ThreadGetAllEmailsAndTimes();
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return t.getEmailsAndTimes();
    }

    public List<TimeData> getEmailAndTimesOfFriends(List<String> friends) {
        ThreadGetEmailsAndTimesOfFriends t = new ThreadGetEmailsAndTimesOfFriends(friends);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return t.getEmailsAndTimes();
    }

    private class ThreadGetEmailsAndTimesOfFriends extends Thread {
        private List<TimeData> times;
        private final List<String> friends;

        public ThreadGetEmailsAndTimesOfFriends(List<String> friends) {
            this.friends = friends;
        }

        public void run() {
            try {
                Task<QuerySnapshot> task = db.collection(COLLECTION_NAME)
                        .whereIn("email", friends)
                        .orderBy("seconds", Query.Direction.DESCENDING).limit(5)
                        .get();
                QuerySnapshot result = Tasks.await(task);

                if (result != null && !result.isEmpty()) {
                    List<TimeData> times = new ArrayList<>();

                    for (DocumentSnapshot document : result.getDocuments()) {
                        times.add(assembleTodo(document));
                    }

                    this.times = times;
                } else {
                    this.times = new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.times = new ArrayList<>();
            }
        }

        public List<TimeData> getEmailsAndTimes() {
            return this.times;
        }
    }

    private class ThreadGetAllEmailsAndTimes extends Thread {
        private List<TimeData> times;

        public ThreadGetAllEmailsAndTimes() {
        }

        public void run() {
            try {
                Task<QuerySnapshot> task = db.collection(COLLECTION_NAME).orderBy("seconds", Query.Direction.DESCENDING).limit(5)
                        .get();
                QuerySnapshot result = Tasks.await(task);

                if (result != null && !result.isEmpty()) {
                    List<TimeData> times = new ArrayList<>();

                    for (DocumentSnapshot document : result.getDocuments()) {
                        times.add(assembleTodo(document));
                    }

                    this.times = times;
                } else {
                    this.times = new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<TimeData> getEmailsAndTimes() {
            return this.times;
        }
    }

    private TimeData assembleTodo(DocumentSnapshot document) {
        String email = document.getId();
        long seconds = (Long) document.get("seconds");
        return new TimeData(email, seconds);
    }
}
