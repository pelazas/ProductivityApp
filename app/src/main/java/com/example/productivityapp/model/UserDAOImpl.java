package com.example.productivityapp.model;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void registerUser(String uuid, String email) {
        Map<String, Object> document = new HashMap<>();

        document.put("userId", uuid);
        document.put("email", email);
        document.put("friends", new ArrayList<String>());
        document.put("friendRequests", new ArrayList<String>());

        db.collection("users")
                .add(document);
    }

    @Override
    public void sendFriendRequest(String fromEmail, String toEmail) {
        db.collection("users")
                .whereEqualTo("email", toEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<String> friendRequests = (List<String>) document.get("friendRequests");

                            if (friendRequests != null) {
                                friendRequests.add(fromEmail);

                                db.collection("users")
                                        .document(document.getId())
                                        .update("friendRequests", friendRequests);
                            }
                        }
                    }
                });
    }

    @Override
    public void acceptFriendRequest(String uuid, String friendAcceptedEmail) {
        db.collection("users")
                .whereEqualTo("userId", uuid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<String> friends = (List<String>) document.get("friends");
                            List<String> friendRequests = (List<String>) document.get("friendRequests");

                            if (friendRequests != null && friends != null) {
                                friendRequests.remove(friendAcceptedEmail);
                                friends.add(friendAcceptedEmail);

                                Map<String, Object> d = new HashMap<>();

                                d.put("friendRequests", friendRequests);
                                d.put("friends", friends);

                                db.collection("users")
                                        .document(document.getId())
                                        .update(d);
                            }
                        }
                    }
                });
    }

    @Override
    public User getUser(String uuid) {
        ThreadGetUser t = new ThreadGetUser(uuid);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return t.getUser();
    }

    private class ThreadGetUser extends Thread {

        private String uuid;
        private User user;

        public ThreadGetUser(String uuid) {
            this.uuid = uuid;
        }

        public void run() {
            try {
                Task<QuerySnapshot> task = db.collection("users")
                        .whereEqualTo("userId", this.uuid)
                        .get();
                QuerySnapshot result = Tasks.await(task);

                if (result != null && !result.isEmpty()) {
                    for (DocumentSnapshot document : result.getDocuments()) {
                        this.user = assembleUser(document);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public User getUser() {
            return this.user;
        }
    }


    private User assembleUser(DocumentSnapshot document) {
        String userId = (String) document.get("userId");
        String email = (String) document.get("email");
        List<String> friends = (List<String>) document.get("friends");
        List<String> friendRequests = (List<String>) document.get("friendRequests");

        return new User(userId, email, friends, friendRequests);
    }
}
