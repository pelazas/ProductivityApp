package com.example.productivityapp.presentacion.social.searchFriends;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.model.User;
import com.example.productivityapp.model.UserDAO;
import com.example.productivityapp.presentacion.LoginActivity;
import com.example.productivityapp.presentacion.adapters.UserItemAdapter;
import com.example.productivityapp.presentacion.social.TimeTableAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendsFragment extends Fragment {
    public SearchFriendsFragment() { }

    private ImageButton searchBtn;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_friends, container, false);

        searchBtn = root.findViewById(R.id.imageButton);
        EditText editTxNewFriend = root.findViewById(R.id.editTxNewFriend);

        mAuth = FirebaseAuth.getInstance();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = editTxNewFriend.getText().toString();

                UserDAO dao = AppDatabase.getDatabase().getUserDAO();

                User foundUser = dao.getUserByEmail(searchText);
                User currentUser = dao.getUser(mAuth.getCurrentUser().getUid());

                if (foundUser.getUserId().equals("NULL")) {
                    showUserNotFoundDialog();
                } else if(foundUser.getUserId().equals("ITSELF")){
                    showUserItselfDialog();
                } else if(foundUser.getUserId().equals("FRIEND")){
                    showUserAlreadyFriendDialog();
                } else if (currentUser.getFriendRequests().contains(foundUser.getEmail())) {
                    showFoundUserAlreadySentRequestDialog();
                } else if (foundUser.getFriendRequests().contains(currentUser.getEmail())) {
                    showCurrentUserAlreadySentRequestDialog();
                } else {
                    showUserFoundDialog(foundUser);
                }
            }
        });

        return root;
    }

    private void showUserFoundDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Usuario encontrado");
        builder.setMessage("Quieres enviarle peticion de amistad a: " + user.getEmail());
        Activity currentActivity = this.getActivity();

        builder.setPositiveButton("Enviar petición", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserDAO dao = AppDatabase.getDatabase().getUserDAO();
                String fromEmail = mAuth.getCurrentUser().getEmail();
                String toEmail = user.getEmail();

                dao.sendFriendRequest(fromEmail, toEmail);

                Toast.makeText(currentActivity, String.format("Has enviado una petición de amistad a %s con éxito.", toEmail),
                        Toast.LENGTH_SHORT).show();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUserNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Usuario no encontrado");
        builder.setMessage("El correo ingresado no forma parte del sistema");
        builder.setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUserItselfDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error en la busqueda");
        builder.setMessage("El correo ingresado es el suyo");
        builder.setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUserAlreadyFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error en la busqueda");
        builder.setMessage("El usuario encontrado ya es su amigo");
        builder.setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showFoundUserAlreadySentRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error en la busqueda");
        builder.setMessage("¡El usuario al que has intentado mandar una petición ya te ha mandado una petición previamente!");
        builder.setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showCurrentUserAlreadySentRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error en la busqueda");
        builder.setMessage("¡Ya has mandado una petición a ese usuario previamente!");
        builder.setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}